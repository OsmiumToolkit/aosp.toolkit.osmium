package com.earth.OsToolkit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.earth.OsToolkit.Working.BaseClass.Copy;

import java.io.*;

public class ScriptActivity extends AppCompatActivity {
    String script;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script);


        setToolBar();
        script = getIntent().getStringExtra("script");
        runScript(script);
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

    public void runScript(String fileName) {
        TextView textView = findViewById(R.id.script_txt);
        final String path = getCacheDir() + File.separator + fileName;

        textView.append(path + "\n");

        //DataOutputStream dos = null;
        BufferedReader bufferedReaderIn = null;
        BufferedReader bufferedReaderError = null;

        Process process = null;

        if (new File(path).exists()) {
            textView.append("File Already exists \n");
            Copy.setScriptPermission(this, fileName);
        } else {
            if (Copy.copyAssets2Cache(this, script) == 1) {
                textView.append("Copy Success");
                if (Copy.setScriptPermission(this, fileName)) {
                    textView.append("Setting Success\n");
                } else {
                    textView.append("Setting Failed\n");
                }
            } else {
                textView.append("Copy Failed\n");
            }
        }

        try {
            textView.append("Run Script\n");
            process = Runtime.getRuntime()
                    .exec(new String[]{"source",
                            path});
            process.waitFor();

            bufferedReaderIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            bufferedReaderError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));

            String newLine;

            while ((newLine = bufferedReaderIn.readLine()) != null) {
                textView.append(newLine + "\n");
            }
            while ((newLine = bufferedReaderError.readLine()) != null) {
                textView.append(newLine + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(bufferedReaderIn);
            closeStream(bufferedReaderError);

            if (process != null) {
                process.destroy();
            }
        }

        try {
            textView.append("Removing script file");
            process = Runtime.getRuntime().exec(new String[]{"su -c ,","rm -rf ",path});
            process.waitFor();
            textView.append(process.waitFor()+"");
            if (new File(path).exists()) {
                textView.append("Error when removing. Try remove it by cleaning cache in device setting.\n");
            } else {
                textView.append("Remove success.\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        ScriptActivity.this.setResult(RESULT_CANCELED, new Intent().putExtra("result", true));
        super.onBackPressed();
        Log.e("Script", "backPress");
    }
}