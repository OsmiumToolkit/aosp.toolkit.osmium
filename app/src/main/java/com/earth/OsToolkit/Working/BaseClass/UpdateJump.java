package com.earth.OsToolkit.Working.BaseClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.widget.Toast;

import com.earth.OsToolkit.R;

import java.util.ArrayList;
import java.util.List;

import static com.earth.OsToolkit.Working.BaseClass.BaseIndex.*;

public class UpdateJump {
	public static void jumpGithub(Activity activity) {
		try {
			activity.startActivity(new Intent()
					                       .setData(Uri.parse("https://github.com/1552980358/com.earth.OsToolkit/releases"))
					                       .setAction(Intent.ACTION_VIEW));
		} catch (Exception e) {
			Toast.makeText(activity, activity.getText(R.string.load_fail), Toast.LENGTH_SHORT).show();
		}
	}

	public static void jumpCoolapk(Activity activity, Context context) {
		final List<PackageInfo> packageInfoList = context.getPackageManager().getInstalledPackages(0);
		List<String> packageName = new ArrayList<>();
		if (!packageInfoList.isEmpty()) {
			for (int i = 1; i < packageInfoList.size(); i++) {
				packageName.add(packageInfoList.get(i).packageName);
			}
		}

		try {
			if (packageName.contains(CoolapkPackageName)) {
				Toast.makeText(activity, activity.getString(R.string.toast_coolapk), Toast.LENGTH_SHORT).show();
				activity.startActivity(new Intent("android.intent.action.VIEW")
						                       .setData(Uri.parse("market://details?id=" + PackageName))
						                       .setPackage(CoolapkPackageName)
						                       .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
			} else {
				activity.startActivity(new Intent()
						                       .setData(Uri.parse("http://www.coolapk.com/apk/" + PackageName))
						                       .setAction(Intent.ACTION_VIEW));
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(activity, activity.getString(R.string.load_fail), Toast.LENGTH_SHORT).show();
		}

	}
}
