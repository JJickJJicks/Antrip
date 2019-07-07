package teamprj.antrip.ui.login;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import teamprj.antrip.R;
import teamprj.antrip.data.model.Member;

import static android.util.TypedValue.TYPE_NULL;

public class SignupActivity extends Activity {
    private FirebaseAuth mAuth;
    private  FirebaseDatabase database = FirebaseDatabase.getInstance();
    private  DatabaseReference myRef = database.getReference();
    private static final String TAG = "signUp";

    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };
    private EditText emailText, passwordText, pwCheckText, nameText, birthText;

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
        birthText = findViewById(R.id.signup_birthText);
        birthText.setInputType(TYPE_NULL);
        birthText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SignupActivity.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public void signUp(View v) {
        emailText = findViewById(R.id.signup_emailText);
        passwordText = findViewById(R.id.signup_pwText);
        pwCheckText = findViewById(R.id.signup_pwCheckText);
        nameText = findViewById(R.id.signup_nameText);
        birthText = findViewById(R.id.signup_birthText);

        if (checkError()) {
            register();
        }
    }

    public boolean checkError() {
        emailText = findViewById(R.id.signup_emailText);
        passwordText = findViewById(R.id.signup_pwText);
        pwCheckText = findViewById(R.id.signup_pwCheckText);
        nameText = findViewById(R.id.signup_nameText);
        birthText = findViewById(R.id.signup_birthText);

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
        if (birthText.getText().toString().equals("")) {
            birthText.setError(getText(R.string.wrongBirth));
            return false;
        } else
            birthText.setError(null);
        return true;
    }

    public void register() {
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            additionalInfo();
                            Toast.makeText(getApplicationContext(), "Authentication success.", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void additionalInfo() {
        Member member = new Member(emailText.getText().toString().trim(), nameText.getText().toString().trim(), birthText.getText().toString().trim(), 1);

        // Write a message to the database
        myRef.child("users").push().setValue(member);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        return event.getAction() != MotionEvent.ACTION_OUTSIDE;
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";    // 출력형식   2019-06-26
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        birthText.setText(sdf.format(myCalendar.getTime()));
    }

}