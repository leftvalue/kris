package extensions;

import basetool.Colors;
import basetool.EscapeUnescape;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.fusesource.jansi.Ansi;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * @author linxi
 * www.leftvalue.top
 * extensions
 * Date 2018/8/5 11:18 PM
 */
public class YoudaoDict {
    private static final String API = "YouDaoCV";
    private static final String API_KEY = "659600698";

    private static JSONObject visit(String keyword) throws IOException {
        String body = Jsoup.connect("http://fanyi.youdao.com/openapi.do?keyfrom=" +
                API + "&key=" +
                API_KEY + "&type=data&doctype=json&version=1.2&q=" + URLEncoder.encode(keyword)).timeout(10000).ignoreContentType(true)
                .ignoreHttpErrors(true)
                .execute().body();
        JSONObject ob = JSON.parseObject(body);
        return ob;
    }

    public static void searchEng(String keyword) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(keyword + "\n");
            JSONObject ob = visit(keyword);
            if (ob.getInteger("errorCode") == 0) {
                JSONObject basic = ob.getJSONObject("basic");
                String uk = basic.getString("uk-phonetic");
                String uk_speech = basic.getString("speech");

                String us = basic.getString("us-phonetic");
                String us_speech = basic.getString("us-speech");

                sb.append("US: [" + Colors.getCS(Ansi.Color.GREEN, us) + "]  UK: [" + Colors.getCS(Ansi.Color.GREEN, uk) + "]\n");
                JSONArray explains = basic.getJSONArray("explains");
                if (explains.size() > 0) {
                    sb.append("\t" + Colors.getCS(Ansi.Color.CYAN, "Word Explanation:") + "\n");
                }
                for (Object o : explains) {
                    sb.append("\t\t* " + o.toString() + "\n");
                }
                JSONArray web = ob.getJSONArray("web");
                if (web.size() > 0) {
                    sb.append("\t" + Colors.getCS(Ansi.Color.CYAN, "Web Reference:") + "\n");
                }
                for (Object w : web) {
                    JSONObject mean = JSON.parseObject(w.toString());
                    String k = mean.getString("key");
                    String v = mean.getJSONArray("value").stream().map(o -> o.toString()).collect(Collectors.joining(";"));
                    sb.append("\t\t* " + Colors.getCS(Ansi.Color.YELLOW, k) + "\n");
                    sb.append("\t\t  " + v + "\n");
                }
                System.out.println(sb.toString());
            } else {
                System.out.println("Search Error");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        YoudaoDictComplete.getCandidates("beaut").forEach(v -> System.out.println(v));
    }
}

class YoudaoDictComplete {
    static String visit(String keyword) throws IOException {
        Connection.Response resp = Jsoup.connect("https://dsuggest.ydstatic.com/suggest.s?query=" + URLEncoder.encode(keyword) +
                "&keyfrom=dict2.index.suggest&o=form&rn=20&h=0&le=eng")
                .ignoreContentType(true).ignoreHttpErrors(true).timeout(10000).execute();
        String body = resp.body();
        body = body.substring(body.indexOf("form.updateCall(\"") + "form.updateCall(\"".length(), body.lastIndexOf("\");"));
        body = EscapeUnescape.unescape(body);
        return body;
    }

    public static LinkedList<String> getCandidates(String keyword) {
        LinkedList<String> candidates = new LinkedList<>();
        try {
            String html = visit(keyword);
            org.jsoup.nodes.Document doc = Jsoup.parse(html);
            Elements tds = doc.select("td[align=\"left\"][bgcolor=\"white\"][class=\"remindtt75\"][colspan=\"2\"]");
            for (Element td : tds) {
                candidates.add(td.text());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (candidates.isEmpty()) {
            System.out.println("别找啦,没有你想要的联想");
        }
        return candidates;
    }
}