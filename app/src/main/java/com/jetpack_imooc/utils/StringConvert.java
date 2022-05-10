package com.jetpack_imooc.utils;

/**
 * @author dhl
 * @version V1.0
 * @Title: StringConvert
 * @Package
 * @Description: StringConvert
 * @date 2022 0426
 */
public class StringConvert {

    public static String convertFeedUgc(int count){
        if(count < 10000){
            return String.valueOf(count);
        }

        return count/10000 + "万";
    }
}
