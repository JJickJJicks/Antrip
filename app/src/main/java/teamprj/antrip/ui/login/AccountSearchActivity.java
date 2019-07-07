package teamprj.antrip.ui.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;

import teamprj.antrip.R;

public class AccountSearchActivity extends Activity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    ProgressDialog progressDialog;

    EditText emailEditText;
    Button changePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_account_search);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        emailEditText = findViewById(R.id.acc_search_emailText);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                }
            }
        };

        changePassword = findViewById(R.id.acc_search_confirmBtn);


        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PassResetViaEmail();
            }
        });
    }

    private void PassResetViaEmail(){
        String email = emailEditText.getText().toString().trim();
        checkEmail();
        if(mAuth != null) {
            Toast.makeText(getApplicationContext(), "Recovery Email has been  sent to " + email, Toast.LENGTH_LONG).show();
            mAuth.sendPasswordResetEmail(email);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_LONG).show();
        }
    }

    public boolean checkEmail() {
        emailEditText = findViewById(R.id.acc_search_emailText);

        if (emailEditText.getText().toString().equals("") || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
            emailEditText.setError(getText(R.string.wrongEmail));
            return false;
        }
        emailEditText.setError(null);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        return event.getAction() != MotionEvent.ACTION_OUTSIDE;
    }
}