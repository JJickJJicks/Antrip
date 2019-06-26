package teamprj.antrip;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class LicenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        WebView webView = findViewById(R.id.webview);
        webView.loadUrl("file:///android_asset/www/license.html");
    }
}
