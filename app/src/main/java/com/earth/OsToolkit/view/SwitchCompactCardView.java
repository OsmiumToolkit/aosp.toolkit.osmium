package com.earth.OsToolkit.view;
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


import android.content.*;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import android.support.v7.widget.SwitchCompat;

import com.earth.OsToolkit.R;
import com.earth.OsToolkit.ScriptActivity;

import com.earth.OsToolkit.base.BaseKotlinOperation;

public class SwitchCompactCardView extends LinearLayout {

    public SwitchCompactCardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.cardview_switchcompact, this);

        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.SwitchCompactCardView);

        TextView title = findViewById(R.id.title);
        TextView summary = findViewById(R.id.summary);

        title.setText(typedArray.getString(R.styleable.SwitchCompactCardView_title));
        summary.setText(typedArray.getString(R.styleable.SwitchCompactCardView_summary));

        typedArray.recycle();
        setLinearLayoutOnClickListener();
    }

    public void setLinearLayoutOnClickListener() {
        LinearLayout root = findViewById(R.id.root);
        RelativeLayout content = findViewById(R.id.content);
        ImageView viewIndicator = findViewById(R.id.viewIndicator);
        root.setOnClickListener(v -> {
            if (content.getVisibility() == GONE) {
                viewIndicator.setImageResource(R.drawable.ic_open);
                content.setVisibility(VISIBLE);
            } else {
                viewIndicator.setImageResource(R.drawable.ic_hide);
                content.setVisibility(GONE);
            }
        });
    }

    public void initSwitchCompact(Fragment fragment, String file, String type, String index, String name) {
        SwitchCompat switchCompat = findViewById(R.id.switchCompact);
        TextView indicator = findViewById(R.id.indicator);

        if (!BaseKotlinOperation.Companion.checkFilePresent(file)) {
            switchCompat.setClickable(false);
            indicator.setText(R.string.sw_none);
        } else {
            boolean status = false;
            if (BaseKotlinOperation.Companion.readFile(file).equals("1")) {
                status = true;
            }

            switchCompat.setChecked(status);
            indicator.setText(status ? R.string.sw_en : R.string.sw_dis);
            switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
                new Thread(() -> fragment.startActivityForResult(new Intent(getContext(), ScriptActivity.class)
                                .putExtra("type", type)
                                .putExtra("index", index)
                                .putExtra("name", name),
                        0)).start();

            });
        }
    }

    public void initSwitchCompact(Fragment fragment, String command, String type, String index, String name, boolean mode) {
        SwitchCompat switchCompat = findViewById(R.id.switchCompact);
        TextView indicator = findViewById(R.id.indicator);

        if (!mode) {
            switchCompat.setClickable(false);
            indicator.setText(R.string.sw_none);
        } else {
            boolean status = false;
            if (BaseKotlinOperation.Companion.readShellContent(command).equals("1")) {
                status = true;
            }

            switchCompat.setChecked(status);
            indicator.setText(status ? R.string.sw_en : R.string.sw_dis);
            switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
                new Thread(() -> fragment.startActivityForResult(new Intent(getContext(), ScriptActivity.class)
                                .putExtra("type", type)
                                .putExtra("index", index)
                                .putExtra("name", name),
                        0)).start();

            });
        }
    }
}
