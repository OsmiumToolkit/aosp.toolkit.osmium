package com.earth.OsToolkit.Items;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.widget.*;
import com.earth.OsToolkit.R;

@SuppressWarnings("all")
public class AboutItem extends LinearLayout {
	private LinearLayout linearLayout;
	private ImageView imageView;
	private TextView textView_title;
	private TextView textView_summary;

	public AboutItem(Context context, int resId, int title) {
		this(context, resId, title, "");
	}

	public AboutItem(Context context, int resId, int title, String summary) {
		super(context);
		LayoutInflater.from(context)
				.inflate(R.layout.item_about, this);
		this.linearLayout = findViewById(R.id.item_about_layout);
		this.imageView = findViewById(R.id.item_about_icon);
		this.textView_title = findViewById(R.id.item_about_title);
		this.textView_summary = findViewById(R.id.item_about_summary);
		setIcon(resId);
		setTitle(context.getString(title));
		if (summary.isEmpty()) {
			this.textView_summary.setVisibility(GONE);
		} else {
			setSummary(summary);
		}
	}

	public void setIcon(int resId) {
		this.imageView.setImageDrawable(getResources().getDrawable(resId));
	}

	public void setTitle(CharSequence title) {
		this.textView_title.setText(title);
	}

	public void setSummary(CharSequence summary) {
		this.textView_summary.setText(summary);
	}

	@Override
	public void setOnClickListener(@Nullable OnClickListener l) {
		this.linearLayout.setOnClickListener(l);
	}

	@Override
	public void setOnLongClickListener(@Nullable OnLongClickListener l) {
		this.linearLayout.setOnLongClickListener(l);
	}
}
