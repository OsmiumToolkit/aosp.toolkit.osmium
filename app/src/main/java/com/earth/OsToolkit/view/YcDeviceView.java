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

import android.app.AlertDialog;
import android.content.*;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.widget.*;
import com.earth.OsToolkit.R;
import com.earth.OsToolkit.ScriptActivity;

import static com.earth.OsToolkit.base.BaseIndex.*;

@SuppressWarnings("ViewConstructor")
public class YcDeviceView extends LinearLayout {
    public YcDeviceView(Context context, Fragment fragment, String index, String board) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_ycdevice, this);

        TextView textView = findViewById(R.id.title);
        textView.setText(board);
        RelativeLayout relativeLayout = findViewById(R.id.root);
        relativeLayout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(board).setMessage(String.format(fragment.getString(R.string.apply_confirm), board))
                    .setPositiveButton(R.string.cont, (dialog, which) -> {
                        fragment.startActivity(new Intent(context, ScriptActivity.class)
                                .putExtra("type", type_yc)
                                .putExtra("index", index)
                                .putExtra("name", board));
                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) -> {

                    }).show();
        });
    }
}
