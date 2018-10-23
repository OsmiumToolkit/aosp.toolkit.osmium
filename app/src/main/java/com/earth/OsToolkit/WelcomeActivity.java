package com.earth.OsToolkit;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.*;
import android.support.v7.app.AppCompatActivity;
import android.view.*;

import com.earth.OsToolkit.Fragment.WelcomeFragment.WelcomeFragment1;
import com.earth.OsToolkit.Fragment.WelcomeFragment.WelcomeFragment2;
import com.earth.OsToolkit.Fragment.WelcomeFragment.WelcomeFragment3;
import com.earth.OsToolkit.Fragment.WelcomeFragment.WelcomeFragment4;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {
    private List<View> viewList;

    @Override
    public void onCreate(Bundle savedInstanceSatte){
        super.onCreate(savedInstanceSatte);
        setContentView(R.layout.activity_welcome);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.navy_blue));

        initialize();
    }

    /*public void initialize(){
        ViewPager viewPager = findViewById(R.id.viewpager);
        LayoutInflater inflater=getLayoutInflater();

        viewList = new ArrayList<View>();
        viewList.add(inflater.inflate(R.layout.activity_welcome_1,null));
        viewList.add(inflater.inflate(R.layout.activity_welcome_2,null));
        viewList.add(inflater.inflate(R.layout.activity_welcome_3,null));
        viewList.add(inflater.inflate(R.layout.activity_welcome_4,null));

        LinearLayout start_su = findViewById(R.id.start_su);

        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public boolean isViewFromObject(@NonNull View arg0,
                                            @NonNull Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container,
                                    int position,
                                    @NonNull Object object) {
                container.removeView(viewList.get(position));
            }

            @Override
            @NonNull
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }
        };


        viewPager.setAdapter(pagerAdapter);


        start_su.setOnClickListener(v -> getSu());
    }*/

    public void initialize() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        ArrayList<Fragment> listFragment = new ArrayList<>();

        listFragment.add(new WelcomeFragment1());
        listFragment.add(new WelcomeFragment2());
        listFragment.add(new WelcomeFragment3());
        listFragment.add(new WelcomeFragment4());

        myFragmentPagerAdapter myFragmentPagerAdapter = new myFragmentPagerAdapter(getSupportFragmentManager(),listFragment);
        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.setCurrentItem(0);
    }

    public class myFragmentPagerAdapter extends FragmentPagerAdapter {

        private FragmentManager fragmetnManager;  //创建FragmentManager
        private List<Fragment> listFragment; //创建一个List<Fragment>
        private myFragmentPagerAdapter(FragmentManager fm,List<Fragment> list) {
            super(fm);
            this.fragmetnManager=fm;
            this.listFragment=list;
        }

        @Override
        public Fragment getItem(int arg0) {
            return listFragment.get(arg0); //返回第几个fragment
        }

        @Override
        public int getCount() {
            return listFragment.size(); //总共有多少个fragment
        }


    }

}
