package com.earth.OsToolkit.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.earth.OsToolkit.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class UpdateDialogFragment extends DialogFragment {
    TextView version;
    TextView date;
    TextView changelog;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        return inflater.inflate(R.layout.dialogfragment_update, container);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getDialog().getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.string.update_found);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        version = view.findViewById(R.id.df_update_version);
        date = view.findViewById(R.id.df_update_date);
        changelog = view.findViewById(R.id.df_update_changelog);

        setContent();
    }

    public void setContent() {
        String s_version;
        try {
            URL url = new URL("https://raw.githubusercontent.com/1552980358/1552980358.github.io/master/OsToolkit");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(url.openStream(), "UTF-8"));

            version.append(bufferedReader.readLine());
            date.append(bufferedReader.readLine());

            while ((s_version = bufferedReader.readLine()) != null) {
                changelog.append(s_version + "\n");
            }

            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
