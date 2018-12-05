package com.earth.OsToolkit;

import android.Manifest;
import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.earth.OsToolkit.Working.BaseClass.Checking;

import static com.earth.OsToolkit.Working.BaseClass.Checking.checkRoot;

@SuppressWarnings("all")
public class SplashActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceSatate) {
		super.onCreate(savedInstanceSatate);
		setContentView(R.layout.activity_welcome);

		SharedPreferences sp = getSharedPreferences("save", MODE_PRIVATE);
		int welcome = sp.getInt("welcome", 0);

		Checking.checkVersion(this);
		ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,}, 1);

		Intent intent = new Intent();
		if (welcome == 0 || !checkRoot()) {
			intent.setClass(this, WelcomeActivity.class);
		} else {
			intent.setClass(this, MainActivity.class);
		}

		startActivity(intent);
		this.finish();
	}

}
