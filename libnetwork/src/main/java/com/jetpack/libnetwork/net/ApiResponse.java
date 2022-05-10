package com.jetpack.libnetwork.net;

/**
 * @author dhl
 * @version V1.0
 * @Title:
 * @Package $
 * @Description: ApiResponse
 * @date 2022 0425
 */
public  class ApiResponse<T> {

    public boolean success;
    public int status;
    public String message;
    public T body;

}
