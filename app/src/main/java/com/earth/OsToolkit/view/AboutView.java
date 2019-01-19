package com.earth.OsToolkit.view;
/*
 * OsToolkit - Kotlin
 *
 * Date : 5/1/2019
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


import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.earth.OsToolkit.R;


public class AboutView extends LinearLayout {


    public AboutView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.view_about, this);

        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.AboutView);

        TextView title = findViewById(R.id.title);
        TextView summary = findViewById(R.id.summary);
        ImageView icon = findViewById(R.id.icon);
        title.setText(typedArray.getString(R.styleable.AboutView_title));
        summary.setText(typedArray.getString(R.styleable.AboutView_summary));
        icon.setImageDrawable(typedArray.getDrawable(R.styleable.AboutView_icon));

        if (typedArray.getBoolean(R.styleable.AboutView_hideSummary, false)) {
            summary.setVisibility(GONE);
        }

        typedArray.recycle();
    }

    public void setSummary(String s) {
        TextView summary = findViewById(R.id.summary);
        summary.setText(s);
    }

    @Override
    public void setOnClickListener(View.OnClickListener l) {
        LinearLayout linearLayout = findViewById(R.id.root);
        linearLayout.setOnClickListener(l);
    }

    /*
    public AboutView(Context context, int img, int title) {
        this(context, img, context.getString(title), "");
    }

    public AboutView(Context context, int img, int title, int summary) {
        this(context, img, context.getString(title), context.getString(summary));
    }

    public AboutView(Context context, int img, int title, String summary) {
        this(context, img, context.getString(title), summary);
    }

    public AboutView(Context context, int img, String title, String summary) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.view_about, this);

        TextView textView_title = findViewById(R.id.about_title);
        TextView textView_summary = findViewById(R.id.about_summary);
        ImageView imageView = findViewById(R.id.about_img);

        textView_title.setText(title);
        if (summary.isEmpty()) {
            textView_summary.setVisibility(View.GONE);
        } else {
            textView_summary.setText(summary);
        }
        imageView.setImageResource(img);
    }
    */

}
