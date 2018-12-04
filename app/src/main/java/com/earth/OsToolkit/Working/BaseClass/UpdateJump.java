package com.earth.OsToolkit.Working.BaseClass;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.earth.OsToolkit.R;

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
}
