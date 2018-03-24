package com.sososeen09.module.test1;

import android.util.Log;

import com.sososeen09.modular.annotation.Route;
import com.sososeen09.module.common.TestService;

/**
 * Created by yunlong on 2018/3/24.
 */

@Route(path = "/module1/service")
public class TestServiceImpl implements TestService {

    @Override
    public void test() {
        Log.e("Service", "我是Module1模块测试服务通信");
    }

}
