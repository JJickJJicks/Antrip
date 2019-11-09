package teamprj.antrip.ui.function;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

import teamprj.antrip.R;
import teamprj.antrip.ui.intro.IntroActivity;

public class SplashActivity extends AppCompatActivity {
    private String[] USES_PERMISSIONS = {
            "android.permission.READ_CONTACTS",
            "android.permission.ACCESS_FINE_LOCATION"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) {
            if (checkPermissions(USES_PERMISSIONS)) {
                Intent i = new Intent(this, IntroActivity.class);
                startActivity(i);
                finish();
            }

        } else {
            try {
                Thread.sleep(1000);
                Toast.makeText(this, R.string.wrongNetwork, Toast.LENGTH_SHORT).show();
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
                Intent i = new Intent(this, IntroActivity.class);
                startActivity(i);
                finish();

            } else {
                Toast.makeText(this, R.string.wrongPermissions, Toast.LENGTH_SHORT).show();
                finish();

            }
        }
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
