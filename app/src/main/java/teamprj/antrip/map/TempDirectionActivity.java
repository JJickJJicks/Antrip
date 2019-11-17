package teamprj.antrip.map;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import teamprj.antrip.R;

public class TempDirectionActivity extends AppCompatActivity {
    DirectionFragment googleMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_direction);

        if (savedInstanceState == null) {
            googleMapFragment = new DirectionFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.googleMapFragmentTemp, googleMapFragment, "main")
                    .commit();
        }

        Button btn = findViewById(R.id.tempDirection_Btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng origin = new LatLng(40.722543,-73.998585);
                LatLng destination = new LatLng(40.7064, -74.0094);
                googleMapFragment.draw(origin, destination, "origin name", "dest name");
            }
        });

        Button btn2 = findViewById(R.id.tempDirection_Btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng origin = new LatLng(40.7057, -73.9964);
                LatLng destination = new LatLng(40.7064, -74.0094);
                googleMapFragment.draw(origin, destination, "origin name", "dest name");
            }
        });
    }
}
