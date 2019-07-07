package teamprj.antrip.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import teamprj.antrip.R;
import teamprj.antrip.ui.MainActivity;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "Login";

    private EditText emailEditText, passwordEditText;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.login_emailText);
        passwordEditText = findViewById(R.id.login_passwordText);
        Button loginButton = findViewById(R.id.login_loginBtn);
        TextView signUpBtn = findViewById(R.id.login_signUpBtn);

        //Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // 로그인 버튼
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        // 자동 로그인
        CheckBox autoLogin = findViewById(R.id.login_autologin);
        SharedPreferences setting = getSharedPreferences("setting", 0);
        editor = setting.edit();

        if (setting.getBoolean("Auto_Login_enabled", false)) {
            emailEditText.setText(setting.getString("ID", ""));
            passwordEditText.setText(setting.getString("PW", ""));
            autoLogin.setChecked(true);

            login();
        }

        autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    String ID = emailEditText.getText().toString();
                    String PW = passwordEditText.getText().toString();

                    editor.putString("ID", ID);
                    editor.putString("PW", PW);
                    editor.putBoolean("Auto_Login_enabled", true);
                    editor.commit();
                } else {
                    editor.remove("ID");
                    editor.remove("PW");
                    editor.remove("Auto_Login_enabled");
                    editor.commit();
                }
            }
        });
    }

    public void login() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(getApplicationContext(), R.string.welcome, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void searchClick(View v) {
        //데이터 담아서 팝업(액티비티) 호출
        Intent intent = new Intent(this, AccountSearchActivity.class);
        startActivity(intent);
    }

    public void temploginClick(View v) {
        final EditText emailEditText = findViewById(R.id.login_emailText);
        final EditText passwordEditText = findViewById(R.id.login_passwordText);
        emailEditText.setText("admin@test.com");
        passwordEditText.setText("123456");
    }
}
