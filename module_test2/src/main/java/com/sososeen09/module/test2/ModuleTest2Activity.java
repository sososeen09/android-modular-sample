package com.sososeen09.module.test2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.sososeen09.modular.annotation.Extra;
import com.sososeen09.modular.annotation.Route;
import com.sososeen09.modular.api.EasyRouter;
import com.sososeen09.module.common.TestService;


@Route(path = "/module2/test")
public class ModuleTest2Activity extends Activity {

    @Extra
    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_test2ctivity);
        EasyRouter.getInstance().inject(this);
        Log.e("module2", "我是模块2:" + msg);

        //当处于组件模式的时候
        if (BuildConfig.isModule) {
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

    }

    public void mainJump(View view) {
        if (BuildConfig.isModule) {
            EasyRouter.getInstance().build("/main/test").withString("a",
                    "从Module2").navigation(this);
        } else {
            Toast.makeText(this, "当前处于组件模式,无法使用此功能", 0).show();
        }
    }

    public void module1Jump(View view) {
        EasyRouter.getInstance().build("/module1/test").withString("msg",
                "从Module2").navigation(this);
    }
}
