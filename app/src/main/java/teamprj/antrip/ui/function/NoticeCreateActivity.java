package teamprj.antrip.ui.function;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import teamprj.antrip.R;
import teamprj.antrip.data.model.Member;
import teamprj.antrip.data.model.Notice;

public class NoticeCreateActivity extends AppCompatActivity {
    private ArticleFragment articleFragment;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_create);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        count = intent.getIntExtra("count", 0);
        CreateFragment();
    }

    @Override
    protected void onResume() {
        Button btn = articleFragment.view.findViewById(R.id.register_btn);
        btn.setVisibility(View.VISIBLE);
        super.onResume();
    }

    public void CreateFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        articleFragment = ArticleFragment.newInstance();
        transaction.replace(R.id.notice_create_layout, articleFragment);
        transaction.commit();
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

    public void onRegisterArticleClick(View v) {
        //FirebaseDB에 공지사항 등록
        if(checkError() == true) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("notice");
            EditText edit_title = articleFragment.view.findViewById(R.id.input_main_title);
            EditText edit_content = articleFragment.view.findViewById(R.id.input_content);

            String id = Long.toString(count);
            String title = edit_title.getText().toString();
            String date = GetCurntTime();
            String content = edit_content.getText().toString();

            Notice notice = new Notice(id, title, date, content);
            databaseReference.child(id).setValue(notice.toMap());
            finish();
        }
    }

    public String GetCurntTime(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formatDate = simpleDateFormat.format(date);

        return formatDate;
    }

    public boolean checkError() {
        EditText edit_title = articleFragment.view.findViewById(R.id.input_main_title);
        EditText edit_content  =articleFragment.view.findViewById(R.id.input_content);

        if (edit_title.getText().toString().equals("")) {
            edit_title.setError(getText(R.string.notice_title_hint));
            return false;
        }
        else
            edit_title.setError(null);

        if (edit_content.getText().toString().equals("")) {
            edit_content.setError(getText(R.string.notice_content_hint));
            return false;
        }
        else
            edit_content.setError(null);

        return true;
    }
}
