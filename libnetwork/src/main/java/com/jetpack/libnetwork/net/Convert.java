package com.jetpack.libnetwork.net;

import java.lang.reflect.Type;

/**
 * @author dhl
 * @version V1.0
 * @Title: Convert
 * @Package $
 * @Description: Convert
 * @date 2022 0425
 */
public interface Convert<T> {
    T convert(String response, Type type);

    T convert(String response, Class claz);

}
