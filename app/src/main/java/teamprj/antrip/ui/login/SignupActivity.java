package teamprj.antrip.ui.login;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import teamprj.antrip.R;

public class SignupActivity extends Activity {
    private FirebaseAuth mAuth;
    private static final String TAG = "signUp";
    private EditText emailText, passwordText, pwCheckText, nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signup);

        //Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        emailText = findViewById(R.id.signup_emailText);
        passwordText = findViewById(R.id.signup_pwText);
        pwCheckText = findViewById(R.id.signup_pwCheckText);
        nameText = findViewById(R.id.signup_nameText);
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
                                            }
                                        }
                                    });
                            Toast.makeText(getApplicationContext(), "Authentication success.", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        return event.getAction() != MotionEvent.ACTION_OUTSIDE;
    }
}