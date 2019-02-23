package com.earth.OsToolkit.fragments;
/*
 * OsToolkit - Kotlin
 *
 * Date : 1/1/2019
 *
 * By   : 1552980358
 *
 */

/*
 * Modify
 *
 * 9/1/2019
 * 23/1/2019
 *
 */


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.earth.OsToolkit.R;
import com.earth.OsToolkit.ScriptActivity;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

import static com.earth.OsToolkit.base.BaseIndex.type_yc;

public class ApplyYCFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_applyyc, container, false);
    }


    @SuppressWarnings("all")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(LayoutInflater.from(getActivity()).inflate(R.layout.dialog_loading, null));
        dialog.setCancelable(false);
        dialog.show();

        getActivity().getSharedPreferences("1", Context.MODE_PRIVATE).edit().commit();

        LinearLayout linearLayout_snap = view.findViewById(R.id.yc_snap);
        LinearLayout linearLayout_exynos = view.findViewById(R.id.yc_exynos);
        LinearLayout linearLayout_mtk = view.findViewById(R.id.yc_mtk);
        LinearLayout linearLayout_kirin = view.findViewById(R.id.yc_kirin);
        LinearLayout linearLayout_atom = view.findViewById(R.id.yc_atom);

        new Thread(() -> {
            try {
                URL url = new URL("https://raw.githubusercontent.com/1552980358/1552980358.github.io/master/yc_processor_table");
                InputStream inputStream = url.openStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String date = bufferedReader.readLine();
                Log.i("date", date);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    YcDeviceView ycDeviceView = new YcDeviceView(getActivity(), this, date, line);
                    if (line.startsWith("sd")) {
                        getActivity().runOnUiThread(() -> linearLayout_snap.addView(ycDeviceView));
                    } else if (line.startsWith("exynos")) {
                        getActivity().runOnUiThread(() -> linearLayout_exynos.addView(ycDeviceView));
                    } else if (line.startsWith("kirin")) {
                        getActivity().runOnUiThread(() -> linearLayout_kirin.addView(ycDeviceView));
                    } else if (line.startsWith("helio")) {
                        getActivity().runOnUiThread(() -> linearLayout_mtk.addView(ycDeviceView));
                    } else {
                        getActivity().runOnUiThread(() -> linearLayout_atom.addView(ycDeviceView));
                    }
                }

                inputStream.close();
                inputStreamReader.close();
                bufferedReader.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.cancel();
                        }
                    });
                }
            }, 1000);

        }).start();
    }

    public class YcDeviceView extends LinearLayout {
        public YcDeviceView(Context context, Fragment fragment, String index, String board) {
            super(context);
            LayoutInflater.from(context).inflate(R.layout.view_ycdevice, this);

            TextView textView = findViewById(R.id.title);
            textView.setText(board);
            RelativeLayout relativeLayout = findViewById(R.id.root);
            relativeLayout.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(board).setMessage(String.format(fragment.getString(R.string.apply_confirm), board))
                        .setPositiveButton(R.string.cont, (dialog, which) -> {
                            fragment.startActivity(new Intent(context, ScriptActivity.class)
                                    .putExtra("type", type_yc)
                                    .putExtra("index", index)
                                    .putExtra("name", board));
                        })
                        .setNegativeButton(R.string.cancel, (dialog, which) -> {

                        }).show();
            });
        }
    }
}