package teamprj.antrip.ui.function;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import teamprj.antrip.R;
import teamprj.antrip.map.GoogleMapFragment;

public class PlanResultActivity extends AppCompatActivity {
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_result);

        GoogleMapFragment googleMapFragment = new GoogleMapFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.googleMapFragment, googleMapFragment, "main")
                .commit();
    }
}
