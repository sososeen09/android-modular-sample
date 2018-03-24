package com.sososeen09.modular.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sososeen09.modular.annotation.Route;

/**
 * @author yunlong
 */
@Route(path = "/test/second")
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }
}
