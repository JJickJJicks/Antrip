package teamprj.antrip.ui.function;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import teamprj.antrip.R;

public class NoticeActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    //private FirebaseDatabase database = FirebaseDatabase.getInstance();
    //private DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        recyclerView = findViewById(R.id.notice_recycler_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String[] main_title = {"[공지사항] 임시적인 메인 타이틀1", "[공지사항] 임시적인 메인 타이틀2", "[공지사항] 임시적인 메인 타이틀3", "[공지사항] 임시적인 메인 타이틀4"};
        String[] sub_title = {"2019.12.12", "2019.12.12", "2019.12.12", "2019.12.12"};

        adapter = new NoticeAdapter(main_title, sub_title, this);
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
