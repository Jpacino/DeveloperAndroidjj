package com.example.androidjj.giftapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidxx.yangjw.httplibrary.HttpUtils;
import com.androidxx.yangjw.httplibrary.ICallback;
import com.androidxx.yangjw.imageloader.ImageLoader;
import com.example.androidjj.giftapp.JavaBean.DetailsBean;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class GiftDetailsActivity extends AppCompatActivity implements ICallback{
    public static final String TAG = "androidjjj";
    public static final String GIFT_DETAILS_URL = "http://www.1688wan.com/majax.action?method=getGiftInfo";
    private Map<String,Object> giftMaps = new HashMap<>();
    private String jsonString;
    private Intent intent;
    private String ids;
    private TextView explainsTv;
    private DetailsBean infos;
    private TextView exchangesTv;
    private TextView overtimeTv;
    private TextView nameTv;
    private CircleImageView circleIv;
    private TextView descsTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_gift_details);
        initView();
        reloadData();


    }

    private void reloadData() {

    }

    private void initView() {
        initData();
        explainsTv = (TextView) findViewById(R.id.detail_content1_tv);
        exchangesTv = (TextView) findViewById(R.id.detail_exchanges_tv);
        overtimeTv = (TextView) findViewById(R.id.detail_overtime_tv);
        nameTv = (TextView) findViewById(R.id.detail_name_tv);
        circleIv = (CircleImageView) findViewById(R.id.detail_circle_iv);
        descsTv = (TextView) findViewById(R.id.detail_content2_tv);
    }



    public void click (View view){
        switch (view.getId()){
            case R.id.gift_back_btn:
                finish();
                break;
            case R.id.detail_bottom_btn:
                Intent loginIntent = new Intent(this,LoginActivity.class);
                startActivity(loginIntent);
        }
    }
    private void initData() {
        intent = getIntent();
        ids = intent.getStringExtra("id");
        giftMaps.clear();
        giftMaps.put("id",ids);
        HttpUtils.load(GIFT_DETAILS_URL).post(giftMaps).callback(this,1);
    }

    @Override
    public void success(String result, int requestCode) {
        if (requestCode == 1){
            jsonString = result;
        }
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String infoString = jsonObject.getString("info");
            Gson gson = new Gson();
            infos = gson.fromJson(infoString,DetailsBean.class);
            nameTv.setText(infos.getGname());
            overtimeTv.setText("有效期:"+infos.getOvertime());
            exchangesTv.setText("剩余:"+String.valueOf(infos.getExchanges()));
            explainsTv.setText(infos.getExplains());
            descsTv.setText(infos.getDescs());
            ImageLoader.init(this).load("http://www.1688wan.com"+infos.getIconurl(),circleIv);

        } catch (JSONException e) {
        }









    }
}
