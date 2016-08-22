package com.example.androidjj.giftapp.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidxx.yangjw.httplibrary.HttpUtils;
import com.androidxx.yangjw.httplibrary.ICallback;
import com.androidxx.yangjw.imageloader.ImageLoader;
import com.example.androidjj.giftapp.GameHotActivity;
import com.example.androidjj.giftapp.JavaBean.HotPush1Bean;
import com.example.androidjj.giftapp.JavaBean.HotPush2Bean;
import com.example.androidjj.giftapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jpacino on 2016/8/15.
 */
public class HotFragment extends Fragment implements ICallback {
    public static final String TAG = "androidjjj";
    public static final String HOT_URL = "http://www.1688wan.com//majax.action?method=hotpushForAndroid";
    private Context mContext;
    private ListView hotLv;
    private MyLvAdapter myLvAdapter;
    private MyGvAdapter myGvAdapter;
    private GridView hotGv;
    private String jsonString;
    private HotPush1Bean hotPush1Bean;
    private String appid;
    private List<HotPush1Bean> push1s = new ArrayList<>();
    private List<HotPush2Bean> push2s = new ArrayList<>();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            myLvAdapter.notifyDataSetChanged();
            myGvAdapter.notifyDataSetChanged();
        }
    };
    public static HotFragment newInstance(){
        return new HotFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hot_fragment_layout,container,false);
        initVIew(view);
        return view;
    }

    private void initVIew(View view) {
        loadData();
        hotLv = (ListView) view.findViewById(R.id.hot_lv);
        hotGv = (GridView) view.findViewById(R.id.hot_gv);
        myLvAdapter = new MyLvAdapter();
        hotLv.setAdapter(myLvAdapter);
        myGvAdapter = new MyGvAdapter();
        hotGv.setAdapter(myGvAdapter);


    }

    private void loadData() {
        HttpUtils.load(HOT_URL).callback(this,1);
    }

    @Override
    public void success(String result, int requestCode) {
        if (requestCode == 1){
            jsonString = result;
        }
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject jsonObject1 = jsonObject.getJSONObject("info");
            //常规JSON解析push1
            JSONArray jsonArraypush1 = jsonObject1.getJSONArray("push1");
            for (int i = 0; i<jsonArraypush1.length();i++){
                JSONObject jsonObjectPush1 = jsonArraypush1.getJSONObject(i);
                String logo = jsonObjectPush1.getString("logo");
                String name = jsonObjectPush1.getString("name");
                String appid = jsonObjectPush1.getString("appid");
                String typename = jsonObjectPush1.getString("typename");
                String clicks = jsonObjectPush1.getString("clicks");
                String size;
                if(i==0){
                    size = "0";
                }else {
                    size = jsonObjectPush1.getString("size");
                }
                hotPush1Bean = new HotPush1Bean();
                hotPush1Bean.setLogo(logo);
                hotPush1Bean.setAppid(appid);
                hotPush1Bean.setName(name);
                hotPush1Bean.setTypename(typename);
                hotPush1Bean.setClicks(clicks);
                hotPush1Bean.setSize(size);
                push1s.add(hotPush1Bean);
            }
            //GSON解析包含JsonArray的push2
            String stringArraypush2 = jsonObject1.getString("push2");
            Gson gson = new Gson();
            Type type = new TypeToken<List<HotPush2Bean>>(){}.getType();
            push2s = gson.fromJson(stringArraypush2,type);
            Log.d(TAG, "success: " +push1s.get(0).getLogo());
            handler.sendEmptyMessage(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class MyLvAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return push1s.size();
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
            if(view == null){
                view = LayoutInflater.from(mContext).inflate(R.layout.hot_item_list_view,parent,false);
            }
            ImageView imageView = (ImageView) view.findViewById(R.id.gift_item_image_icon);
            TextView clicksTv = (TextView) view.findViewById(R.id.hot_item_clicks);
            TextView nameTv = (TextView) view.findViewById(R.id.gift_item_text_name);
            TextView typenameTv = (TextView) view.findViewById(R.id.hot_item_text_typename);
            TextView sizeTv = (TextView) view.findViewById(R.id.gift_item_text_size);
            imageView.setImageResource(R.mipmap.ic_launcher);
            ImageLoader.init(mContext).load("http://www.1688wan.com"+push1s.get(position).getLogo(),imageView);
            clicksTv.setText(push1s.get(position).getClicks());
            nameTv.setText(push1s.get(position).getName());
            typenameTv.setText(push1s.get(position).getTypename());
            sizeTv.setText(push1s.get(position).getSize());
            hotLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext, GameHotActivity.class);
                    appid = push1s.get(position).getAppid();
                    Log.d(TAG, "appid"+push1s.size());
                    intent.putExtra("gid",appid);
                    startActivity(intent);
                }
            });
            return view;
        }
    }
    class MyGvAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return push2s.size();
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
            final int positions = position;
            View view = convertView;
            if (view == null){
                view = LayoutInflater.from(mContext).inflate(R.layout.hot_item_grid_view,parent,false);
            }
            ImageView imageView = (ImageView) view.findViewById(R.id.hot_item_gv_image_icon);
            TextView nameTv = (TextView) view.findViewById(R.id.hot_item_gv_text_name);
            imageView.setImageResource(R.mipmap.ic_launcher);
            ImageLoader.init(mContext).load("http://www.1688wan.com"+push2s.get(position).getLogo(),imageView);
            nameTv.setText(push2s.get(position).getName());
            hotGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext,GameHotActivity.class);
                    intent.putExtra("gid",push2s.get(position).getAppid());
                    startActivity(intent);
                }
            });
            return view;
        }
    }
}
