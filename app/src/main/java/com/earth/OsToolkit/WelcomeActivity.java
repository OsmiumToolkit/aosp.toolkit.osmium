package com.earth.OsToolkit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.*;
import android.support.v7.app.AppCompatActivity;

import com.earth.OsToolkit.Fragment.WelcomeFragment.*;

import java.util.*;

public class WelcomeActivity extends AppCompatActivity {

	@Override
	public void onCreate(Bundle savedInstanceSatte) {
		super.onCreate(savedInstanceSatte);
		setContentView(R.layout.activity_welcome);
		getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.navy_blue));

		initialize();
	}

	public void initialize() {
		ViewPager viewPager = findViewById(R.id.viewpager);
		ArrayList<Fragment> listFragment = new ArrayList<>();

		listFragment.add(new WelcomeFragment1());
		listFragment.add(new WelcomeFragment2());
		listFragment.add(new WelcomeFragment3());
		listFragment.add(new WelcomeFragment4());

		myFragmentPagerAdapter myFragmentPagerAdapter =
				new myFragmentPagerAdapter(
						getSupportFragmentManager()
						, listFragment);
		viewPager.setAdapter(myFragmentPagerAdapter);
		viewPager.setCurrentItem(0);
	}

	public class myFragmentPagerAdapter extends FragmentPagerAdapter {

		private FragmentManager fragmetnManager;  //创建FragmentManager
		private List<Fragment> listFragment; //创建一个List<Fragment>

		private myFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
			super(fm);
			this.fragmetnManager = fm;
			this.listFragment = list;
		}

		@Override
		public Fragment getItem(int arg0) {
			return listFragment.get(arg0); //返回fragment
		}

		@Override
		public int getCount() {
			return listFragment.size(); // fragment
		}


	}

}
