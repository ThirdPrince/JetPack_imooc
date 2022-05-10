package com.jetpack_imooc.libnavannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author dhl
 * @version V1.0
 * @Title: ActivityDestination$
 * @Package
 * @Description: Activity Destination$
 * @date 2022 04 25
 */
@Target(ElementType.TYPE)
public @interface ActivityDestination {
    String pageUrl();
    boolean needLogin() default false;
    boolean asStarter() default false;
}
