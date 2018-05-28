package com.sososeen09.module.test1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.sososeen09.modular.annotation.Extra;
import com.sososeen09.modular.annotation.Route;
import com.sososeen09.modular.api.EasyRouter;

@Route(path = "/module1/article")
public class ArticleActivity extends AppCompatActivity {

    @Extra
    String title;

    TextView tvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        EasyRouter.getInstance().inject(this);
        tvTitle = findViewById(R.id.tv_title);

        tvTitle.setText(title);
    }
}
