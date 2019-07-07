package teamprj.antrip.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import teamprj.antrip.R;
import teamprj.antrip.data.model.Member;

import static android.util.TypedValue.TYPE_NULL;

public class ProfileCorrectActivity extends AppCompatActivity {
    private static final String TAG = "signUp";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("users");
    private String email;
    private EditText emailText, passwordText, pwCheckText, nameText, birthText;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_correct);

        emailText = findViewById(R.id.acc_correct_emailText);
        passwordText = findViewById(R.id.acc_correct_pwText);
        pwCheckText = findViewById(R.id.acc_correct_pwCheckText);
        nameText = findViewById(R.id.acc_correct_nameText);
        birthText = findViewById(R.id.acc_correct_birthText);

        birthText.setInputType(TYPE_NULL);
        birthText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ProfileCorrectActivity.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
        getUserData(email);
    }

    public void getUserData(String email) {
        myRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Member member = data.getValue(Member.class);
                            emailText = findViewById(R.id.acc_correct_emailText);
                            nameText = findViewById(R.id.acc_correct_nameText);
                            birthText = findViewById(R.id.acc_correct_birthText);
                            emailText.setText(member.getEmail());
                            nameText.setText(member.getName());
                            birthText.setText(member.getBirth());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("MyApp", "getUser:onCancelled", databaseError.toException());
                    }
                }
        );
    }

    public void update(View v) {
        emailText = findViewById(R.id.acc_correct_emailText);
        passwordText = findViewById(R.id.acc_correct_pwText);
        pwCheckText = findViewById(R.id.acc_correct_pwCheckText);
        nameText = findViewById(R.id.acc_correct_nameText);
        birthText = findViewById(R.id.acc_correct_birthText);

        if (checkError()) {
            register(emailText.getText().toString(), passwordText.getText().toString(), nameText.getText().toString(), birthText.getText().toString());
        }
    }

    public void register(final String email, final String password, final String name, final String birth) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Password updated");
                                    } else {
                                        Log.d(TAG, "Error password not updated");
                                    }
                                }
                            });
                        } else {
                            Log.d(TAG, "Error auth failed");
                        }
                    }
                });

        myRef.orderByChild("email").equalTo(email).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Member member = new Member(email, name, birth, 1);
                            Map<String, Object> postValues = member.toMap();
                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put(data.getKey(), postValues);
                            myRef.updateChildren(childUpdates);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    public boolean checkError() {
        emailText = findViewById(R.id.acc_correct_emailText);
        passwordText = findViewById(R.id.acc_correct_pwText);
        pwCheckText = findViewById(R.id.acc_correct_pwCheckText);
        nameText = findViewById(R.id.acc_correct_nameText);
        birthText = findViewById(R.id.acc_correct_birthText);

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

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";    // 출력형식   2019-06-26
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        birthText.setText(sdf.format(myCalendar.getTime()));
    }

}
