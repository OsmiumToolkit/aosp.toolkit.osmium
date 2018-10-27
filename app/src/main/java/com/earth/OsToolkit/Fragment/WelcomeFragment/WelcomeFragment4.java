package com.earth.OsToolkit.Fragment.WelcomeFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.*;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.*;
import android.widget.*;

import com.earth.OsToolkit.MainActivity;
import com.earth.OsToolkit.R;

import static android.content.Context.MODE_PRIVATE;
import static android.support.constraint.Constraints.TAG;
import static com.earth.OsToolkit.Working.BaseClass.Checking.checkRoot;

public class WelcomeFragment4 extends Fragment {
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.activity_welcome_4,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout linearLayout = view.findViewById(R.id.start_su);
        linearLayout.setOnClickListener(v -> suAction());
    }

    public void suAction(){
        Toast.makeText(getActivity(), getString(R.string.welcome_get_root),
                Toast.LENGTH_SHORT).show();
        if (getSu()) {
            Intent intent = new Intent(getActivity(),MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), getString(R.string.welcome_get_root_fail), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean getSu(){
        try {
            Process process = Runtime.getRuntime().exec("su");
            if (checkRoot()) {
                SharedPreferences sp = getActivity().getSharedPreferences("save",MODE_PRIVATE);
                sp.edit().putInt("welcome",1).apply();
                Toast.makeText(getActivity(), getString(R.string.welcome_get_root_success),
                        Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(getActivity(), getString(R.string.welcome_get_root_fail),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.welcome_get_root_pbm),
                    Toast.LENGTH_SHORT).show();
            Log.e(TAG, "getSu");
            return false;
        }
    }
}