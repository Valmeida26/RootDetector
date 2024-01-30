package com.example.rootdetect;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckDangerPermitions {
    static String[] permitions = {"android.permission.WRITE_SELINUX_POLICY", "android.permission.MODIFY_KERNEL_SETTINGS ", "android.permission.MODIFY_KERNEL_SETTINGS", "android.permission.SETENFORCE", "android.permission.SETSEPOLICY", "android.permission.PERMISSION_SUPERUSER", "android.permission.ACCESS_SUPERUSER"};

    //Returns which apps are using the permissions in the list and which permissions they are using
    public static List<Pair<String, String>> getAppsUsePermissions(Context context) {
        PackageManager pm = context.getPackageManager();
        List<Pair<String, String>> appsWithPermissions = new ArrayList<>();
        try {
            List<PackageInfo> packages = pm.getInstalledPackages(4096);
            for (PackageInfo packageInfo : packages) {
                if (packageInfo.requestedPermissions != null) {
                    for (String permission : packageInfo.requestedPermissions) {
                        if (Arrays.asList(permitions).contains(permission)) {
                            appsWithPermissions.add(new Pair<>(packageInfo.packageName, permission));
                            StringBuilder resultText = new StringBuilder();
                            for (Pair<String, String> appInfo : appsWithPermissions) {
                                String packageName = (String) appInfo.first;
                                String permitionName = (String) appInfo.second;
                                resultText.append(packageName).append(permitionName);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appsWithPermissions;
    }

    //Returns one if any of these permissions are in use
    public static boolean permissionAreUse(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            List<PackageInfo> packages = pm.getInstalledPackages(4096);
            for (PackageInfo packageInfo : packages) {
                if (packageInfo.requestedPermissions != null) {
                    for (String permission : packageInfo.requestedPermissions) {
                        if (Arrays.asList(permitions).contains(permission)) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //Returns the name and package of a list of installed applications
    public static JSONArray listPackages(Context context) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        PackageManager packageManager = context.getPackageManager();
        String packageName;
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(128);
        for (ApplicationInfo appInfo : installedApplications) {
            String appName = appInfo.loadLabel(packageManager).toString();
            packageName = appInfo.packageName;
            try {
                jsonObject.put("name", appName);
                jsonObject.put("package", packageName);
                jsonArray.put(jsonObject.toString().replace("\"", ""));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return jsonArray;
    }
}
