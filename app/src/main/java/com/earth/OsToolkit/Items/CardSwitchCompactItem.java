package com.earth.OsToolkit.Items;

import android.content.*;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.*;

import com.earth.OsToolkit.R;
import com.earth.OsToolkit.ScriptActivity;

public class CardSwitchCompactItem extends LinearLayout {

	private Context context;

	private RelativeLayout relativeLayout;
	private LinearLayout linearLayout;

	private TextView textViewTitle;
	private TextView textViewSummary;

	private ImageView imageView;

	private SwitchCompat switchCompat;
	private TextView textViewSwitchCompact;

	public CardSwitchCompactItem(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);

		this.context = context;

		LayoutInflater.from(context).inflate(R.layout.item_card_switch, this);


		relativeLayout = findViewById(R.id.item_sw_relative);
		linearLayout = findViewById(R.id.item_sw_linear);
		textViewTitle = findViewById(R.id.item_sw_title);
		textViewSummary = findViewById(R.id.item_sw_summary);

		imageView = findViewById(R.id.item_sw_img);

		switchCompat = findViewById(R.id.item_sw_switch);
		textViewSwitchCompact = findViewById(R.id.item_sw_txt);

	}

	public void setTitle(int title) {
		this.textViewTitle.setText(context.getText(title));
	}


	public void setTitle(String title) {
		this.textViewTitle.setText(title);
	}

	public void setSummary(int summary) {
		this.textViewSummary.setText(context.getText(summary));
	}

	public void setSummary(String summary) {
		this.textViewSummary.setText(summary);
	}

	public void disableSwitchCompact() {
		this.textViewSwitchCompact.setText(R.string.sw_none);
		this.switchCompat.setClickable(false);
	}

	public void setSwitchCompatChecked(boolean checked) {
		if (checked) {
			this.textViewSwitchCompact.setText(R.string.sw_en);
		} else {
			this.textViewSwitchCompact.setText(R.string.sw_dis);
		}

		this.switchCompat.setChecked(checked);

	}

	public void setLayoutListener() {
		this.linearLayout.setOnClickListener(v -> {
			if (relativeLayout.getVisibility() == GONE) {
				imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_open));
				relativeLayout.setVisibility(VISIBLE);
			} else {
				imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_hide));
				relativeLayout.setVisibility(GONE);
			}
		});
	}

	public void setSwitchCompatOnChangeListener(CompoundButton.OnCheckedChangeListener onCheckedChangeListener) {
		this.switchCompat.setOnCheckedChangeListener(onCheckedChangeListener);
	}

	public void setSwitchCompatOnChangeListener(Fragment fragment, Context context, String fileName) {
		this.switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
			fragment.startActivityForResult(new Intent(context, ScriptActivity.class)
					                                .putExtra("script", fileName), 0);
		});
	}
}
