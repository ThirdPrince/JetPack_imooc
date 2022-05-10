package com.jetpack_imooc.utils;

import android.content.ComponentName;

import androidx.fragment.app.FragmentActivity;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;

import com.jetpack.libcommon.utils.AppGlobals;
import com.jetpack_imooc.FixFragmentNavigator;
import com.jetpack_imooc.model.Destination;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dhl
 * @version V1.0
 * @Title: NavGraphBuilder
 * @Package
 * @Description: $(用一句话描述)
 * @date 2022 0425
 */
public class NavGraphBuilder {

    public static void build(FragmentActivity fragmentActivity,NavController controller,int containerId){

        NavigatorProvider provider = controller.getNavigatorProvider();
        FixFragmentNavigator fragmentNavigator = new FixFragmentNavigator(fragmentActivity,fragmentActivity.getSupportFragmentManager(),containerId);
        provider.addNavigator(fragmentNavigator);
                //provider.getNavigator(FragmentNavigator.class);
        ActivityNavigator activityNavigator = provider.getNavigator(ActivityNavigator.class);

        Map<String, Destination> destConfig = AppConfig.getSDesConfig();

        NavGraph navGraph  = new NavGraph(new NavGraphNavigator(provider));
        for (Destination value:destConfig.values()){
            if(value.isFragment()){
                FixFragmentNavigator.Destination destination = fragmentNavigator.createDestination();
                destination.setClassName(value.getClassName());
                destination.setId(value.getId());
                destination.addDeepLink(value.getPageUrl());
                navGraph.addDestination(destination);
            }else{
                ActivityNavigator.Destination destination = activityNavigator.createDestination();
                destination.setComponentName(new ComponentName(AppGlobals.getApplication().getPackageName(),value.getClassName()));
                destination.setId(value.getId());
                destination.addDeepLink(value.getPageUrl());
                navGraph.addDestination(destination);
            }
            //给APP页面导航结果图 设置一个默认的展示页的id
            if (value.getAsStarter()) {
                navGraph.setStartDestination(value.getId());
            }
        }

        controller.setGraph(navGraph);





    }
}
