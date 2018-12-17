package com.earth.OsToolkit.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.earth.OsToolkit.Items.CoresCardView;
import com.earth.OsToolkit.R;
import com.earth.OsToolkit.Working.BaseClass.Checking;
import com.earth.OsToolkit.Working.FileWorking;

import java.util.List;

public class CoresFragment extends Fragment {
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_cores, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view,
	                          @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);


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
					Log.i("cores_freq_list_" + j, list.get(j));
					break;
				}
				else
					j++;
			}

			coresCardView.setMaxCurrentFreq(i,j,list);

			currentFreq = Checking.getCoresFreq(getActivity(), i, "min");
			j = 0;
			while (j < list.size()) {
				if (list.get(j).equals(currentFreq)) {
					Log.i("cores_freq_list_" + j, list.get(j));
					break;
				}
				else
					j++;
			}

			coresCardView.setMinCurrentFreq(i, j, list);

			// 调整器
			String[] strings_governor = getResources().getStringArray(R.array.governor);

			String mode = FileWorking.readFile(getActivity(),
					"sys/devices/system/cpu/cpu" + i + "/cpufreq/scaling_governor");
			Log.i("coresMode", mode);

			j = 0;
			while (j < strings_governor.length) {
				if (mode.equals(strings_governor[j])) {
					break;
				} else {
					j++;
				}
			}

			if (j != strings_governor.length)
				coresCardView.setGovernor(i, j);
			else
				coresCardView.setGovernor(i, 5);

			Log.i("coreNo", i + "");
			Log.i("coresMAX", Checking.getCoresFreq(getActivity(), i, "max"));
			Log.i("coresMIN", Checking.getCoresFreq(getActivity(), i, "min"));
			linearLayout.addView(coresCardView);
		}


	}
}
