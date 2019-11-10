package teamprj.antrip.ui.function;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import teamprj.antrip.R;
import teamprj.antrip.data.model.Plan;

public class TravelInfoActivity extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbar;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();
    private String tripName = "새 여행 1";
    private boolean isSelectTripName = false;

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
        myRef.child("plan").child("admin").addValueEventListener(
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
                                myRef.child("plan").child("admin").child(tripName).setValue("dummy");
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
//                Intent intent = new Intent(getApplicationContext(), TravelPlannerActivity.class);
//                startActivity(intent);

                final EditText edittext = new EditText(TravelInfoActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(TravelInfoActivity.this);
                builder.setTitle("여행 기간 입력");
                builder.setMessage("기간 입력");
                builder.setView(edittext);
                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(), TravelPlanActivity.class);
                                intent.putExtra("admin", "admin");
                                intent.putExtra("tripName", tripName);
                                intent.putExtra("period", edittext.getText().toString());
                                intent.putExtra("savedTrip", "false");
                                startActivity(intent);
                            }
                        });
                builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
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
                //TODO: 3번 버튼 동작
                final EditText edittext = new EditText(TravelInfoActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(TravelInfoActivity.this);
                builder.setTitle("공유하기");
                builder.setMessage("공유할 대상의 아이디를 입력해주시기 바랍니다.");
                builder.setView(edittext);
                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.show();
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
                        myRef.child("plan").child("admin").child(tripName).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        final Plan plan = dataSnapshot.getValue(Plan.class);
                                        myRef.child("plan").child("admin").child(inputName).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                                    myRef.child("plan").child("admin").child(tripName).removeValue();
                                                    tripName = inputName;
                                                    collapsingToolbar.setTitle(tripName);
                                                    myRef.child("plan").child("admin").child(tripName).setValue(plan);
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

    private void getTravelData(String tripName) {

    }
}
