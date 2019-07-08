package teamprj.antrip.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import teamprj.antrip.R;

public class ProfileCorrectActivity extends AppCompatActivity {
    private static final String TAG = "signUp";
    private EditText emailText, passwordText, pwCheckText, nameText;
    private Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_correct);

        emailText = findViewById(R.id.acc_correct_emailText);
        passwordText = findViewById(R.id.acc_correct_pwText);
        pwCheckText = findViewById(R.id.acc_correct_pwCheckText);
        nameText = findViewById(R.id.acc_correct_nameText);
        updateBtn = findViewById(R.id.acc_correct_confirmBtn);

        getUserData();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkError()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(nameText.getText().toString().trim())
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User profile updated.");
                                    }
                                }
                            });


                    user.updatePassword(passwordText.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User password updated.");
                                    }
                                }
                            });
                    Toast.makeText(getApplicationContext(), R.string.success, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void getUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        emailText.setText(user.getEmail());
        nameText.setText(user.getDisplayName());
    }

    public boolean checkError() {
        emailText = findViewById(R.id.acc_correct_emailText);
        passwordText = findViewById(R.id.acc_correct_pwText);
        pwCheckText = findViewById(R.id.acc_correct_pwCheckText);
        nameText = findViewById(R.id.acc_correct_nameText);

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

}
