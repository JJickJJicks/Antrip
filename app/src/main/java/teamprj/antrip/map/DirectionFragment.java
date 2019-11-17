package teamprj.antrip.map;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import teamprj.antrip.BuildConfig;
import teamprj.antrip.R;

public class DirectionFragment extends Fragment implements OnMapReadyCallback {
    private View rootView;
    private MapView mapView;
    private static GoogleMap mMap;
    private static HashMap<String, Marker> markerHashMap;
    private static LatLngBounds.Builder builder;
    private static LatLng origin;
    private static LatLng destination;
    private static String originnName;
    private static String destinationName;

    public DirectionFragment() {
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        markerHashMap = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.content_temp_map, container, false);
        mapView = rootView.findViewById(R.id.mapviewTemp);
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
        this.mMap = googleMap;
    }

    void draw(LatLng oLatLng, LatLng dLatLng, String oName, String dName) {
        final JSONParser parser = new JSONParser();
        final String[] result = new String[1];
        origin = oLatLng;
        destination = dLatLng;
        originnName = oName;
        destinationName = dName;

        try {
            Thread thread = new Thread() {
                public void run() {
                    result[0] = parser.doInBackground(getMapsApiDirectionsUrl());
                }
            };
            thread.start();
            thread.join();
            drawRoute(result[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getMapsApiDirectionsUrl() {
        String origin = "origin=" + DirectionFragment.origin.latitude + "," + DirectionFragment.origin.longitude;
//        String waypoints = "waypoints=optimize:true|" + BROOKLYN_BRIDGE.latitude + "," + BROOKLYN_BRIDGE.longitude + "|";
        String destination = "destination=" + DirectionFragment.destination.latitude + "," + DirectionFragment.destination.longitude;

        String sensor = "sensor=false";
        String key = "key=" + BuildConfig.places_api_key;
        String params = origin + "&" + destination + "&" + sensor + "&" + key;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
        return url;
    }

    private void drawRoute(String result) {
        try {
            //Tranform the string into a json object
            mMap.clear(); // 지도에 마커, 폴리라인 등 초기화
            builder = new LatLngBounds.Builder(); // 지도 확대 축소하기 위한 빌더 초기화
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);

            Polyline line = mMap.addPolyline(new PolylineOptions()  // 폴리라인 그리기
                    .addAll(list)
                    .width(12)
                    .color(Color.parseColor("#05b1fb"))//Google maps blue color
                    .geodesic(true)
            );

            JSONArray arrayLegs = routes.getJSONArray("legs");
            JSONObject legs = arrayLegs.getJSONObject(0);
            JSONArray stepsArray = legs.getJSONArray("steps");
            //put initial point

            for(int i = 0;i < stepsArray.length();i++)  // step marker 찍기
            {
                Step step = new Step(stepsArray.getJSONObject(i));
                mMap.addMarker(new MarkerOptions()
                        .position(step.location)
                        .title(step.distance)
                        .snippet(step.instructions)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                builder.include(step.location);
            }

            int height = 130;
            int width = 130;
            BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.ic_marker_map);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

            // 출, 도착지 marker 찍기
            MarkerOptions markerOption = new MarkerOptions().position(origin).title(originnName);
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            mMap.addMarker(markerOption);
            markerOption = new MarkerOptions().position(destination).title(destinationName);
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            mMap.addMarker(markerOption);

            builder.include(origin);
            builder.include(destination);

            // 찍은 marker를 기준으로 카메라 확대 축소
            LatLngBounds bounds = builder.build();
            mMap.setPadding(0, 100, 0, 0);
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,17));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }

    private class Step
    {
        public String distance;
        public LatLng location;
        public String instructions;

        Step(JSONObject stepJSON)
        {
            JSONObject startLocation;
            try {

                distance = stepJSON.getJSONObject("distance").getString("text");
                startLocation = stepJSON.getJSONObject("start_location");
                location = new LatLng(startLocation.getDouble("lat"),startLocation.getDouble("lng"));
                try {
                    instructions = URLDecoder.decode(Html.fromHtml(stepJSON.getString("html_instructions")).toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
