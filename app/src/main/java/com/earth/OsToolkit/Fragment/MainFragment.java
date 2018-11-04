package com.earth.OsToolkit.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.Toast;

import com.earth.OsToolkit.Items.CardItem;
import com.earth.OsToolkit.*;

import java.io.File;

@SuppressWarnings("all")
public class MainFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_main,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstance){
        super.onViewCreated(view,savedInstance);
        setDeviceInfo(view);
        setRebootMenu();
    }

    public void setDeviceInfo(View view){
        // 实例化CardItem
        // Initialize CardItem
        CardItem device = view.findViewById(R.id.device);
        // 获取设备信息
        // Get Device info
        CardItem.Item menufactor = new CardItem.Item(getActivity(),R.drawable.ic_item_card_device_phone,getDevice());
        // 获取安卓版本
        // Get Android Version
        CardItem.Item android = new CardItem.Item(getActivity(), R.drawable.ic_item_card_device_android,
                String.format(getString(R.string.item_card_device_android),
                        Build.VERSION.RELEASE, getAndroidVersion(), Build.VERSION.SDK_INT));

        CardItem.Item cpu = new CardItem.Item(getActivity(),R.drawable.ic_item_developer_board,getCpu());
        // 获取CPU架构
        // Get CPU Info
        CardItem.Item cpuinfo = new CardItem.Item(getActivity(),R.drawable.ic_item_memory,getCpuABI());
        device.addItems(android,menufactor,cpu,cpuinfo);
    }

    private String getDevice() {
        // 获取手机机型 [厂商 + 机型]
        // Get Phone Device [Manufactor + Device]
        String manufacturer =
                Character.toUpperCase(android.os.Build.MANUFACTURER.charAt(0)) +
                        android.os.Build.MANUFACTURER.substring(1);
        if (!android.os.Build.BRAND.equals(android.os.Build.MANUFACTURER)) {
            manufacturer += " " + Character.toUpperCase(android.os.Build.BRAND.charAt(0)) + android.os.Build.BRAND.substring(1);
        }
        manufacturer += " " + Build.MODEL + " ";
        if (manufacturer.contains("Samsung")) { // 三星TouchWiz
            manufacturer += new File("/system/framework/twframework.jar")
                    .exists() ? "(TouchWiz)" : "(AOSP-based ROM)";
        } else if (manufacturer.contains("Xioami")) { // 小米MIUI
            manufacturer += new File("/system/framework/framework-miui-res.apk").
                    exists() ? "(MIUI)" : "(AOSP-based ROM)";
        }
        return manufacturer;
    }

    private String getAndroidVersion() {
        // 获取Android版本
        // Get Android Version
        switch (Build.VERSION.SDK_INT) {
            case 23: return "Marshmallow";
            case 24: return "Nougat";
            case 25: return "Nougat";
            case 26: return "Oreo";
            case 27: return "Oreo";
            case 28: return "Pie";
            default: return "unknown";
        }
    }

    private static String getCpu(){
        // 获取处理器制造商
        // Fetch SoC manufactor
        String socManu = Build.HARDWARE.toUpperCase();
        String manufactor;
        if (socManu.equals("QCOM") || socManu.equals("QUALCOMM")) {
            manufactor = "Qualcomm Snapdragon";
        } else if (socManu.equals("MTK") || socManu.equals("MEDIATEK")) {
            manufactor = "MediaTek";
        } else if (socManu.equals("EXYNOS")) {
            manufactor = "Samsung Exynos";
        } else {
            manufactor = socManu;
        }

        // 获取SoC型号
        // Fetch SoC model
        String soc = Build.BOARD.toUpperCase();
        String board;
        if (soc.contains("EXYNOS")) {
            board = soc.replace("EXYNOS","");
        } else if (soc.contains("MSM")) {
            board = soc.replace("MSM","msm");
        } else {
            board = soc;
        }

        return manufactor + " " + board;
    }

    private static String getCpuABI(){
        //获取CPU架构
        // Get CPU ABIs
        if (Build.CPU_ABI.equals("arm64-v8a")) {
            return "arm64";
        } else if (Build.CPU_ABI.equals("x86_64")) {
            return "x86_64";
        } else if (Build.CPU_ABI.equals("mips64")) {
            return "mips64";
        } else if (Build.CPU_ABI.startsWith("x86") || Build.CPU_ABI2.startsWith("x86")) {
            return "x86";
        } else if (Build.CPU_ABI.startsWith("mips")) {
            return "mips";
        } else if (Build.CPU_ABI.startsWith("armeabi-v5") || Build.CPU_ABI.startsWith("armeabi-v6")) {
            return "armv5";
        } else {
            return "arm";
        }
    }

    public void setRebootMenu(){
        CardItem reboot = getView().findViewById(R.id.reboot);
        CardItem.Item linux = new CardItem.Item(getActivity(),
                R.drawable.ic_nav_reboot,R.string.reboot_click);
        CardItem.Item soft = new CardItem.Item(getActivity(),
                R.drawable.ic_nav_reboot,getString(R.string.re_soft_click));
        CardItem.Item recovery = new CardItem.Item(getActivity(),
                R.drawable.ic_nav_reboot,R.string.re_rec_click);

        linux.setOnClickListener(v -> reboot_linux());
        soft.setOnClickListener(v -> reboot_soft());
        recovery.setOnClickListener(v -> reboot_recovery());

        reboot.addItems(linux,soft,recovery);
    }

    Process process;

    public void reboot_linux() {
        try {                       // 使用linux shell的reboot重启
            Toast.makeText(getActivity(), getString(R.string.reboot_getRoot),
                    Toast.LENGTH_SHORT).show();
            process = Runtime.getRuntime().exec("su -c reboot");
        } catch (Exception e){
            Toast.makeText(getActivity(), getString(R.string.reboot_fail),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void reboot_soft() {
        try {                       // 使用android的killall zygote杀死所有进程
            Toast.makeText(getActivity(), getString(R.string.reboot_getRoot),
                    Toast.LENGTH_SHORT).show();
            process = Runtime.getRuntime().exec(new String[]{"su -c","killall zygote"});
        } catch (Exception e){
            Toast.makeText(getActivity(), getString(R.string.reboot_fail),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void reboot_recovery() {
        try {                       // 使用linux shell的reboot重启
            Toast.makeText(getActivity(), getString(R.string.reboot_getRoot),
                    Toast.LENGTH_SHORT).show();
            process = Runtime.getRuntime().exec(new String[]{"su -c","reboot recovery"});
        } catch (Exception e){
            Toast.makeText(getActivity(), getString(R.string.reboot_fail),
                    Toast.LENGTH_SHORT).show();
        }
    }

}
