package com.earth.OsToolkit.base;
/*
 * OsToolkit - Kotlin
 *
 * Date : 31/12/2018
 *
 * By   : 1552980358
 *
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import com.earth.OsToolkit.BuildConfig;
import com.earth.OsToolkit.MainActivity;
import com.earth.OsToolkit.R;
import com.earth.OsToolkit.WelcomeActivity;
import com.earth.OsToolkit.fragments.*;
import com.topjohnwu.superuser.ContainerApp;
import com.topjohnwu.superuser.Shell;

public class BaseManager extends ContainerApp {

    public BaseManager() {

    }

    static {
        // Set configurations in a static block
        Shell.Config.setFlags(Shell.FLAG_REDIRECT_STDERR);
        Shell.Config.verboseLogging(BuildConfig.DEBUG);
        Shell.Config.setTimeout(60);
    }

    public static BaseManager instance;

    private WelcomeActivity welcomeActivity;

    //public List<AppCompatActivity> appCompatActivities = new ArrayList<>();

    public synchronized static BaseManager getInstance() {
        if (instance == null) {
            instance = new BaseManager();
        }
        return instance;
    }

    public void setWelcomeActivity(WelcomeActivity welcomeActivity) {
        this.welcomeActivity = welcomeActivity;
    }

    public void finishActivities() {
        if (welcomeActivity != null) {
            try {
                welcomeActivity.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (mainActivity != null) {
            try {
                mainActivity.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private MainActivity mainActivity;
    private MainFragment mainFragment;

    public void setMainActivity(MainActivity mainActivity, MainFragment mainFragment) {
        this.mainActivity = mainActivity;
        this.mainFragment = mainFragment;
    }

    public void exceptionBreaker(Fragment fragment) {
        mainActivity.getSupportFragmentManager().beginTransaction().remove(fragment).show(mainFragment).commit();
        mainActivity.exceptionBeaker();
    }

    private ChargingFragment chargingFragment;

    public void setChargingFragment(ChargingFragment chargingFragment) {
        this.chargingFragment = chargingFragment;
    }

    public void restartChargingFragment() {
        ChargingFragment tmp = new ChargingFragment();
        FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(chargingFragment).add(R.id.frameLayout_main, tmp).commit();
        this.chargingFragment = tmp;
        mainActivity.onRecreateChargingFragment(tmp);
    }

    //private RomIOFragment romIOFragment;
    public void setRomIOFragment(RomIOFragment romIOFragment) {
        //this.romIOFragment = romIOFragment;
    }
    /*
    public void restartRomIOFragment() {
        RomIOFragment tmp = new RomIOFragment();
        FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(romIOFragment).add(R.id.frameLayout_main, tmp).commit();
        this.romIOFragment = tmp;
        mainActivity.onRecreateRomIOFragment(tmp);
    }
    */
}