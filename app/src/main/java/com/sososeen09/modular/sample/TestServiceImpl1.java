package com.sososeen09.modular.sample;

import android.util.Log;

import com.sososeen09.modular.annotation.Route;
import com.sososeen09.module.common.TestService;

/**
 * Created by yunlong.su on 2018/3/22.
 */

@Route(path = "/main/service1")
public class TestServiceImpl1 implements TestService {

    private static final String TAG = "service";

    @Override
    public void test() {
        Log.e(TAG, "test: " + "主app模块中通信1");
    }
}
