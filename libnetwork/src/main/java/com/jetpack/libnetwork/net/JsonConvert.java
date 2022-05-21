package com.jetpack.libnetwork.net;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.lang.reflect.Type;

/**
 * @author dhl
 * @version V1.0
 * @Title: JsonConvert
 * @Package $
 * @Description: JsonConvert  使用阿里的JSON 可以序列化
 * @date 2022 0425
 */
public class JsonConvert implements Convert {


    //默认的Json转 Java Bean的转换器
    @Override
    public Object convert(String response, Type type) {
        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response);
        JSONObject data = jsonObject.getJSONObject("data");
        if (data != null) {
            Object data1 = data.get("data");
            return JSON.parseObject(data1.toString(), type);
        }
        return null;
    }

    @Override
    public Object convert(String response, Class claz) {
        return null;
    }


}
