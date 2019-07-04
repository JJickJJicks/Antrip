package teamprj.antrip.ui.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import teamprj.antrip.R;
import teamprj.antrip.data.AppSingleton;

public class AccountSearchActivity extends Activity {

    private static final String TAG = "Login";
    private static final String URL_FOR_FINDUSER = "http://antrip.kro.kr/app/" + "checkemail.php";
    String randNum, user_email;
    ProgressDialog progressDialog;

    EditText emailEditText, codeEditText, pwEditText, pwCheckEditText;
    Button sendBtn, codeBtn;
    Boolean state = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_account_search);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        emailEditText = findViewById(R.id.acc_search_emailText);
        sendBtn = findViewById(R.id.acc_search_sendBtn);
        codeEditText = findViewById(R.id.acc_search_codeText);
        codeBtn = findViewById(R.id.acc_search_codeBtn);
        pwEditText = findViewById(R.id.acc_search_pwText);
        pwCheckEditText = findViewById(R.id.acc_search_pwCheckText);

        // 로그인 버튼
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmail()) {
                    checkUser(emailEditText.getText().toString());
                }

            }
        });

        // 로그인 버튼
        codeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeEditText = findViewById(R.id.acc_search_codeText);
                String code = codeEditText.getText().toString();
                if (code.equals(randNum)) {
                    Toast.makeText(getApplicationContext(), "성공 " + randNum, Toast.LENGTH_LONG).show();
                    findViewById(R.id.acc_search_pwText).setEnabled(true);
                    findViewById(R.id.acc_search_pwCheckText).setEnabled(true);
                } else
                    Toast.makeText(getApplicationContext(), "실패 " + randNum, Toast.LENGTH_LONG).show();

            }
        });
    }

    private void checkUser(final String email) {
        // Tag used to cancel the request
        String cancel_req_tag = "login";
        progressDialog.setMessage("Logging you in...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_FINDUSER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response);
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        user_email = email;
                        int rand = (int) (Math.random() * 1000000);
                        randNum = String.format("%06d", rand);
                        findViewById(R.id.acc_search_codeText).setEnabled(true);
                        findViewById(R.id.acc_search_codeBtn).setEnabled(true);
                        Toast.makeText(getApplicationContext(), randNum, Toast.LENGTH_LONG).show(); // 원래 이메일로 보내야 되지만 임시로 Toast로 출력
                        //TODO: 인증 메일 보내기
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
                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    public void findUser(View v) {
        pwEditText = findViewById(R.id.acc_search_pwText);
        pwCheckEditText = findViewById(R.id.acc_search_pwCheckText);

        if (checkPassword()) {
            Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_LONG).show();

            //TODO: 비밀번호 변경
//            update(user_email, pwEditText.getText().toString());
        }

    }

    public boolean checkPassword() {
        pwEditText = findViewById(R.id.acc_search_pwText);
        pwCheckEditText = findViewById(R.id.acc_search_pwCheckText);

        if (pwEditText.getText().toString().equals("") || !pwEditText.getText().toString().equals(pwCheckEditText.getText().toString())) {
            pwEditText.setError(getText(R.string.wrongPassword));
            return false;
        }
        pwEditText.setError(null);
        return true;
    }

    public boolean checkEmail() {
        emailEditText = findViewById(R.id.acc_search_emailText);

        TextInputLayout emailLayout = findViewById(R.id.login_emailLayout);

        if (emailEditText.getText().toString().equals("") || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
            emailEditText.setError(getText(R.string.wrongEmail));
            return false;
        }
        emailEditText.setError(null);
        return true;
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    //확인 버튼 클릭
    public void mOnClose(View v) {
        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        return event.getAction() != MotionEvent.ACTION_OUTSIDE;
    }
}