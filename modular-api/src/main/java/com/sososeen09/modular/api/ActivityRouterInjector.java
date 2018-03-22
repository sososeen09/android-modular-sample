package com.sososeen09.modular.api;

import android.app.Activity;

/**
 * Created by yunlong.su on 2018/3/22.
 */

public class ActivityRouterInjector {

    private static final String SUFFIX = "$$ActivityInject";


    public static void inject(Activity activity) {
        ActivityInject activityInject = findProxyActivity(activity);
        activityInject.inject(activity);
    }

    private static ActivityInject findProxyActivity(Activity activity) {
        try {
            Class clazz = activity.getClass();
            Class injectorClazz = Class.forName(clazz.getName() + SUFFIX);
            return (ActivityInject) injectorClazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new RuntimeException(String.format("can not find %s , something when compiler.", activity.getClass().getSimpleName() + SUFFIX));
    }

}
