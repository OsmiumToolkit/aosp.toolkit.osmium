package com.earth.OsToolkit.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;

import com.earth.OsToolkit.Items.CardItem;
import com.earth.OsToolkit.R;

import java.io.File;

import static com.earth.OsToolkit.Base.Checking.*;

@SuppressWarnings("all")
public class MainFragment extends Fragment {

    /*
     * 27 Dec 2018
     *
     * By 1552980358
     *
     */

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);
        setDeviceInfo(view);
        setTitleImage(view);
        setRebootMenu();
    }

    public void setDeviceInfo(View view) {
        // 实例化CardItem
        // Initialize CardItem
        CardItem device = view.findViewById(R.id.device);
        // 获取设备信息
        // Get Device info
        CardItem.Item menufactor = new CardItem.Item(getActivity(),
                R.drawable.ic_device, getDevice());
        // 获取安卓版本
        // Get Android Version
        CardItem.Item android = new CardItem.Item(getActivity(),
                R.drawable.ic_android,
                String.format(getString(R.string.item_card_device_android),
                        getAndroidVersion(),
                        getAndroidVersionName(),
                        Build.VERSION.SDK_INT));

        CardItem.Item cpu = new CardItem.Item(getActivity(), R.drawable.ic_developer_board, getCpu());
        // 获取CPU架构
        // Get CPU Info
        CardItem.Item cpuinfo = new CardItem.Item(getActivity(), R.drawable.ic_item_memory, getCpuABI());
        device.addItems(android, menufactor, cpu, cpuinfo);
    }

    private String getDevice() {
        // 获取手机机型 [厂商 + 机型]
        // Get Phone Device [Manufactor + Device]
        String manufacturer =
                Character.toUpperCase(android.os.Build.MANUFACTURER.charAt(0)) +
                        android.os.Build.MANUFACTURER.substring(1);
        if (!android.os.Build.BRAND.equals(android.os.Build.MANUFACTURER)) {
            manufacturer += " " + Character.toUpperCase(android.os.Build.BRAND.charAt(0))
                                    + android.os.Build.BRAND.substring(1);
        }
        manufacturer += " " + Build.MODEL + " ";
        if (manufacturer.contains("Samsung")) { // 三星TouchWiz
            manufacturer += new File("/system/framework/twframework.jar")
                                    .exists() ? "(TouchWiz)" : "(AOSP-based ROM)";
        } else if (manufacturer.contains("Xioami")) { // 小米MIUI
            manufacturer += new File("/system/framework/framework-miui-res.apk").exists() ? "(MIUI)" : "(AOSP-based ROM)";
        }
        return manufacturer;
    }

    public void setTitleImage(View view) {
        ImageView imageView = view.findViewById(R.id.cpu_manu);
        if (getCpu().contains("Qualcomm")) {
            imageView.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_snapdragon));
        } else if (getCpu().contains("Exynos")) {
            imageView.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_exynos));
        } else if (getCpu().contains("MediaTel")) {
            imageView.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_mediatek));
        }

        ImageView imageView1 = view.findViewById(R.id.android_ver);
        switch (getAndroidVersionName()) {
            case "Lolipop":
                imageView1.setImageDrawable(getResources().getDrawable(R.drawable.ic_android_lollipop, null));
                break;
            case "Marshmallow":
                imageView1.setImageDrawable(getResources().getDrawable(R.drawable.ic_android_marshmallow, null));
                break;
            case "Nougat":
                imageView1.setImageDrawable(getResources().getDrawable(R.drawable.ic_android_nougat, null));
                break;
            case "Oreo":
                imageView1.setImageDrawable(getResources().getDrawable(R.drawable.ic_android_oreo, null));
                break;
            case "Pie":
                imageView1.setImageDrawable(getResources().getDrawable(R.drawable.ic_android_pie, null));
                break;
        }
    }

    private static String getCpu() {
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
            board = soc.replace("EXYNOS", "");
        } else if (soc.contains("MSM")) {
            board = soc.replace("MSM", "msm");
        } else {
            board = soc;
        }

        if (Build.PRODUCT.equals("kenzo")) {
            board = "msm8956";
        }

        return manufactor + " " + board;
    }

    private static String getCpuABI() {
        switch (Build.CPU_ABI) {
            case "arm64-v8a":
                return "arm64";
            case "x86_64":
                return Build.CPU_ABI;
            default:
                if (Build.CPU_ABI.startsWith("x86") || Build.CPU_ABI2.startsWith("x86")) {
                    return "x86";
                } else if (Build.CPU_ABI.startsWith("armeabi-v5") || Build.CPU_ABI.startsWith("armeabi-v6")) {
                    return "mips";
                } else {
                    return Build.CPU_ABI;
                }
        }


    }

    public void setRebootMenu() {
        CardItem reboot = getView().findViewById(R.id.reboot);
        CardItem.Item linux = new CardItem.Item(getActivity(),
                R.drawable.ic_nav_reboot, R.string.reboot_click);
        CardItem.Item soft = new CardItem.Item(getActivity(),
                R.drawable.ic_nav_reboot, getString(R.string.re_soft_click));
        CardItem.Item recovery = new CardItem.Item(getActivity(),
                R.drawable.ic_nav_reboot, R.string.re_rec_click);

        linux.setOnClickListener(v -> reboot_linux());
        soft.setOnClickListener(v -> reboot_soft());
        recovery.setOnClickListener(v -> reboot_recovery());

        reboot.addItems(linux, soft, recovery);
    }

    Process process;

    public void reboot_linux() {
        try {                       // 使用linux shell的reboot重启
            Toast.makeText(getActivity(), getString(R.string.reboot_getRoot),
                    Toast.LENGTH_SHORT).show();
            process = Runtime.getRuntime().exec("su -c reboot");
        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.reboot_fail),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void reboot_soft() {
        try {                       // 使用android的killall zygote杀死所有进程
            Toast.makeText(getActivity(), getString(R.string.reboot_getRoot),
                    Toast.LENGTH_SHORT).show();
            process = Runtime.getRuntime().exec(new String[]{"su -c ", "killall","zygote"});
        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.reboot_fail),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void reboot_recovery() {
        try {                       // 使用linux shell的reboot重启
            Toast.makeText(getActivity(), getString(R.string.reboot_getRoot),
                    Toast.LENGTH_SHORT).show();
            process = Runtime.getRuntime().exec(new String[]{"su -c", "reboot","recovery"});
        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.reboot_fail),
                    Toast.LENGTH_SHORT).show();
        }
    }

}
