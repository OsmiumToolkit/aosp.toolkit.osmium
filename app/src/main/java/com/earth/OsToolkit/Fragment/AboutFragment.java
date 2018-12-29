package com.earth.OsToolkit.Fragment;

import android.content.*;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.annotation.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.earth.OsToolkit.Fragment.Dialog.UpdateDialogFragment;
import com.earth.OsToolkit.Items.AboutItem;
import com.earth.OsToolkit.MainActivity;
import com.earth.OsToolkit.R;
import com.earth.OsToolkit.Base.CheckUpdate;
import com.earth.OsToolkit.Base.Checking;

import java.util.ArrayList;
import java.util.List;

import static com.earth.OsToolkit.Base.Jumping.*;
import static com.earth.OsToolkit.Base.Checking.*;
import static com.earth.OsToolkit.Base.BaseIndex.*;

@SuppressWarnings("all")
public class AboutFragment extends Fragment {

	/*
	 * 27 Dec 2018
	 *
	 * By 1552980358
	 *
	 */

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_about, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		LinearLayout linearLayout = view.findViewById(R.id.about_linear);

		String PackageVersionName = getVersionName(getActivity());

		AboutItem maintainer = new AboutItem(getActivity(), R.drawable.ic_about_maintainer,
				R.string.about_item_maintainer, getString(R.string.nav_header_subtitle));

		AboutItem version = new AboutItem(getActivity(), R.drawable.ic_about_version,
				R.string.about_item_version, PackageVersionName + "");

		AboutItem update = new AboutItem(getActivity(), R.drawable.ic_about_update,
				R.string.update_check);

		AboutItem source = new AboutItem(getActivity(), R.drawable.ic_about_github,
				R.string.about_item_sourcecode, "https://github.com/1552980358/com.earth.OsToolkit");

		AboutItem xzr = new AboutItem(getActivity(), R.drawable.ic_about_maintainer,
				R.string.about_item_xzr, R.string.about_item_xzr_sum);

		AboutItem yc = new AboutItem(getActivity(), R.drawable.ic_about_maintainer,
				R.string.about_item_yc9559, R.string.about_item_yc9559_sum);

		maintainer.setOnClickListener(v -> jumpCoolapkAccount(getActivity(), "724354"));

		update.setOnClickListener(v -> {
			Toast.makeText(getActivity(), R.string.update_checking, Toast.LENGTH_SHORT).show();

			CheckUpdate checkUpdate = new CheckUpdate();
			checkUpdate.checkUpdate();

			// Method blocking Main Thread, wait for finish working of new Thread
			// 方法堵塞主线程，等待新线程完成工作
			checkUpdate.waitFor();

			if (checkUpdate.complete && !checkUpdate.getVersion().equals(Checking.getVersionName(getActivity()))) {
				if (!checkUpdate.getVersion().equals("Fail")) {

					// New Dialog/实例化Dialog
					UpdateDialogFragment updateDialogFragment = new UpdateDialogFragment();
					FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

					String ver = checkUpdate.getVersion();
					String d = checkUpdate.getDate();
					String cC = checkUpdate.getChangelogCn();
					String cE = checkUpdate.getChangelogEng();


					// Transmit data/发送数据
					updateDialogFragment.setVersion(ver);
					updateDialogFragment.setDate(d);
					updateDialogFragment.setChangelogEng(cC);
					updateDialogFragment.setChangelogCn(cE);

					// Show Dialog/显示Dialog
					updateDialogFragment.show(fragmentTransaction, "updateDialogFragment");
				} else {
					Toast.makeText(getActivity(), getString(R.string.update_fail), Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getActivity(), getString(R.string.update_newest), Toast.LENGTH_SHORT).show();
			}

		});

		source.setOnClickListener(v -> jumpSource(getActivity()));

		xzr.setOnClickListener(v -> jumpCoolapkAccount(getActivity(), "528761"));
		yc.setOnClickListener(v -> jumpCoolapkAccount(getActivity(), "557898"));

		linearLayout.addView(maintainer);
		linearLayout.addView(version);
		linearLayout.addView(update);
		linearLayout.addView(source);
		linearLayout.addView(xzr);
		linearLayout.addView(yc);

		TextView coolapk = view.findViewById(R.id.about_coolapk);
		TextView github = view.findViewById(R.id.about_github);

		coolapk.setOnClickListener(v -> jumpCoolapk(getActivity(), PackageName));
		github.setOnClickListener(v -> jumpSource(getActivity()));

		RelativeLayout relativeLayout_navBar = view.findViewById(R.id.about_navBarRelative);
		SwitchCompat switchCompat_navBar = view.findViewById(R.id.about_navBarSwitch);
		RelativeLayout relativeLayout_Theme = view.findViewById(R.id.about_themeRelative);

		SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ui", Context.MODE_PRIVATE);

		if (sharedPreferences.getBoolean("navBar", true)) {
			switchCompat_navBar.setChecked(true);
		}

		/*
		 * 28 Dec 2018
		 *
		 * By 1552980358
		 *
		 */

		relativeLayout_navBar.setOnClickListener(v -> {
			if (switchCompat_navBar.isChecked()) {
				sharedPreferences.edit().putBoolean("navBar", false).apply();
				switchCompat_navBar.setChecked(false);
			} else {
				sharedPreferences.edit().putBoolean("navBar", true).apply();
				switchCompat_navBar.setChecked(true);
			}

			Intent intent = new Intent(getActivity(), MainActivity.class);
			startActivity(intent);
		});

		switchCompat_navBar.setOnCheckedChangeListener((buttonView, isChecked) -> {
			if (isChecked) {
				sharedPreferences.edit().putBoolean("navBar", true).apply();
			} else {
				sharedPreferences.edit().putBoolean("navBar", false).apply();
			}
			Intent intent = new Intent(getActivity(), MainActivity.class);
			startActivity(intent);
		});

		relativeLayout_Theme.setOnClickListener(v -> {
			List <PackageInfo> packageInfoList = getActivity().getPackageManager().getInstalledPackages(0);
			List<String> packageName = new ArrayList<>();

			if (!packageInfoList.isEmpty()) {
				for (int i = 1; i < packageInfoList.size(); i++) {
					packageName.add(packageInfoList.get(i).packageName);
				}
			}

			for (int i = 0; i < packageName.size(); i++)
			    Log.i("packageName",packageName.get(i));

            Log.i("containSubTheme",packageName.contains(OsToolkitSubstratumName) + "");
			Log.i("containSubMan",packageName.contains(SubstratumName) + "");

			if (packageName.contains(OsToolkitSubstratumName)) {
				if (packageName.contains(SubstratumName)) {
					startActivity(getActivity().getPackageManager().getLaunchIntentForPackage(SubstratumName));
				} else {
					Toast.makeText(getActivity(), getString(R.string.about_toast_sub_not_installed), Toast.LENGTH_SHORT).show();
					jumpCoolapk(getActivity(), SubstratumName);
				}
			} else {
				Toast.makeText(getActivity(), getString(R.string.about_toast_theme_not_installed), Toast.LENGTH_SHORT).show();
				jumpCoolapk(getActivity(), OsToolkitSubstratumName);
			}

		});

	}

}