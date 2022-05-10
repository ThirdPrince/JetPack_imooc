package com.jetpack.libnetwork.net;

import java.util.Map;

import okhttp3.FormBody;

/**
 * @author dhl
 * @version V1.0
 * @Title: PostRequest
 * @Package
 * @Description: PostRequest
 * @date 2022 0425
 */
public class PostRequest<T> extends Request<T,PostRequest> {

    public PostRequest(String url) {
        super(url);
    }

    @Override
    protected okhttp3.Request generateRequest(okhttp3.Request.Builder builder) {
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        for(Map.Entry<String,Object> entry:params.entrySet()){
            bodyBuilder.add(entry.getKey(),String.valueOf(entry.getValue()));
        }
        okhttp3.Request request =    builder.url(mUrl).post(bodyBuilder.build()).build();
        return request;
    }
}
