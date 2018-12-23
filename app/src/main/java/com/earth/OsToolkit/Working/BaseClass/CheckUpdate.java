package com.earth.OsToolkit.Working.BaseClass;

import android.util.Log;

import java.io.*;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class CheckUpdate {

	// 数据
	// Data
	private String version;
	private String date;
	private StringBuilder stringBuilderEng = new StringBuilder();
	private StringBuilder stringBuilderCn = new StringBuilder();

	// 完成flag，当线程完成时返回true
	// Flag showing thread finished, return true when finished, otherwise return false
	public boolean complete = false;
	public boolean done = false;

	private Thread thread;

	public void checkUpdate() {
		thread = new Thread(() -> {
			try {
				URL url = new URL("https://raw.githubusercontent.com/1552980358/1552980358.github.io/master/OsToolkit");
				Log.i("URL_Update", url.toString());
				try {
					InputStream inputStream = url.openStream();
					InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

					// 返回数据
					// return data
					String line;

					if ((line = bufferedReader.readLine()) != null) {
						this.version = line;
					} else {
						this.version = "Fail";
					}
					Log.i("version", version);

					if ((line = bufferedReader.readLine()) != null) {
						this.date = line;
					} else {
						this.version = "Fail";
					}
					Log.i("date", date);

					if (bufferedReader.readLine().equals("<ENG>")) {
						// 英语日志
						// English changelog
						while (!(line = bufferedReader.readLine()).equals("<CN>")) {
							this.stringBuilderEng.append(line);
							this.stringBuilderEng.append("\n");
						}
						Log.i("stringBuilderEng", stringBuilderEng.toString());

						// 中文日志
						// Simplified Chinese changelog
						while ((line = bufferedReader.readLine()) != null) {
							this.stringBuilderCn.append(line);
							this.stringBuilderCn.append("\n");
						}
						Log.i("stringBuilderCn", stringBuilderCn.toString());
					}

					// 释放资源
					// Release resources
					inputStream.close();
					inputStreamReader.close();
					bufferedReader.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}


		});

		thread.start();
	}

	public int waitFor() {
		if (!thread.isInterrupted()) {
			while (thread.isAlive()) {
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
					}
				}, 10);
			}
			return 0;
		} else {
			return 1;
		}
	}


	public String getVersion() {
		return this.version;
	}

	public String getDate() {
		return this.date;
	}

	public String getChangelogEng() {
		return this.stringBuilderEng.toString();
	}

	public String getChangelogCn() {
		return this.stringBuilderCn.toString();
	}
}
