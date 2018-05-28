package com.sososeen09.module.test2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.sososeen09.modular.annotation.Extra;
import com.sososeen09.modular.annotation.Route;
import com.sososeen09.modular.api.EasyRouter;

@Route(path = "/module2/order")
public class OrderActivity extends AppCompatActivity {

    @Extra(name = "order")
    String orderInfo;

    TextView tvOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        EasyRouter.getInstance().inject(this);
        tvOrder = findViewById(R.id.tv_order);

        tvOrder.setText(orderInfo);
    }
}
