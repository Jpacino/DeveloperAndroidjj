package com.example.androidjj.giftapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidxx.yangjw.httplibrary.HttpUtils;
import com.androidxx.yangjw.httplibrary.ICallback;
import com.androidxx.yangjw.imageloader.ImageLoader;
import com.example.androidjj.giftapp.JavaBean.SpecialRightBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpecialDetailsRightActivity extends AppCompatActivity implements ICallback{
    private static final String TAG = "androidjjj";
    private static final String SPECIAL_DETAILS_RIGHT = "http://www.1688wan.com/majax.action?method=getWeekllChid";
    private Map<String,Object> maps = new HashMap<>();
    private Context mContext;
    private View headView;
    private ListView rightLv;
    private MyLvAdapter myLvAdapter;
    private String ids;
    private String image;
    private String descs;
    private String name;
    private List<SpecialRightBean> lists = new ArrayList<>();
    private Intent intent;
    private ImageView headIv;
    private TextView descsTv;
    private TextView nameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_special_details_right);
        initView();
    }

    private void initView() {
        loadData();
        headView = LayoutInflater.from(mContext).inflate(R.layout.special_details_right_headview,null);

        initHeadView();
        rightLv = (ListView) findViewById(R.id.details_tese_right_lv);
        rightLv.addHeaderView(headView);
        myLvAdapter = new MyLvAdapter();
        rightLv.setAdapter(myLvAdapter);



    }

    private void loadData() {
        intent = getIntent();
        ids = intent.getStringExtra("id");
        image = intent.getStringExtra("image");
        name = intent.getStringExtra("name");
        descs = intent.getStringExtra("descs");
        maps.clear();
        maps.put("id",ids);
        HttpUtils.load(SPECIAL_DETAILS_RIGHT).post(maps).callback(this,1);
    }

    public void click(View view){
        switch (view.getId()){
            case R.id.back_btn:
                finish();
                break;
        }
    }

    private void initHeadView() {
        headIv = (ImageView) headView.findViewById(R.id.details_tese_right_headview_iv);
        descsTv = (TextView) headView.findViewById(R.id.details_tese_right_headview_content_tv);
        nameTv = (TextView) headView.findViewById(R.id.details_tese_right_lv_name_tv);
        descsTv.setText(descs);
//        nameTv.setText(name);
        ImageLoader.init(mContext).load(image,headIv);


    }

    @Override
    public void success(String result, int requestCode) {
        if(requestCode == 1){
            try {
                JSONObject jsonObject = new JSONObject(result);
                String listArrayString = jsonObject.getString("list");
                Gson gson = new Gson();
                Type type = new TypeToken<List<SpecialRightBean>>(){}.getType();
                lists = gson.fromJson(listArrayString,type);
                myLvAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    class MyLvAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return lists == null ?0:lists.size();
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
            if (view == null){
                view = LayoutInflater.from(mContext).inflate(R.layout.special_details_right_listview_layout,null);
                viewHolder = new ViewHolder(view);
            }else{
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.contentTv.setText(lists.get(position).getDescs());
            viewHolder.nameTv.setText(lists.get(position).getAppname());
            viewHolder.typeTv.setText(lists.get(position).getTypename());
            ImageLoader.init(mContext).load("http://www.1688wan.com"+lists.get(position).getIconurl(),viewHolder.imageView);
            rightLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext,GameHotActivity.class);
                    intent.putExtra("gid",lists.get(position-1).getAppid());
                    startActivity(intent);
                }
            });
            return view;
        }
        class ViewHolder{
            private final TextView nameTv;
            private final TextView typeTv;
            private final TextView contentTv;
            private final ImageView imageView;

            public ViewHolder(View view){
                view.setTag(this);
                nameTv = (TextView) view.findViewById(R.id.details_tese_right_lv_name_tv);
                typeTv = (TextView) view.findViewById(R.id.details_tese_right_lv_type_tv);
                contentTv = (TextView) view.findViewById(R.id.details_tese_right_lv_content_tv);
                imageView = (ImageView) view.findViewById(R.id.details_tese_right_lv_iv);
            }
        }
    }
}
