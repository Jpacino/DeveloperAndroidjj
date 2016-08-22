package com.example.androidjj.giftapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        backBtn = (Button) findViewById(R.id.login_back_btn);
    }
    public void click (View view){
        switch (view.getId()){
            case R.id.login_back_btn:
                finish();
                break;
            case R.id.login_register_btn:
                break;

        }
    }
}
