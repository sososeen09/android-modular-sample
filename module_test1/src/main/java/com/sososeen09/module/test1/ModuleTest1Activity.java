package com.sososeen09.module.test1;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.sososeen09.modular.annotation.Extra;
import com.sososeen09.modular.annotation.Route;
import com.sososeen09.modular.api.EasyRouter;
import com.sososeen09.module.common.TestService;


/**
 * Created by yunlong.su on 2018/3/22.
 */

@Route(path = "/module1/test")
public class ModuleTest1Activity extends Activity {
    @Extra
    String msg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_test1);
        EasyRouter.getInstance().inject(this);
        Log.e("module1", "我是模块1:" + msg);


        TestService testService = (TestService) EasyRouter.getInstance().build("/main/service1")
                .navigation();
        testService.test();

        TestService testService1 = (TestService) EasyRouter.getInstance().build("/main/service2")
                .navigation();
        testService1.test();

        TestService testService2 = (TestService) EasyRouter.getInstance().build("/module1/service")
                .navigation();
        testService2.test();

        TestService testService3 = (TestService) EasyRouter.getInstance().build("/module2/service")
                .navigation();
        testService3.test();
    }

    public void mainJump(View view) {
        EasyRouter.getInstance().build("/main/test").withString("a",
                "从Module1").navigation(this);
    }

    public void module2Jump(View view) {
        EasyRouter.getInstance().build("/module2/test").withString("msg",
                "从Module1").navigation(this);
    }
}
