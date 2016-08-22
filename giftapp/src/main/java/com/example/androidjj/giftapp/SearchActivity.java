package com.example.androidjj.giftapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidxx.yangjw.httplibrary.HttpUtils;
import com.androidxx.yangjw.httplibrary.ICallback;
import com.androidxx.yangjw.imageloader.ImageLoader;
import com.example.androidjj.giftapp.JavaBean.SearchBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements ICallback{
    public static final String TAG = "androidjjj";
    public static final String SEARCH_URL = "http://www.1688wan.com/majax.action?method=searchGift";
    private Button backBtn;
    private Button searchBtn;
    private Map<String,Object> maps = new HashMap<>();
    private String jsonString;
    private List<SearchBean> lists = new ArrayList<>();
    private Context mContext;
    private MyLvAdapter myLvAdapter;
    private String search;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            myLvAdapter.notifyDataSetChanged();
        }

    };
    private ListView searchLv;
    private EditText searchEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_search);
        mContext = this;
        initView();
    }

    @Override
    protected void onDestroy() {
        lists.removeAll(lists);
        maps.clear();
        super.onDestroy();
    }

    private void initView() {
        maps.put("key"," ");
        loadData();
        backBtn = (Button) findViewById(R.id.search_back_btn);
        searchBtn = (Button) findViewById(R.id.search_search_btn);
        searchLv = (ListView) findViewById(R.id.search_list_view);
        searchEt = (EditText) findViewById(R.id.search_et);
        myLvAdapter = new MyLvAdapter();
        searchLv.setAdapter(myLvAdapter);

    }


    public void click (View view){
        switch(view.getId()){
            case R.id.search_back_btn:
                finish();
                break;
            case R.id.search_search_btn:
                lists.removeAll(lists);
                jsonString = null;

                myLvAdapter.notifyDataSetChanged();
                search = searchEt.getText().toString();
                maps.clear();
                try {
                    search = URLEncoder.encode(search,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                maps.put("key",search);
                Log.d(TAG, "search"+search);
                loadData();
                break;
        }
    }
    private void loadData() {
        HttpUtils.load(SEARCH_URL).post(maps).callback(this,1);
    }


    @Override
    public void success(String result, int requestCode) {
        if(requestCode == 1){
            jsonString = result;
        }


        try {
            if(!jsonString.equals("{\"list\":}")){
            JSONObject jsonObject = new JSONObject(jsonString);
            String jsonArrayLists = jsonObject.getString("list");
            Gson gson = new Gson();
            Type type = new TypeToken<List<SearchBean>>(){}.getType();
            lists = gson.fromJson(jsonArrayLists,type);}
            Log.d(TAG, "list"+lists.size());
            handler.sendEmptyMessage(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private class MyLvAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return lists == null ? 0 : lists.size();
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
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.gift_item_list_view, parent, false);
                viewHolder = new ViewHolder(view);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.imageView.setImageResource(R.mipmap.ic_launcher);
            String listUrl = "http://www.1688wan.com" + lists.get(position).getIconurl();
            ImageLoader.init(mContext).load(listUrl, viewHolder.imageView);
            viewHolder.nameTextView.setText(lists.get(position).getGiftname());
            viewHolder.name2TextView.setText(lists.get(position).getGname());
            viewHolder.numberTextView.setText("剩余: " + lists.get(position).getNumber());
            viewHolder.addtimeTextView.setText("时间: " + lists.get(position).getAddtime());
            if (Integer.valueOf(lists.get(position).getNumber()) == 0){
                viewHolder.button.setText("淘号");
                viewHolder.button.setBackgroundColor(Color.parseColor("#f09b9b9b"));
            }else {
                viewHolder.button.setText("立即领取");
                viewHolder.button.setBackgroundColor(Color.parseColor("#fd6563"));
            }
            searchLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext,GameHotActivity.class);
                    intent.putExtra("gid",lists.get(position).getId());
                    startActivity(intent);
                }
            });

            return view;
        }

        class ViewHolder {
            public ImageView imageView;
            public TextView nameTextView;
            public TextView name2TextView;
            public TextView numberTextView;
            public TextView addtimeTextView;
            public Button button;

            public ViewHolder(View view) {
                view.setTag(this);
                button = (Button) view.findViewById(R.id.gift_item_btn);
                imageView = (ImageView) view.findViewById(R.id.gift_item_image_icon);
                nameTextView = (TextView) view.findViewById(R.id.gift_item_text_view01);
                name2TextView = (TextView) view.findViewById(R.id.gift_item_text_name);
                numberTextView = (TextView) view.findViewById(R.id.gift_item_text_size);
                addtimeTextView = (TextView) view.findViewById(R.id.gift_item_text_addtime);
            }


        }

    }
}
