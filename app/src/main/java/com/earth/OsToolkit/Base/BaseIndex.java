package com.earth.OsToolkit.Base;

@SuppressWarnings("all")
public class BaseIndex {

	// Package Information/信息
	public static String PackageName = "com.earth.OsToolkit";
	public static String CoolapkPackageName = "com.coolapk.market";
	public static String OsToolkitSubstratumName = "substratum.earth.OsToolkit";
	public static String SubstratumName = "projekt.substratum";

	// Shell
	public static String CHARGE_ALLOW = "charge_allow.sh";
	public static String CHARGE_QC3 = "charge_qc3.sh";
	public static String CHARGE_USBQC = "charge_usbqc.sh";

	// Path/路径
	public static String Charging_Allow = "/sys/class/power_supply/battery/battery_charging_enabled";
	public static String Charging_QC3 = "/sys/class/power_supply/battery/allow_hvdcp3";
	public static String Charging_USBQC = "/sys/kernel/fast_charge/force_fast_charge";
}
