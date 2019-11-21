package teamprj.antrip.map;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import javax.net.ssl.HttpsURLConnection;

public class JSONParser extends AsyncTask<String, String, String> {

    protected String doInBackground(String... params) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
//                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

            }

            return buffer.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


//    @Override
//    protected void onPostExecute(String result) {
//        super.onPostExecute(result);
//        if (pd.isShowing()){
//            pd.dismiss();
//        }
//        txtJson.setText(result);
//    }

//    static InputStream is = null;
//    static JSONObject jObj = null;
//    static String json = "";
//    // constructor
//    public JSONParser() {
//    }
//
//    @Override
//    protected String doInBackground(String... url) {
//        return getJSONFromUrl(url[0]);
//    }
//
//    public String getJSONFromUrl(String url)  {
//
//        // Making HTTP request
//        try {
//
//            URL urlnew = new URL("https://www.google.com/");
//            HttpsURLConnection urlConnection = (HttpsURLConnection) urlnew.openConnection();
//            urlConnection.setRequestMethod("GET"); // or POST
//            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
//            urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
//            urlConnection.setDoInput(true);
//            urlConnection.setDoOutput(true);
//            is = new BufferedInputStream(urlConnection.getInputStream());
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    is, "iso-8859-1"), 8);
//            StringBuilder sb = new StringBuilder();
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//
//            json = sb.toString();
//            is.close();
//        } catch (Exception e) {
//            Log.e("Buffer Error", "Error converting result " + e.toString());
//        }
//        Log.d("JSON_RUTA", json);
//        return json;
//
//    }
}