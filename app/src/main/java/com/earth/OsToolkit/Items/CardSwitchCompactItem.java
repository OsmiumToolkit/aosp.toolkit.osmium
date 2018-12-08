package com.earth.OsToolkit.Items;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

	private FragmentActivity activity;

	private String script;

	private Fragment fragment;

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

	public void setScript(FragmentActivity activity, String script) {
		this.activity = activity;
		this.script = script;
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

		this.switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
			activity.startActivity(new Intent(activity, ScriptActivity.class)
					                       .putExtra("script", script));


				FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(R.id.main_fragment,fragment).commit();


		});

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

	public void setCurrentFragment(Fragment fragment) {
		this.fragment = fragment;
	}
}
