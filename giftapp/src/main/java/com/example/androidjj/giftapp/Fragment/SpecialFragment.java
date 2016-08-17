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
public class SpecialFragment extends Fragment {

    private Context mContext;
    private ViewPager specialVp;
    private TabLayout specialTl;
    private List<String> titles = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();
    private MyVpAdapter myVpAdapter;

    public static SpecialFragment newInstance(){
        return new SpecialFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.special_fragment_layout,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        loadData();
        specialVp = (ViewPager) view.findViewById(R.id.special_view_pager);
        specialTl = (TabLayout) view.findViewById(R.id.special_tab_layout);
        myVpAdapter = new MyVpAdapter(getChildFragmentManager());
        specialVp.setAdapter(myVpAdapter);
        specialTl.setupWithViewPager(specialVp);
    }

    private void loadData() {
        titles.add("暴打星期三");
        titles.add("新游周刊");
        fragments.add(SpecialLeftFragment.newInstance());
        fragments.add(SpecialRightFragment.newInstance());
    }
    class MyVpAdapter extends FragmentPagerAdapter{

        public MyVpAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return titles == null ? 0 : titles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
