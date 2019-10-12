package teamprj.antrip.ui.function;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.core.view.Change;

import java.util.ArrayList;
import java.util.HashMap;

import teamprj.antrip.R;
import teamprj.antrip.data.model.Member;
import teamprj.antrip.data.model.Notice;

public class NoticeActivity extends AppCompatActivity {
    Context context = this;
    LinearLayout linearLayout;
    RecyclerView recyclerView;
    FloatingActionButton fab;
    FloatingActionButton fab_write;
    RecyclerView.Adapter adapter;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private ArrayList<HashMap<String, String>> noticeList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> noticeList_reverse = new ArrayList<>();
    private boolean isFabOpen = false;
    private static final int MESSAGE_OK = 1;
    private static final int MESSAGE_FAIL = -1;
    private static final int MESSAGE_ADMIN = 0;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.notice_recycler_view);
        linearLayout = findViewById(R.id.notice_fail);
        fab = findViewById(R.id.notice_fab);
        fab_write = findViewById(R.id.notice_write_fab);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference = database.getReference("users");
        databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Member member = dataSnapshot.getValue(Member.class);
                    type = member.getType();
                    if(type == 0)
                        fab.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onResume() {
        GetNotice();
        super.onResume();
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

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_OK) {
                recyclerView.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                adapter = new NoticeAdapter(noticeList_reverse, context);
                recyclerView.setAdapter(adapter);
            }
            else if(msg.what == MESSAGE_FAIL) {
                recyclerView.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
            }
        }
    };

    public void GetNotice(){
        noticeList_reverse.clear();
        noticeList.clear();
        recyclerView.setVisibility(View.GONE);
        mThread mThread = new mThread();
        mThread.setDaemon(true);
        mThread.start();
    }

    public class mThread extends Thread{
        public void run(){
            DatabaseReference databaseReference = database.getReference("notice");
            databaseReference.orderByChild("date").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        Notice notice = dataSnapshot1.getValue(Notice.class);
                        noticeList.add(notice.toMap());
                    }

                    for(int count = noticeList.size()-1; count >= 0; count--)
                        noticeList_reverse.add(noticeList.get(count));


                    if(noticeList.size() > 0)
                        mHandler.sendEmptyMessage(MESSAGE_OK);
                    else
                        mHandler.sendEmptyMessage(MESSAGE_FAIL);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(context, R.string.fail_notice, Toast.LENGTH_SHORT).show();
                    mHandler.sendEmptyMessage(MESSAGE_FAIL);
                }
            });
        }
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.notice_fab:
                SetFabVisibie();
                break;
            case R.id.notice_write_fab:
                SetFabVisibie();
                Intent intent = new Intent(context, NoticeCreateActivity.class);
                intent.putExtra("count", adapter.getItemCount());
                startActivity(intent);
                break;
        }
    }

    public void SetFabVisibie(){
        if(isFabOpen){
            fab_write.setClickable(false);
            fab_write.setVisibility(View.GONE);
            isFabOpen = false;
        }
        else{
            fab_write.setClickable(true);
            fab_write.setVisibility(View.VISIBLE);
            isFabOpen = true;
        }
    }
}
