package extensions;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.*;

import static basetool.KJson.parseJSONArray;

/**
 * @author linxi
 * www.leftvalue.top
 * extensions
 * Date 2018/8/8 11:11 AM
 * todo: 破解 iconfont
 */
public class IconFonts {
    public static void main(String[] args) throws Exception {
        String dir = "icons";
        File fdir = new File(dir);
        if (!fdir.exists()) {
            fdir.mkdirs();
        }

        Connection.Response response = Jsoup.connect("http://www.iconfont.cn/api/icon/search.json")
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:62.0) Gecko/20100101 Firefox/62.0")
                .referrer("http://www.iconfont.cn/search/index?searchType=icon&q=rai")
                .cookie("ctoken", "NGmL7A7zZHW35ocaz8Zyicon-font")
                .cookie("trace", "AQAAAEEb0G3E9QkARKyHPW42xAQqpLY/")
                .cookie("cna", "zEzxE18s+koCAT2HrERvni/C")
                .cookie("EGG_SESS_ICONFONT=1EdpmC3Guiw93DXBnxo3XEI4KnUAx6gol44mVeuW0EhORMU08mvqkPrqtFFCt4t-gNMlLtL6nrFK-mpn0xtuaynSqsKAn9QePGyyljXaNll15Zfe56vsGht-WMBeTYmeHK3jthYltF-Fup5GbRykZcRp8EkTYesfGOYgnDa48OA", "")
                .cookie("isg", "BKGhm0RsXhrxn_LuLeosJh0as2uxBmwPgaQbLwN3pqg6asY8WZ3wEIzsyFbJua14")
                .header("Accept", "application/json, text/javascript, */*; q=0.01")
                .header("X-Requested-With", "XMLHttpRequest")
                .header("Cache-Control", "no-cache")
                .header("Connection", "keep-alive")
                .header("Accept-Language", "en-US,en;q=0.5")
                .header("DNT", "1")
                .header("Pragma", "no-cache")
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .data("ctoken", "NGmL7A7zZHW35ocaz8Zyicon-font")
                .data("q", "rai")
                .data("t", "1533697773154")
                .data("sortType", "updated_at")
                .data("pageSize", "54")
                .data("page", "1")
                .data("fromCollection", "1")
                .data("fills", "null")
                .method(org.jsoup.Connection.Method.POST)
                .ignoreContentType(true)
                .timeout(10000)
                .execute();
        String body = response.body();
        JSONObject root = JSON.parseObject(body);
        if (root.getInteger("code") == 200) {
            JSONObject data = root.getJSONObject("data");
            int count = data.getInteger("count");
            JSONArray icons = data.getJSONArray("icons");
            for (JSONObject icon : parseJSONArray(icons)) {
                int id = icon.getInteger("id");
                String name = icon.getString("name");
                String svg = icon.getString("svg");
                System.out.println(svg);
                String path = fdir.getAbsolutePath() + "/" + id + ".png";
//                convertToPng(svg, path);
                System.exit(1);
            }
        }
    }

    /**
     * 将svg字符串转换为png
     *
     * @param svgCode     svg代码
     * @param pngFilePath 保存的路径
     */
    public static void convertToPng(String svgCode, String pngFilePath) throws IOException {

        File file = new File(pngFilePath);
        FileOutputStream outputStream = null;
        try {
            file.createNewFile();
            outputStream = new FileOutputStream(file);
            convertToPng(svgCode, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将svgCode转换成png文件，直接输出到流中
     *
     * @param svgCode      svg代码
     * @param outputStream 输出流
     * @throws TranscoderException 异常
     * @throws IOException         io异常
     */
    public static void convertToPng(String svgCode, OutputStream outputStream)
            throws TranscoderException, IOException {
        try {
            byte[] bytes = svgCode.getBytes("utf-8");
            PNGTranscoder t = new PNGTranscoder();
            TranscoderInput input = new TranscoderInput(new ByteArrayInputStream(bytes));
            TranscoderOutput output = new TranscoderOutput(outputStream);
            t.transcode(input, output);
            outputStream.flush();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
