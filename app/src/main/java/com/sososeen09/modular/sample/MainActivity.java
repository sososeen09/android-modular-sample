package com.sososeen09.modular.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sososeen09.modular.annotation.Router;
import com.sososeen09.modular.api.ActivityRouterInjector;

/**
 * Created by yunlong.su on 2018/3/22.
 */

@Router(path = "test/module1")
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityRouterInjector.inject(this);
    }
}
