package com.example.androidjj.giftapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.example.androidjj.giftapp.Fragment.GameFragment;
import com.example.androidjj.giftapp.Fragment.GiftFragment;
import com.example.androidjj.giftapp.Fragment.HotFragment;
import com.example.androidjj.giftapp.Fragment.SpecialFragment;

public class MainActivity extends AppCompatActivity {
    GiftFragment giftFragment;
    HotFragment hotFragment;
    SpecialFragment specialFragment;
    GameFragment gameFragment;

    //a
    private Fragment showFragment;

    private RadioGroup mRadioGroup;
    private FragmentManager mSupportFragmentManager;
    private Button searchBtn;
    private FrameLayout actionBarFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        initView();
        initFragment();

    }
    private void initView() {
        mSupportFragmentManager = getSupportFragmentManager();
        mRadioGroup = (RadioGroup) findViewById(R.id.main_radio_group);
        searchBtn = (Button) findViewById(R.id.main_search_btn);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View giftActionView = layoutInflater.inflate(R.layout.gift_actionbar_layout,null,false);
        final View gameActionView = layoutInflater.inflate(R.layout.game_actionbar_layout,null,false);
        final View specialActionView = layoutInflater.inflate(R.layout.special_actionbar_layout,null,false);
        final View hotActionView = layoutInflater.inflate(R.layout.hot_actionbar_layout,null,false);
        actionBarFrame = (FrameLayout) findViewById(R.id.actionbar_frame_layout);
        actionBarFrame.addView(giftActionView);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.gift_rb:
                        ctrlFragment(giftFragment);
                        if(actionBarFrame != null){
                            actionBarFrame.removeAllViews();
                        }
                        actionBarFrame.addView(giftActionView);
                        break;
                    case R.id.hot_rb:
                        ctrlFragment(hotFragment);
                        if(actionBarFrame != null){
                            actionBarFrame.removeAllViews();
                        }
                        actionBarFrame.addView(hotActionView);
                        break;
                    case R.id.special_rb:
                        ctrlFragment(specialFragment);
                        if(actionBarFrame != null){
                            actionBarFrame.removeAllViews();
                        }
                        actionBarFrame.addView(specialActionView);
                        break;
                    case R.id.game_rb:
                        ctrlFragment(gameFragment);
                        if(actionBarFrame != null){
                            actionBarFrame.removeAllViews();
                        }
                        actionBarFrame.addView(gameActionView);
                        break;
                }
            }
        });
    }
    private void ctrlFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = mSupportFragmentManager.beginTransaction();
        if (showFragment != null && showFragment.isAdded()){
            fragmentTransaction.hide(showFragment);
        }
        if (!fragment.isAdded()){
            fragmentTransaction.add(R.id.main_frame_layout,fragment);
        }else {
            fragmentTransaction.show(fragment);
        }

        fragmentTransaction.commit();
        showFragment = fragment;
    }
    private void initFragment() {
        giftFragment = GiftFragment.newInstance();
        hotFragment = HotFragment.newInstance();
        specialFragment = SpecialFragment.newInstance();
        gameFragment = GameFragment.newInstance();
        ctrlFragment(giftFragment);


    }
}
