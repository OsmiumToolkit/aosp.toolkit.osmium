package com.earth.OsToolkit.Working.BaseClass;

import android.content.Context;
import android.widget.Toast;

import com.earth.OsToolkit.R;

public class ExitApplication {
	public static void exit() {
		System.exit(0);
	}

	public static void shellKill(Context context) {
		String pid = android.os.Process.myPid() + "";
		try {
			Process process = Runtime.getRuntime().exec(new String[]{"su","-c","/system/bin/sh","kill","-9",pid});
			process.waitFor();
			Toast.makeText(context, context.getText(R.string.toast_failed), Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Toast.makeText(context, context.getText(R.string.toast_failed), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
 	}

	public static void killProcessPID() {
		int pid = android.os.Process.myPid();
		android.os.Process.killProcess(pid);
	}

}
