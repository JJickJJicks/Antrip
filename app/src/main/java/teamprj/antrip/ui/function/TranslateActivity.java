package teamprj.antrip.ui.function;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import teamprj.antrip.R;


public class TranslateActivity extends Activity {
    private static final int MESSAGE_OK = 0;
    private static String clientId = "XXar7pmbbGZwyqwCnKvq";//애플리케이션 클라이언트 아이디값";
    private static String clientSecret = "JSXbNKzVtN";//애플리케이션 클라이언트 시크릿값";
    TextView translatedTextView;
    private static String from, after, res, origin_lang, target_lang;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_OK:
                    translatedTextView.setText(res);
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        final EditText inputText = findViewById(R.id.input_text);
        translatedTextView = findViewById(R.id.translated_text);

        Button translateButton = findViewById(R.id.translateButton);
        translateButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                from = inputText.getText().toString();

                try {
                    mThread mThread = new mThread();
                    mThread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private static String detectLang(String text) {
        try {
            String query = URLEncoder.encode(text, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/papago/detectLangs";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            // post request
            String postParams = "query=" + query;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(response.toString());
            res = jsonObject.get("langCode").toString();
            return res.substring(1, res.length() - 1);
        } catch (Exception e) {
            return e.toString();
        }
    }

    private String translate(String text, String origin_lang, String target_lang) {
        try {
            text = URLEncoder.encode(text, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/language/translate";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            // post request
            String postParams = "source=en&target=ko&text=" + text;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            return response.toString();
        } catch (Exception e) {
            return e.toString();
        }
    }

    public class mThread extends Thread {
        public void run() {
            origin_lang = detectLang(from);
            target_lang = "ko";
            after = translate(from, origin_lang, target_lang);
            Log.d("temp", after);
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(after);
            JsonObject message = (JsonObject) jsonObject.get("message");
            JsonObject result = (JsonObject) message.get("result");
            res = result.get("translatedText").toString();
            res = res.substring(1, res.length() - 1);
            Log.d("temp", res);
            mHandler.sendEmptyMessage(MESSAGE_OK);
        }
    }
}