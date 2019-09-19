package teamprj.antrip;

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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;


public class TranslateActivity extends Activity {
    private static final int MESSAGE_OK = 0;
    private static String app_key = "9b26fe91f2c319fbd4a6db3a5ed7eb0e";
    StringBuffer stringBuffer = new StringBuffer();
    TextView translatedTextView;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_OK:
                    translatedTextView.setText(stringBuffer);
                    break;
            }
        }
    };
    private String origin_lang = "kr";
    private String target_lang = "en";
    private String from;
    private String after;

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

    public String translate(String text, String origin_lang, String target_lang) {
        try {
            String postParams = "src_lang=" + origin_lang + "&target_lang=" + target_lang + "&query=" + URLEncoder.encode(text, "UTF-8");
            String api_URL = "https://kapi.kakao.com/v1/translation/translate?" + postParams;
            URL url = new URL(api_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            String userCredentials = app_key;
            String basicAuth = "KakaoAK " + userCredentials;
            con.setRequestProperty("Authorization", basicAuth);
            con.setRequestMethod("GET");
            con.setRequestProperty("Contact-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("charset", "utf-8");
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200)
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            else
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String inputLine;
            StringBuffer res = new StringBuffer();
            while ((inputLine = br.readLine()) != null)
                res.append(inputLine);
            return res.toString();
        } catch (Exception e) {
            return e.toString();
        }

    }

    public class mThread extends Thread {
        public void run() {
            after = translate(from, origin_lang, target_lang);
            Log.d("temp", after);
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(after);
            JsonArray jsonArray = (JsonArray) jsonObject.get("translated_text");
            Iterator iterator = jsonArray.iterator();

            while (iterator.hasNext()) {
                JsonArray arr = (JsonArray) iterator.next();
                String temp = arr.get(0).toString();
                stringBuffer.append(temp.substring(1, temp.length() - 1));
                stringBuffer.append("\n");
            }
            stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());
            Log.d("temp", stringBuffer.toString());
            mHandler.sendEmptyMessage(MESSAGE_OK);
        }
    }
}