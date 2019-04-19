package aosp.toolkit.perseus.view;
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
import android.widget.*;
import android.support.v7.widget.SwitchCompat;

import aosp.toolkit.perseus.ScriptActivity;
import aosp.toolkit.perseus.R;

import aosp.toolkit.perseus.base.BaseOperation;

@SuppressWarnings("all")
public class LoadScriptView extends LinearLayout {

    public LoadScriptView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        LayoutInflater.from(context).inflate(R.layout.view_loadscript, this);

        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.LoadScriptView);

        TextView title = findViewById(R.id.title);
        TextView summary = findViewById(R.id.summary);

        title.setText(typedArray.getString(R.styleable.LoadScriptView_title));
        summary.setText(typedArray.getString(R.styleable.LoadScriptView_summary));

        typedArray.recycle();
    }

    public void init(Fragment fragment, String file, String type, String index, String name) {
        SwitchCompat switchCompat = findViewById(R.id.switchCompact);
        TextView indicator = findViewById(R.id.indicator);

        if (!BaseOperation.Companion.checkFilePresent(file)) {
            switchCompat.setClickable(false);
            indicator.setText(R.string.sw_none);
        } else {
            boolean status;
            if (BaseOperation.Companion.javaFileReadLine(file) != "Fail") {
                status = BaseOperation.Companion.javaFileReadLine(file).equals("1");
            } else {
                status = BaseOperation.Companion.suFileReadLine(file).equals("1");
            }

            switchCompat.setChecked(status);
            indicator.setText(status ? R.string.sw_en : R.string.sw_dis);
            switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
                startActivityForResult(fragment, type, index, name);

            });
        }
        setLinearLayoutOnClickListener();
    }

    public void init(Fragment fragment, String command, String type, String index, String name, boolean enable) {
        SwitchCompat switchCompat = findViewById(R.id.switchCompact);
        TextView indicator = findViewById(R.id.indicator);

        if (!enable) {
            switchCompat.setClickable(false);
            indicator.setText(R.string.sw_none);
        } else {
            boolean status = false;
            if (BaseOperation.Companion.readShellContent(command).equals("1")) {
                status = true;
            }

            switchCompat.setChecked(status);
            indicator.setText(status ? R.string.sw_en : R.string.sw_dis);
            switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
                startActivityForResult(fragment, type, index, name);
            });
        }
        setLinearLayoutOnClickListener();
    }

    public void init(Fragment fragment, String type, String index, String name) {
        LinearLayout root = findViewById(R.id.root);
        ImageView imageView = findViewById(R.id.viewIndicator);
        imageView.setImageResource(R.drawable.ic_go);
        root.setOnClickListener(v -> {
            startActivityForResult(fragment, type, index, name);
        });
    }

    private void startActivityForResult(Fragment fragment, String type, String index, String name) {
        new Thread(() -> fragment.startActivityForResult(new Intent(getContext(), ScriptActivity.class)
                        .putExtra("path", type + "/" + index + "/" ).putExtra("script", name),
                0)).start();
    }

    private void setLinearLayoutOnClickListener() {
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
}
