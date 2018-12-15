package com.earth.OsToolkit.Working.BaseClass;

import android.content.*;

import android.os.Build;
import android.util.Log;

import com.earth.OsToolkit.Working.FileWorking;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@SuppressWarnings("all")
public class Checking {
	public static boolean checkRoot() {
		Process process;
		DataOutputStream os;
		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes("exit\n");
			os.flush();
			int exitValue = process.waitFor();
			if (exitValue == 1)
				return false;
			else
				return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean checkFilePresent(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	public static boolean checkVersion(Context context) {
		try {
			URL url = new URL("https://raw.githubusercontent.com/1552980358/1552980358.github.io/master/OsToolkit");
			Log.i("URL", url.toString());
			new Thread(() -> {
				try {
					InputStream inputStream = url.openStream();
					InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

					String version = bufferedReader.readLine();
					String date = bufferedReader.readLine();
					StringBuilder stringBuilderEng = new StringBuilder();
					StringBuilder stringBuilderCN = new StringBuilder();
					if (bufferedReader.readLine().equals("<ENG>")) {
						String line;
						while (!(line = bufferedReader.readLine()).equals("<CN>")) {
							stringBuilderEng.append(line);
							stringBuilderEng.append("\n");
						}

						while ((line = bufferedReader.readLine()) != null) {
							stringBuilderCN.append(line);
							stringBuilderCN.append("\n");
						}
					}

					Log.i("c_version", version);
					Log.i("c_date", date);
					Log.i("c_stringBuilderEng",stringBuilderEng.toString());
					Log.i("c_stringBuilderCN",stringBuilderCN.toString());

					// 共享资源
					// Save data in SharedPreference
					SharedPreferences sharedPreferences = context.getSharedPreferences("UpdateSP", Context.MODE_PRIVATE);
					sharedPreferences.edit()
							.putString("updateVersion", version)
							.putString("updateDate", date)
							.putString("updateChangelogEng", stringBuilderEng.toString())
							.putString("updateChangelogCN", stringBuilderCN.toString())
							.apply();


					// 释放资源
					// Release resources
					inputStream.close();
					inputStreamReader.close();
					bufferedReader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static int getCPUCores() {
		try {
			File dir = new File("/sys/devices/system/cpu/");
			File[] files = dir.listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					if (Pattern.matches("cpu[0-9]",pathname.getName()))
						return true;
					return false;
				}
			});

			Log.i("cpuCores",files.length+"");
			return files.length ;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public static String getAllAPI() {
		StringBuilder abis = new StringBuilder();
		for (int i = 0; i < Build.SUPPORTED_ABIS.length; i++) {
			abis.append(Build.SUPPORTED_ABIS[i]);
			if (i < Build.SUPPORTED_ABIS.length - 1) {
				abis.append("\n");
			}
		}
		Log.i("abis", abis.toString());

		return abis.toString();
	}

	public static String get64API() {
		StringBuilder abis64 = new StringBuilder();
		for (int i = 0; i < Build.SUPPORTED_64_BIT_ABIS.length; i++) {
			abis64.append(Build.SUPPORTED_ABIS[i]);
			if (i < Build.SUPPORTED_64_BIT_ABIS.length - 1) {
				abis64.append("\n");
			}
		}
		Log.i("abis64", abis64.toString());

		return abis64.toString();
	}

	public static String get32API() {
		StringBuilder abis32 = new StringBuilder();
		for (int i = 0; i < Build.SUPPORTED_32_BIT_ABIS.length; i++) {
			abis32.append(Build.SUPPORTED_32_BIT_ABIS[i]);
			if (i < Build.SUPPORTED_32_BIT_ABIS.length - 1) {
				abis32.append("\n");
			}
		}
		Log.i("abis32", abis32.toString());

		return abis32.toString();

	}

	public static String getCoresFreq(Context context, int coreNo, String type) {
		String fileName = "scaling_" + type + "_freq";
		String file = "/sys/devices/system/cpu/cpu" + coreNo + "/cpufreq/" + fileName;

		return FileWorking.readFile(context, file);
	}

	public static List<String> getAvailableFreq(Context context, int core) {
		String available_freq = FileWorking.readFile(context,
				"/sys/devices/system/cpu/cpu" + core + "/cpufreq/scaling_available_frequencies");

		List<String> list = Arrays.asList(available_freq.split(" "));

		return list;
	}

}

