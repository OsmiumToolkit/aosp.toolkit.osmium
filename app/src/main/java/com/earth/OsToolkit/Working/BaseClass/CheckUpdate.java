package com.earth.OsToolkit.Working.BaseClass;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class CheckUpdate extends AsyncTask<Void, Integer, Integer> {
	public boolean complete = false;
	private String version;
	private String date;
	private StringBuilder stringBuilderEng = new StringBuilder();
	private StringBuilder stringBuilderCn = new StringBuilder();

	@Override
	protected Integer doInBackground(Void... voids) {
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
		return null;
	}

	public String getVersion() {
		return this.version;
	}

	public String getDate() {
		return this.date;
	}

	public String getChangelog_Eng() {
		return this.stringBuilderEng.toString();
	}

	public String getChangelog_Cn() {
		return this.stringBuilderCn.toString();
	}
}
