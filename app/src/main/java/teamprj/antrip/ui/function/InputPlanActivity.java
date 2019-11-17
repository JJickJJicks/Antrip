package teamprj.antrip.ui.function;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

import teamprj.antrip.BuildConfig;
import teamprj.antrip.R;


public class InputPlanActivity extends FragmentActivity {

    Intent intent;
    private int index;
    private Place place;
    private LatLng latLng;
    private CheckBox accommodationCheck;

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
                    OkAlertDialog.viewOkAlertDialog(InputPlanActivity.this, "장소를 선택하지 않았습니다.", "장소를 선택해주시기 바랍니다.");
                } else if (accommodationCheck.isChecked() && TravelPlanActivity.checkAccommodation(index) == -1) {
                    OkAlertDialog.viewOkAlertDialog(InputPlanActivity.this, "숙소를 이미 선택하셨습니다.", "숙소는 한 군데만 선택할 수 있습니다.");
                } else if (!TravelPlanActivity.checkDuplicateData(index, place.getName(), getCountry(place))) {
                    OkAlertDialog.viewOkAlertDialog(InputPlanActivity.this, "이미 선택한 여행지 입니다.", "같은 여행지는 추가할 수 없습니다.");
                } else {
                    finish();
                    String country = getCountry(place);
                    TravelPlanActivity.addItem(index, place.getName(), country, latLng, accommodationCheck.isChecked());
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

    private String getCountry(Place place) {
        for (int i = 0; i < place.getAddressComponents().asList().size(); i++) {
            if (place.getAddressComponents().asList().get(i).getTypes().get(0).equals("country")) {
                return place.getAddressComponents().asList().get(i).getName();
            }
        }
        return "null";
    }
}
