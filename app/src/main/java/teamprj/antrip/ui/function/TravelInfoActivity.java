package teamprj.antrip.ui.function;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import teamprj.antrip.R;
import teamprj.antrip.adapter.DayPlanAdapter;
import teamprj.antrip.data.model.DayPlan;
import teamprj.antrip.data.model.Plan;
import teamprj.antrip.data.model.Plans;
import teamprj.antrip.data.model.Travel;
import teamprj.antrip.fragment.SublimePickerFragment;

public class TravelInfoActivity extends AppCompatActivity {
    final private int TRAVEL_INFO_REQUEST_CODE = 100;
    final private String GOOGLE_SEARCH_URL = "http://www.google.co.kr/search?complete=1&hl=ko&q=";
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
    String userName = user.getEmail().replace(".", "_");
    private String tripName = "새 여행 1";
    private boolean isSelectTripName = false;
    private boolean isSaved = false;

    String sd=null,ed=null;
    int rec = 0;
    Date StartDate = null;

    SublimePickerFragment.Callback mFragmentCallback = new SublimePickerFragment.Callback() {
        @Override
        public void onCancelled() {
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
        final String name = intent.getExtras().getString("name");

        //TextView maintext = findViewById(R.id.travelInfo_text);
        //maintext.setText(name);

        collapsingToolbar.setTitle(tripName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 초기 여행 이름 및 더미 설정
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
                                myRef.child("plan").child(userName).child(tripName).child("save").setValue(false);
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
                myRef.child("plan").child(userName).child(tripName).child("save").addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if ((boolean)dataSnapshot.getValue()) {
                                    isSaved = true;
                                } else {
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
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.d("ErrorTravelInfoActivity", "data receive error");
                            }
                        });
                if (isSaved) {
                    Intent intent = new Intent(getApplicationContext(), TravelPlanActivity.class);
                    intent.putExtra("tripName", tripName);
                    intent.putExtra("savedTrip", "true");
                    startActivityForResult(intent, TRAVEL_INFO_REQUEST_CODE);
                }
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

        ImageButton weather = (ImageButton) findViewById(R.id.weather_icon);
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = null;
                long calDateDays = 0;
                SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd");
                Date now = new Date();
                if(rec == 0){
                    url = "https://www.google.com/search?q=" + name + "+weather";
                } else {
                    long calDate = 0;
                    calDate = StartDate.getTime() - now.getTime();
                    calDateDays = calDate / (24*60*60*1000);
                    if(Math.abs(calDateDays) > 9){
                        url = "https://www.google.com/search?q=" + name + "+weather";
                    } else {
                        String date = format.format(StartDate);
                        url = "https://www.google.com/search?q=" + name + "+weather+" + date;
                    }
                }
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
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

        return new Pair<>(displayOptions != 0 ? Boolean.TRUE : Boolean.FALSE, options);
    }

    // Show date, time & recurrence options that have been selected
    private void sendData() {
        if (mSelectedDate != null) {
            Log.d("errorCheck", mSelectedDate.getStartDate().getTime().toString());
            String start_date = mSelectedDate.getStartDate().getTime().toString();
            String end_date = mSelectedDate.getEndDate().getTime().toString();
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM d hh:mm:ss z yyyy");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

                StartDate = inputFormat.parse(start_date);
                Date EndDate = inputFormat.parse(end_date);
                long calDate = EndDate.getTime() - StartDate.getTime();
                long calDateDays = calDate / (24 * 60 * 60 * 1000);
                calDateDays = Math.abs(calDateDays) + 1;

                rec++;

                Intent intent = new Intent(getApplicationContext(), TravelPlanActivity.class);
                intent.putExtra("tripName", tripName);
                intent.putExtra("period", String.valueOf(calDateDays));
                intent.putExtra("start_date", outputFormat.format(StartDate));
                intent.putExtra("end_date", outputFormat.format(EndDate));
                intent.putExtra("savedTrip", "false");
                startActivityForResult(intent, TRAVEL_INFO_REQUEST_CODE);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    // 일정 변경 적용
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == TRAVEL_INFO_REQUEST_CODE && resultCode == RESULT_OK) {
            HashMap<String, ArrayList<Travel>> travelMap = (HashMap<String, ArrayList<Travel>>) data.getSerializableExtra("plan"); // plan data 로드됨 (이 데이터 이용해서 하단 뷰 생성)
            updateWeatherView(travelMap);
//            updateExchangeView(travelMap);
            RecyclerView recyclerView = findViewById(R.id.recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            ArrayList<Plans> plansArrayList = new ArrayList<>();

            ArrayList<DayPlan> dayPlanArrayList = new ArrayList<>();

            for(int i=1;i<=travelMap.size();i++){
                for(int j=1;j<travelMap.get(i+"_day").size();j++){
                    dayPlanArrayList.add(new DayPlan(travelMap.get(i+"_day").get(j).getName()));
                }
                plansArrayList.add(new Plans(i+"일차 (" + travelMap.get(i+"_day").get(1).getName() + " ~ )",(List<DayPlan>) dayPlanArrayList.clone()));
                dayPlanArrayList.clear();
            }

            DayPlanAdapter adapter = new DayPlanAdapter(plansArrayList);
            recyclerView.setAdapter(adapter);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateWeatherView(HashMap<String, ArrayList<Travel>> travelMap) { // 날씨 정보 파싱
        ArrayList<String> weatherData = getWeatherData(travelMap); // weather Data 파싱
        StringBuffer weather = new StringBuffer("Weather");
        for (String i : weatherData)
            weather.append("\n" + i);
        Toast.makeText(this, weather.toString(), Toast.LENGTH_SHORT).show();
    }

    private ArrayList<String> getWeatherData(HashMap<String, ArrayList<Travel>> travelMap) {
        ArrayList<String> weather = new ArrayList<>();
        for (int i = 0; i < travelMap.size(); i++) {
            ArrayList<Travel> travelList = travelMap.get((i + 1) + "_day");
            Travel travel = travelList.get(0);
            String name = travel.getName();
            weather.add(GOOGLE_SEARCH_URL + name + " Weather");
        }
        return weather;
    }

    private void updateExchangeView(HashMap<String, ArrayList<Travel>> travelMap) { // 환율 정보 파싱
        ArrayList<String> exchangeData = getExchangeData(travelMap); // exchange rate Data 파싱
        StringBuffer exchange = new StringBuffer("Exchange");
        for (String i : exchangeData)
            exchange.append("\n" + i);
        Toast.makeText(this, exchange.toString(), Toast.LENGTH_SHORT).show();
    }

    private ArrayList<String> getExchangeData(HashMap<String, ArrayList<Travel>> travelMap) {
        ArrayList<String> exchange = new ArrayList<>();
        for (int i = 0; i < travelMap.size(); i++) {
            ArrayList<Travel> travelList = travelMap.get(i + "_day");
            Travel travel = travelList.get(0);
            String country = travel.getCountry();
            exchange.add(GOOGLE_SEARCH_URL + country + " Exchange Rate");
        }
        return exchange;
    }
}
