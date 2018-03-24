package com.sososeen09.modular.sample;

import android.os.Parcelable;
import com.sososeen09.modular.api.EasyRouter;
import com.sososeen09.modular.api.template.IExtra;
import com.sososeen09.module.common.TestService;
import java.lang.Object;
import java.lang.Override;

public class SecondActivity$$Extra implements IExtra {
  @Override
  public void loadExtra(Object target) {
    SecondActivity t = (SecondActivity)target;
    t.a = t.getIntent().getStringExtra("a");
    t.b = t.getIntent().getIntExtra("b", t.b);
    t.c = t.getIntent().getShortExtra("c", t.c);
    t.d = t.getIntent().getLongExtra("d", t.d);
    t.e = t.getIntent().getFloatExtra("e", t.e);
    t.f = t.getIntent().getDoubleExtra("f", t.f);
    t.g = t.getIntent().getByteExtra("g", t.g);
    t.h = t.getIntent().getBooleanExtra("h", t.h);
    t.i = t.getIntent().getCharExtra("i", t.i);
    t.aa = t.getIntent().getStringArrayExtra("aa");
    t.bb = t.getIntent().getIntArrayExtra("bb");
    t.cc = t.getIntent().getShortArrayExtra("cc");
    t.dd = t.getIntent().getLongArrayExtra("dd");
    t.ee = t.getIntent().getFloatArrayExtra("ee");
    t.ff = t.getIntent().getDoubleArrayExtra("ff");
    t.gg = t.getIntent().getByteArrayExtra("gg");
    t.hh = t.getIntent().getBooleanArrayExtra("hh");
    t.ii = t.getIntent().getCharArrayExtra("ii");
    t.j = t.getIntent().getParcelableExtra("j");
    Parcelable[] jj = t.getIntent().getParcelableArrayExtra("jj");
    if( null != jj) {
      t.jj = new TestParcelable[jj.length];
      for (int i = 0; i < jj.length; i++) {
        t.jj[i] = (TestParcelable)jj[i];
      }
    }
    t.k1 = t.getIntent().getParcelableArrayListExtra("k1");
    t.k2 = t.getIntent().getParcelableArrayListExtra("k2");
    t.k3 = t.getIntent().getStringArrayListExtra("k3");
    t.k4 = t.getIntent().getIntegerArrayListExtra("k4");
    t.test = t.getIntent().getIntExtra("hhhhhh", t.test);
    t.testService1 = (TestService) EasyRouter.getInstance().build("/main/service1").navigation();
    t.testService2 = (TestService) EasyRouter.getInstance().build("/main/service2").navigation();
    t.testService3 = (TestService) EasyRouter.getInstance().build("/module1/service").navigation();
    t.testService4 = (TestService) EasyRouter.getInstance().build("/module2/service").navigation();
  }
}
