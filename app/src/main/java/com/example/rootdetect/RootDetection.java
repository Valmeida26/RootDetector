package com.example.rootdetect;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class RootDetection {

    static boolean isSuspectFolder() {
        String[] paths = { "/xbin", "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su",
                "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su","/system/bin/failsafe/su",
                "/data/local/su", "/su/bin/su"};
        for (String path : paths) {
            if (new File(path).exists()) {
                return true;
            }
        }
        return false;
    }
    static boolean checkTestKeys() {
        String tags = android.os.Build.TAGS;
        return tags != null && tags.contains("test-keys");
    }
    public static boolean checkCommandSu() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("su");
            process.getOutputStream().write("\n".getBytes());
            process.getOutputStream().write("exit\n".getBytes());
            process.getOutputStream().flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            if (line != null) {
                if (!line.contains("Process denied")) {
                    process.destroy();
                    return false;
                }
            }
            process.destroy();
            return true;
        } catch (IOException e) {
            if (process != null) {
                process.destroy();
            }
            return false;
        } catch (Throwable th) {
            if (process != null) {
                process.destroy();
            }
            throw th;
        }
    }

    public static boolean checkCyanogen() {
        String s = Build.HOST;
        if (s.toUpperCase().contains("CYANOGEN")) {
            return true;
        }
        return false;
    }

    public static boolean isSuperUserAppRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfoList = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfoList) {
            String processName = processInfo.processName;
            if (processName != null && (processName.contains("su") || processName.contains("su"))) {
                return true;
            }
        }
        return false;
    }
}
