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
import java.util.Objects;

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

        getWindow().setStatusBarColor(ContextCompat.getColor(this,android.R.color.black));

        // 设置标题属性
        // Set Title property
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        // 设置返回监听
        // Set Navigation button listener
        toolbar.setNavigationOnClickListener(v -> ActivityFinish());
    }

    public void runScript(String fileName) {
        final TextView textView = findViewById(R.id.script_txt);
        final String path = getCacheDir().getAbsolutePath() + File.separator + fileName;
        int working;

        DataOutputStream dos = null;
        BufferedReader bufferedReader = null;

        if (!new File(path).exists()) {
            textView.append("Copy script from assets to cache\n");
            working = Copy.copyAssets2Cache(this, fileName) * 10;
            if (working == 10) {
                textView.append("Set permission\n");
                working += (Copy.setScriptPermission(this, fileName));
                if (working == 9 || working == 10) {
                    textView.append("Error when setting permission");
                }
            } else {
                textView.append("Error when copying script to cache");
            }
        } else {
            textView.append("Script already exist. Now set permission\n");
            working = (Copy.setScriptPermission(this, fileName));
            if (working == 0 || working == -1) {
                textView.append("Error when setting permission");
            }
        }

        if (working == 11 || working == 1) {
            try {
                Process process = Runtime.getRuntime()
                        .exec(new String[]{"source",
                                path});

                bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String newLine;

                while ((newLine = bufferedReader.readLine()) != null ) {
                    textView.append(newLine + "\n");
                }

                textView.append("Removing script file");
                process = Runtime.getRuntime().exec("rm -rf" + path);
                if (new File(path).exists()) {
                    textView.append("Error when removing. Try remove it by cleaning cache in device setting.\n");
                } else {
                    textView.append("Remove success.\n");
                }


                /*
                if (process.waitFor() == 0) {
                    dos = new DataOutputStream(process.getOutputStream());
                    bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    dos.writeBytes("source "
                            + path
                            + "\n");
                    dos.flush();
                    dos.writeBytes("exit\n");
                    dos.flush();
                    String line;
                    if ((line = bufferedReader.readLine()) != null) {
                        textView.append(line + "\n");
                    }
                    if (process.waitFor() == 0) {
                        textView.append(String.format("Remove script file located at '%s'\n", path));
                        process = Runtime.getRuntime().exec(new String[]{"su -c rm -rf ", path});
                        if (process.waitFor() == 0 && !new File(path).exists()) {
                            textView.append("File Removed!");
                        } else {
                            textView.append("File remove failed! Try remove by cleaning cache in system setting.");
                        }
                    }
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }


                /*
                if (dos != null) {
                    try {
                        dos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/
            }

        }

        /*final StringBuilder result = new StringBuilder();

        try {
            Process process = Runtime.getRuntime().exec(new String[]{"su -c ","source",fileName});
            if (process.waitFor() == 0) {
                dos = new DataOutputStream(process.getOutputStream());
                bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));


                dos.writeBytes("su -c source " + fileName + "\n");
                dos.flush();
                dos.writeBytes("exit\n");
                dos.flush();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }

                process.waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } */
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
        Log.e("Script","backPress");
    }
}