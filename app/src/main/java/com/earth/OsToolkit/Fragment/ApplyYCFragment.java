package com.earth.OsToolkit.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.earth.OsToolkit.ApplyYCActivity;
import com.earth.OsToolkit.Items.ApplyYCRelativeLayout;
import com.earth.OsToolkit.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ApplyYCFragment extends Fragment {
	List<String> list = new ArrayList<>();
	TextView textView_date;
	ProgressBar progressBar;
	LinearLayout linearLayout_root;
	LinearLayout linearLayout_snap;
	LinearLayout linearLayout_exynos;
	LinearLayout linearLayout_mtk;
	LinearLayout linearLayout_kirin;
	LinearLayout linearLayout_atom;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_applyyc, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		linearLayout_root = view.findViewById(R.id.apply_linear_root);
		progressBar = view.findViewById(R.id.applyyc_loading);
		linearLayout_snap = view.findViewById(R.id.applyyc_snap);
		linearLayout_exynos = view.findViewById(R.id.applyyc_exynos);
		linearLayout_mtk = view.findViewById(R.id.applyyc_mtk);
		linearLayout_kirin = view.findViewById(R.id.applyyc_kirin);
		linearLayout_atom = view.findViewById(R.id.applyyc_atom);

		textView_date = view.findViewById(R.id.apply_date);

		GetSupportProcessor getSupportProcessor = new GetSupportProcessor();
		getSupportProcessor.execute();

		// 方法堵塞，等待线程完成
		// Method stop : wait for the finish of new thread
		while (!getSupportProcessor.complete) {
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {}
			}, 50);
		}

		progressBar.setVisibility(View.GONE);
	}

	public class GetSupportProcessor extends AsyncTask<Void, Integer, Integer> {
		public boolean complete = false;

		@Override
		protected Integer doInBackground(Void... voids) {
			try {
				URL url = new URL("https://raw.githubusercontent.com/1552980358/1552980358.github.io/master/yc_processor_table");
				InputStream inputStream = url.openStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

				textView_date.setText(bufferedReader.readLine());

				String line;
				while ((line = bufferedReader.readLine()) != null) {
					list.add(line);
				}

				inputStream.close();
				inputStreamReader.close();
				bufferedReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			complete = true;

			for (int i = 0; i < list.size(); i++) {
				ApplyYCRelativeLayout applyYCRelativeLayout = new ApplyYCRelativeLayout(getActivity(), getParentFragment(), list.get(i));
				final int no = i;
				applyYCRelativeLayout.setClickListener(v -> {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setTitle(list.get(no))
							.setMessage(String.format(getString(R.string.apply_confirm), list.get(no)))
							.setPositiveButton(getString(R.string.cont), (dialog, which) -> {
								startActivity(new Intent(getActivity(), ApplyYCActivity.class).putExtra("board",list.get(no)));
							})
							.setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
							}).show();
				});

				if (list.get(i).startsWith("sd")) {
					linearLayout_snap.addView(applyYCRelativeLayout);
				} else if (list.get(i).startsWith("exynos")) {
					linearLayout_exynos.addView(applyYCRelativeLayout);
				} else if (list.get(i).startsWith("kirin")) {
					linearLayout_kirin.addView(applyYCRelativeLayout);
				} else if (list.get(i).startsWith("helio")) {
					linearLayout_mtk.addView(applyYCRelativeLayout);
				} else {
					linearLayout_atom.addView(applyYCRelativeLayout);
				}


			}
			return null;
		}

		@Override
		protected void onPostExecute(Integer integer) {
			super.onPostExecute(integer);
			linearLayout_root.setVisibility(View.VISIBLE);
		}
	}
}
