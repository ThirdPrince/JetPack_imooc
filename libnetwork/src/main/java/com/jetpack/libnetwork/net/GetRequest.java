package com.jetpack.libnetwork.net;

/**
 * @author dhl
 * @version V1.0
 * @Title: $
 * @Package $
 * @Description: $(用一句话描述)
 * @date 2022 0425
 */
public class GetRequest<T> extends Request<T, GetRequest> {


    public GetRequest(String url) {
        super(url);
    }

    @Override
    protected okhttp3.Request generateRequest(okhttp3.Request.Builder builder) {
        okhttp3.Request request = builder.get().url(UrlCreator.createUrlFormParams(mUrl, params)).build();
        return request;
    }


}
