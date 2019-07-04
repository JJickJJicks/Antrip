package teamprj.antrip.ui.function;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import teamprj.antrip.R;
import teamprj.antrip.ui.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            try {
                Thread.sleep(3000);
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
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
}
