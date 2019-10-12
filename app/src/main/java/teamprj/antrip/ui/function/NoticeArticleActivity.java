package teamprj.antrip.ui.function;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import teamprj.antrip.R;
import teamprj.antrip.data.model.Member;

public class NoticeArticleActivity extends AppCompatActivity {
    Context context = this;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private boolean isFabOpen = false;
    FloatingActionButton fab;
    FloatingActionButton fab_edit;
    String main_title, sub_title, content, notice_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_article);

        fab = findViewById(R.id.noticeArticle_fab);
        fab_edit = findViewById(R.id.noticeArticle_edit_fab);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference = database.getReference("users");
        databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Member member = dataSnapshot.getValue(Member.class);
                    if(member.getType() == 0)
                        fab.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, R.string.fail_notice, Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = getIntent();
        notice_id = intent.getStringExtra("notice_id");
        main_title = intent.getStringExtra("main_title");
        sub_title = intent.getStringExtra("sub_title");
        content = intent.getStringExtra("content");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView temp = findViewById(R.id.main_title);
        temp.setText(main_title);
        temp = findViewById(R.id.sub_title);
        temp.setText(sub_title);
        temp = findViewById(R.id.content);
        temp.setText(content);
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

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.noticeArticle_fab:
                SetFabVisibie();
                break;
            case R.id.noticeArticle_edit_fab:
                SetFabVisibie();
                Intent intent = new Intent(context, NoticeEditActivity.class);
                intent.putExtra("notice_id", notice_id);
                intent.putExtra("title", main_title);
                intent.putExtra("content", content);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void SetFabVisibie(){
        if(isFabOpen){
            fab_edit.setClickable(false);
            fab_edit.setVisibility(View.GONE);
            isFabOpen = false;
        }
        else{
            fab_edit.setClickable(true);
            fab_edit.setVisibility(View.VISIBLE);
            isFabOpen = true;
        }
    }
}
