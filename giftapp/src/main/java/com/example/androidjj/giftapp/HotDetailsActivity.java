package com.example.androidjj.giftapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HotDetailsActivity extends AppCompatActivity {

    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_hot_detail);
        initView();
    }

    private void initView() {
        backBtn = (Button) findViewById(R.id.hot_back_btn);
    }
    public void click (View view){
        switch (view.getId()){
            case R.id.hot_back_btn:
                finish();
                break;
        }
    }
}
