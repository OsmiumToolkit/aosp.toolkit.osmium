package com.earth.OsToolkit.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.*;

import com.earth.OsToolkit.Items.CardSwitchCompactItem;
import com.earth.OsToolkit.R;

import java.io.*;

import static com.earth.OsToolkit.Working.BaseClass.BaseIndex.CHARGE_QC3;
import static com.earth.OsToolkit.Working.BaseClass.Checking.checkSupportQC3;

public class ChargingFragment extends Fragment{

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container,
	                         Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_charging, container, false);
	}

	CardSwitchCompactItem qc3;

	@Override
	public void onViewCreated(@NonNull View view,
	                          Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		setWarning(view);
		setQC3(view);
	}

	public void setWarning(View view) {
		CardView cardView = view.findViewById(R.id.charging_cardview);
		Log.i("checkSupportQC3",String.valueOf(checkSupportQC3()));
		if (checkSupportQC3()) {
			cardView.setVisibility(View.GONE);
		}
	}

	public void setQC3(View view) {
		qc3 = view.findViewById(R.id.charging_qc3);
		qc3.setTitle(R.string.charging_qc3_title);
		qc3.setSummary(R.string.charging_qc3_des);
		qc3.setScript(getActivity(),CHARGE_QC3);
		qc3.setLayoutListener();
		qc3.setCurrentFragment(new ChargingFragment());
		qc3.setSwitchCompatChecked(true);

		if (checkSupportQC3()) {
			new Thread(() -> {
				try {
					Process p = Runtime.getRuntime().exec("su");
					p.waitFor();
					Process process = Runtime.getRuntime().exec(
							new String[]{"/system/bin/sh", "-c",
									"cat", "/sys/class/power_supply/battery/allow_hvdcp3"});
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(process.getInputStream(), "utf-8"));
					if (bufferedReader.readLine().equals("1")) {
						qc3.setSwitchCompatChecked(true);
					} else {
						qc3.setSwitchCompatChecked(false);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
		} else {
			qc3.disableSwitchCompact();
		}

	}
}