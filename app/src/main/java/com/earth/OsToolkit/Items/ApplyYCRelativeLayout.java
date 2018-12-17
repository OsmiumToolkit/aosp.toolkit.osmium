package com.earth.OsToolkit.Items;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.earth.OsToolkit.R;

public class ApplyYCRelativeLayout extends LinearLayout {
	LinearLayout linearLayout;

	public ApplyYCRelativeLayout(Context context, Fragment fragment , String board) {
		super(context);

		LayoutInflater.from(context).inflate(R.layout.relativelayout_applyyc,this);

		TextView textView = findViewById(R.id.applyyc_title);
		this.linearLayout = findViewById(R.id.apply_linear);

		textView.setText(board);
	}

	public void setClickListener(OnClickListener l) {
		this.linearLayout.setOnClickListener(l);
	}
}
