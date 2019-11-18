package teamprj.antrip.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import teamprj.antrip.R;
import teamprj.antrip.data.model.Member;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "signUp";
    private static final int USER_TYPE = 1;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private EditText emailText, passwordText, pwCheckText, nameText;
    private ImageView image1, image2, image3;
    private RadioGroup radioGroup;
    private String profile = "";
    private CircularProgressButton btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();

        mSharedPreferences = getSharedPreferences("profileInfo", MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        //Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        emailText = findViewById(R.id.editTextEmail);
        passwordText = findViewById(R.id.editTextPassword);
        pwCheckText = findViewById(R.id.editTextPasswordCheck);
        nameText = findViewById(R.id.editTextName);
        radioGroup = findViewById(R.id.rg_radioGroup);
        btnSignUp = findViewById(R.id.cirRegisterButton);

        image1 = findViewById(R.id.iv_image1);
        image2 = findViewById(R.id.iv_image2);
        image3 = findViewById(R.id.iv_image3);

        image1.setImageResource(R.drawable.img_sample1);
        image2.setImageResource(R.drawable.img_sample2);
        image3.setImageResource(R.drawable.img_sample3);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_btn1:
                        profile = "img_sample1";
                        break;
                    case R.id.rb_btn2:
                        profile = "img_sample2";
                        break;
                    case R.id.rb_btn3:
                        profile = "img_sample3";
                        break;
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkError()) {
                    register();
                }
            }
        });
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    public void onLoginClick(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
    }


    public boolean checkError() {
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
        if (profile.equals("")) {
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
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
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
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "가입에 실패하였습니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void RegisterDB() {
        //유저가 회원가입 때 입력한 사항들 DB에 추가
        final String email = emailText.getText().toString().trim();
        Member member = new Member(email, USER_TYPE, profile);

        final String userKey = email.replace(".", "_");
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userKey);
        databaseReference.setValue(member.toMap());
    }
}

