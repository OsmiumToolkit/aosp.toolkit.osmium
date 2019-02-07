package com.earth.OsToolkit.view;
/*
 * OsToolkit - Kotlin
 *
 * Date : 3/1/2019
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


import android.app.Activity;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.earth.OsToolkit.R;
import com.earth.OsToolkit.base.*;

import java.util.*;

@SuppressWarnings("ViewConstructor")
public class CoreCardView extends LinearLayout {
    private int core;
    private Activity activity;

    public CoreCardView(Activity activity, int core) {
        super(activity);
        this.core = core;
        this.activity = activity;

        LayoutInflater.from(activity).inflate(R.layout.cardview_core, this);

        TextView textView = findViewById(R.id.ccv_title);
        textView.append("CPU" + core);

        new Thread(this::setMaxCurrentFreq).start();
        new Thread(this::setMinCurrentFreq).start();
        new Thread(this::setGovernor).start();
    }

    public int i = 0;
    private void setMaxCurrentFreq() {
        Spinner spinner = findViewById(R.id.ccv_max_freq);

        ArrayList<String> list = new ArrayList<>(Arrays.asList(
                BaseKotlinOperation.Companion.readFile("/sys/devices/system/cpu/cpu"
                        + core + "/cpufreq/scaling_available_frequencies")
                        .split(" ")));

        String freq = BaseKotlinOperation.Companion.readFile("/sys/devices/system/cpu/cpu"
                + core + "/cpufreq/" + "scaling_max_freq");

        if (list.contains(freq)) {
            list.add(freq);
            i = list.size() - 1;
        } else {
            while (i < list.size()) {
                if (list.get(i).equals(freq)) {
                    break;
                } else {
                    i++;
                }
            }
        }

        activity.runOnUiThread(() -> {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity,
                    android.R.layout.simple_spinner_dropdown_item,
                    list);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
            spinner.setSelection(i);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        Process process = Runtime.getRuntime().exec(
                                new String[]{"su", "-c",
                                        "echo", "\"" + list.get(position) + "\"", ">",
                                        "sys/devices/system/cpu/cpu" + core + "/cpufreq/scaling_max_freq"});

                        Log.i("cores_change_freq_max", process.waitFor() + "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        });

    }

    private int j = 0;
    private void setMinCurrentFreq() {
        Spinner spinner = findViewById(R.id.ccv_min_freq);

        ArrayList<String> list = new ArrayList<>(Arrays.asList(
                BaseKotlinOperation.Companion.readFile("/sys/devices/system/cpu/cpu"
                        + core + "/cpufreq/scaling_available_frequencies")
                        .split(" ")));

        String freq = BaseKotlinOperation.Companion.readFile("/sys/devices/system/cpu/cpu" + core + "/cpufreq/" + "scaling_min_freq");

        if (!list.contains(freq)) {
            list.add(freq);
            j = list.size() - 1;
        } else {
            while (j < list.size()) {
                if (list.get(j).equals(freq)) {
                    break;
                } else {
                    j++;
                }
            }
        }

        activity.runOnUiThread(() -> {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity,
                    android.R.layout.simple_spinner_dropdown_item,
                    list);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
            spinner.setSelection(j);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        Process process = Runtime.getRuntime().exec(
                                new String[]{"su", "-c",
                                        "echo", "\"" + list.get(position) + "\"", ">",
                                        "sys/devices/system/cpu/cpu" + core + "/cpufreq/scaling_min_freq"});

                        Log.i("cores_change_freq_min", process.waitFor() + "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        });

    }

    private int k = 0;
    public void setGovernor() {

        Spinner spinner = findViewById(R.id.ccv_governor);

        ArrayList<String> list = new ArrayList<>(Arrays.asList(
                BaseKotlinOperation.Companion.readFile("/sys/devices/system/cpu/cpu"
                        + core + "/cpufreq/scaling_available_governors")
                        .split(" ")));

        String governor = BaseKotlinOperation.Companion.readFile("sys/devices/system/cpu/cpu"
                + core + "/cpufreq/scaling_governor");

        while (k < list.size()) {
            if (list.get(k).equals(governor)) {
                break;
            } else {
                k++;
            }
        }

        activity.runOnUiThread(() -> {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity,
                    android.R.layout.simple_spinner_dropdown_item,
                    list);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(arrayAdapter);
            spinner.setSelection(k);
            spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    Log.i("cores_core", core + "");
                    Log.i("cores_position", position + " " + list.get(position));

                    try {
                        Process process = Runtime.getRuntime().exec(
                                new String[]{"su", "-c",
                                        "echo", "\"" + list.get(position) + "\"", ">",
                                        "sys/devices/system/cpu/cpu" + core + "/cpufreq/scaling_governor"});

                        Log.i("core_change_governor", process.waitFor() + "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
        });


    }


}
