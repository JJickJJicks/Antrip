package teamprj.antrip.ui.function;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import teamprj.antrip.R;
import teamprj.antrip.adapter.NoticeAdapter;
import teamprj.antrip.data.model.Notice;

public class NoticeActivity extends AppCompatActivity {
    final private static int NOTICE_CREATE = 20;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    final private String ADMIN_TYPE = "1";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("notice");
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference curntUserInfo;
    private ArrayList<Notice> noticeList = new ArrayList<>();
    private FloatingActionButton fab;
    private String type = "1";
    private LinearLayout li_fail;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        li_fail = findViewById(R.id.notice_fail);
        fab = findViewById(R.id.notice_fab);

        fab.setVisibility(View.INVISIBLE);

        final String userKey= user.getEmail().replace(".", "_");
        curntUserInfo = database.getReference("users").child(userKey).child("type");
        curntUserInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                type = dataSnapshot.getValue().toString();
                if (type.equals(ADMIN_TYPE))
                    fab.setVisibility(View.VISIBLE);
                else
                    fab.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView = findViewById(R.id.notice_recycler_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NoticeCreateActivity.class);
                startActivityForResult(intent, NOTICE_CREATE);
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    li_fail.setVisibility(View.INVISIBLE);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Notice notice = snapshot.getValue(Notice.class);
                        noticeList.add(notice);
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new NoticeAdapter(noticeList, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                } else {
                    li_fail.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NOTICE_CREATE && resultCode == RESULT_OK) {
            noticeList.clear();

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Notice notice = snapshot.getValue(Notice.class);
                        noticeList.add(notice);
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter = new NoticeAdapter(noticeList, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // 뒤로 가기
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
