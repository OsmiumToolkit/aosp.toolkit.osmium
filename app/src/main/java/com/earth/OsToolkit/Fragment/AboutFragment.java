package com.earth.OsToolkit.Fragment;

import android.os.Bundle;
import android.support.annotation.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.method.LinkMovementMethod;
import android.view.*;
import android.widget.*;

import com.earth.OsToolkit.Fragment.Dialog.UpdateDialogFragment;
import com.earth.OsToolkit.Items.AboutItem;
import com.earth.OsToolkit.R;
import com.earth.OsToolkit.Base.CheckUpdate;
import com.earth.OsToolkit.Base.Checking;
import com.earth.OsToolkit.Base.Jumping;

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

	String PackageVersionCode;

	@SuppressWarnings("all")
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		LinearLayout linearLayout = view.findViewById(R.id.about_linear);

		try {
			PackageVersionCode = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		AboutItem maintainer = new AboutItem(getActivity(), R.drawable.ic_about_maintainer,
				R.string.about_item_maintainer, getString(R.string.nav_header_subtitle));

		AboutItem version = new AboutItem(getActivity(), R.drawable.ic_about_version,
				R.string.about_item_version, PackageVersionCode + "");

		AboutItem update = new AboutItem(getActivity(), R.drawable.ic_about_update,
				R.string.update_check);

		AboutItem source = new AboutItem(getActivity(), R.drawable.ic_about_github,
				R.string.about_item_sourcecode, "https://github.com/1552980358/com.earth.OsToolkit");

		maintainer.setOnClickListener(v -> Jumping.jumpCoolapkAccount(getActivity()));

		update.setOnClickListener(v -> {
			Toast.makeText(getActivity(), R.string.update_checking, Toast.LENGTH_SHORT).show();

			CheckUpdate checkUpdate = new CheckUpdate();
			checkUpdate.checkUpdate();

			// 方法堵塞主线程，等待新线程完成工作
			// Method blocking Main Thread, wait for finish working of new Thread
			checkUpdate.waitFor();

			if (checkUpdate.complete && !checkUpdate.getVersion().equals(Checking.getVersionName(getActivity()))) {
				if (!checkUpdate.getVersion().equals("Fail")) {

					// 实例化Dialog
					// New Dialog
					UpdateDialogFragment updateDialogFragment = new UpdateDialogFragment();
					FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

					String ver = checkUpdate.getVersion();
					String d = checkUpdate.getDate();
					String cC = checkUpdate.getChangelogCn();
					String cE = checkUpdate.getChangelogEng();


					//发送数据
					// Transmit data
					updateDialogFragment.setVersion(ver);
					updateDialogFragment.setDate(d);
					updateDialogFragment.setChangelogEng(cC);
					updateDialogFragment.setChangelogCn(cE);

					// 显示Dialog
					// Show Dialog
					updateDialogFragment.show(fragmentTransaction, "updateDialogFragment");
				} else {
					Toast.makeText(getActivity(), getString(R.string.update_fail), Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getActivity(), getString(R.string.update_newest), Toast.LENGTH_SHORT).show();
			}

		});

		source.setOnClickListener(v -> Jumping.jumpSource(getActivity()));

		linearLayout.addView(maintainer);
		linearLayout.addView(version);
		linearLayout.addView(update);
		linearLayout.addView(source);

		TextView coolapk = view.findViewById(R.id.about_coolapk);
		TextView github = view.findViewById(R.id.about_github);

		coolapk.setOnClickListener(v -> Jumping.jumpCoolapk(getActivity()));
		coolapk.setMovementMethod(LinkMovementMethod.getInstance());

		github.setOnClickListener(v -> Jumping.jumpSource(getActivity()));
		github.setMovementMethod(LinkMovementMethod.getInstance());
	}

}