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
 *
 */


import android.os.*;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.earth.OsToolkit.R;
import com.earth.OsToolkit.view.YcDeviceView;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class ApplyYCFragment extends Fragment {
    ArrayList<String> list = new ArrayList<>();
    private String date = null;
    Fragment fragment;
    Handler handler = new Handler();
    LinearLayout linearLayout_snap;
    LinearLayout linearLayout_exynos;
    LinearLayout linearLayout_mtk;
    LinearLayout linearLayout_kirin;
    LinearLayout linearLayout_atom;
    ProgressBar progressBar;
    //boolean created = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_applyyc, container, false);
        //this.view = view;
        fragment = this;
        linearLayout_snap = view.findViewById(R.id.yc_snap);
        linearLayout_exynos = view.findViewById(R.id.yc_exynos);
        linearLayout_mtk = view.findViewById(R.id.yc_mtk);
        linearLayout_kirin = view.findViewById(R.id.yc_kirin);
        linearLayout_atom = view.findViewById(R.id.yc_atom);
        progressBar = view.findViewById(R.id.progressBar);
        LoadDevice loadDevice = new LoadDevice();
        loadDevice.start();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //created = true;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    Runnable setBoard = () -> {
        for (int i = 0; i < list.size(); i++) {
            String board = list.get(i);
            Log.i("board", board);
            YcDeviceView ycDeviceView = new YcDeviceView(getActivity(), fragment, date, list.get(i));
            if (board.startsWith("sd")) {
                linearLayout_snap.addView(ycDeviceView);
            } else if (board.startsWith("exynos")) {
                linearLayout_exynos.addView(ycDeviceView);
            } else if (board.startsWith("kirin")) {
                linearLayout_kirin.addView(ycDeviceView);
            } else if (board.startsWith("helio")) {
                linearLayout_mtk.addView(ycDeviceView);
            } else {
                linearLayout_atom.addView(ycDeviceView);
            }
        }

        try {
            Thread.sleep(1000);
            progressBar.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    };

    class LoadDevice extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                URL url = new URL("https://raw.githubusercontent.com/1552980358/1552980358.github.io/master/yc_processor_table");
                InputStream inputStream = url.openStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                date = bufferedReader.readLine();

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    list.add(line);
                }

                inputStream.close();
                inputStreamReader.close();
                bufferedReader.close();
                /*
                while (!created) {
                    sleep(10);
                }
                */
                handler.post(setBoard);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
