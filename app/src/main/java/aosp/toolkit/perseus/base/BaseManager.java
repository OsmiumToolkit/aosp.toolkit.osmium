package aosp.toolkit.perseus.base;
/*
 * OsToolkit - Kotlin
 *
 * Date : 31/12/2018
 *
 * By   : 1552980358
 *
 */

import android.support.v4.app.Fragment;

import aosp.toolkit.perseus.BuildConfig;
import aosp.toolkit.perseus.fragments.MainFragment;
import aosp.toolkit.perseus.*;

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

    public MainActivity getMainActivity() {
        return this.mainActivity;
    }

    public void exceptionBreaker(Fragment fragment) {
        mainActivity.getSupportFragmentManager().beginTransaction().remove(fragment).show(mainFragment).commit();
        mainActivity.exceptionBeaker();
    }

    /*
    private ChargingFragment chargingFragment;
    private OtherFragment extendsFragment;

    public void setChargingFragment(ChargingFragment chargingFragment) {
        this.chargingFragment = chargingFragment;
    }

    public void setExtendsFragment(OtherFragment extendsFragment) {
        this.extendsFragment = extendsFragment;
    }


    public void restartChargingFragment() {
        ChargingFragment tmp = new ChargingFragment();
        FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(chargingFragment).add(R.id.frameLayout_main, tmp).commit();
        this.chargingFragment = tmp;
        mainActivity.onRecreateChargingFragment(tmp);
    }

    public void restartExtendsFragmet() {
        OtherFragment tmp = new OtherFragment();
        FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(extendsFragment).add(R.id.frameLayout_main, tmp).commit();
        this.extendsFragment = tmp;
        mainActivity.onRecreateExtendsFragment(tmp);
    }
    */
}