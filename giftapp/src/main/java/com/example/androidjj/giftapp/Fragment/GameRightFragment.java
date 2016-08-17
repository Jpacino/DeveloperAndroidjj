package com.example.androidjj.giftapp.Fragment;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import java.util.List;

/**
 * Created by Jpacino on 2016/8/15.
 */
public class GameRightFragment extends Fragment implements ICallback {
    private static final String TAG = "androidjjj";
    private static final String GAME_URL = "http://www.1688wan.com/majax.action?method=getWebfutureTest";
    private Context mContext;
    private PullToRefreshListView gameRightLv;
    private MylvAdapter mylvAdapter;
    String jsonString;
    private List<GameBean> infolists;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mylvAdapter.notifyDataSetChanged();
        }
    };

    public static GameRightFragment newInstance() {
        return new GameRightFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.game_right_fragment_layout, container, false);
        initVIew(view);
        return view;
    }

    private void initVIew(View view) {
        loadData();
        gameRightLv = (PullToRefreshListView) view.findViewById(R.id.game_right_lv);
        mylvAdapter = new MylvAdapter();
        gameRightLv.setAdapter(mylvAdapter);
    }

    private void loadData() {
        HttpUtils.load(GAME_URL).callback(this, 1);

    }

    @Override
    public void success(String result, int requestCode) {
        if (requestCode == 1) {
            jsonString = result;
        }
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String info = jsonObject.getString("info");
            Gson gson = new Gson();
            Type type = new TypeToken<List<GameBean>>() {
            }.getType();
            infolists = gson.fromJson(info, type);
            mHandler.sendEmptyMessage(1);


        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    class MylvAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return infolists == null ? 0 : infolists.size();
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
            }else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.imageView.setImageResource(R.mipmap.ic_launcher);
            ImageLoader.init(mContext).load("http://www.1688wan.com"+infolists.get(position).getIconurl(),viewHolder.imageView);
            viewHolder.operatorsTv.setText("运营商:"+infolists.get(position).getOperators());
            viewHolder.addtimeTv.setText(infolists.get(position).getAddtime());
            viewHolder.gnameTv.setText(infolists.get(position).getGname());
            viewHolder.button.setText("查看");
            viewHolder.button.setBackgroundColor(Color.parseColor("#fd6563"));
            return view;

        }

        class ViewHolder {

            private Button button;
            public ImageView imageView;
            public TextView operatorsTv;
            public TextView gnameTv;
            public TextView addtimeTv;

            public ViewHolder(View view) {
                view.setTag(this);
                imageView = (ImageView) view.findViewById(R.id.hot_item_image_icon);
                operatorsTv = (TextView) view.findViewById(R.id.gift_item_text_view01);
                gnameTv = (TextView) view.findViewById(R.id.hot_item_text_name);
                addtimeTv = (TextView) view.findViewById(R.id.gift_item_text_addtime);
                button = (Button) view.findViewById(R.id.gift_item_btn);
            }
        }
    }
}
