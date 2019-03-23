package aosp.toolkit.osmium.view;
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
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import aosp.toolkit.osmium.R;


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

}
