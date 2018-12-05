package com.earth.OsToolkit.Working.BaseClass;

import android.content.*;

import android.util.Log;
import java.io.*;
import java.net.*;

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
			return false;
		}
	}

	public static boolean checkSupportQC3() {
		File file = new File("/sys/class/power_supply/battery/allow_hvdcp3");
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
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
					StringBuilder stringBuilder = new StringBuilder();
					String line;
					while ((line = bufferedReader.readLine()) != null) {
						stringBuilder.append(line);
						stringBuilder.append("\n");
					}

					Log.i("c_version", version);
					Log.i("c_date", date);
					Log.i("c_stringBuilder", stringBuilder.toString());

					// 共享资源
					// Save data in SharedPreference
					SharedPreferences sharedPreferences = context.getSharedPreferences("UpdateSP", Context.MODE_PRIVATE);
					sharedPreferences.edit()
							.putString("updateVersion", version)
							.putString("updateDate", date)
							.putString("updateChangelog", stringBuilder.toString())
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

}

