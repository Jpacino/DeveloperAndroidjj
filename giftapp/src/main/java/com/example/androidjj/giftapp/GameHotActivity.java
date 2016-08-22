package com.example.androidjj.giftapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidxx.yangjw.httplibrary.HttpUtils;
import com.androidxx.yangjw.httplibrary.ICallback;
import com.androidxx.yangjw.imageloader.ImageLoader;
import com.example.androidjj.giftapp.JavaBean.Details2Bean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameHotActivity extends AppCompatActivity implements ICallback {
    public static final String TAG = "androidjjj";
    public static final String GAME_HOT_URL = "http://www.1688wan.com/majax.action?method=getAppInfo";
    private Button backBtn;
    private Intent intent;
    private Map<String,Object> maps = new HashMap<>();
    private String gid;
    private String jsonString;
    private Details2Bean apps;
    private TextView nameTv;
    private TextView typeNameTv;
    private TextView sizeTv;
    private ImageView iconIv;
    private TextView descriptionTv;
    private List<Details2Bean> images = new ArrayList<>();
    private Context mContext;
    private HorizontalScrollView scrollView;
    private LinearLayout scrollViewll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_game_hot);
        mContext = this;
        initView();
    }


    private void initView() {
        initData();
        nameTv = (TextView) findViewById(R.id.gift_item_text_name);
        typeNameTv = (TextView) findViewById(R.id.hot_item_text_typename);
        sizeTv = (TextView) findViewById(R.id.gift_item_text_size);
        iconIv = (ImageView) findViewById(R.id.gift_item_image_icon);
        descriptionTv = (TextView) findViewById(R.id.special_left_content_tv);
        scrollView = (HorizontalScrollView) findViewById(R.id.game_hot_sv);
        scrollViewll = (LinearLayout) findViewById(R.id.game_hot_svll);


    }

    private void initData() {
        maps.clear();
        intent = getIntent();

        gid = intent.getStringExtra("gid");

        Log.d(TAG, "gid: "+gid);
        maps.put("id",gid);
        HttpUtils.load(GAME_HOT_URL).post(maps).callback(this,1);

    }

    public void click (View view){
        switch(view.getId()){
            case R.id.back_btn:
                finish();
                break;

        }
    }

    @Override
    public void success(String result, int requestCode) {
        jsonString = result;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String appObjectString = jsonObject.getString("app");
            Gson gson = new Gson();
            apps = gson.fromJson(appObjectString,Details2Bean.class);
            nameTv.setText(apps.getName());
            typeNameTv.setText(apps.getTypename());
            descriptionTv.setText(apps.getDescription());
            if (apps.getAppsize().isEmpty()){
                sizeTv.setText("大小：未知");
            }else{

            sizeTv.setText("大小："+apps.getAppsize());
            }
            Log.d(TAG, "size"+apps.getAppsize());
            ImageLoader.init(this).load("http://www.1688wan.com"+apps.getLogo(),iconIv);
            String imageArrayString = jsonObject.getString("img");
            Type type = new TypeToken<List<Details2Bean>>(){}.getType();
            images = gson.fromJson(imageArrayString,type);
            Log.d(TAG, "success: "+images.size());
            for (int i = 0 ; i < images.size();i++){
                View view = LayoutInflater.from(mContext).inflate(R.layout.image_scrollview, null);
                ImageView scrollViewIv = (ImageView) view.findViewById(R.id.game_hot_item_iv);
                ImageLoader.init(this).load("http://www.1688wan.com"+images.get(i).getAddress(),scrollViewIv);
                scrollViewll.addView(view);
            }




        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
