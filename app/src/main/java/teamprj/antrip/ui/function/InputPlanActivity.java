package teamprj.antrip.ui.function;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import teamprj.antrip.BuildConfig;
import teamprj.antrip.R;
import teamprj.antrip.ui.MainActivity;


public class InputPlanActivity extends FragmentActivity {

    Intent intent;
    int index;
    Place place;
    LatLng latLng;
    CheckBox accommodationCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_plan);
        intent = getIntent();
        index = intent.getExtras().getInt("position");

        accommodationCheck = findViewById(R.id.accommodationCheck);


        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), BuildConfig.places_api_key);
        }
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place selectPlace) {
                place = selectPlace;
                latLng = place.getLatLng();
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.d("An error occurred: ", String.valueOf(status));
            }
        });


        Button okButton = findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (place == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(InputPlanActivity.this);
                    builder.setTitle("장소를 선택하지 않았습니다.");
                    builder.setMessage("장소를 선택해주시기 바랍니다.");
                    builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    builder.show();
                } else if (accommodationCheck.isChecked() && TravelPlanActivity.checkAccommodation(index) == -1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(InputPlanActivity.this);
                    builder.setTitle("숙소를 이미 선택하셨습니다.");
                    builder.setMessage("숙소는 하루에 한 곳만 지정할 수 있습니다.");
                    builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    builder.show();
                } else {
                    finish();
//                    Log.d("add", place.getAddress());
//                    String country = place.getAddressComponents().asList().get(place.getAddressComponents().asList().size() - 1).getName();
//                    Log.d("country", country);
                    TravelPlanActivity.addItem(index, place.getName(), latLng, accommodationCheck.isChecked());
                }
            }
        });

        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
