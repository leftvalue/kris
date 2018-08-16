package extensions;

import basetool.EscapeUnescape;
import com.alibaba.fastjson.JSON;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.HashMap;

/**
 * @author linxi
 * www.leftvalue.top
 * extensions
 * Date 2018/8/8 10:03 AM
 * 生成吃鸡的分享外链
 * todo: 根据击杀数自动调整其他参数 , 增加真实感
 */
public class EatChicken {
    public static void main(String[] args) throws Exception {
        String qq = "1072509797";
        String name_url = "http://users.qzone.qq.com/fcg-bin/cgi_get_portrait.fcg?uins=" + qq;
        Connection.Response resp = Jsoup.connect(name_url).execute();
        System.out.println(resp.body());
        String nickname = JSON.parseObject(resp.body().replace("portraitCallBack(", "").replaceAll("\\)$", "")).getJSONArray(qq).get(6).toString();
        System.out.println(nickname);
        String icon_url = "http://qlogo2.store.qq.com/qzone/" + qq + "/" + qq + "/100";
        HashMap<String, String> data = new HashMap<>();
        data.put("changerating", "");
        data.put("currentroundrank", "1");
        data.put("damageamount", "72323");
        data.put("dteventtime", "2018-07-15 22,33,51");
        data.put("flowratinglevel", "SSS");
        data.put("headUri", icon_url);
        data.put("headshotrate", "76%");
        data.put("healtimes", "2");
        data.put("killingcount", "16");
        data.put("mode", "103");
        data.put("movingdistance", "5259");
        data.put("ratingvalueafterchanged", "1264");
        data.put("rolename", nickname);
        data.put("sumscore", "9030");
        data.put("survivaltime", "2324");
        String prefix = "http://qzs.qq.com/open/yyb/yyb_game_chiji/release/html/record-detail.html?type=share&";
        String profix = "from=singlemessage";
        String url = prefix + "data=" + EscapeUnescape.escape(JSON.toJSONString(data)) + "&" + profix;
        System.out.println(url);
        url = new AShortUrl().execute(url);
        System.out.println(url);
    }
}
