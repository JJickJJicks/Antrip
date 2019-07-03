package teamprj.antrip.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import teamprj.antrip.MainActivity;
import teamprj.antrip.R;
import teamprj.antrip.data.AppSingleton;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Login";
    private static final String URL_FOR_LOGIN = "http://antrip.kro.kr/app/" + "login.php";
    ProgressDialog progressDialog;

    EditText emailEditText, passwordEditText;
    Button loginButton;
    CheckBox autoLogin;
    SharedPreferences setting;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.login_emailText);
        passwordEditText = findViewById(R.id.login_passwordText);
        loginButton = findViewById(R.id.login);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        // 로그인 버튼
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkError()) {
                    loginUser(emailEditText.getText().toString(), passwordEditText.getText().toString());
                }

            }
        });

        // 자동 로그인
        autoLogin = findViewById(R.id.login_autologin);

        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();

        if (setting.getBoolean("Auto_Login_enabled", false)) {
            emailEditText.setText(setting.getString("ID", ""));
            passwordEditText.setText(setting.getString("PW", ""));
            autoLogin.setChecked(true);
            if (checkError()) {
                loginUser(emailEditText.getText().toString(), passwordEditText.getText().toString());
            }
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

    private void loginUser(final String email, final String password) {
        // Tag used to cancel the request
        String cancel_req_tag = "login";
        progressDialog.setMessage("Logging you in...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response);
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("email", jObj.getJSONObject("user").getString("email"));
                        intent.putExtra("name", jObj.getJSONObject("user").getString("name"));
                        startActivity(intent);
                        finish();
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public boolean checkError() {
        emailEditText = findViewById(R.id.login_emailText);
        passwordEditText = findViewById(R.id.login_passwordText);

        TextInputLayout emailLayout = findViewById(R.id.emailLayout);
        TextInputLayout passwordLayout = findViewById(R.id.passwordLayout);

        if (emailEditText.getText().toString().equals("") || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches())
            emailLayout.setError(getText(R.string.wrongEmail));
        else if (passwordEditText.getText().toString().equals(""))
            passwordLayout.setError(getText(R.string.wrongPassword));
        else if (passwordEditText.getText().toString().length() < 5)
            passwordLayout.setError(getText(R.string.wrongPwLength));
        else
            return true;
        return false;
    }

    public void signupClick(View v) {
        //데이터 담아서 팝업(액티비티) 호출
        Intent intent = new Intent(this, SignupActivity.class);
        startActivityForResult(intent, 1);
    }

    public void searchClick(View v) {
        //데이터 담아서 팝업(액티비티) 호출
        Intent intent = new Intent(this, AccountSearchActivity.class);
        startActivityForResult(intent, 1);
    }

    public void temploginClick(View v) {
        final EditText emailEditText = findViewById(R.id.login_emailText);
        final EditText passwordEditText = findViewById(R.id.login_passwordText);
        emailEditText.setText("admin@test.com");
        passwordEditText.setText("123456");
    }
}
