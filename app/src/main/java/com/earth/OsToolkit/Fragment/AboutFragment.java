package com.earth.OsToolkit.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.earth.OsToolkit.Items.AboutItem;
import com.earth.OsToolkit.R;
import com.earth.OsToolkit.Working.BaseClass.Checking;

import static android.content.Context.MODE_PRIVATE;

public class AboutFragment extends Fragment {

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_about,container,false);
	}

	String PackageVersionCode;
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		LinearLayout linearLayout = view.findViewById(R.id.about_linear);

		try {
			PackageVersionCode = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(),0).versionName;
		}catch (Exception e) {
			e.printStackTrace();
		}
		AboutItem maintainer = new AboutItem(getActivity(),R.drawable.ic_about_maintainer,
				R.string.about_item_maintainer,getString(R.string.nav_header_subtitle));

		AboutItem version = new AboutItem(getActivity(), R.drawable.ic_about_version,
				R.string.about_item_version, PackageVersionCode + "");

		AboutItem update = new AboutItem(getActivity(),R.drawable.ic_about_update,
				R.string.update_check);

		AboutItem source = new AboutItem(getActivity(),R.drawable.ic_about_github,
				R.string.about_item_sourcecode,"https://github.com/1552980358/com.earth.OsToolkit");

		update.setOnClickListener(v -> {
			Toast.makeText(getActivity(), R.string.update_checking, Toast.LENGTH_SHORT).show();
			Checking.checkVersion(getActivity());

			SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UpdateSP",MODE_PRIVATE);
			if (!sharedPreferences.getString("updateVersion","fail")
					     .equals(PackageVersionCode)) {
				UpdateDialogFragment updateDialogFragment = new UpdateDialogFragment();
				FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
				updateDialogFragment.show(fragmentTransaction, "updateDialogFragment");
			}

		});

		linearLayout.addView(maintainer);
		linearLayout.addView(version);
		linearLayout.addView(update);
		linearLayout.addView(source);
	}
}