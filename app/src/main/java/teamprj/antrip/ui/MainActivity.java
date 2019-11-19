package teamprj.antrip.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import teamprj.antrip.BuildConfig;
import teamprj.antrip.R;
import teamprj.antrip.adapter.ImageFragmentAdapter;
import teamprj.antrip.data.model.LoginUserInfo;
import teamprj.antrip.data.model.RecommandCity;
import teamprj.antrip.fragment.ImageFragment;
import teamprj.antrip.ui.function.MyPlanActivity;
import teamprj.antrip.ui.function.NoticeActivity;
import teamprj.antrip.ui.function.TravelInfoActivity;
import teamprj.antrip.ui.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final int PROFILE_CHANGE = 1;
    private TextView nav_nameview, nav_emailview;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String userName = user.getEmail().replace(".", "_");
    private DatabaseReference myRef = database.getReference("users").child(userName);
    private static final int MESSAGE_OK = 1;
    NavigationView navigationView;
    View nav_header_view;
    CircleImageView nav_profileview;
    LoginUserInfo userInfo = new LoginUserInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.main_navView);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userInfo = dataSnapshot.getValue(LoginUserInfo.class);
                    Log.d("user", userInfo.toString());
                    mHandler.sendEmptyMessage(MESSAGE_OK);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Nav 이메일, 이름 로드
        nav_header_view = navigationView.getHeaderView(0);
        nav_nameview = nav_header_view.findViewById(R.id.nav_nameText);
        nav_emailview = nav_header_view.findViewById(R.id.nav_emailText);
        nav_profileview = nav_header_view.findViewById(R.id.nav_profileImg);

        // Firebase 로드
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        nav_emailview.setText(user.getEmail());
        nav_nameview.setText(user.getDisplayName());

        // 주소 자동 완성 정의
        Places.initialize(getApplicationContext(), BuildConfig.places_api_key);
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.main_autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteFragment.setTypeFilter(TypeFilter.CITIES);

        // 동작부
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Intent intent = new Intent(MainActivity.this, TravelInfoActivity.class);
                intent.putExtra("name", place.getName());
                startActivity(intent);
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: 장소 검색 에러 발생시 대응 방법
                Snackbar.make(findViewById(android.R.id.content), "An error occurred: " + status, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        ViewPager viewPager = findViewById(R.id.mainViewPager);
        ImageFragmentAdapter fragmentAdapter = new ImageFragmentAdapter(getSupportFragmentManager());
        // ViewPager와  FragmentAdapter 연결
        viewPager.setAdapter(fragmentAdapter);

        viewPager.setClipToPadding(false);
        int dpValue = 16;
        float d = getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d);
        viewPager.setPadding(margin, 0, margin, 0);
        viewPager.setPageMargin(margin/2);

        HashMap<String, Integer> cityMap = new HashMap<>(RecommandCity.getRandomCity(4));

        ArrayList<Integer> cityImageList = new ArrayList<>();
        ArrayList<String> cityNameList = new ArrayList<>(Arrays.asList(cityMap.keySet().toArray(new String[0])));
        for (String i : cityNameList) {
            cityImageList.add(cityMap.get(i));
        }

        for (int i = 0; i < cityImageList.size(); i++) {
            ImageFragment imageFragment = new ImageFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("imgRes", cityImageList.get(i));
            bundle.putString("text", cityNameList.get(i));
            imageFragment.setArguments(bundle);
            fragmentAdapter.addItem(imageFragment);
        }
        fragmentAdapter.notifyDataSetChanged();
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_OK:
                    switch (userInfo.getProfile()) {
                        case "img_sample1" : nav_profileview.setImageResource(R.drawable.img_sample1); break;
                        case "img_sample2" : nav_profileview.setImageResource(R.drawable.img_sample2); break;
                        case "img_sample3" : nav_profileview.setImageResource(R.drawable.img_sample3); break;
                    }
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_travel) {
            Intent intent = new Intent(this, MyPlanActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivityForResult(intent, PROFILE_CHANGE);
        } else if (id == R.id.nav_notice) {
            Intent intent = new Intent(this, NoticeActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_contact) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:ilovejava@kakao.com"));
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PROFILE_CHANGE && resultCode == RESULT_OK) {
            nav_emailview.setText(data.getStringExtra("email"));
            nav_nameview.setText(data.getStringExtra("name"));
        }
    }
}
