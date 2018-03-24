package com.sososeen09.module.test2;

import android.util.Log;

import com.sososeen09.modular.annotation.Route;
import com.sososeen09.module.common.TestService;

/**
 * Created by yunlong on 2018/3/24.
 */

@Route(path = "/module2/service")
public class TestServiceImpl implements TestService {

    @Override
    public void test() {
        Log.e("Service", "我是Module2模块测试服务通信");
    }

}
