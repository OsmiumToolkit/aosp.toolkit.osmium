package com.earth.OsToolkit.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.*;

import com.earth.OsToolkit.Items.CardSwitchCompactItem;
import com.earth.OsToolkit.R;
import com.earth.OsToolkit.Working.FileWorking;

import static com.earth.OsToolkit.Working.BaseClass.BaseIndex.*;
import static com.earth.OsToolkit.Working.BaseClass.Checking.checkFilePresent;

public class ChargingFragment extends Fragment {

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container,
	                         Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_charging, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view,
	                          Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		setWarning(view);
		setAllow(view);
		setQC3(view);

	}

	public void setWarning(View view) {
		CardView cardView = view.findViewById(R.id.charging_cardview);
		if (!checkFilePresent("/sys/class/power_supply/battery/battery_charging_enabled")
				|| !checkFilePresent("/sys/class/power_supply/battery/allow_hvdcp3")){
			cardView.setVisibility(View.VISIBLE);
		}
	}

	public void setAllow(View view) {
		CardSwitchCompactItem allow = view.findViewById(R.id.charging_allow);
		allow.setTitle(R.string.charging_allow_title);
		allow.setSummary(R.string.charging_allow_sum);
		allow.setLayoutListener();

		if (checkFilePresent("/sys/class/power_supply/battery/battery_charging_enabled")) {
			if (FileWorking.readFile(getActivity(),"/sys/class/power_supply/battery/battery_charging_enabled")
					    .equals("1")) {
				allow.setSwitchCompatChecked(true);
			} else {
				allow.setSwitchCompatChecked(false);
			}
			allow.setSwitchCompatOnChangeListener(this,getActivity(),CHARGE_ALLOW);
		} else {
			allow.disableSwitchCompact();
		}
	}

	public void setQC3(View view) {
		CardSwitchCompactItem qc3 = view.findViewById(R.id.charging_qc3);
		qc3.setTitle(R.string.charging_qc3_title);
		qc3.setSummary(R.string.charging_qc3_sum);
		qc3.setLayoutListener();

		if (checkFilePresent("/sys/class/power_supply/battery/allow_hvdcp3")) {
			if (FileWorking.readFile(getActivity(), "/sys/class/power_supply/battery/allow_hvdcp3")
					    .equals("1")) {
				qc3.setSwitchCompatChecked(true);
			} else {
				qc3.setSwitchCompatChecked(false);
			}
			qc3.setSwitchCompatOnChangeListener(this,getActivity(),CHARGE_QC3);
		} else {
			qc3.disableSwitchCompact();
		}

	}

	@SuppressWarnings("all")
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.main_fragment, new ChargingFragment()).commit();

		super.onActivityResult(requestCode, resultCode, data);
	}
}