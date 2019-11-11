package teamprj.antrip.ui.function;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import teamprj.antrip.R;
import teamprj.antrip.data.model.Language;


public class TranslateActivity extends AppCompatActivity {
    private static final int MESSAGE_OK = 0;
    private static String clientId = "XXar7pmbbGZwyqwCnKvq";//애플리케이션 클라이언트 아이디값";
    private static String clientSecret = "JSXbNKzVtN";//애플리케이션 클라이언트 시크릿값";
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_OK:
                    translatedText.setText(res); // 파파고에서는 String 한줄로 주는 관계로 그냥 String으로 변경
                    break;
            }
        }
    };
    private static String from, after, res, origin_lang, target_lang;
    private EditText inputText, translatedText;
    private Spinner origin_lan, target_lan;

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
            return res.substring(1, res.length() - 1); // Json이 아닌 String으로 언어 코드 제공 (https://developers.naver.com/docs/papago/papago-detectlangs-api-reference.md)
        } catch (Exception e) {
            return e.toString();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        origin_lan = findViewById(R.id.originLang);
        target_lan = findViewById(R.id.targetLang);

        final Language lang = new Language();

        inputText = findViewById(R.id.input_text);
        translatedText = findViewById(R.id.translated_text);

        Button translateButton = findViewById(R.id.translateButton);
        translateButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                from = inputText.getText().toString();
                origin_lang = lang.getLangCode(origin_lan.getSelectedItem().toString());
                target_lang = lang.getLangCode(target_lan.getSelectedItem().toString());

                try {
                    mThread mThread = new mThread();
                    mThread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private String translate(String text, String origin_lang, String target_lang) { // PAPAGO SMT (https://developers.naver.com/docs/labs/translator/)
        try {
            text = URLEncoder.encode(text, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/language/translate";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            // post request
            String postParams = "source=" + origin_lang + "&target=" + target_lang + "&text=" + text;
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
                return "error"; // 에러 발생 여부를 return 해줌
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
            if (origin_lang == "user") // 언어 코드가 아닐 경우 언어를 감지해서 적용함
                origin_lang = detectLang(from);
            after = translate(from, origin_lang, target_lang);
            if (!after.equals("error")) {
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
}