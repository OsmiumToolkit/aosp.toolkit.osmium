package com.earth.OsToolkit.view;
/*
 * OsToolkit - Kotlin
 *
 * Date : 9/1/2019
 *
 * By   : 1552980358
 *
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.earth.OsToolkit.R;

public class DeviceInfoView extends LinearLayout {

    public DeviceInfoView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.view_deviceinfo, this);

        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.DeviceInfoView);

        TextView title = findViewById(R.id.title);
        title.setText(typedArray.getString(R.styleable.DeviceInfoView_title));

        typedArray.recycle();
    }

    public void addViews(ChildView... views) {
        LinearLayout root = findViewById(R.id.root);
        for (ChildView view : views) {
            root.addView(view);
        }
    }

    @SuppressWarnings("ViewConstructor")
    public static class ChildView extends LinearLayout {
        public ChildView(Context context, int i, String v) {
            this(context, context.getString(i), v);
        }

        public ChildView(Context context, String i, String v) {
            super(context);

            LayoutInflater.from(context).inflate(R.layout.childview_switchcompact, this);
            TextView item = findViewById(R.id.item);
            TextView value = findViewById(R.id.value);
            item.setText(i);
            value.setText(v);
        }
    }
}
