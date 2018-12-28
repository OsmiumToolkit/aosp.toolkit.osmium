package com.earth.OsToolkit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.*;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.earth.OsToolkit.Fragment.WelcomeFragment.*;
import com.earth.OsToolkit.Base.KillActivity;

import java.util.*;

public class WelcomeActivity extends AppCompatActivity {

	/*
	 * 27 Dec 2018
	 *
	 * By 1552980358
	 *
	 */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 放入应用变量
		// Save in Application Variable
		KillActivity.getInstance().setAppCompatActivity(this);

		// Source / 来源
		// https://blog.csdn.net/hmmhhmmhmhhm/article/details/77840604
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
						View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

		setContentView(R.layout.activity_welcome);



		initialize();
	}

	public void initialize() {
		ViewPager viewPager = findViewById(R.id.viewPager);

		ArrayList<Fragment> listFragment = new ArrayList<>();

		listFragment.add(new WelcomeFragment1());
		listFragment.add(new WelcomeFragment2());
		listFragment.add(new WelcomeFragment3());
		listFragment.add(new WelcomeFragment4());

		viewPager.setCurrentItem(0);

		viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public Fragment getItem(int arg0) {
				return listFragment.get(arg0); //返回fragment
			}

			@Override
			public int getCount() {
				return listFragment.size(); // fragment计算
			}
		});

	}

}


