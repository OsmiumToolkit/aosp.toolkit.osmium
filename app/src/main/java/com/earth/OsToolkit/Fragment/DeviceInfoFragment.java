package com.earth.OsToolkit.Fragment;

import android.app.ActivityManager;
import android.content.Context;
import android.os.*;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;

import com.earth.OsToolkit.Items.DeviceInfoCardView;

import static com.earth.OsToolkit.Items.DeviceInfoCardView.Item;

import com.earth.OsToolkit.R;
import com.earth.OsToolkit.Working.BaseClass.Checking;

public class DeviceInfoFragment extends Fragment {
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_deviceinfo, container, false);
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
		DeviceInfoCardView general = view.findViewById(R.id.deviceinfo_general);
		general.setTitle(R.string.deviceinfo_general_title);

		Item manufacturer = new Item(getActivity(), R.string.deviceinfo_general_manufacturer,
				Character.toUpperCase(android.os.Build.MANUFACTURER.charAt(0)) +
						android.os.Build.MANUFACTURER.substring(1));
		Item brand = new Item(getActivity(), R.string.deviceinfo_general_brand,
				Character.toUpperCase(android.os.Build.BRAND.charAt(0))
						+ android.os.Build.BRAND.substring(1));
		Item model = new Item(getActivity(), R.string.deviceinfo_general_model, Build.MODEL);

		Item device = new Item(getActivity(), R.string.deviceinfo_general_product, Build.PRODUCT);

		general.addItems(manufacturer, brand, model, device);

	}

	public void setSoC(View view) {
		DeviceInfoCardView soc = view.findViewById(R.id.deviceinfo_soc);
		soc.setTitle(R.string.deviceinfo_soc_title);

		Item manufacturer = new Item(getActivity(), R.string.deviceinfo_soc_manufacturer,
				Character.toUpperCase(Build.HARDWARE.charAt(0)) +
						Build.HARDWARE.substring(1));

		Item product = new Item(getActivity(), R.string.deviceinfo_soc_model, Build.BOARD);

		Item cores = new Item(getActivity(), R.string.deviceinfo_soc_cores, Checking.getCPUCores() + "");

		Item abi = new Item(getActivity(), R.string.deviceinfo_soc_abis, Checking.getAllAPI());

		Item abi64 = new Item(getActivity(), R.string.deviceinfo_soc_abis64, Checking.get64API());

		Item abi32 = new Item(getActivity(), R.string.deviceinfo_soc_abis32, Checking.get32API());

		soc.addItems(manufacturer, product, cores, abi, abi64, abi32);
	}


	public void setRam(View view) {
		ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		ActivityManager activityManager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(memoryInfo);

		long totalMem = memoryInfo.totalMem;
		Log.i("totalMem", totalMem + "");

		long threshold = memoryInfo.threshold;

		DeviceInfoCardView ram = view.findViewById(R.id.deviceinfo_ram);
		ram.setTitle(R.string.deviceinfo_ram_title);

		Item totalMemory = new Item(getActivity(), R.string.deviceinfo_ram_totalMem, convertUnit(totalMem));

		Item thresholdMem = new Item(getActivity(), R.string.deviceinfo_ram_threshold, convertUnit(threshold));

		ram.addItems(totalMemory, thresholdMem);

	}

	public void setRumtime(View view) {
		DeviceInfoCardView runtime = view.findViewById(R.id.deviceinfo_runtime);
		runtime.setTitle(R.string.deviceinfo_javaruntime_title);

		Runtime r = Runtime.getRuntime();

		long maxMemory = r.maxMemory();
		long totalMemory = r.totalMemory();

		Item maxMem = new Item(getActivity(), R.string.deviceinfo_javaruntime_maxmem, convertUnit(maxMemory));

		Item totalMem = new Item(getActivity(), R.string.deviceinfo_javaruntime_totalmem, convertUnit(totalMemory));

		runtime.addItems(maxMem, totalMem);
	}


	private String convertUnit(long no) {
		if (no > 1024 * 1024) {
			return no / 1024 / 1024 + " MB";
		} else if (no > 1024) {
			return no / 1024 + "KB";
		} else {
			return no + "B";
		}
	}

}
