package com.jetpack.libnetwork.net;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * @author dhl
 * @version V1.0
 * @Title: JsonConvert
 * @Package $
 * @Description: JsonConvert
 * @date 2022 0425
 */
public class JsonConvert implements Convert {

    private Gson gson = new Gson();

    @Override
    public Object convert(String response, Type type) {
        //JSONObject jsonObject = JSON.parseObject(response);
        //JSONObject data = jsonObject.getJSONObject("data");
        try {
            JSONObject jsonObject = new JSONObject(response).getJSONObject("data");
            JSONArray data = jsonObject.getJSONArray("data");
            if(data != null){
                //JSONObject data1 = data.getJSONObject(0);
                Object o = gson.fromJson(data.toString(),type);
                return o;
                // JSON.parseObject(data1.toString(),type);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public Object convert(String response, Class claz) {
//        JSONObject jsonObject = JSON.parseObject(response);
//        JSONObject data = jsonObject.getJSONObject("data");
//        if(data != null){
//            Object data1 = data.get("data");
//            return JSON.parseObject(data1.toString(),claz);
//        }
        return null;
    }
}
