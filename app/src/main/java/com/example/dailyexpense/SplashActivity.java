package com.example.dailyexpense;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    String versionName;
    private static final String SHARED_PREF_NAME = "Login";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView txtVer = findViewById(R.id.verNo);
        try {
            versionName = getApplicationContext().getPackageManager()
                    .getPackageInfo(getApplicationContext().getPackageName(), 0)
                    .versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        txtVer.setText("VERSION " + versionName);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPref = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                boolean check = sharedPref.getBoolean("FLAG", false);
                if (check) {
                    goToMainActivity();
                } else {
                    goToNavigationActivity();
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("FLAG", true);
                    editor.apply();
                }
            }
        }, 3000);
    }

    private void goToNavigationActivity() {
        startActivity(new Intent(SplashActivity.this, NavigationActivity.class));
        finish();
    }

    private void goToMainActivity() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }
}
