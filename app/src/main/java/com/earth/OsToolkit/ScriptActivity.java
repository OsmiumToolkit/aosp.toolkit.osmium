package com.earth.OsToolkit;
/*
 * OsToolkit - Kotlin
 *
 * Date : 31/12/2018
 *
 * By   : 1552980358
 *
 */

/*
 * Modify
 *
 * 9/1/2019
 *
 */

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.earth.OsToolkit.base.BaseKotlinOperation;

import java.io.*;
import java.net.URL;

public class ScriptActivity extends AppCompatActivity {

    private String filePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script);

        String type = getIntent().getStringExtra("type");
        String index = getIntent().getStringExtra("index");
        StringBuilder name = new StringBuilder(getIntent().getStringExtra("name"));

        if (!name.toString().endsWith(".sh")) {
            name.append(".sh");
        }

        filePath = getCacheDir().getAbsolutePath() + File.separator + name.toString();

        initialize();
        download(type, index, name.toString());

    }

    public void initialize() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.black));

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    public void download(String type, String index, String name) {
        DownloadScript downloadScript = new DownloadScript();
        downloadScript.setInfo(type, index, name);
        downloadScript.start();
    }

    public void permission() {
        TextView textView_download = findViewById(R.id.script_download);
        if (new File(filePath).length() > 0) {
            textView_download.append(new File(filePath).length()+"Bytes\n");
            textView_download.append(getString(R.string.script_download_done));
            TextView textView_target_title = findViewById(R.id.script_target_title);
            textView_target_title.setVisibility(View.VISIBLE);
            TextView textView_target = findViewById(R.id.script_target);
            textView_target.setText(filePath);

            TextView textView_permission_title = findViewById(R.id.script_permission_title);
            textView_permission_title.setVisibility(View.VISIBLE);

            TextView textView_permission = findViewById(R.id.script_permission);
            if (BaseKotlinOperation.Companion.setPermission(filePath)) {
                textView_permission.setText(R.string.script_permission_done);
                command();
                runScript();
            } else {
                textView_permission.setText(R.string.script_permission_fail);
            }
        } else {
            textView_download.setText(R.string.script_download_fail);
        }
    }

    public void command() {
        TextView textView_command_title = findViewById(R.id.script_command_title);
        textView_command_title.setVisibility(View.VISIBLE);
        TextView textView_command = findViewById(R.id.script_command);
        String command = "su -c /system/bin/sh " + filePath;
        textView_command.setText(command);
    }

    public void runScript() {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"su", "-c", "/system/bin/sh", filePath});
            Log.i("script_run", process.waitFor() + "");
            InputStream inputStream = process.getInputStream();
            InputStream inputStreamError = process.getErrorStream();

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            InputStreamReader inputStreamReaderError = new InputStreamReader(inputStreamError, "utf-8");

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            BufferedReader bufferedReaderError = new BufferedReader(inputStreamReaderError);

            String line;
            if ((line = bufferedReader.readLine()) != null) {
                TextView textView_process_title = findViewById(R.id.script_process_title);
                textView_process_title.setVisibility(View.VISIBLE);
                TextView textView_process = findViewById(R.id.script_process);
                textView_process.append(line);
                while ((line = bufferedReader.readLine()) != null) {
                    textView_process.append(line + "\n");
                }
            }

            if ((line = bufferedReaderError.readLine()) != null) {
                TextView textView_error_title = findViewById(R.id.script_error_title);
                textView_error_title.setVisibility(View.VISIBLE);
                TextView textView_error = findViewById(R.id.script_error);
                textView_error.append(line);
                while ((line = bufferedReaderError.readLine()) != null) {
                    textView_error.append(line + "\n");
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
        }
    }

    Handler handler = new Handler();
    Runnable runnable = this::permission;

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        this.finish();
        super.onBackPressed();
    }

    public class DownloadScript extends Thread {
        private String type;
        private String index;
        private String name;

        @Override
        public void run() {
            super.run();

            try {
                URL url = new URL("https://raw.githubusercontent.com/1552980358/1552980358.github.io/master/" +
                        type + File.separator +
                        index + File.separator +
                        name);
                Log.i("URL", url.toString());

                File file = new File(filePath);
                Log.i("filePath", file.toString());
                InputStream inputStream = url.openStream();

                if (!file.exists() || file.length() != -1) {

                    FileOutputStream fileOutputStream = new FileOutputStream(filePath);

                    byte[] buffer = new byte[10240];

                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                    }

                    Log.i("fileLength", file.exists() + " / " + file.length());

                    fileOutputStream.flush();
                    inputStream.close();
                    fileOutputStream.close();
                }
                handler.post(runnable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void setInfo(String type, String index, String name) {
            this.type = type;
            this.index = index;
            this.name = name;
        }
    }
}
