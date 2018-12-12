package com.earth.OsToolkit.Fragment;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.earth.OsToolkit.Items.DeviceInfoCardViewItem;
import static com.earth.OsToolkit.Items.DeviceInfoCardViewItem.Item;
import com.earth.OsToolkit.R;

public class DeviceInfoFragment extends Fragment {
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_deviceinfo,container,false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		setGeneral(view);
		setSoC(view);
		setRam(view);
		setRumtime(view);

	}

	public void setGeneral(View view) {
		DeviceInfoCardViewItem general = view.findViewById(R.id.deviceinfo_general);
		general.setTitle(R.string.deviceinfo_general_title);

		Item manufacturer = new Item(getActivity(), R.string.deviceinfo_general_manufacturer,
				Character.toUpperCase(android.os.Build.MANUFACTURER.charAt(0)) +
						android.os.Build.MANUFACTURER.substring(1));
		Item brand = new Item(getActivity(), R.string.deviceinfo_general_brand,
				Character.toUpperCase(android.os.Build.BRAND.charAt(0))
						+ android.os.Build.BRAND.substring(1));
		Item model = new Item(getActivity(), R.string.deviceinfo_general_model, Build.MODEL);

		Item device = new Item(getActivity(), R.string.deviceinfo_general_product, Build.PRODUCT);

		general.addItems(manufacturer,brand,model,device);

	}

	public void setSoC(View view) {
		DeviceInfoCardViewItem soc = view.findViewById(R.id.deviceinfo_soc);
		soc.setTitle(R.string.deviceinfo_soc_title);

		Item manufacturer = new Item(getActivity(), R.string.deviceinfo_soc_manufacturer,
				Character.toUpperCase(Build.HARDWARE.charAt(0)) +
						Build.HARDWARE.substring(1));

		Item product = new Item(getActivity(),R.string.deviceinfo_soc_model,Build.HARDWARE);

		StringBuilder abis = new StringBuilder();

		for (int i = 0; i < Build.SUPPORTED_ABIS.length; i++) {
			abis.append(Build.SUPPORTED_ABIS[i]);
			if (i < Build.SUPPORTED_ABIS.length - 1) {
				abis.append("\n");
			}
		}

		Log.i("abis",abis.toString());

		Item abi = new Item(getActivity(),R.string.deviceinfo_soc_abis,abis.toString());

		soc.addItems(manufacturer,product,abi);
	}



	public void setRam(View view) {
		ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(memoryInfo);

		long totalMem = memoryInfo.totalMem;
		Log.i("totalMem",totalMem+"");

		long threshold = memoryInfo.threshold;

		DeviceInfoCardViewItem ram = view.findViewById(R.id.deviceinfo_ram);
		ram.setTitle(R.string.deviceinfo_ram_title);

		Item totalMemory = new Item(getActivity(), R.string.deviceinfo_ram_totalMem, convertUnit(totalMem));

		Item thresholdMem = new Item(getActivity(), R.string.deviceinfo_ram_threshold, convertUnit(threshold));

		ram.addItems(totalMemory,thresholdMem);

	}

	public void setRumtime(View view) {
		DeviceInfoCardViewItem runtime = view.findViewById(R.id.deviceinfo_runtime);
		runtime.setTitle(R.string.deviceinfo_javaruntime_title);

		Runtime r = Runtime.getRuntime();

		long maxMemory = r.maxMemory();
		long totalMemory = r.totalMemory();

		Item maxMem = new Item(getActivity(), R.string.deviceinfo_javaruntime_maxmem, convertUnit(maxMemory));

		Item totalMem = new Item(getActivity(),R.string.deviceinfo_javaruntime_totalmem,convertUnit(totalMemory));

		runtime.addItems(maxMem,totalMem);
	}


	private String convertUnit(long no) {
		if (no > 1024*1024*1024) {
			return no / 1024 / 1024/ 1024 + " GB";
		} else if (no > 1024*1024) {
			return no / 1024 / 1024 + " MB";
		} else if (no > 1024){
			return no / 1024 + "KB";
		} else {
			return no + "B";
		}
	}

}
