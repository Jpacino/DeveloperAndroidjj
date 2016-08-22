package com.example.androidjj.giftapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GiftActivity extends AppCompatActivity {

    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_gift);
        initView();
    }

    private void initView() {
        backBtn = (Button) findViewById(R.id.my_gift_back_btn);
    }
    public void click(View view){
        switch (view.getId()){
            case R.id.my_gift_back_btn:
                finish();
                break;
            case R.id.my_gift_login_btn:
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
