package com.earth.OsToolkit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.earth.OsToolkit.Base.FileWorking;

import java.io.*;
import java.net.URL;
import java.util.*;

public class ApplyYCActivity extends AppCompatActivity {

	/*
	 * 27 Dec 2018
	 *
	 * By 1552980358
	 *
	 */

	TextView textView;
	String board;

	String filePath;

	File file;

	boolean complete = false;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_script);
		textView = findViewById(R.id.script_txt);

		board = getIntent().getStringExtra("board");
		filePath = getCacheDir().getAbsolutePath() + File.separator + board + ".sh";
		file = new File(filePath);

		setToolBar();
		downloadScript();
	}

	public void setToolBar() {
		// 呼出Toolbar
		// Call Toolbar
		Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle(board);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null)
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.black));

		// 设置标题属性
		// Set Title property
		toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
		toolbar.setNavigationOnClickListener(v -> onBackPressed());
	}

	public void downloadScript() {
		textView.append("Downloading YC script from Github/下载YC调度脚本中...\n\n");

		new Thread(() -> {
			try {
				URL url = new URL("https://raw.githubusercontent.com/1552980358/1552980358.github.io/master/yc/20180603/" + board + ".sh");

				if (!file.exists() || file.length() != 1) {
					InputStream inputStream = url.openStream();

					FileOutputStream fileOutputStream = new FileOutputStream(filePath);

					byte[] buffer = new byte[1024];

					int len;
					while ((len = inputStream.read(buffer)) != -1) {
						fileOutputStream.write(buffer, 0, len);
						textView.append(file.length() + "Byte/字节\n");
					}

					fileOutputStream.flush();
					inputStream.close();
					fileOutputStream.close();

					complete = true;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}


		}).start();

		while (!complete) {
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
				}
			}, 10);
		}

		if (file.exists() && file.length() > 1) {
			textView.append("\nDownload successfully/下载完成!\n\n");
			runScript();
		}
	}


	public void runScript() {
		try {
			textView.append("Target/目标: \n");
			textView.append(filePath + "\n\n");

			textView.append("Setting permission/设置限权...\n");
			if (FileWorking.setScriptPermission(this, board + ".sh")) {

				textView.append("Succeed to set permission/限权设置完成!\n\n");

				textView.append("Command/命令:\n");

				textView.append("su -c /system/bin/sh " + filePath + "\n\n");

				Process process = Runtime.getRuntime().exec(new String[]{"su", "-c", "/system/bin/sh", filePath});
				Log.i("ApplyYC_P_waitfor", process.waitFor() + "");

				InputStream inputStream = process.getInputStream();
				InputStream inputStreamError = process.getErrorStream();

				InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
				InputStreamReader inputStreamReaderError = new InputStreamReader(inputStreamError, "utf-8");

				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				BufferedReader bufferedReaderError = new BufferedReader(inputStreamReaderError);

				String line;
				if ((line = bufferedReader.readLine())!= null) {
					textView.append("Process/过程: \n");
					textView.append(line + "\n");
					while ((line = bufferedReader.readLine()) != null) {
						textView.append(line + "\n");
					}
				}

				if ((line = bufferedReaderError.readLine()) != null) {
					textView.append("\nError Message/错误信息: \n");
					textView.append(line + "\n");
					while ((line = bufferedReaderError.readLine()) != null) {
						textView.append(line + "\n");
					}

				}

			} else {
				textView.append("Fail to set permission/限权设置失败!\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

}
