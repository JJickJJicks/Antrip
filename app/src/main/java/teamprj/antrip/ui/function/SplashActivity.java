package teamprj.antrip.ui.function;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

import teamprj.antrip.R;
import teamprj.antrip.ui.intro.IntroActivity;
import teamprj.antrip.ui.login.LoginActivity;

public class SplashActivity extends Activity {
    private final int SPLASH_DISPLAY_TIME = 3000;
    private SharedPreferences.Editor editor;

    private String[] USES_PERMISSIONS = {
            "android.permission.READ_CONTACTS",
            "android.permission.ACCESS_FINE_LOCATION"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = cm.getActiveNetworkInfo();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (activeNetwork != null) {
                    if (checkPermissions(USES_PERMISSIONS)) {
                        start();
                    }
                } else {
                    try {
                        Thread.sleep(1000);
                        Toast.makeText(getApplicationContext(), R.string.wrongNetwork, Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, SPLASH_DISPLAY_TIME);

    }

    private Boolean checkPermissions(String[] permissions) {
        boolean isGranted = false;
        ArrayList<String> notGrantedPermission = new ArrayList<String>();
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            int result = ActivityCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                notGrantedPermission.add(permission);
            }
        }
        if (notGrantedPermission.size() < 1) {
            isGranted = true;
        } else {
            ActivityCompat.requestPermissions(this, notGrantedPermission.toArray(new String[notGrantedPermission.size()]), 1);
        }
        return isGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            ArrayList<String> userNotAllowPermissions = new ArrayList();
            boolean isGranted = false;

            if (grantResults.length > 0) {
                isGranted = true;
                int index = 0;
                for (int i = 0; i < grantResults.length; i++) {
                    int grantResult = grantResults[i];
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        isGranted = false;
                        //[다시보지 않기] 거부
                        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[index])) {
                            userNotAllowPermissions.add(permissions[index]);
                        }
                    }
                    index++;
                }
            }
            if (isGranted) {
                start();
            } else {
                Toast.makeText(this, R.string.wrongPermissions, Toast.LENGTH_SHORT).show();
                finish();

            }
        }
    }

    private void start() {
        SharedPreferences setting = getSharedPreferences("start", 0);
        editor = setting.edit();

        Intent i = null;
        if (setting.getBoolean("Intro_pass", false)) {
            i = new Intent(this, IntroActivity.class);
            editor.putBoolean("Intro_pass", true);
            editor.commit();
        } else
            i = new Intent(this, LoginActivity.class);
        startActivity(i);
        SplashActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
