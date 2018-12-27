package com.earth.OsToolkit;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.*;

import java.io.*;

import static com.earth.OsToolkit.Base.FileWorking.*;

public class ScriptActivity extends AppCompatActivity {

	/*
	 * 27 Dec 2018
	 *
	 * By 1552980358
	 *
	 */

	TextView textView;
	String script;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_script);
		textView = findViewById(R.id.script_txt);

		script = getIntent().getStringExtra("script");
		setToolBar();
		ScriptWorking();
	}

	public void setToolBar() {
		String title = String.format(getString(R.string.script_title_head), script);

		// 呼出Toolbar
		// Call Toolbar
		Toolbar toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle(title);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null)
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.black));

		// 设置标题属性
		// Set Title property
		toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
		// 设置返回监听
		// Set Navigation button listener
		toolbar.setNavigationOnClickListener(v -> onBackPressed());
	}


	public void ScriptWorking() {
		String path = getCacheDir().getAbsolutePath() + File.separator + script;

		textView.append("Copying script from assets to Cache/从Assets复制脚本到Cache中...\n");

		if (copyAssets2Cache(ScriptActivity.this, script)) {
			textView.append("Copied successfully, File exists/复制成功, 文件存在。\n\n");

			textView.append("Target/目标:\n");
			textView.append(path + "\n\n");

			textView.append("Setting permission/设置限权...\n");

			if (setScriptPermission(ScriptActivity.this, script)) {
				textView.append("Succeed to set permission/限权设置完成!\n\n");
				runScript(path);
			} else {
				textView.append("Fail to set permission/限权设置失败!\n");
			}
		} else {
			textView.append("Failed to copy file/文件复制失败\n");
		}
	}

	public void runScript(String filePath) {
		try {
			textView.append("Command/命令:\n");
			textView.append("su -c /system/bin/sh " + filePath + "\n\n");

			Process process = Runtime.getRuntime().exec(new String[]{"su", "-c", "/system/bin/sh", filePath});
			process.waitFor();
			InputStream inputStream = process.getInputStream();
			InputStream inputStreamError = process.getErrorStream();

			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			InputStreamReader inputStreamReaderError = new InputStreamReader(inputStreamError, "UTF-8");

			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			BufferedReader bufferedReaderError = new BufferedReader(inputStreamReaderError);

			textView.append("Process/过程: \n");

			String line;
			while ((line = bufferedReader.readLine()) != null) {
				textView.append(line + "\n");
			}

			if ((line = bufferedReaderError.readLine()) != null) {
				textView.append("\nError Message/错误信息: \n");
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
	}

	@Override
	public void onBackPressed() {
		Toast.makeText(this, getText(R.string.refresh), Toast.LENGTH_SHORT).show();
		setResult(RESULT_OK);
		finish();
		super.onBackPressed();
	}
}