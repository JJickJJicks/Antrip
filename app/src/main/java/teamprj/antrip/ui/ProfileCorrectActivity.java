package teamprj.antrip.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import teamprj.antrip.R;
import teamprj.antrip.data.AppSingleton;

import static android.util.TypedValue.TYPE_NULL;

public class ProfileCorrectActivity extends AppCompatActivity {
    private static final String TAG = "signUp";
    private static final String URL_FOR_UPDATE = "http://antrip.kro.kr/app/" + "updateuser.php";
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
        setContentView(R.layout.activity_profile_correct);

        emailText = findViewById(R.id.acc_correct_emailText);
        passwordText = findViewById(R.id.acc_correct_pwText);
        pwCheckText = findViewById(R.id.acc_correct_pwCheckText);
        nameText = findViewById(R.id.acc_correct_nameText);
        birthText = findViewById(R.id.acc_correct_birthText);
        emailText.setText(getIntent().getExtras().getString("email"));
        birthText.setInputType(TYPE_NULL);
        birthText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ProfileCorrectActivity.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
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

    private void register(final String email, final String password, final String name, final String birth) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_UPDATE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        Toast.makeText(getApplicationContext(), R.string.success, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                params.put("name", name);
                params.put("birth", birth);
                params.put("type", "1");
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";    // 출력형식   2019-06-26
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        birthText.setText(sdf.format(myCalendar.getTime()));
    }
}
