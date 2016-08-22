package com.example.androidjj.giftapp.Fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidxx.yangjw.httplibrary.HttpUtils;
import com.androidxx.yangjw.httplibrary.ICallback;
import com.androidxx.yangjw.imageloader.ImageLoader;
import com.example.androidjj.giftapp.GameHotActivity;
import com.example.androidjj.giftapp.JavaBean.GameBean;
import com.example.androidjj.giftapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jpacino on 2016/8/15.3
 */
public class GameLeftFragment extends Fragment implements ICallback{
    public static final String GAME_URL = "http://www.1688wan.com/majax.action?method=getJtkaifu";
    public static final String TAG = "androidjjj";
    private PullToRefreshExpandableListView gameLeftLv;
    private MyLvAdapter myLvAdapter;
    private String jsonString;
    private List<GameBean> infoLists;
    private List<String> addTimeLists = new ArrayList<>();
    private Map<String,List<GameBean>> datas = new HashMap<>();
    private ArrayList<GameBean> childLists;
    private String gids;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            gameLeftLv.onRefreshComplete();
            myLvAdapter.notifyDataSetChanged();
            for (int i = 0; i < addTimeLists.size(); i++) {
                gameLeftLv.getRefreshableView().expandGroup(i);
            }
        }
    };
    public static GameLeftFragment newInstance() {
        return new GameLeftFragment();
    }

    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.game_left_fragment_layout, null, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        loadData();
        gameLeftLv = (PullToRefreshExpandableListView) view.findViewById(R.id.game_left_lv);
        myLvAdapter = new MyLvAdapter();
        gameLeftLv.getRefreshableView().setAdapter(myLvAdapter);
        gameLeftLv.getRefreshableView().setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        gameLeftLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: ");
                Intent intent = new Intent(mContext, GameHotActivity.class);
                startActivity(intent);
            }
        });
        gameLeftLv.setMode(PullToRefreshBase.Mode.BOTH);
        gameLeftLv.setLastUpdatedLabel(new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss").format(new Date()));
        setupRefreshControl();



    }
    private void setupRefreshControl() {
        gameLeftLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ExpandableListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                if(infoLists!=null){
                    infoLists.clear();
                }
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
                if(infoLists!=null){
                    infoLists.clear();
                }
                loadData();
            }
        });

    }

    private void loadData() {

        HttpUtils.load(GAME_URL).callback(this,1);
    }

    @Override
    public void success(String result, int requestCode) {
        if(requestCode == 1){
            jsonString = result;
        }
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String info = jsonObject.getString("info");
            Type type = new TypeToken<List<GameBean>>(){}.getType();
            Gson gson = new Gson();
            infoLists = gson.fromJson(info,type);
            for (int i = 0;i<infoLists.size();i++){
                if (!addTimeLists.contains(infoLists.get(i).getAddtime())){
                addTimeLists.add(infoLists.get(i).getAddtime());}
            }
            for (int i = 0 ;i<addTimeLists.size();i++){
                childLists = new ArrayList<>();
                for(int j = 0;j<infoLists.size();j++){
                    if (addTimeLists.get(i).equals(infoLists.get(j).getAddtime())){
                        childLists.add(infoLists.get(j));
                    }
                }
                datas.put(addTimeLists.get(i),childLists);
            }
            mHandler.sendEmptyMessage(1);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class MyLvAdapter extends BaseExpandableListAdapter{


        @Override
        public int getGroupCount() {
            return addTimeLists == null?0:addTimeLists.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {

            return datas.get(addTimeLists.get(groupPosition)) == null?0:datas.get(addTimeLists.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.game_item_left_parent, parent, false);

            }
            TextView addTimeIv = (TextView) view.findViewById(R.id.game_left_item_tv);
            addTimeIv.setText(addTimeLists.get(groupPosition));
            return view;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final  int groupPositions = groupPosition;
           final int childPositions = childPosition;

            View view = convertView;
            ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.gift_item_list_view, parent, false);
                viewHolder = new ViewHolder(view);
            }else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.imageView.setImageResource(R.drawable.ic_app);
            ImageLoader.init(mContext).load("http://www.1688wan.com"+datas.get(addTimeLists.get(groupPosition)).get(childPosition).getIconurl(),viewHolder.imageView);
            viewHolder.gnameTv.setText(datas.get(addTimeLists.get(groupPosition)).get(childPosition).getGname());
            viewHolder.operatorsTv.setText("运营商："+datas.get(addTimeLists.get(groupPosition)).get(childPosition).getOperators());
            viewHolder.startTimeTv.setText(datas.get(addTimeLists.get(groupPosition)).get(childPosition).getLinkurl());
            viewHolder.button.setText("查看");
            viewHolder.button.setBackgroundColor(Color.parseColor("#fd6563"));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,GameHotActivity.class);
                    gids =  datas.get(addTimeLists.get(groupPosition)).get(childPosition).getGid();
                    intent.putExtra("gid",gids);
                    startActivity(intent);
                }
            });
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,GameHotActivity.class);
                    gids =  datas.get(addTimeLists.get(groupPosition)).get(childPosition).getGid();
                    intent.putExtra("gid",gids);
                    startActivity(intent);
                }
            });


            return view;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        class ViewHolder {
            private final TextView gnameTv;
            private final TextView startTimeTv;
            private final TextView operatorsTv;
            private final Button button;
            private ImageView imageView;

            public ViewHolder(View view){
                view.setTag(this);
                imageView = (ImageView) view.findViewById(R.id.gift_item_image_icon);
                gnameTv = (TextView) view.findViewById(R.id.gift_item_text_name);
                startTimeTv = (TextView) view.findViewById(R.id.gift_item_text_view01);
                operatorsTv = (TextView) view.findViewById(R.id.gift_item_text_addtime);
                button = (Button) view.findViewById(R.id.gift_item_btn);

            }
        }

    }

}