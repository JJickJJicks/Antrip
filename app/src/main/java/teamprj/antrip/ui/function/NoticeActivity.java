package teamprj.antrip.ui.function;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("notice");
    private ArrayList<Notice> noticeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        final LinearLayout li_fail = findViewById(R.id.notice_fail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
