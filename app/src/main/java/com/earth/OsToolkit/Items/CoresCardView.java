package com.earth.OsToolkit.Items;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.earth.OsToolkit.R;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class CoresCardView extends LinearLayout {

	Context context;
	public CoresCardView(Context context, String core) {
		super(context);

		LayoutInflater.from(context).inflate(R.layout.cardview_cores, this);

		TextView textView_title = findViewById(R.id.cores_title);
		textView_title.setText(core);

	}

	public void setContext(Context context) {
		this.context = context;
	}

	public void setMaxCurrentFreq(int core, int freq, List<String> list) {
		Spinner spinner_max_Freq = findViewById(R.id.cores_max_freq);

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context,
				android.R.layout.simple_spinner_dropdown_item,
				list);

		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner_max_Freq.setAdapter(arrayAdapter);

		spinner_max_Freq.setSelection(freq);

		spinner_max_Freq.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					Process process = Runtime.getRuntime().exec(
							new String[]{"su","-c",
									"echo","\"" + list.get(position) + "\"",">",
									"sys/devices/system/cpu/cpu" + core + "/cpufreq/scaling_max_freq"});

					Log.i("cores_change_freq_max",process.waitFor() + "");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	public void setMinCurrentFreq(int core, int freq, List<String> list) {
		Spinner spinner_min_Freq = findViewById(R.id.cores_min_freq);

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context,
				android.R.layout.simple_spinner_dropdown_item,
				list);

		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner_min_Freq.setAdapter(arrayAdapter);

		spinner_min_Freq.setSelection(freq);

		spinner_min_Freq.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				try {
					Process process = Runtime.getRuntime().exec(
							new String[]{"su","-c",
									"echo","\"" + list.get(position) + "\"",">",
									"sys/devices/system/cpu/cpu" + core + "/cpufreq/scaling_min_freq"});

					Log.i("cores_change_freq_min",process.waitFor()+"");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}





	public void setGovernor(int core, int mode) {
		Spinner spinner_Freq = findViewById(R.id.cores_spinner);
		List<String> list = new ArrayList<>();

		String[] strings = getResources().getStringArray(R.array.governor);

		for (int i = 0; i < strings.length; i++)
			list.add(strings[i]);


		Log.i("listSpinner",list.isEmpty()+"");

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item,list);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_Freq.setAdapter(arrayAdapter);

		Log.i("cores_mode",mode+"");
		spinner_Freq.setSelection(mode);

		spinner_Freq.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				Log.i("cores_core",core + "");
				Log.i("cores_position",position + " " + list.get(position));

				try {

					Process process = Runtime.getRuntime().exec(
							new String[]{"su","-c",
									"echo","\"" + list.get(position) + "\"",">",
									"sys/devices/system/cpu/cpu" + core + "/cpufreq/scaling_governor"});

					process.waitFor();

				} catch (Exception e) {
					e.printStackTrace();
				}


			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

}
