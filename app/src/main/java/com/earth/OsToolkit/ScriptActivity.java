package com.earth.OsToolkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import java.io.*;

import com.earth.OsToolkit.Working.BaseClass.Copy;

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
        toolbar.setNavigationOnClickListener(v -> ActivityFinish());
    }

    TextView textView;

    public void ScriptWorking(String fileName) {
        textView = findViewById(R.id.script_txt);
        String path = getCacheDir().getAbsolutePath() + File.separator + fileName;

        textView.append("Copying file.");
        if (Copy.copyAssets2Cache(ScriptActivity.this, fileName)) {
            textView.append("File exists.\nTarget: " + "\"" + path + "\"\n");
            textView.append("Set permission.\n");
            if (Copy.setScriptPermission(ScriptActivity.this, fileName)) {
                textView.append("Permission setting succeed.\nNow try running script.\n");
                runScript(path);
            }
        } else {
            textView.append("Fail when copying file.");
        }
    }

    public void runScript(String filePath) {
        textView.append("Start a new thread to run script.\n");
        new Thread(() -> {
            textView.append("New Thread started succeed.\n");
            try {
                textView.append("source " + filePath + "\n");
                Process process = Runtime.getRuntime().exec(new String[]{"/system/bin/sh",filePath});
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

                while ((line = bufferedReaderError.readLine()) != null) {
                    textView.append(line + "\n");
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


    public void ActivityFinish() {
        ScriptActivity.this.setResult(
                RESULT_CANCELED,
                new Intent().putExtra("result", true));

        ScriptActivity.this.finish();
        Log.e("ScriptActivity", "return true");
    }


    @Override
    public void onBackPressed() {
        ScriptActivity.this.setResult(
                RESULT_CANCELED,
                new Intent().putExtra("result", true));
        super.onBackPressed();
        Log.e("Script", "backPress");
    }
}