package com.jetpack.libnetwork.net;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import androidx.annotation.IntDef;
import androidx.arch.core.executor.ArchTaskExecutor;

import com.jetpack.libnetwork.cache.Cache;
import com.jetpack.libnetwork.cache.CacheManager;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author dhl
 * @version V1.0
 * @Title: $
 * @Package $
 * @Description: Request
 * @date 2022 0425
 */
public abstract class Request<T, R extends Request> implements Cloneable{

    protected String mUrl;
    protected HashMap<String, String> headers = new HashMap<>();
    protected HashMap<String, Object> params = new HashMap<>();


    private String cacheKey;

    private Type mType;
    /**
     * 缓存策略
     * 1,只访问缓存
     * 2，先访问缓存，同时发起网络请求，成功后缓存本地
     * 3，仅仅访问服务器，不存任何存储
     * 4，先访问网络，成功后缓存到本地
     */

    public static final int CACHE_ONLY = 1;

    public static final int CACHE_FIRST = 2;

    public static final int NET_ONLY = 3;

    public static final int NET_CACHE = 4;

    private int mCacheStrategy = NET_CACHE;


    @IntDef({CACHE_ONLY, CACHE_FIRST, NET_ONLY, NET_CACHE})
    public @interface CacheStrategy {

    }

    public Request(String url) {

        mUrl = url;
    }
    public R addHeader(String key, String value) {
        headers.put(key, value);
        return (R) this;
    }


    public R addParam(String key, Object value) {
        if (value == null) {
            return (R) this;
        }
        //int byte char short long double float boolean 和他们的包装类型，但是除了 String.class 所以要额外判断
        try {
            if (value.getClass() == String.class) {
                params.put(key, value);
            } else {
                Field field = value.getClass().getField("TYPE");
                Class claz = (Class) field.get(null);
                if (claz.isPrimitive()) {
                    params.put(key, value);
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return (R) this;
    }

    public R cacheStrategy(@CacheStrategy int cacheStrategy) {
        mCacheStrategy = cacheStrategy;
        return (R) this;
    }

    public R cacheKey(String key) {
        this.cacheKey = key;
        return (R) this;
    }

    public R responseType(Type type){
        mType = type;
        return (R)this;
    }

    /**
     * 同步方法
     * @return
     */
    public ApiResponse<T> execute(){
        if (mType == null){
            throw new RuntimeException("mType 不能为null");
        }
        if(mCacheStrategy == CACHE_ONLY){
            return readCache();
        }
        if(mCacheStrategy != CACHE_ONLY){
            ApiResponse<T> result = null;
            try{
                Response response = getCall().execute();
                result = parseResponse(response,null);
            }catch (IOException e){
                e.printStackTrace();
                if(result == null){
                    result = new ApiResponse<>();
                    result.message = e.getMessage();
                }
            }
            return result;
        }
       return null;
    }

    @SuppressLint("RestrictedApi")
    public void execute(JsonCallback<T> callback) {

        if (mCacheStrategy != NET_ONLY) {
            ArchTaskExecutor.getIOThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {

                    ApiResponse<T> response = readCache();
                    if (callback != null){
                        callback.onCacheSuccess(response);
                    }

                }
            });
        }

        getCall().enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                ApiResponse<T> response = new ApiResponse<>();
                response.message = e.getMessage();
                callback.onError(response);

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ApiResponse<T> responseSuccess = parseResponse(response, callback);
                if (responseSuccess.success) {
                    callback.onSuccess(responseSuccess);
                } else {
                    callback.onError(responseSuccess);
                }

            }
        });
    }

    protected ApiResponse<T> readCache() {
        String key = TextUtils.isEmpty(cacheKey) ? generateCacheKey() : cacheKey;
        Object cache = CacheManager.getCache(key);
        ApiResponse<T> result = new ApiResponse<>();
        result.status = 304;
        result.message = "缓存获取成功";
        result.body = (T) cache;
        result.success = true;
        return result;
    }

    private Call getCall() {
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        addHeaders(builder);
        okhttp3.Request request = generateRequest(builder);
        Call call = ApiService.okhttpClient.newCall(request);
        return call;

    }

    protected abstract okhttp3.Request generateRequest(okhttp3.Request.Builder builder);


    protected void addHeaders(okhttp3.Request.Builder builder) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
    }

    private ApiResponse<T> parseResponse(Response response, JsonCallback<T> callback) {
        String message = null;
        int status = response.code();
        boolean success = response.isSuccessful();
        ApiResponse<T> result = new ApiResponse<>();
        Convert convert = ApiService.sConvert;
        try {
            String content = response.body().string();
            if (success) {
                if (callback != null) {
                    ParameterizedType type = (ParameterizedType) callback.getClass().getGenericSuperclass();
                    Type argument = type.getActualTypeArguments()[0];
                    result.body = (T) convert.convert(content, argument);
                }
            } else {
                message = content;
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = e.getMessage();
            success = false;
        }
        result.success = success;
        result.status = status;
        result.message = message;
        if (mCacheStrategy != NET_ONLY && result.success && result.body != null) {
            saveCache(result.body);
        }
        return result;

    }

    protected void saveCache(T body) {
        String key = TextUtils.isEmpty(cacheKey) ? generateCacheKey() : cacheKey;
        CacheManager.save(key, body);
    }

    protected String generateCacheKey() {
        cacheKey = UrlCreator.createUrlFormParams(mUrl, params).toString();
        return cacheKey;
    }
}
