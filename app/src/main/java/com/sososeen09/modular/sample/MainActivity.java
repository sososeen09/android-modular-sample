package com.sososeen09.modular.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sososeen09.modular.api.ActivityRouterInjector;

/**
 * Created by yunlong.su on 2018/3/22.
 */

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityRouterInjector.inject(this);
        if (BuildConfig.isModule) {

        }
    }

    public void innerJump(View view) {

    }

    public void module1Jump(View view) {

    }

    public void module2Jump(View view) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
