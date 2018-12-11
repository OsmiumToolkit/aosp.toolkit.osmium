package com.earth.OsToolkit.Fragment;

import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.support.annotation.*;
import android.support.v4.app.DialogFragment;
import android.view.*;
import android.widget.TextView;

import com.earth.OsToolkit.R;
import com.earth.OsToolkit.Working.BaseClass.Jumping;


public class UpdateDialogFragment extends DialogFragment {

	@SuppressWarnings("all")
	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle(R.string.update_found);

		View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialogfragment_update, null);

		builder.setView(view);

		SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UpdateSP", Context.MODE_PRIVATE);

		TextView version = view.findViewById(R.id.df_update_version);
		TextView date = view.findViewById(R.id.df_update_date);

		TextView changelofEngTitle = view.findViewById(R.id.df_update_changelog_Eng_title);
		TextView changelogCNTitle = view.findViewById(R.id.df_update_changelog_CN_title);

		TextView changelogEng = view.findViewById(R.id.df_update_changelog_Eng);
		TextView changelogCN = view.findViewById(R.id.df_update_changelog_CN);

		String vers = sharedPreferences.getString("updateVersion", "Failed!");
		String d = sharedPreferences.getString("updateDate", "Failed!");
		String cEng = sharedPreferences.getString("updateChangelogEng","Failed!");
		String cCN = sharedPreferences.getString("updateChangelogCN","Failed!");

		version.setText(vers);
		date.setText(d);
		changelogEng.setText(cEng);
		changelogCN.setText(cCN);

		changelofEngTitle.setOnClickListener(v -> {
			if (changelogEng.getVisibility() == View.GONE) {
				changelogEng.setVisibility(View.VISIBLE);
			} else {
				changelogEng.setVisibility(View.GONE);
			}
		});

		changelogCNTitle.setOnClickListener(v -> {
			if (changelogCN.getVisibility() == View.GONE) {
				changelogCN.setVisibility(View.VISIBLE);
			} else {
				changelogCN.setVisibility(View.GONE);
			}
		});

		builder.setPositiveButton(getText(R.string.update_github), (dialog, which) -> Jumping.jumpGithub(getActivity()));

		builder.setNegativeButton(getText(R.string.update_coolapk), (dialog, which) -> Jumping.jumpCoolapk(getActivity()));

		builder.setNeutralButton(getString(R.string.cancel), (dialog, which) -> {});

		return builder.create();
	}
}