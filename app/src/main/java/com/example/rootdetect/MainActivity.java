package com.example.rootdetect;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);

        textView.setText("Suspicious Permissions: "+ CheckDangerPermitions.getAppsUsePermissions(this)+
                "\nSuspicious Permission in Use: "+CheckDangerPermitions.permissionAreUse(this) +
                "\nSuspicious Folder: " + RootDetection.isSuspectFolder()+"\nTest-Keys: " + RootDetection.checkTestKeys()+
                "\nSu Command: " + RootDetection.checkCommandSu()+"\nCyanogen: "+RootDetection.checkCyanogen()+
                "\n\nApplications analyzed: "+CheckDangerPermitions.listPackages(this));
    }
}