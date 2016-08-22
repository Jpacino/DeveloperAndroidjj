package com.example.androidjj.giftapp.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidxx.yangjw.httplibrary.HttpUtils;
import com.androidxx.yangjw.httplibrary.ICallback;
import com.androidxx.yangjw.imageloader.ImageLoader;
import com.example.androidjj.giftapp.GiftActivity;
import com.example.androidjj.giftapp.GiftDetailsActivity;
import com.example.androidjj.giftapp.JavaBean.GiftAdBean;
import com.example.androidjj.giftapp.JavaBean.GiftListBean;
import com.example.androidjj.giftapp.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.SimpleFormatter;

/**
 * Created by Jpacino on 2016/8/15.
 */
public class GiftFragment extends Fragment implements ICallback {
    public static final String URL_GIFT = "http://www.1688wan.com/majax.action?method=getGiftList";
    private static final String TAG = "androidjjj";
    private Context mContext;
    private PullToRefreshListView giftLv;
    private ViewPager giftVp;
    private MyPagerAdapter myPagerAdapter = new MyPagerAdapter();
    private MyLvAdapter myLvAdapter = new MyLvAdapter();
    private List<String> imageUrls = new ArrayList<>();
    private Map<String, Object> postMaps = new HashMap<>();
    private String jsonString;
    private LinearLayout mLinearLayout;
    private int childCount;
    private int num = 1000;
    private int pageno =1;
    private String ids;
    private boolean isAutoScroll = true;
    private List<GiftListBean> lists = new ArrayList();
    private List<GiftAdBean> ads = new ArrayList();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                giftLv.onRefreshComplete();
                myLvAdapter.notifyDataSetChanged();

            }
            if (msg.what == 2) {
                giftVp.setAdapter(myPagerAdapter);
//                myPagerAdapter.notifyDataSetChanged();
            }
            if (msg.what == 3) {
                num += 1;
                giftVp.setCurrentItem(num);
                if (isAutoScroll) {
                    mHandler.sendEmptyMessageDelayed(3, 2000);
                }


            }

        }
    };


    public static GiftFragment newInstance() {
        return new GiftFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gift_fragment_layout, container, false);
        View headView = inflater.inflate(R.layout.gift_headview, null);
        initView(view);

        giftLv.getRefreshableView().addHeaderView(headView);
        giftLv.setMode(PullToRefreshBase.Mode.BOTH);
        initHeadView(headView);
        loadNetDate();
        mHandler.sendEmptyMessageDelayed(3, 2000);
//        giftVp.setAdapter(myPagerAdapter);
        giftLv.setAdapter(myLvAdapter);

        return view;
    }

    private void loadNetDate() {
        postMaps.clear();
        postMaps.put("pageno", "1");
        HttpUtils.load(URL_GIFT).post(postMaps).callback(this, 1);


    }


    private void initHeadView(View headView) {
        giftVp = (ViewPager) headView.findViewById(R.id.gift_viewpager);
        mLinearLayout = (LinearLayout) headView.findViewById(R.id.point);
        childCount = mLinearLayout.getChildCount();
        controlPoint(0);
        giftVp.addOnPageChangeListener(listener);
        giftVp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isAutoScroll = false;
                        mHandler.removeCallbacksAndMessages(null);
                        break;
                    case MotionEvent.ACTION_UP:
                        isAutoScroll = true;
                        mHandler.sendEmptyMessageDelayed(3,2000);
                        break;
                }
                return false;
            }
        });

    }

    private void initView(View view) {
        giftLv = (PullToRefreshListView) view.findViewById(R.id.gift_lv);
        giftLv.setMode(PullToRefreshBase.Mode.BOTH);

        giftLv.setLastUpdatedLabel(new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss").format(new Date()));
        setupRefreshControl();

    }
    private void setupRefreshControl() {
        giftLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                postMaps.clear();
                for(int i = 1 ; i <=pageno;i++) {
                    postMaps.put("pageno", i);
                    if (i == 1) {
                        HttpUtils.load(URL_GIFT).post(postMaps).callback(GiftFragment.this, 1);
                    }else {
                        HttpUtils.load(URL_GIFT).post(postMaps).callback(GiftFragment.this, 2);
                    }
                }

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                postMaps.clear();
                pageno +=1;
                postMaps.put("pageno",pageno);
                HttpUtils.load(URL_GIFT).post(postMaps).callback(GiftFragment.this, 2);

            }
        });
    }

    private void controlPoint(int index) {
        ImageView imageView = (ImageView) mLinearLayout.getChildAt(index);
        for (int i = 0; i < childCount; i++) {
            ImageView childView = (ImageView) mLinearLayout.getChildAt(i);
            childView.setEnabled(true);
        }
        imageView.setEnabled(false);
    }

    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            controlPoint(position % 5);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };


    @Override
    public void success(String result, int requestCode) {
        if (requestCode == 1) {
            lists.clear();
            jsonString = result;
        }
        if (requestCode == 2){

        jsonString = result;
        }
        try {
            JSONObject giftObject = new JSONObject(jsonString);
            JSONArray jsonArrayList = giftObject.getJSONArray("list");
            JSONArray jsonArrayAd = giftObject.getJSONArray("ad");
            int listLength = jsonArrayList.length();
            int adLength = jsonArrayAd.length();
            for (int i = 0; i < listLength; i++) {
                JSONObject jsonObjectList = jsonArrayList.getJSONObject(i);
                String giftname = jsonObjectList.getString("giftname");
                String gname = jsonObjectList.getString("gname");
                String iconurl = jsonObjectList.getString("iconurl");
                String number = jsonObjectList.getString("number");
                String addtime = jsonObjectList.getString("addtime");
                String id = jsonObjectList.getString("id");
                GiftListBean giftList = new GiftListBean(giftname, gname, iconurl, number, addtime,id);
                lists.add(giftList);
            }
            mHandler.sendEmptyMessage(1);
            for (int i = 0; i < adLength; i++) {
                JSONObject jsonObjectAd = jsonArrayAd.getJSONObject(i);
                String iconurl = jsonObjectAd.getString("iconurl");
                GiftAdBean giftAd = new GiftAdBean(iconurl);
                ads.add(giftAd);
            }
            mHandler.sendEmptyMessage(2);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            if (!ads.isEmpty()) {
                ImageLoader.init(mContext).load("http://www.1688wan.com" + ads.get(position % 5).getIconurl(), imageView);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }
            container.addView(imageView);
            return imageView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
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
            final int positions = position;
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
            viewHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, GiftDetailsActivity.class);
                    ids = lists.get(positions).getId();
                    intent.putExtra("id",ids);
                    startActivity(intent);
                }

            });
            giftLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext, GiftDetailsActivity.class);
                    ids = lists.get(position-2).getId();
                    intent.putExtra("id",ids);
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
