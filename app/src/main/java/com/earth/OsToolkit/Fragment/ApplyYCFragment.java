package com.earth.OsToolkit.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.*;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.*;
import android.widget.*;

import com.earth.OsToolkit.ApplyYCActivity;
import com.earth.OsToolkit.Items.ApplyYCRelativeLayout;
import com.earth.OsToolkit.R;

import java.io.*;
import java.net.URL;
import java.util.*;

public class ApplyYCFragment extends Fragment {

	/*
	 * 27 Dec 2018
	 *
	 * By 1552980358
	 *
	 */

	List<String> list = new ArrayList<>();
	FragmentManager fragmentManager;
	Fragment fragment_loading = new LoadingFragment();

	private boolean complete = false;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentManager = getFragmentManager();

		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.hide(this).add(R.id.main_fragment, fragment_loading).show(fragment_loading).commit();

	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_applyyc, null);

		TextView textView_date = view.findViewById(R.id.apply_date);

		LinearLayout linearLayout_snap = view.findViewById(R.id.applyyc_snap);
		LinearLayout linearLayout_exynos = view.findViewById(R.id.applyyc_exynos);
		LinearLayout linearLayout_mtk = view.findViewById(R.id.applyyc_mtk);
		LinearLayout linearLayout_kirin = view.findViewById(R.id.applyyc_kirin);
		LinearLayout linearLayout_atom = view.findViewById(R.id.applyyc_atom);

		new Thread(() -> {

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

		}).start();

		while (!complete) {
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
				}
			}, 10);
		}

		for (int i = 0; i < list.size(); i++) {
			ApplyYCRelativeLayout applyYCRelativeLayout = new ApplyYCRelativeLayout(getActivity(), getParentFragment(), list.get(i));
			final int no = i;
			applyYCRelativeLayout.setClickListener(v -> {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle(list.get(no))
						.setMessage(String.format(getString(R.string.apply_confirm), list.get(no)))
						.setPositiveButton(getString(R.string.cont), (dialog, which) -> {
							startActivity(new Intent(getActivity(), ApplyYCActivity.class).putExtra("board", list.get(no)));
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


		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Fragment fragment = this;

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				FragmentTransaction fragmentTransactionEnd = fragmentManager.beginTransaction();
				fragmentTransactionEnd.show(fragment).remove(fragment_loading).commit();
			}
		}, 1000);

	}

}
