package com.earth.OsToolkit.Fragment.Dialog;

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

	private String version;
	private String date;
	private String changelogEng;
	private String changelogCn;


	@SuppressWarnings("all")
	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setTitle(R.string.update_found);

		View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialogfragment_update, null);

		builder.setView(view);

		TextView textView_version = view.findViewById(R.id.df_update_version);
		TextView textView_date = view.findViewById(R.id.df_update_date);

		TextView textView_changelofEngTitle = view.findViewById(R.id.df_update_changelog_Eng_title);
		TextView textView_changelogCNTitle = view.findViewById(R.id.df_update_changelog_CN_title);

		TextView textView_changelogEng = view.findViewById(R.id.df_update_changelog_Eng);
		TextView textView_changelogCN = view.findViewById(R.id.df_update_changelog_CN);


		textView_version.setText(version);
		textView_date.setText(date);
		textView_changelogEng.setText(changelogEng);
		textView_changelogCN.setText(changelogCn);

		textView_changelofEngTitle.setOnClickListener(v -> {
			if (textView_changelogEng.getVisibility() == View.GONE) {
				textView_changelogEng.setVisibility(View.VISIBLE);
			} else {
				textView_changelogEng.setVisibility(View.GONE);
			}
		});

		textView_changelogCNTitle.setOnClickListener(v -> {
			if (textView_changelogCN.getVisibility() == View.GONE) {
				textView_changelogCN.setVisibility(View.VISIBLE);
			} else {
				textView_changelogCN.setVisibility(View.GONE);
			}
		});

		builder.setPositiveButton(getText(R.string.update_github), (dialog, which) -> Jumping.jumpGithub(getActivity()));

		builder.setNegativeButton(getText(R.string.update_coolapk), (dialog, which) -> Jumping.jumpCoolapk(getActivity()));

		builder.setNeutralButton(getString(R.string.cancel), (dialog, which) -> {});

		return builder.create();
	}

	public void setVerision(String version) {
		this.version = version;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setChangelogEng(String changelog) {
		this.changelogEng = changelog;
	}
	public void setChangelogCn(String changelog) {
		this.changelogCn = changelog;
	}
}