package com.example.androidjj.giftapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidxx.yangjw.httplibrary.HttpUtils;
import com.androidxx.yangjw.httplibrary.ICallback;
import com.androidxx.yangjw.imageloader.ImageLoader;
import com.example.androidjj.giftapp.JavaBean.SpecialDetailsBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpecialDetailsLeftActivity extends AppCompatActivity implements ICallback{
    public static final String TAG = "androidjjj";
    public static final String SPECIAL_DETAILS_LEFT = "http://www.1688wan.com/majax.action?method=bdxqschild";
    private Intent intent;
    private String id;
    private String addtime;
    private String iconurl;
    private String descs;
    private Map<String,Object> maps = new HashMap<>();
    private List<SpecialDetailsBean> lists;
    private TextView addtimeTv;
    private TextView descsTv;
    private String name;
    private TextView nameTv;
    private ImageView imageView;
    private GridView gridView;
    private MyGvAdapter myGvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_special_detail_left);
        initView();
    }

    private void initView() {
        initData();
        addtimeTv = (TextView) findViewById(R.id.special_item_left_tv_time);
        descsTv = (TextView) findViewById(R.id.special_left_content_tv);
        nameTv = (TextView) findViewById(R.id.special_name_tv);
        imageView = (ImageView) findViewById(R.id.special_item_right_iv);
        gridView = (GridView) findViewById(R.id.special_left_gv);
        addtimeTv.setText(addtime);
        descsTv.setText(descs);
        nameTv.setText(name);
        ImageLoader.init(this).load(iconurl,imageView);
        myGvAdapter = new MyGvAdapter();
        gridView.setAdapter(myGvAdapter);


    }

    private void initData() {
        intent = getIntent();
        id = intent.getStringExtra("id");
        addtime = intent.getStringExtra("addtime");
        iconurl = intent.getStringExtra("iconurl");
        descs = intent.getStringExtra("descs");
        name = intent.getStringExtra("name");
        maps.clear();
        maps.put("id",id);
        HttpUtils.load(SPECIAL_DETAILS_LEFT).post(maps).callback(this,1);



    }

    public void click (View view){
        switch (view.getId()){
            case R.id.special_back_btn:
                finish();
                break;
        }
    }

    @Override
    public void success(String result, int requestCode) {
        if(requestCode == 1){
            try {
                JSONObject jsonObject = new JSONObject(result);
                String listArrayString = jsonObject.getString("list");
                Gson gson = new Gson();
                Type type = new TypeToken<List<SpecialDetailsBean>>(){}.getType();
                lists = gson.fromJson(listArrayString,type);
                Log.d(TAG, "lists"+lists.size());
                myGvAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    class MyGvAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return lists == null ? 0:lists.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null){
                view =LayoutInflater.from(SpecialDetailsLeftActivity.this).inflate(R.layout.special_left_grid_view,null);
            }
            ImageView imageView = (ImageView) view.findViewById(R.id.hot_item_gv_image_icon);
            TextView nameTv = (TextView) view.findViewById(R.id.hot_item_gv_text_name);
            ImageLoader.init(SpecialDetailsLeftActivity.this).load("http://www.1688wan.com"+lists.get(position).getAppicon(),imageView);
            nameTv.setText(lists.get(position).getAppname());
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(SpecialDetailsLeftActivity.this,GameHotActivity.class);
                    intent.putExtra("gid",lists.get(position).getAppid());
                    startActivity(intent);
                }
            });
            return view;
        }
    }
}
