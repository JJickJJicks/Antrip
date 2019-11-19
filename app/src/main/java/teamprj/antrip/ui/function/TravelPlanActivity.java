package teamprj.antrip.ui.function;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import teamprj.antrip.R;
import teamprj.antrip.adapter.ExpandableListAdapter;
import teamprj.antrip.data.model.Plan;
import teamprj.antrip.data.model.Travel;
import teamprj.antrip.map.DestinationDetailActivity;
import teamprj.antrip.map.GoogleMapFragment;


public class TravelPlanActivity extends AppCompatActivity implements ExpandableListAdapter.OnStartDragListner {

    private static List<ExpandableListAdapter.Item> data;
    private static RecyclerView recyclerview;
    private static ExpandableListAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    private ExpandableListAdapter.OnStartDragListner thisListener;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String userName = user.getEmail().replace(".", "_");
    private String tripName;
    private ArrayList<String> authList = null;
    private int period;
    private String start_date;
    private String end_date;
    private String headerText = "0일차";
    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_plan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            GoogleMapFragment googleMapFragment = new GoogleMapFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.googleMapFragment, googleMapFragment, "main")
                    .commit();
        }

        thisListener = this;
        recyclerview = findViewById(R.id.recyclerView_travel_plan);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        data = new ArrayList<>();

        Intent intent = getIntent();
        tripName = intent.getExtras().getString("tripName");
        setTitle(tripName);

        if (intent.getStringExtra("savedTrip").equals("true")) { // 이미 저장된 여행
            myRef.child("plan").child(userName).child(tripName).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Plan plan = dataSnapshot.getValue(Plan.class);
                            period = plan.getPeriod();
                            start_date = plan.getStart_date();
                            end_date = plan.getEnd_date();
                            HashMap<String, ArrayList<Travel>> planHashMap = plan.getTravel();
                            for (int i = 1; i <= plan.getPeriod(); i++) {
                                ArrayList<Travel> travelList = planHashMap.get(i + "_day");
                                headerText = headerText.replace(Integer.toString(i - 1), Integer.toString(i));
                                data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, headerText));
                                for (int j = 0; j < travelList.size(); j++) {
                                    String name = travelList.get(j).getName();
                                    String country = travelList.get(j).getCountry();
                                    LatLng latLng = new LatLng(travelList.get(j).getLatitude(), travelList.get(j).getLongitude());
                                    data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.DATA,
                                            name, country, latLng, travelList.get(j).isAccommodation()));
                                    GoogleMapFragment.selectPlace(latLng, name, country);
                                }
                                data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "추가"));
                            }
                            mAdapter = new ExpandableListAdapter(data, thisListener);

                            ItemTouchHelperCallback mCallback = new ItemTouchHelperCallback(mAdapter);
                            mItemTouchHelper = new ItemTouchHelper(mCallback);
                            mItemTouchHelper.attachToRecyclerView(recyclerview);
                            recyclerview.setAdapter(mAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d("ErrorTravelPlanActivity", "data receive error");
                        }
                    });
        } else {
            start_date = intent.getStringExtra("start_date");
            end_date = intent.getStringExtra("end_date");
            period = Integer.parseInt(intent.getExtras().getString("period"));

            Date startDate;
            SimpleDateFormat format;
            try {
                format = new SimpleDateFormat("yyyy-MM-dd");
                startDate = format.parse(start_date);

                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);

                for (int i = 0; i < period; i++) {
                    headerText = headerText.replace(Integer.toString(i), Integer.toString(i + 1));
                    data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, headerText + " / " + format.format(cal.getTime())));
                    data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "추가"));
                    cal.add(Calendar.DATE, 1);
                }
                mAdapter = new ExpandableListAdapter(data, this);

                ItemTouchHelperCallback mCallback = new ItemTouchHelperCallback(mAdapter);
                mItemTouchHelper = new ItemTouchHelper(mCallback);
                mItemTouchHelper.attachToRecyclerView(recyclerview);
                recyclerview.setAdapter(mAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        myRef.child("plan").child(userName).child(tripName).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("authority")) {
                            authList = (ArrayList) ((HashMap) dataSnapshot.getValue()).get("authority");
                        } else {
                            authList = new ArrayList<>();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("ErrorTravelPlanActivity", "data receive error");
                    }
                });
    }

    void sort(int date) { // 정렬
        ArrayList<String>[] listArr = new ArrayList[date]; // 날짜별 여행지를 저장할 String ArrayList를 만듬
        for (int i = 0, j = -1; i < data.size(); i++) {
            if (data.get(i).type == ExpandableListAdapter.HEADER) {
                j++;
                listArr[j] = new ArrayList<>();
            } else if (data.get(i).type == ExpandableListAdapter.DATA) {
                listArr[j].add(data.get(i).name);
            }
        }

        for (int i = 0; i < date; i++) {
            final int day = i + 1;
            list = new ArrayList<>();
            list.addAll(listArr[i]);

            EAX eax = new EAX(list);
            ArrayList<String> result = eax.run();
            Log.d("JsonCheck", result.toString());

            int days = 0;
            int startPosition = 0;
            for (int j = 0; j < data.size(); j++) { // Header 탐색(일차 찾아가기)
                if (data.get(j).type == ExpandableListAdapter.HEADER) {
                    days++;
                    if (days == day) {
                        startPosition = j + 1;      // j는 Header 위치이므로 Data 부터 접근하기 위해 + 1
                        break;
                    }
                }
            }
//            for (int k = 0; k < result.size(); k++) {
//                for (int j = startPosition; j < data.size(); j++) {
//                    if (data.get(j).name.equals(result.get(k))) {
//                        mAdapter.onItemMove(startPosition + k, j);
//                        break;
//                    }
//                }
//            }

            List<ExpandableListAdapter.Item> tempData = new ArrayList<>();

            for (int k = 1; k < result.size(); k++) {
                for (int n = startPosition; n < data.size(); n++) {
                    if (data.get(n).name.equals(result.get(k))) {
//                        mAdapter.onItemMove(startPosition + i, j);
                        tempData.add(data.get(n));
                        data.remove(n);
                        break;
                    }
                }
            }

            for (int k = 0; k < tempData.size(); k++) {
                ExpandableListAdapter.Item it = tempData.get(k);
                data.add(startPosition + k + 1, it);
            }
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder holder) {
        mItemTouchHelper.startDrag(holder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_travel_plan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
            case R.id.action_save_title: {
                clickSaveButton(false);
                return true;
            }
            case R.id.action_calc_title: {
                sort(period);

                recyclerview.setAdapter(mAdapter);
                List<Travel> calcList = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    ExpandableListAdapter.Item getData = data.get(i);
                    if (getData.type == ExpandableListAdapter.DATA) {
                        calcList.add(new Travel(getData.name, getData.country, getData.latLng.latitude, getData.latLng.longitude, getData.accommodation));
                    }
                }
                for (int i = 0; i < calcList.size(); i++) {
                    Log.d("calcList", calcList.get(i).getName() + ", " + calcList.get(i).isAccommodation() + ", " +
                            calcList.get(i).getLatitude() + ", " + calcList.get(i).getLongitude());
                }
                if (isAccommodationSelected()) {
                    savePlan();
                } else {
                    OkAlertDialog.viewOkAlertDialog(TravelPlanActivity.this, "숙소를 선택하지 않았습니다.", "각 일차별로 숙소를 지정해주시기 바랍니다.");
                }
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TravelPlanActivity.this);
        builder.setTitle("저장 확인");
        builder.setMessage("저장 하시겠습니까?.");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        clickSaveButton(true);
                    }
                });
        builder.setNegativeButton("아니요",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        finish();
                        setResult(RESULT_CANCELED, intent);
                    }
                });
        builder.show();
    }

    public static void addItem(int index, String name, String country, LatLng latLng, boolean accommodation) {
        data.add(index, new ExpandableListAdapter.Item(ExpandableListAdapter.DATA, name, country, latLng, accommodation));
        if (accommodation) {
            index = mAdapter.moveAccommodation(index);
        }
        recyclerview.scrollToPosition(index);
        recyclerview.setAdapter(mAdapter);
        GoogleMapFragment.selectPlace(latLng, name, country);
    }

    public static void synchronize(List<ExpandableListAdapter.Item> getData) {
        data = getData;
    }

    public static int checkAccommodation(int position) {
        for (int i = position; i >= 0; i--) {
            if (data.get(i).type == ExpandableListAdapter.DATA) {
                if (data.get(i).accommodation) {
                    return -1;
                }
            } else if (data.get(i).type == ExpandableListAdapter.HEADER) {
                break;
            }
        }
        return 0;
    }

    public static boolean checkDuplicateData(int index, String name, String country) {
        for (int i = index; i >= 0; i--) {
            if (data.get(i).type == ExpandableListAdapter.DATA) {
                if (data.get(i).name.equals(name) && data.get(i).country.equals(country)) {
                    return false;
                }
            } else if (data.get(i).type == ExpandableListAdapter.HEADER) {
                return true;
            }
        }
        return true;
    }

    private static boolean isAccommodationSelected() {
        for (int i = 0; i < data.size() - 1; i++) {
            ExpandableListAdapter.Item item = data.get(i);
            if (item.type == ExpandableListAdapter.HEADER) {
                List<ExpandableListAdapter.Item> invisibleChild = item.invisibleChildren;
                if (invisibleChild != null) {
                    if (!invisibleChild.get(0).accommodation) { // 접힌 데이터 중 첫번째
                        return false;
                    }
                } else if (!data.get(i + 1).accommodation) {
                    return false;
                }
            }
        }
        return true;
    }

    private void clickSaveButton(boolean isFinish) {
        if (!isAccommodationSelected()) {
            OkAlertDialog.viewOkAlertDialog(TravelPlanActivity.this, "숙소를 선택하지 않았습니다.", "각 일차별로 숙소를 지정해주시기 바랍니다.");
        } else {
            savePlan(isFinish);
        }
    }

    private void savePlan(final boolean isFinish) {
        final LinkedHashMap<String, ArrayList<Travel>> travelMap = new LinkedHashMap<>();
        ArrayList<Travel> travelLIst = new ArrayList<>();
        int day = 0;
        try {
            for (int i = 0; i < data.size(); i++) {
                int type = data.get(i).type;
                String name = data.get(i).name;
                String country = data.get(i).country;
                boolean accommodation = data.get(i).accommodation;

                if (type == ExpandableListAdapter.CHILD) {
                    // 한 일차의 마지막 지점
                    day++;
                    travelMap.put(day + "_day", travelLIst);
                    travelLIst = new ArrayList<>();
                } else if (type == ExpandableListAdapter.HEADER) {
                    List<ExpandableListAdapter.Item> invisibleChild = data.get(i).invisibleChildren;
                    if (invisibleChild != null) {
                        for (int j = 0; j < invisibleChild.size(); j++) {
                            if (invisibleChild.get(j).type == ExpandableListAdapter.CHILD) {
                                // 접힌 부분의 추가 버튼
                                day++;
                                travelMap.put(day + "_day", travelLIst);
                                travelLIst = new ArrayList<>();
                            } else {
                                LatLng latLng = invisibleChild.get(j).latLng;
                                travelLIst.add(new Travel(invisibleChild.get(j).name, country, latLng.latitude, latLng.longitude, accommodation));
                            }
                        }
                    }
                } else if (type == ExpandableListAdapter.DATA) {
                    LatLng latLng = data.get(i).latLng;
                    travelLIst.add(new Travel(name, country, latLng.latitude, latLng.longitude, accommodation));
                }
            }
            Plan plan = new Plan();
            plan.setAuthority(authList);
            plan.setPeriod(period);
            plan.setTravel(travelMap);
            final HashMap<String, ArrayList<Travel>> finalMap = travelMap;
            plan.setStart_date(start_date);
            plan.setEnd_date(end_date);
            plan.setSave(true);
            myRef.child("plan").child(userName).child(tripName).setValue(plan);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("저장 완료");
            builder.setMessage("저장이 완료되었습니다.");
            builder.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (isFinish) {
                                Intent intent = new Intent();
                                intent.putExtra("plan", finalMap);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }
                    });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void savePlan() {
        final LinkedHashMap<String, ArrayList<Travel>> travelMap = new LinkedHashMap<>();
        ArrayList<Travel> travelLIst = new ArrayList<>();
        int day = 0;
        try {
            for (int i = 0; i < data.size(); i++) {
                int type = data.get(i).type;
                String name = data.get(i).name;
                String country = data.get(i).country;
                boolean accommodation = data.get(i).accommodation;

                if (type == ExpandableListAdapter.CHILD) {
                    // 한 일차의 마지막 지점
                    day++;
                    travelMap.put(day + "_day", travelLIst);
                    travelLIst = new ArrayList<>();
                } else if (type == ExpandableListAdapter.HEADER) {
                    List<ExpandableListAdapter.Item> invisibleChild = data.get(i).invisibleChildren;
                    if (invisibleChild != null) {
                        for (int j = 0; j < invisibleChild.size(); j++) {
                            if (invisibleChild.get(j).type == ExpandableListAdapter.CHILD) {
                                // 접힌 부분의 추가 버튼
                                day++;
                                travelMap.put(day + "_day", travelLIst);
                                travelLIst = new ArrayList<>();
                            } else {
                                LatLng latLng = invisibleChild.get(j).latLng;
                                travelLIst.add(new Travel(invisibleChild.get(j).name, country, latLng.latitude, latLng.longitude, accommodation));
                            }
                        }
                    }
                } else if (type == ExpandableListAdapter.DATA) {
                    LatLng latLng = data.get(i).latLng;
                    travelLIst.add(new Travel(name, country, latLng.latitude, latLng.longitude, accommodation));
                }
            }
            Plan plan = new Plan();
            plan.setAuthority(authList);
            plan.setPeriod(period);
            plan.setTravel(travelMap);
            final HashMap<String, ArrayList<Travel>> finalMap = travelMap;
            plan.setStart_date(start_date);
            plan.setEnd_date(end_date);
            plan.setSave(true);
            myRef.child("plan").child(userName).child(tripName).setValue(plan);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("저장 완료");
            builder.setMessage("저장이 완료되었습니다.");
            builder.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.putExtra("plan", finalMap);
                            setResult(RESULT_OK, intent);

                            Intent intent2 = new Intent(getApplicationContext(), DestinationDetailActivity.class);
                            intent2.putExtra("TripName", tripName);
                            startActivity(intent2);
                        }
                    });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

