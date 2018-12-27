package com.earth.OsToolkit.Base;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;

public class KillActivity extends Application {

	/*
	 * 27 Dec 2018
	 *
	 * By 1552980358
	 *
	 */

	private AppCompatActivity appCompatActivity;

	// 静态对象
	// Static Object
	public static KillActivity instance;

	public KillActivity(){}

	// 实例化一次
	// Call once
	public synchronized static KillActivity getInstance() {
		if (instance == null) {
			instance = new KillActivity();
		}
		return instance;
	}

	public void setAppCompatActivity(AppCompatActivity appCompatActivity) {
		this.appCompatActivity = appCompatActivity;
	}

	public void killActivity() {
		if (appCompatActivity != null) {
			try {
				appCompatActivity.finish();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
