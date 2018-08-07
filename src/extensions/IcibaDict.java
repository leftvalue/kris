package extensions;

import basetool.Colors;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.fusesource.jansi.Ansi;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.stream.Collectors;

/**
 * @author linxi
 * www.leftvalue.top
 * extensions
 * Date 2018/8/6 12:26 PM
 */
public class IcibaDict {
    static JSONObject visit(String keyword) throws IOException {
        String body = Jsoup.connect("http://www.iciba.com/index.php?callback=jQuery&a=getWordMean&c=search&list=&word=" + URLEncoder.encode(keyword))
                .timeout(10000).ignoreContentType(true).ignoreHttpErrors(true).execute().body();
        body = body.substring(body.indexOf("(") + 1, body.lastIndexOf(")"));
        JSONObject ob = JSON.parseObject(body);
        return ob;
    }

    public static void searchChn(String keyword) {
        try {
            JSONObject ob = visit(keyword);
            StringBuilder sb = new StringBuilder();
            JSONObject baesInfo = ob.getJSONObject("baesInfo");
            String wordname = baesInfo.getString("word_name");
            sb.append(Colors.getCS(Ansi.Color.GREEN, wordname) + "\n");
            JSONArray symbols = baesInfo.getJSONArray("symbols");
            /***
             * 基本释义
             */
            if (symbols != null && (!symbols.isEmpty())) {
                sb.append(Colors.getCS(Ansi.Color.YELLOW, "基础释义") + "\n");
                for (Object o : symbols) {
                    /**
                     * 对应每一种读音
                     */
                    JSONObject symbol = JSON.parseObject(o.toString());
                    String word_symbol = symbol.getString("word_symbol");
                    String symbol_mp3 = symbol.getString("symbol_mp3");
                    if (!word_symbol.trim().isEmpty()) {
                        sb.append("\t" + Colors.getCS(Ansi.Color.CYAN, "[" + word_symbol + "]") + "\n");
                    }
                    JSONArray parts = symbol.getJSONArray("parts");
                    for (Object part_ : parts) {
                        JSONObject part = JSONObject.parseObject(part_.toString());
                        String part_name = part.getString("part");
                        String means = part.getJSONArray("means").stream().map(v -> v.toString()).collect(Collectors.joining("; "));
                        sb.append("\t\t* " + Colors.getCS(Ansi.Color.YELLOW, part_name) + " " + means + "\n");
                    }
                }
            }
            JSONArray sentences = ob.getJSONArray("sentence");
            if (sentences != null && sentences.size() > 0) {
                sb.append(Colors.getCS(Ansi.Color.YELLOW, "双语例句") + "\n");
                for (Object o : sentences) {
                    JSONObject sentence = JSON.parseObject(o.toString());
                    String Network_en = sentence.getString("Network_en");
                    String Network_cn = sentence.getString("Network_cn");
                    Network_cn = Network_cn.replace(wordname, Colors.getCS(Ansi.Color.YELLOW, wordname));
                    sb.append("\t\t* " + Network_en + "\n");
                    sb.append("\t\t  " + Network_cn + "\n");
                }
            }
            /**
             * 词组搭配
             */
            JSONArray phrase = ob.getJSONArray("phrase");
            if (phrase != null && phrase.size() > 0) {
                sb.append(Colors.getCS(Ansi.Color.YELLOW, "词组搭配") + "\n");
                char titleA = 'A';
                for (Object o : phrase) {
                    /**
                     * 每个词组
                     */
                    JSONObject p = JSON.parseObject(o.toString());
                    String cizu_name = p.getString("cizu_name");
                    sb.append("\t" + (titleA++) + " " + Colors.getCS(Ansi.Color.CYAN, cizu_name) + "\n");
                    JSONArray jx = p.getJSONArray("jx");
                    /*
                     * 词组的每个意思
                     */
                    int title1 = 1;
                    for (Object oo : jx) {
                        JSONObject mean = JSON.parseObject(oo.toString());
                        String jx_cn_mean = mean.getString("jx_cn_mean");
                        String jx_en_mean = mean.getString("jx_en_mean");
                        sb.append("\t\t" + (title1++) + ". " + jx_en_mean + " " + jx_cn_mean + "\n");
                    }
                }
            }
            /**
             * 英汉双向大词典
             */
            JSONArray synthesize = ob.getJSONArray("synthesize");
            if (synthesize != null && synthesize.size() > 0) {
                sb.append(Colors.getCS(Ansi.Color.YELLOW, "英汉双向大词典") + "\n");
                for (Object o : synthesize) {
                    /**
                     * 遍历每一种读音
                     */
                    JSONObject yin = JSON.parseObject(o.toString());
                    String word_symbol = yin.getString("word_symbol");
                    sb.append("\t" + wordname + Colors.getCS(Ansi.Color.CYAN, "[" + word_symbol + "]") + "\n");
                    JSONArray parts = yin.getJSONArray("parts");
                    if (parts == null) {
                        continue;
                    }
                    for (Object oo : parts) {
                        /**
                         * 遍历每一种词性
                         */
                        JSONObject part = JSON.parseObject(oo.toString());
                        String part_name = part.getString("part_name");
                        sb.append("\t" + Colors.getCS(Ansi.Color.YELLOW, part_name) + "\n");
                        char titleA = 'A';
                        JSONArray means = part.getJSONArray("means");
                        if (means == null) {
                            continue;
                        }
                        for (Object ooo : means) {
                            JSONObject mean = JSON.parseObject(ooo.toString());
                            String word_mean = mean.getString("word_mean");
                            sb.append("\t\t " + (titleA++) + " " + word_mean + "\n");
                            JSONArray ljs = mean.getJSONArray("ljs");
                            if (ljs == null) {
                                continue;
                            }
                            for (Object oooo : ljs) {
                                /**
                                 * 遍历每种例子
                                 */
                                JSONObject lj = JSON.parseObject(oooo.toString());
                                String ly = lj.getString("ly");
                                String ls = lj.getString("ls");
                                sb.append("\t\t\t* " + ls + "\n");
                                sb.append("\t\t\t  " + ly + "\n");
                            }
                        }
                    }
                }
            }
            System.out.println(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Search Error");
        }
    }

    public static void main(String[] args) throws IOException {
        searchChn("车");
    }
}
