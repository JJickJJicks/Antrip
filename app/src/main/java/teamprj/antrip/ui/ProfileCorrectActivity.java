package teamprj.antrip.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import cn.pedant.SweetAlert.SweetAlertDialog;
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
                    new SweetAlertDialog(getApplicationContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("완료되었습니다!")
                            .setConfirmText("확인")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    Intent intent = new Intent();
                                    intent.putExtra("email", emailText.getText().toString());
                                    intent.putExtra("name", nameText.getText().toString());
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            })
                            .show();
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
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("경고")
                    .setContentText(getResources().getString(R.string.wrongEmail))
                    .setConfirmText("확인")
                    .show();
            return false;
        } else
            emailText.setError(null);
        if (passwordText.getText().toString().equals("") || !passwordText.getText().toString().equals(pwCheckText.getText().toString())) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("경고")
                    .setContentText(getResources().getString(R.string.wrongPassword))
                    .setConfirmText("확인")
                    .show();
            return false;
        } else
            passwordText.setError(null);
        if (nameText.getText().toString().equals("")) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("경고")
                    .setContentText(getResources().getString(R.string.wrongName))
                    .setConfirmText("확인")
                    .show();
            return false;
        } else
            nameText.setError(null);
        return true;
    }

}
