package com.jetpack_imooc.libnavannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author dhl
 * @version V1.0
 * @Title: FragmentDestination
 * @Package
 * @Description: FragmentDestination
 * @date 2022 04 22
 */
@Target(ElementType.TYPE)
public @interface FragmentDestination {

    String pageUrl();
    boolean needLogin() default  false;
    boolean asStarter() default  false;

}
