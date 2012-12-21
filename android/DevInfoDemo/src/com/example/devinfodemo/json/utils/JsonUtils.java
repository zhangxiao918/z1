
package com.example.devinfodemo.json.utils;

import com.example.devinfodemo.json.bean.Phone;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @ClassName: JsonUtils
 * @Description: TODO
 * @author Bluestome.Zhang
 * @date 2012-10-8 下午02:41:40
 */
public class JsonUtils {

    public static JSONObject toJsonFromObject(Phone obj) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("date", obj.getCurrentTime());
        json.put("clientIp", obj.getIp());
        JSONObject hd = new JSONObject();
        json.put("hardware", hd);

        // hd.put("", value)
        JSONObject sd = new JSONObject();
        json.put("software", sd);
        return json;
    }
}
