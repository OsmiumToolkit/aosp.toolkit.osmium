package aosp.toolkit.perseus.fragments.dialog;
/*
 * OsToolkit - Kotlin
 *
 * Date : 6/1/2019
 *
 * By   : 1552980358
 *
 */

/*
 * Modify
 *
 * 9/1/2019
 *
 */


import android.app.*;
import android.os.Bundle;
import android.support.annotation.*;
import android.support.v4.app.DialogFragment;
import android.view.*;
import android.widget.TextView;

import aosp.toolkit.perseus.R;
import aosp.toolkit.perseus.base.Accessing;

import static aosp.toolkit.perseus.base.BaseIndex.*;

public class UpdateDialogFragment extends DialogFragment {
    String version;
    String date;
    String changelogEn;
    String changelogZh;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialogfragment_update, null);

        TextView v = view.findViewById(R.id.update_version);
        TextView d = view.findViewById(R.id.update_date);
        TextView Zh = view.findViewById(R.id.update_changelogZh_title);
        TextView En = view.findViewById(R.id.update_changelogEn_title);
        TextView cEn = view.findViewById(R.id.update_changelogEn);
        TextView cZh = view.findViewById(R.id.update_changelogZh);

        v.setText(version);
        d.setText(date);
        cEn.setText(changelogEn);
        cZh.setText(changelogZh);

        Zh.setOnClickListener(v1 -> cZh.setVisibility(cZh.getVisibility() == View.GONE ? View.VISIBLE : View.GONE));
        En.setOnClickListener(v1 -> cEn.setVisibility(cEn.getVisibility() == View.GONE ? View.VISIBLE : View.GONE));

        builder.setTitle(R.string.update_found);
        builder.setPositiveButton(R.string.update_github, (dialog, which) -> {
            Accessing.Companion.accessGitHub(getActivity(), Repo_Release);
        }).setNegativeButton(R.string.update_coolapk, (dialog, which) -> {
            Accessing.Companion.accessCoolapkRelease(getActivity(), PackageName);
        }).setNeutralButton(R.string.cancel, (dialog, which) -> {

        });

        builder.setView(view);
        return builder.create();
    }

    public UpdateDialogFragment setData(String version, String date, String changelogZh, String changelogEn) {
        this.version = version;
        this.date = date;
        this.changelogZh = changelogZh;
        this.changelogEn = changelogEn;
        return this;
    }
}
