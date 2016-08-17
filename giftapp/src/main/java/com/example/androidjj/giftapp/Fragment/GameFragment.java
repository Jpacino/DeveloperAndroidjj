package com.example.androidjj.giftapp.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidjj.giftapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jpacino on 2016/8/15.
 */
public class GameFragment extends Fragment {
    private Context mContext;
    private TabLayout gameTabLayout;
    private ViewPager gameViewPager;
    private List<String> titles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private MyPagerAdapter myPagerAdapter;
    public static GameFragment newInstance(){
        return new GameFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.game_fragment_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        loadData();
        gameTabLayout = (TabLayout) view.findViewById(R.id.game_tab_layout);
        gameViewPager = (ViewPager) view.findViewById(R.id.game_viewpager);
        myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        gameViewPager.setAdapter(myPagerAdapter);
        gameTabLayout.setupWithViewPager(gameViewPager);
    }

    private void loadData() {
        fragments.add(GameLeftFragment.newInstance());
        fragments.add(GameRightFragment.newInstance());
        titles.add("开服");
        titles.add("开测");
    }
    class MyPagerAdapter extends FragmentPagerAdapter{

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments == null ? 0 : fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
