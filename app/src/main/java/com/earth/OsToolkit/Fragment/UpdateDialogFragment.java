package com.earth.OsToolkit.Fragment;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.earth.OsToolkit.R;
import com.earth.OsToolkit.Working.BaseClass.UpdateJump;


public class UpdateDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.update_found);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialogfragment_update,null);

        builder.setView(view);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("save", Context.MODE_PRIVATE);

        TextView version = view.findViewById(R.id.df_update_version);
        TextView date = view.findViewById(R.id.df_update_date);
        TextView changelog = view.findViewById(R.id.df_update_changelog);

        version.setText(sharedPreferences.getString("updateVersion","fail"));
        date.setText(sharedPreferences.getString("updateDate","fail"));
        changelog.setText(sharedPreferences.getString("updateChangelog","fail"));

        builder.setPositiveButton(getText(R.string.update_github), (dialog, which) -> {
            UpdateJump.jumpGithub(getActivity());
        });


        builder.setNegativeButton(getText(R.string.update_coolapk), (dialog, which) -> {

        });

        builder.setNeutralButton(getString(R.string.update_cancle), (dialog, which) -> {});

        return builder.create() ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}