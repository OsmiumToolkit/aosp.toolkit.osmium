package com.earth.OsToolkit.Fragment;

import android.os.Bundle;
import android.support.annotation.*;
import android.support.v4.app.Fragment;
import android.view.*;

import com.earth.OsToolkit.R;

public class LoadingFragment extends Fragment{

	/*
	 * 27 Dec 2018
	 *
	 * By 1552980358
	 *
	 */

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragmen_loading,container,false);
	}
}
