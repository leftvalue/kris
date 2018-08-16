package basetool;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.LinkedList;

/**
 * @author linxi
 * www.leftvalue.top
 * basetool
 * Date 2018/8/8 11:40 AM
 */
public class KJson {
    /**
     * parse JsonArray to LinkedList<JSONObject>
     *
     * @param array
     * @return
     */
    public static LinkedList<JSONObject> parseJSONArray(JSONArray array) {
        LinkedList<JSONObject> results = new LinkedList<>();
        for (Object o : array) {
            results.add(JSONObject.parseObject(o.toString()));
        }
        return results;
    }

    public static LinkedList<JSONObject> getKJsonArray(JSONObject object, String key) {
        return parseJSONArray(object.getJSONArray(key));
    }
}
