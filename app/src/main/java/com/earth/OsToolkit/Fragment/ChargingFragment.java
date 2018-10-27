package com.earth.OsToolkit.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.earth.OsToolkit.R;

import static com.earth.OsToolkit.Working.BaseClass.Checking.checkSupportQC3;

public class ChargingFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_charging,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        setWarning();
    }

    public void setWarning() {
        CardView cardView = getActivity().findViewById(R.id.charging_cardview);
        if (!checkSupportQC3()) {
            cardView.setVisibility(View.GONE);
        }
    }
}