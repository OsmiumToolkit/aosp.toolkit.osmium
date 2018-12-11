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

}

