package com.earth.OsToolkit;

import android.app.ActivityManager;
import android.content.DialogInterface;
import android.os.*;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.*;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.*;

import com.earth.OsToolkit.Fragment.*;
import com.earth.OsToolkit.Fragment.Dialog.UpdateDialogFragment;
import com.earth.OsToolkit.Working.BaseClass.*;

import java.lang.Process;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener {

	Toolbar toolbar;
	DrawerLayout drawer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initUI();
		checkUpdate();
	}

	public void initUI() {
		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		drawer = findViewById(R.id.drawer_layout);


		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.main_fragment, new MainFragment()).commit();

		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
				this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();

		NavigationView navigationView = findViewById(R.id.nav_view);
		View view = navigationView.getHeaderView(0);

		LinearLayout linearLayout_about = view.findViewById(R.id.nav_about);
		linearLayout_about.setOnClickListener(v -> {
			drawer.closeDrawer(GravityCompat.START);

			int j =  getSupportFragmentManager().getFragments().size();
			for (int i = 0; i < j; i++) {
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				ft.remove(getSupportFragmentManager().getFragments().get(0)).commit();
			}

			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.main_fragment,new AboutFragment()).commit();

			toolbar.setTitle(R.string.nav_about);
		});

		LinearLayout linearLayout_device = view.findViewById(R.id.nav_deviceinfo);
		linearLayout_device.setOnClickListener(v -> {
			drawer.closeDrawer(GravityCompat.START);
			int j =  getSupportFragmentManager().getFragments().size();
			for (int i = 0; i < j; i++) {
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				ft.remove(getSupportFragmentManager().getFragments().get(0)).commit();
			}

			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.main_fragment,new DeviceInfoFragment()).commit();

			toolbar.setTitle(R.string.nav_deviceinfo);
		});




		navigationView.setNavigationItemSelectedListener(this);
	}

	public void checkUpdate() {
		Toast.makeText(this, getString(R.string.update_checking), Toast.LENGTH_SHORT).show();

		// 实例化更新类
		// Call checkUpdate class
		CheckUpdate checkUpdate = new CheckUpdate();
		checkUpdate.checkUpdate();

		// 方法堵塞，等待线程完成
		// Method blocking, wait for the finished oi new thread
		checkUpdate.waitFor();

		// 检查获取的数据
		// Check fetched data
		if (checkUpdate.complete && !checkUpdate.getVersion().equals(Checking.getVersionName(this))) {
			if (!checkUpdate.getVersion().equals("Fail")) {
				UpdateDialogFragment updateDialogFragment = new UpdateDialogFragment();
				FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
				updateDialogFragment.setVerision(checkUpdate.getVersion());
				updateDialogFragment.setDate(checkUpdate.getDate());
				updateDialogFragment.setChangelogEng(checkUpdate.getChangelogEng());
				updateDialogFragment.setChangelogCn(checkUpdate.getChangelogCn());
				updateDialogFragment.show(fragmentTransaction, "updateDialogFragment");
			} else {
				Toast.makeText(this, getString(R.string.update_fail), Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onBackPressed() {
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	@SuppressWarnings("all")
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		Process process;
		switch (id) {
			case R.id.menu_exit:
				ExitApplication.exit();
				return true;

			case R.id.menu_shell:
				ExitApplication.shellKill(this);
				return true;

			case R.id.menu_killProcessPID:
				ExitApplication.killProcessPID();
				return true;

			case R.id.menu_null:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(R.string.menu_null_title)
						.setMessage(R.string.menu_null_msg)
						.setPositiveButton(R.string.cont, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								TextView textView = findViewById(R.id.script_txt);
								textView.setText("");
							}
						})
						.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {

							}
						}).show();
				return true;

			default:
				return super.onOptionsItemSelected(item);

		}
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		Fragment fragment = new MainFragment();
		int title = R.string.app_name;
		switch (id) {
			case R.id.nav_main:
				fragment = new MainFragment();
				break;
			case R.id.nav_charging:
				title = R.string.nav_charging;
				fragment = new ChargingFragment();
				break;
			case R.id.nav_cores:
				title = R.string.nav_processor;
				fragment = new CoresFragment();
				break;
			case R.id.nav_applyyc:
				title = R.string.nav_yc;
				fragment = new ApplyYCFragment();
				break;
		}

		toolbar.setTitle(title);

		int j =  getSupportFragmentManager().getFragments().size();
		for (int i = 0; i < j; i++) {
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.remove(getSupportFragmentManager().getFragments().get(0)).commit();
		}

		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
		fragmentTransaction.replace(R.id.main_fragment, fragment).commit();

		DrawerLayout drawer = findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

}
