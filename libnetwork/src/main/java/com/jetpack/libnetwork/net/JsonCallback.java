package com.jetpack.libnetwork.net;

/**
 * @author dhl
 * @version V1.0
 * @Title: JsonCallback
 * @Package $
 * @Description: JsonCallback
 * @date 2022 04 25
 */
public abstract class JsonCallback<T> {

    public void onSuccess(ApiResponse<T> response) {

    }

    public void onError(ApiResponse<T> response) {

    }
    public void onCacheSuccess(ApiResponse<T> response) {

    }

}
