package teamprj.antrip.ui.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import teamprj.antrip.R;
import teamprj.antrip.data.model.Member;
import teamprj.antrip.ui.ProfileActivity;
import teamprj.antrip.ui.function.NoticeActivity;
import teamprj.antrip.ui.login.LoginActivity;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference myRef;
    private SharedPreferences setting;
    private SharedPreferences.Editor editor;
    private RelativeLayout btnProfile;
    private ImageView ivProfileImg;
    private TextView usernameTextView, btnSignOut, btnDeleteData, btnShowNotice, btnContact, btnOpenSource, tvAppVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btnProfile = findViewById(R.id.btnProfile);
        ivProfileImg = findViewById(R.id.ivProfileImg);
        usernameTextView = findViewById(R.id.usernameTextView);
        btnSignOut = findViewById(R.id.btnSignOut);
        btnDeleteData = findViewById(R.id.btnDeleteData);
        btnShowNotice = findViewById(R.id.btnShowNotice);
        btnContact = findViewById(R.id.btnContact);
        btnOpenSource = findViewById(R.id.btnOpenSource);
        tvAppVersion = findViewById(R.id.tvAppVersion);

        btnProfile.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnDeleteData.setOnClickListener(this);
        btnShowNotice.setOnClickListener(this);
        btnContact.setOnClickListener(this);
        btnOpenSource.setOnClickListener(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        setProfile(user.getEmail(), user.getDisplayName());
        tvAppVersion.setText(getResources().getString(R.string.app_version_title) + " : " + getResources().getString(R.string.app_version));

    }

    private void setProfile(String email, String name) {
        usernameTextView.setText(name);

        final String userKey = email.replace(".", "_");
        myRef = FirebaseDatabase.getInstance().getReference("users").child(userKey);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Member member = dataSnapshot.getValue(Member.class);
                String img = member != null ? member.getProfile() : null;
                switch (img) {
                    default: // 오류 떠도 일단 1번 사진으로..
                        ivProfileImg.setImageResource(R.drawable.img_sample1);
                        break;
                    case "img_sample2":
                        ivProfileImg.setImageResource(R.drawable.img_sample2);
                        break;
                    case "img_sample3":
                        ivProfileImg.setImageResource(R.drawable.img_sample3);
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSignOut) {
            new AlertDialog.Builder(this)
                    .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까? 자동 로그인이 해제됩니다.")
                    .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // 자동 로그인 해제
                            setting = getApplicationContext().getSharedPreferences("setting", 0);
                            editor = setting.edit();

                            if (setting.getBoolean("Auto_Login_enabled", false)) {
                                editor.remove("ID");
                                editor.remove("PW");
                                editor.remove("Auto_Login_enabled");
                                editor.commit();
                            }

                            // 초기 로그인 화면으로 돌아감
                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    })
                    .show();
        } else if (view.getId() == R.id.btnDeleteData) {
            new AlertDialog.Builder(this)
                    .setTitle("회원 탈퇴").setMessage("탈퇴하시겠습니까? 재가입의 제한은 없습니다.")
                    .setPositiveButton("탈퇴", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            final String userKey = user.getEmail().replace(".", "_");
                            final DatabaseReference userDB = FirebaseDatabase.getInstance().getReference("users").child(userKey);
                            final DatabaseReference planDB = FirebaseDatabase.getInstance().getReference("plan").child(userKey);
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) { // Firebase Auth에서 정상적으로 user 정보가 삭제된다면
                                                userDB.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            planDB.removeValue();

                                                            // 자동 로그인 해제
                                                            setting = getApplicationContext().getSharedPreferences("setting", 0);
                                                            editor = setting.edit();

                                                            if (setting.getBoolean("Auto_Login_enabled", false)) {
                                                                editor.remove("ID");
                                                                editor.remove("PW");
                                                                editor.remove("Auto_Login_enabled");
                                                                editor.commit();
                                                            }

                                                            // 초기 화면 전환
                                                            Toast.makeText(getApplicationContext(), "탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();
                                                            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(i);
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    })
                    .show();
        } else if (view.getId() == R.id.btnProfile) {
            Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(i);
        } else if (view.getId() == R.id.btnShowNotice) {
            Intent i = new Intent(getApplicationContext(), NoticeActivity.class);
            startActivity(i);
        } else if (view.getId() == R.id.btnContact) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:ilovejava@kakao.com"));
            startActivity(intent);
        } else if (view.getId() == R.id.btnOpenSource) {
            Intent i = new Intent(getApplicationContext(), LicenseActivity.class);
            startActivity(i);
        }
    }
}