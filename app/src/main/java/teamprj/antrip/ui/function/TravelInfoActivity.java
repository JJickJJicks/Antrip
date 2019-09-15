package teamprj.antrip.ui.function;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import teamprj.antrip.R;
import teamprj.antrip.TranslateActivity;

public class TravelInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_info);

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.travelInfo_toolbarLayout);

        Toolbar toolbar = findViewById(R.id.travelInfo_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");

        TextView maintext = findViewById(R.id.travelInfo_text);
        maintext.setText(name);

        //TODO: Titlebar에 들어갈 제목은?
        collapsingToolbar.setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 1번 버튼
        findViewById(R.id.travelInfo_tabBtn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TravelPlannerActivity.class);
                startActivity(intent);
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
