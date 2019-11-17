package teamprj.antrip.map;

import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import teamprj.antrip.R;

public class GoogleMapFragment extends Fragment implements OnMapReadyCallback {
    private View rootView;
    private MapView mapView;
    private static GoogleMap googleMap;
    private static HashMap<String, Marker> markerHashMap;
    private static HashMap<String, Integer> coutHashMap;
    private static LatLngBounds.Builder builder;

    public GoogleMapFragment() {
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        builder = new LatLngBounds.Builder();
        markerHashMap = new HashMap<>();
        coutHashMap = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_map, container, false);
        mapView = rootView.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(this.getActivity());
        this.googleMap = googleMap;
    }

    public static void selectPlace(LatLng latLng, String name, String country) {
        String key = name + ", " + country;

        if (markerHashMap.containsKey(key)) {
            Integer count = coutHashMap.get(key);
            if (count != null) {
                coutHashMap.put(key, count + 1);
            } else {
                coutHashMap.put(key, 1);
            }

        } else {
            MarkerOptions markerOption = new MarkerOptions().position(latLng).title(key);
            Marker marker = googleMap.addMarker(markerOption);

            builder.include(latLng);

            markerHashMap.put(key, marker);
            coutHashMap.put(key, 1);
        }
        LatLngBounds bounds = builder.build();

        googleMap.setPadding(0, 100, 0, 0);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,17));
    }

    public static void removePlace(String name, String country) {
        String key = name + ", " + country;

        if (markerHashMap.containsKey(key)) {
            Integer count = coutHashMap.get(key);
            if (count != null && count > 1) {
                coutHashMap.put(key, count - 1);
            } else {
                markerHashMap.get(key).remove();
                markerHashMap.remove(key);

                builder = new LatLngBounds.Builder();
                if (markerHashMap.size() > 0) {
                    for (Marker marker : markerHashMap.values()) {
                        builder.include(marker.getPosition());
                    }
                    LatLngBounds bounds = builder.build();
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 17));
                }
            }
        }
    }
}


