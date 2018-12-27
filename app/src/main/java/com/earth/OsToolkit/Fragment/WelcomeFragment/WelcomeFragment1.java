package com.earth.OsToolkit.Fragment.WelcomeFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.earth.OsToolkit.R;

public class WelcomeFragment1 extends Fragment {

    /*
     * 27 Dec 2018
     *
     * By 1552980358
     *
     */

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_welcome_1,container,false);
    }
}
