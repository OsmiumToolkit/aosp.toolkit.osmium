package com.earth.OsToolkit.Working.BaseClass;

import android.util.Log;

import java.io.*;
import java.net.URL;

public class CheckUpdate {

	private String version;
	private String date;
	private StringBuilder stringBuilderEng = new StringBuilder();
	private StringBuilder stringBuilderCn = new StringBuilder();

	public boolean complete = false;

	public void checkUpdate() {
		Thread thread = new Thread(() -> {
			try {
				URL url = new URL("https://raw.githubusercontent.com/1552980358/1552980358.github.io/master/OsToolkit");
				Log.i("URL", url.toString());
				try {
					InputStream inputStream = url.openStream();
					InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

					this.version = bufferedReader.readLine();
					this.date = bufferedReader.readLine();
					this.stringBuilderEng = new StringBuilder();
					this.stringBuilderCn = new StringBuilder();
					if (bufferedReader.readLine().equals("<ENG>")) {
						String line;
						while (!(line = bufferedReader.readLine()).equals("<CN>")) {
							this.stringBuilderEng.append(line);
							this.stringBuilderEng.append("\n");
						}

						while ((line = bufferedReader.readLine()) != null) {
							this.stringBuilderCn.append(line);
							this.stringBuilderCn.append("\n");
						}
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

			complete = true;
		});

		thread.start();

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
