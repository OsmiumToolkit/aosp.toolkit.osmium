package com.earth.OsToolkit.Items;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.earth.OsToolkit.R;

public class DeviceInfoCardView extends LinearLayout {

	Context context;

	TextView title;

	LinearLayout linearLayout;


	public DeviceInfoCardView(Context context, AttributeSet attributeSet) {
		super(context,attributeSet);

		this.context = context;

		LayoutInflater.from(context).inflate(R.layout.cardview_deviceinfo,this);

		this.title = findViewById(R.id.item_card_deviceinfo_title);
		this.linearLayout = findViewById(R.id.item_deviceinfo_layout);

	}

	public void setTitle(int title) {
		this.title.setText(context.getString(title));
	}

	public void setTitle(String title) {
		this.title.setText(title);
	}

	public void addItems(Item... items) {
		linearLayout.removeAllViews();
		for (DeviceInfoCardView.Item item : items) {
			addItem(item);
		}
	}

	public void addItem(Item item) {
		if (item != null) {
			linearLayout.addView(item);
		}
	}


	public static class Item extends LinearLayout {

		Context context;
		TextView name;
		TextView info;

		public Item(Context context, int name, String info) {
			this(context,context.getString(name),info);
		}

		public Item(Context context, String name, String info) {
			super(context);

			this.context = context;

			LayoutInflater.from(context).inflate(R.layout.cardview_deviceinfo_item,this);

			this.name = findViewById(R.id.item_card_deviceinfo_item_name);
			this.info = findViewById(R.id.item_card_deviceinfo_item_info);

			setName(name);
			setInfo(info);
		}

		private void setName(String name) {
			this.name.setText(name);
		}

		private void setInfo(String info) {
			this.info.setText(info);
		}

	}
}
