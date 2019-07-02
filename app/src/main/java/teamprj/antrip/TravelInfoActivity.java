package teamprj.antrip;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;

public class TravelInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_info);

        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.toolbar_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");

        TextView maintext = findViewById(R.id.maintext);
        maintext.setText(name);

        //TODO: Titlebar에 들어갈 제목은?
        collapsingToolbar.setTitle(name + " 혹은 여행1");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
