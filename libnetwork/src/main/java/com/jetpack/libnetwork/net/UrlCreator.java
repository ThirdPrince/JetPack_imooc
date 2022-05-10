package com.jetpack.libnetwork.net;

import java.util.Map;

import okhttp3.HttpUrl;

/**
 * @author dhl
 * @version V1.0
 * @Title: $
 * @Package $
 * @Description: UrlCreator
 * @date 2022 0425
 */
public class UrlCreator {

    public static  HttpUrl createUrlFormParams(String url , Map<String,Object> params){
        HttpUrl httpUrl = HttpUrl.parse(url);
        if (httpUrl == null) {
            return null;
        }
        if (params != null) {
            HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(),  entry.getValue()+"");
            }
            httpUrl = urlBuilder.build();
        }
        return  httpUrl;

    }
}
