package com.earth.OsToolkit.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.earth.OsToolkit.R;
import com.earth.OsToolkit.ScriptActivity;

import java.io.*;

import static android.app.Activity.RESULT_CANCELED;
import static com.earth.OsToolkit.Working.BaseClass.BaseIndex.CHARGE_QC3;
import static com.earth.OsToolkit.Working.BaseClass.Checking.checkSupportQC3;

public class ChargingFragment extends Fragment implements View.OnClickListener {

	LinearLayout linearLayout_qc3;
	SwitchCompat qc3_sw;
	TextView qc3_txt;

	Boolean result;

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
		setQC3(view);

		// QC 3.0
		linearLayout_qc3 = view.findViewById(R.id.qc3_linearlayout);
		linearLayout_qc3.setOnClickListener(this);

		qc3_sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
			Intent intent = new Intent(getActivity(), ScriptActivity.class)
					                .putExtra("script", CHARGE_QC3);
			startActivityForResult(intent, 0);
		});
	}

	public void setWarning(View view) {
		CardView cardView = view.findViewById(R.id.charging_cardview);
		if (!checkSupportQC3()) {
			cardView.setVisibility(View.GONE);
		}
	}

	public void setQC3(View view) {
		qc3_txt = view.findViewById(R.id.txt_qc3);
		qc3_sw = view.findViewById(R.id.switch_qc3);

		if (new File("/sys/class/power_supply/battery/allow_hvdcp3").exists()) {
			new Thread(() -> {
				try {
					Process process = Runtime.getRuntime().exec(
							new String[]{"/system/bin/sh", "-c",
									"cat", "/sys/class/power_supply/battery/allow_hvdcp3"});
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(process.getInputStream(), "utf-8"));
					if (bufferedReader.readLine().equals("1")) {
						qc3_txt.setText(R.string.sw_en);
						qc3_sw.setChecked(true);
					} else {
						qc3_txt.setText(R.string.sw_dis);
						qc3_sw.setChecked(false);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
			qc3_sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
				startActivityForResult(new Intent(getActivity(), ScriptActivity.class)
						                       .putExtra("script", CHARGE_QC3), 0);
			});
		} else {
			qc3_txt.setText(R.string.sw_dis);
			qc3_sw.setClickable(false);
		}

	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.qc3_linearlayout:
				RelativeLayout relativeLayout_qc3 = view.findViewById(R.id.rl_qc3);
				if (relativeLayout_qc3.getVisibility() == View.GONE) {
					relativeLayout_qc3.setVisibility(View.VISIBLE);
				} else {
					relativeLayout_qc3.setVisibility(View.GONE);
				}
				break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0 && resultCode == RESULT_CANCELED) {
			result = intent.getBooleanExtra("result", true);
		}

		Toast.makeText(getContext(), getText(R.string.reflash), Toast.LENGTH_SHORT).show();

		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.main_fragment, new ChargingFragment()).commit();

		Log.e("ChargingFragment", "return");
	}
}