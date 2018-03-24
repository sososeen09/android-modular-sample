package com.sososeen09.modular.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.sososeen09.modular.annotation.Extra;
import com.sososeen09.modular.annotation.Route;
import com.sososeen09.modular.api.EasyRouter;
import com.sososeen09.module.common.TestService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yunlong
 */
@Route(path = "/main/test")
public class SecondActivity extends AppCompatActivity {
    private static final String TAG = "SecondActivity";
    @Extra
    String a;
    @Extra
    int b;
    @Extra
    short c;
    @Extra
    long d;
    @Extra
    float e;
    @Extra
    double f;
    @Extra
    byte g;
    @Extra
    boolean h;
    @Extra
    char i;


    @Extra
    String[] aa;
    @Extra
    int[] bb;
    @Extra
    short[] cc;
    @Extra
    long[] dd;
    @Extra
    float[] ee;
    @Extra
    double[] ff;
    @Extra
    byte[] gg;
    @Extra
    boolean[] hh;
    @Extra
    char[] ii;

    @Extra
    TestParcelable j;
    @Extra
    TestParcelable[] jj;


    @Extra
    List<TestParcelable> k1;
    @Extra
    ArrayList<TestParcelable> k2;

    @Extra
    List<String> k3;

    @Extra
    List<Integer> k4;

    @Extra(name = "hhhhhh")
    int test;

    @Extra(name = "/main/service1")
    TestService testService1;
    @Extra(name = "/main/service2")
    TestService testService2;
    @Extra(name = "/module1/service")
    TestService testService3;
    @Extra(name = "/module2/service")
    TestService testService4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        EasyRouter.getInstance().inject(this);

        Log.e(TAG, toString());
    }

    @Override
    public String toString() {
        return "SecondActivity{" +
                "a='" + a + '\'' +
                ", b=" + b +
                ", c=" + c +
                ", d=" + d +
                ", e=" + e +
                ", f=" + f +
                ", g=" + g +
                ", h=" + h +
                ", i=" + i +
                ", aa=" + Arrays.toString(aa) +
                ", bb=" + Arrays.toString(bb) +
                ", cc=" + Arrays.toString(cc) +
                ", dd=" + Arrays.toString(dd) +
                ", ee=" + Arrays.toString(ee) +
                ", ff=" + Arrays.toString(ff) +
                ", gg=" + Arrays.toString(gg) +
                ", hh=" + Arrays.toString(hh) +
                ", ii=" + Arrays.toString(ii) +
                ", j=" + j +
                ", jj=" + Arrays.toString(jj) +
                ", k1=" + k1 +
                ", k2=" + k2 +
                ", k3=" + k3 +
                ", k4=" + k4 +
                '}';
    }
}
