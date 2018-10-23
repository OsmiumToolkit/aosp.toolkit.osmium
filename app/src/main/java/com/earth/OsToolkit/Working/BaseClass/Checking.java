package com.earth.OsToolkit.Working.BaseClass;

import java.io.DataOutputStream;

@SuppressWarnings("all")
public class Checking {
    public static boolean checkRoot(){
        Process process;
        DataOutputStream os;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            int exitValue = process.waitFor();
            if (exitValue == 1)
                return false;
            else
                return true;
        } catch (Exception e) {
            return false;
        }
    }
}
