package com.earth.OsToolkit;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;

import com.earth.OsToolkit.Working.FileWorking;

public class ScriptActivity extends AppCompatActivity {
	String script;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_script);

		setToolBar();
		script = getIntent().getStringExtra("script");
		ScriptWorking(script);
	}

	public void setToolBar() {
		String title = String.format(getString(R.string.script_title_head), script);

		// 呼出Toolbar
		// Call Toolbar
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null)
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.black));

		// 设置标题属性
		// Set Title property
		toolbar.setTitle(title);
		toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
		// 设置返回监听
		// Set Navigation button listener
		toolbar.setNavigationOnClickListener(v -> onBackPressed());
	}

	TextView textView;

	public void ScriptWorking(String fileName) {
		textView = findViewById(R.id.script_txt);
		String path = getCacheDir().getAbsolutePath() + File.separator + fileName;

		textView.append("Copying script from assets to Cache...\n");
		textView.append("从Assets复制脚本到Cache中...\n");
		if (FileWorking.copyAssets2Cache(ScriptActivity.this, fileName)) {
			textView.append("Copied successfully! File exists.\n");
			textView.append("复制成功!文件存在。\n\n");

			textView.append("Target:\n");
			textView.append("目标:\n");
			textView.append(path + "\n\n");

			textView.append("Setting permission...\n");
			textView.append("设置限权...\n");

			if (FileWorking.setScriptPermission(ScriptActivity.this, fileName)) {
				textView.append("Setting permission successfully!\n");
				textView.append("限权设置成功!\n\n");
				runScript(path);
			} else {
				textView.append("Failed!\n");
				textView.append("设置失败!\n");
			}
		} else {
			textView.append("Failed!\n");
			textView.append("复制失败!\n");
		}
	}

	public void runScript(String filePath) {
		textView.append("Launching a new thread to run script...\n");
		textView.append("启动新线程运行脚本中...\n");

		new Thread(() -> {
			textView.append("New Thread launched successfully!\n");
			textView.append("新线程启动成功!\n\n");
			try {
				textView.append("Command:\n");
				textView.append("命令:\n");
				textView.append("su -c /system/bin/sh " + filePath + "\n\n");

				Process process = Runtime.getRuntime().exec(new String[]{"su","-c","/system/bin/sh",filePath});
				process.waitFor();
				InputStream inputStream = process.getInputStream();
				InputStream inputStreamError = process.getErrorStream();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
				InputStreamReader inputStreamReaderError = new InputStreamReader(inputStreamError,"UTF-8");
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				BufferedReader bufferedReaderError = new BufferedReader(inputStreamReaderError);

				String line;
				while ((line = bufferedReader.readLine()) != null) {

					textView.append(line + "\n");
				}

				if ((line = bufferedReaderError.readLine()) != null) {
					textView.append("\n");
					textView.append("Error Message:\n");
					textView.append("错误信息:\n");
					textView.append(line + "\n");
					while ((line = bufferedReaderError.readLine()) != null) {
						textView.append(line + "\n");
					}
				}

				inputStream.close();
				inputStreamError.close();
				inputStreamReader.close();
				inputStreamReaderError.close();
				bufferedReader.close();
				bufferedReaderError.close();
			} catch (Exception e) {
				e.printStackTrace();
				textView.append("Error when trying running script.");
			}
		}).start();
	}

	@Override
	public void onBackPressed() {
		Toast.makeText(this, getText(R.string.refresh), Toast.LENGTH_SHORT).show();
		setResult(RESULT_OK);
		finish();
		super.onBackPressed();
	}
}