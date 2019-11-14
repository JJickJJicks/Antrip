package teamprj.antrip.ui.login;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import teamprj.antrip.R;
import teamprj.antrip.data.model.Member;

public class SignupActivity extends Activity {
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private static final String TAG = "signUp";
    private static final int USER_TYPE = 1;
    private EditText emailText, passwordText, pwCheckText, nameText;
    private ImageView image1, image2, image3;
    private RadioGroup radioGroup;
    private String profile = "";

    private final String URL1 = "https://cdn.pixabay.com/photo/2017/01/20/00/30/maldives-1993704_960_720.jpg";
    private final String URL2 = "https://cdn.pixabay.com/photo/2014/12/15/17/16/pier-569314_960_720.jpg";
    private final String URL3 = "https://cdn.pixabay.com/photo/2018/10/01/11/45/landscape-3715977_960_720.jpg";

    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signup);

        mSharedPreferences = getSharedPreferences("profileInfo", MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        //Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        emailText = findViewById(R.id.signup_emailText);
        passwordText = findViewById(R.id.signup_pwText);
        pwCheckText = findViewById(R.id.signup_pwCheckText);
        nameText = findViewById(R.id.signup_nameText);
        radioGroup = findViewById(R.id.rg_radioGroup);

        image1 = findViewById(R.id.iv_image1);
        image2 = findViewById(R.id.iv_image2);
        image3 = findViewById(R.id.iv_image3);

        Glide.with(this).load(URL1).into(image1);
        Glide.with(this).load(URL2).into(image2);
        Glide.with(this).load(URL3).into(image3);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_btn1:
                        profile = URL1;
                        break;
                    case R.id.rb_btn2:
                        profile = URL2;
                        break;
                    case R.id.rb_btn3:
                        profile = URL3;
                        break;
                }
            }
        });
    }

    public void signUp(View v) {
        emailText = findViewById(R.id.signup_emailText);
        passwordText = findViewById(R.id.signup_pwText);
        pwCheckText = findViewById(R.id.signup_pwCheckText);
        nameText = findViewById(R.id.signup_nameText);

        if (checkError()) {
            register();
        }
    }

    public boolean checkError() {
        emailText = findViewById(R.id.signup_emailText);
        passwordText = findViewById(R.id.signup_pwText);
        pwCheckText = findViewById(R.id.signup_pwCheckText);
        nameText = findViewById(R.id.signup_nameText);

        if (emailText.getText().toString().equals("") || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailText.getText().toString()).matches()) {
            emailText.setError(getText(R.string.wrongEmail));
            return false;
        } else
            emailText.setError(null);
        if (passwordText.getText().toString().equals("") || !passwordText.getText().toString().equals(pwCheckText.getText().toString())) {
            passwordText.setError(getText(R.string.wrongPassword));
            return false;
        } else
            passwordText.setError(null);
        if (nameText.getText().toString().equals("")) {
            nameText.setError(getText(R.string.wrongName));
            return false;
        } else
            nameText.setError(null);
        if (profile == "") {
            Toast.makeText(this, "프로필을 선택 하세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void register() {
        final String email = emailText.getText().toString().trim();
        final String password = passwordText.getText().toString().trim();
        final String name = nameText.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                                RegisterDB();
                                            }
                                        }
                                    });
                            Toast.makeText(getApplicationContext(), "가입을 환영합니다.", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "가입에 실패하였습니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        return event.getAction() != MotionEvent.ACTION_OUTSIDE;
    }

    public void RegisterDB() {
        //유저가 회원가입 때 입력한 사항들 DB에 추가
        final String email = emailText.getText().toString().trim();
        final String name = nameText.getText().toString().trim();
        Member member = new Member(email, USER_TYPE, profile);

        final String userKey = email.replace(".", "_");
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userKey);
        databaseReference.setValue(member.toMap());
    }
}