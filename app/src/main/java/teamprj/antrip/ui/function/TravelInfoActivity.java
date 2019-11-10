package teamprj.antrip.ui.function;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import teamprj.antrip.R;
import teamprj.antrip.data.model.Plan;
import teamprj.antrip.fragment.SublimePickerFragment;

public class TravelInfoActivity extends AppCompatActivity {
    ScrollView svMainContainer;

    // Views to display the chosen Date, Time & Recurrence options
    RelativeLayout rlDateTimeRecurrenceInfo;

    // Chosen values
    SelectedDate mSelectedDate;
    int mHour, mMinute;
    String mRecurrenceOption, mRecurrenceRule;

    private CollapsingToolbarLayout collapsingToolbar;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userName = user.getDisplayName();
    private String tripName = "새 여행 1";
    private boolean isSelectTripName = false;
    SublimePickerFragment.Callback mFragmentCallback = new SublimePickerFragment.Callback() {
        @Override
        public void onCancelled() {
            rlDateTimeRecurrenceInfo.setVisibility(View.GONE);
        }

        @Override
        public void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                            int hourOfDay, int minute,
                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                            String recurrenceRule) {

            mSelectedDate = selectedDate;
            mHour = hourOfDay;
            mMinute = minute;
            mRecurrenceOption = recurrenceOption != null ?
                    recurrenceOption.name() : "n/a";
            mRecurrenceRule = recurrenceRule != null ?
                    recurrenceRule : "n/a";

            sendData();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_travel_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // 뒤로 가기
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            case R.id.action_edit_title: {
                tripNameChange();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void tripNameChange() {
        final EditText edittext = new EditText(TravelInfoActivity.this);
        AlertDialog.Builder builder = new AlertDialog.Builder(TravelInfoActivity.this);
        builder.setTitle("여행 이름 입력 창");
        builder.setMessage("여행 이름을 입력해주시기 바랍니다.");
        builder.setView(edittext);
        builder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final String inputName = edittext.getText().toString();
                        myRef.child("plan").child(userName).child(tripName).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        final Plan plan = dataSnapshot.getValue(Plan.class);
                                        myRef.child("plan").child(userName).child(inputName).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                if (dataSnapshot.exists() && !tripName.equals(savedTripName)) {
                                                if (dataSnapshot.exists()) {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(TravelInfoActivity.this);
                                                    builder.setTitle("이름 중복");
                                                    builder.setMessage("이미 존재하는 이름입니다.");
                                                    builder.setPositiveButton("확인",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    tripNameChange();
                                                                }
                                                            });
                                                    builder.show();
                                                } else {
                                                    myRef.child("plan").child(userName).child(tripName).removeValue();
                                                    tripName = inputName;
                                                    collapsingToolbar.setTitle(tripName);
                                                    myRef.child("plan").child(userName).child(tripName).setValue(plan);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError
                                                                            databaseError) {
                                                Log.d("ErrorTravelInfoActivity", "data receive error");
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.d("ErrorTravelInfoActivity", "data receive error");
                                    }
                                });
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_info);

        collapsingToolbar = findViewById(R.id.travelInfo_toolbarLayout);

        Toolbar toolbar = findViewById(R.id.travelInfo_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");

        TextView maintext = findViewById(R.id.travelInfo_text);
        maintext.setText(name);

        collapsingToolbar.setTitle(tripName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 초기 여행 이름
        myRef.child("plan").child(userName).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> tripNameList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            tripNameList.add(snapshot.getKey());
                        }
                        int i = 0;
                        while (!isSelectTripName) {
                            if (!tripNameList.contains(tripName)) {
                                collapsingToolbar.setTitle(tripName);
                                myRef.child("plan").child(userName).child(tripName).setValue("dummy");
                                isSelectTripName = true;
                                break;
                            } else {
                                i++;
                                String oldNum = String.valueOf(i);
                                String newNum = String.valueOf(i + 1);
                                tripName = tripName.replace(oldNum, newNum);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("ErrorTravelInfoActivity", "data receive error");
                    }
                });


        // 1번 버튼
        findViewById(R.id.travelInfo_tabBtn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SublimePickerFragment pickerFrag = new SublimePickerFragment();
                pickerFrag.setCallback(mFragmentCallback);

                // Options
                Pair<Boolean, SublimeOptions> optionsPair = getOptions();

                Bundle bundle = new Bundle();
                bundle.putParcelable("SUBLIME_OPTIONS", optionsPair.second);
                pickerFrag.setArguments(bundle);

                pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                pickerFrag.show(getSupportFragmentManager(), "SUBLIME_PICKER");
            }
        });

        // 2번 버튼
        findViewById(R.id.travelInfo_tabBtn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TranslateActivity.class);
                startActivity(intent);
            }
        });

        // 3번 버튼
        findViewById(R.id.travelInfo_tabBtn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AuthorityAddActivity.class);
                intent.putExtra("tripName", tripName);
                startActivity(intent);
            }
        });


        // 메인 화면
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.travelInfo_swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //TODO: 메인 화면에 할꺼...

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    Pair<Boolean, SublimeOptions> getOptions() {
        SublimeOptions options = new SublimeOptions();
        int displayOptions = 0;

        displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;
        options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
        options.setDisplayOptions(displayOptions);
        options.setCanPickDateRange(true);

        // Example for setting date range:
        // Note that you can pass a date range as the initial date params
        // even if you have date-range selection disabled. In this case,
        // the user WILL be able to change date-range using the header
        // TextViews, but not using long-press.

        /*Calendar startCal = Calendar.getInstance();
        startCal.set(2016, 2, 4);
        Calendar endCal = Calendar.getInstance();
        endCal.set(2016, 2, 17);

        options.setDateParams(startCal, endCal);*/

        // If 'displayOptions' is zero, the chosen options are not valid
        return new Pair<>(displayOptions != 0 ? Boolean.TRUE : Boolean.FALSE, options);
    }

    // Show date, time & recurrence options that have been selected
    private void sendData() {
        if (mSelectedDate != null) {
            String start_date = DateFormat.getDateInstance().format(mSelectedDate.getStartDate().getTime());
            String end_date = DateFormat.getDateInstance().format(mSelectedDate.getEndDate().getTime());
            try {
                SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy");

                Date FirstDate = format.parse(end_date);
                Date SecondDate = format.parse(start_date);
                long calDate = FirstDate.getTime() - SecondDate.getTime();
                long calDateDays = calDate / (24 * 60 * 60 * 1000);
                calDateDays = Math.abs(calDateDays) + 1;

                Intent intent = new Intent(getApplicationContext(), TravelPlanActivity.class);
                intent.putExtra("admin", "admin");
                intent.putExtra("tripName", tripName);
                intent.putExtra("period", String.valueOf(calDateDays));
                intent.putExtra("start_date", start_date);
                intent.putExtra("end_date", end_date);
                intent.putExtra("savedTrip", "false");
                startActivity(intent);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}
