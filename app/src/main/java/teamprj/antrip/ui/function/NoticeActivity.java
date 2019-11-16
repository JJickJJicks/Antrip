package teamprj.antrip.ui.function;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
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
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private static final String ADMINTYPE = "1";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("notice");
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference curntUserInfo = database.getReference("users");
    private ArrayList<Notice> noticeList = new ArrayList<>();
    private FloatingActionButton fab;
    private String type = "1";

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        final LinearLayout li_fail = findViewById(R.id.notice_fail);

        curntUserInfo.orderByChild("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("email").getValue().equals(user.getEmail())) {
                       type = snapshot.child("type").getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        fab = findViewById(R.id.notice_fab);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (type.equals(ADMINTYPE))
        {
            fab.setVisibility(View.VISIBLE);
        }

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    li_fail.setVisibility(View.GONE);
                } else {
                    li_fail.setVisibility(View.VISIBLE);
                }
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Notice notice = dataSnapshot.getValue(Notice.class);
                    noticeList.add(notice);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        recyclerView = findViewById(R.id.notice_recycler_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NoticeAdapter(noticeList, this);
        recyclerView.setAdapter(adapter);
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
