package com.sososeen09.module.common;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by yunlong.su on 2018/3/22.
 */

public class CommonApp extends Application {

    private static final String TAG = "CommonApp";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.e(TAG, "attachBaseContext: " + getPackageName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: " + getPackageName());
    }
}
