package extensions;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;


/**
 * @author linxi
 * www.leftvalue.top
 * extensions
 * Date 2018/7/27 2:42 PM
 * dwz.cn
 * 百度短网址服务
 */
public class AShortUrl extends AbstractAction {
    @Override
    public String execute(String longurl) {
        try {
            JSONObject body_ = new JSONObject();
            body_.put("url", longurl);
            String body = body_.toJSONString();
            Connection.Response response = Jsoup.connect("http://dwz.cn/admin/create")
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.14; rv:61.0) Gecko/20100101 Firefox/61.0")
                    .referrer("http://dwz.cn/")
                    .requestBody(body)
                    .cookie("Hm_lpvt_3527aa5bda2fb7e5cde48482e280bd9b", "1532672196")
                    .cookie("Hm_lvt_3527aa5bda2fb7e5cde48482e280bd9b", "1532672196")
                    .header("Accept", "application/json, text/javascript, */*; q=0.01")
                    .header("X-Requested-With", "XMLHttpRequest")
                    .header("Cache-Control", "no-cache")
                    .header("Connection", "keep-alive")
                    .header("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2")
                    .header("Pragma", "no-cache")
                    .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                    .method(org.jsoup.Connection.Method.POST)
                    .ignoreContentType(true)
                    .timeout(10000)
                    .execute();
            String resp_test = response.body();
            JSONObject result = JSONObject.parseObject(resp_test);
            if (result.getInteger("Code") == 0) {
                return result.getString("ShortUrl");
            } else {
                return result.getString("ErrMsg");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "缩短目标网址失败(" + e.getMessage() + ")";
        }
    }

    public static void main(String[] args) {
        System.out.println(new AShortUrl().execute("http://data.v.qq.com/videocms/youyunnan/index.php?city=怒江&ts=1532506403&signature=6b31613c1886dae3f68e44af6af0f428"));
    }
}
