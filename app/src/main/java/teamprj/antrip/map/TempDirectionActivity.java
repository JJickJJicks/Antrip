package teamprj.antrip.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.model.DirectionsRoute;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import retrofit2.Call;
import retrofit2.Callback;
import teamprj.antrip.R;
import teamprj.antrip.data.model.Directions;
import teamprj.antrip.map.DirectionFragment;
import teamprj.antrip.map.GoogleMapFragment;
import teamprj.antrip.ui.function.TranslateActivity;

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
