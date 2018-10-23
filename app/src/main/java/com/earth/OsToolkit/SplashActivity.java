package com.earth.OsToolkit;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;

import static com.earth.OsToolkit.Working.BaseClass.Checking.checkRoot;

@SuppressWarnings("all")
public class SplashActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceSatate){
        super.onCreate(savedInstanceSatate);
        setContentView(R.layout.activity_welcome);

        SharedPreferences sp = getSharedPreferences("save",MODE_PRIVATE);
        int welcome = sp.getInt("welcome",0);

        Intent intent = new Intent();
        if (welcome == 0 || !checkRoot()){
            intent.setClass(this,WelcomeActivity.class);
        } else {
            intent.setClass(this,MainActivity.class);
        }

        startActivity(intent);
        this.finish();
    }

}
