package com.example.androidjj.giftapp.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidxx.yangjw.httplibrary.HttpUtils;
import com.androidxx.yangjw.httplibrary.ICallback;
import com.androidxx.yangjw.imageloader.ImageLoader;
import com.example.androidjj.giftapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jpacino on 2016/8/16.
 */
public class SpecialRightFragment extends Fragment implements ICallback {
    public static final String TAG = "androidjjj";
    public static final String SPECIAL_URL = "http://www.1688wan.com/majax.action?method=getWeekll&pageNo=0";
    private Context mContext;
    private PullToRefreshListView specialRightLv;
    private MyLvAdapter myLvAdapter;
    private String jsonString;
    private List<SpecialBean> lists = new ArrayList<>();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            myLvAdapter.notifyDataSetChanged();
        }
    };
    public static SpecialRightFragment newInstance(){
        return new SpecialRightFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.special_right_fragment_layout,container,false);
        initVIew(view);
        return view;
    }

    private void initVIew(View view) {
        initData();
        specialRightLv = (PullToRefreshListView) view.findViewById(R.id.special_right_lv);
        myLvAdapter = new MyLvAdapter();
        specialRightLv.setAdapter(myLvAdapter);
    }

    private void initData() {
        HttpUtils.load(SPECIAL_URL).callback(this,1);
    }

    @Override
    public void success(String result, int requestCode) {
        if(requestCode == 1){
            jsonString = result;
        }
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String stringArraylists = jsonObject.getString("list");
            Gson gson = new Gson();
            Type type = new TypeToken<List<SpecialBean>>(){}.getType();
            lists = gson.fromJson(stringArraylists,type);
            Log.d(TAG, "success: "+lists.size());
            handler.sendEmptyMessage(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    class MyLvAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return lists.size();
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
                view = LayoutInflater.from(mContext).inflate(R.layout.special_item_right_lv,parent,false);
                viewHolder = new ViewHolder(view);
            }else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.iconUrlIv.setImageResource(R.mipmap.ic_launcher);
            ImageLoader.init(mContext).load( "http://www.1688wan.com"+lists.get(position).getIconurl(),viewHolder.iconUrlIv);
            viewHolder.nameTv.setText(lists.get(position).getName());
            return view;
        }
        class ViewHolder{
            private final ImageView iconUrlIv;
            private final TextView nameTv;

            public ViewHolder (View view){
                view.setTag(this);
                iconUrlIv = (ImageView) view.findViewById(R.id.special_item_right_iv);
                nameTv = (TextView) view.findViewById(R.id.special_item_right_tv);

            }
        }
    }
}
