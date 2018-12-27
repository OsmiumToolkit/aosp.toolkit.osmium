package com.earth.OsToolkit.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.earth.OsToolkit.Items.CoresCardView;
import com.earth.OsToolkit.R;
import com.earth.OsToolkit.Base.Checking;
import com.earth.OsToolkit.Base.FileWorking;
import static com.earth.OsToolkit.Base.Checking.*;

import java.util.*;

public class CoresFragment extends Fragment {

	/*
	 * 27 Dec 2018
	 *
	 * By 1552980358
	 *
	 */

	FragmentManager fragmentManager;
	Fragment fragment = new LoadingFragment();

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentManager = getFragmentManager();

		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.main_fragment,fragment).show(fragment).hide(this).commit();
	}

	@SuppressWarnings("all")
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {

		View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_cores,null);

		TextView cores_no = view.findViewById(R.id.cores_no);

		int cores = Checking.getCPUCores();

		cores_no.setText(String.format(getString(R.string.cores_no), cores));

		LinearLayout linearLayout = view.findViewById(R.id.cores_linearLayout);

		for (int i = 0; i < cores; i++) {
			CoresCardView coresCardView = new CoresCardView(getActivity(), "CPU" + i);
			coresCardView.setContext(getActivity());

			List<String> list = Checking.getAvailableFreq(getActivity(), i);

			// 最高频率
			// Max Frequency
			String currentFreq = Checking.getCoresFreq(getActivity(), i, "max");

			int j = 0;
			while (j < list.size()) {
				if (list.get(j).equals(currentFreq)) {
					Log.i("cores_" + i + "_freq_list_" + j, list.get(j));
					break;
				}
				else
					j++;
			}

			if (!checkFreqPresent(currentFreq, list)) {
				list.add(currentFreq);
			}

			coresCardView.setMaxCurrentFreq(i,j,list);

			// 最低频率
			// Min freq
			currentFreq = Checking.getCoresFreq(getActivity(), i, "min");
			j = 0;
			while (j < list.size()) {
				if (list.get(j).equals(currentFreq)) {
					Log.i("cores_" + i + "_freq_list_" + j, list.get(j));
					break;
				}
				else
					j++;
			}

			if (!checkFreqPresent(currentFreq, list)) {
				list.add(currentFreq);
			}

			coresCardView.setMinCurrentFreq(i, j, list);

			// 调整器
			String[] strings_governor = getResources().getStringArray(R.array.governor);

			String mode = FileWorking.readFile(getActivity(),
					"sys/devices/system/cpu/cpu" + i + "/cpufreq/scaling_governor");
			Log.i("cores_" + i + "_Mode", mode);

			j = 0;
			while (j < strings_governor.length) {
				if (mode.equals(strings_governor[j])) {
					break;
				} else {
					j++;
				}
			}

			if (j < strings_governor.length)
				coresCardView.setGovernor(i, j);
			else
				coresCardView.setGovernor(i, 5);

			Log.i("coreNo", i + "");
			Log.i("cores_" + i + "_MAX", Checking.getCoresFreq(getActivity(), i, "max"));
			Log.i("cores_" + i + "_MIN", Checking.getCoresFreq(getActivity(), i, "min"));
			linearLayout.addView(coresCardView);
		}


		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view,
	                          @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Fragment current = this;
		Timer timer = new Timer();

		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				FragmentTransaction fragmentTransactionEnd = fragmentManager.beginTransaction();
				fragmentTransactionEnd.remove(fragment).show(current).commit();
			}
		},1000);

	}
}
