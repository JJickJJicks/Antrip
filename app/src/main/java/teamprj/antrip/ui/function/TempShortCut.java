package teamprj.antrip.ui.function;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import teamprj.antrip.BuildConfig;

public class TempShortCut {
    private URL url = null;
    private String str, receiveMsg;
    private int[][] distance; // 소요시간 정보 저장
    private boolean[] visited; // 방문 여부 저장
    private int cities = 0; // 방문한 도시의 수

    private ArrayList<String> travelList, history;

    TempShortCut(ArrayList<String> travelList) {
        this.travelList = travelList;
        this.distance = new int[travelList.size()][travelList.size()];
        this.visited = new boolean[travelList.size()];
        history = new ArrayList<>();

        this.distance = getDistance();
    }

    public ArrayList<String> tsp() {
        if (travelList.size() == 0)
            return travelList;
        this.history.clear(); // history 초기화!
        Arrays.fill(visited, false); // 초기화!
        cities = 0; // 초기화!

        if (tsp(0))
            return this.history;
        else {
            Log.d("JsonCheck(fail)", history.toString());
            return travelList; // 불가능한 경우엔 미정렬 값 출력!
        }
    }


    private int[] indexSavedSort(int[] arr) {
        int[] indexArr = new int[arr.length];
        int[] sortedArr = Arrays.copyOf(arr, arr.length);
        Arrays.sort(sortedArr);

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                if (sortedArr[i] == arr[j]) {
                    indexArr[i] = j;
                    break;
                }
            }
        }

        return indexArr;
    }

    private boolean tsp(int currentPos) {
        visited[currentPos] = true;
        cities++;
        history.add(travelList.get(currentPos));

        if (cities > travelList.size() - 1) {
            return true;
        } else {
            int[] indexArr = indexSavedSort(distance[currentPos]); // 인덱스로 소트된 배열
            for (int i = 0; i < travelList.size(); i++) {
                int nextPos = indexArr[i];
                if (!visited[nextPos] && (distance[currentPos][nextPos] > 0)) // 방문을 한적 없고 해당 방향으로 진행 가능시
                    if (tsp(nextPos)) { // 해당방향으로 TSP를 함
                        Log.d("JsonProcess(Ok)", travelList.get(currentPos) + " - " + travelList.get(nextPos));
                        return true;
                    } else { // 진행중 문제가 생기면
                        history.remove(travelList.get(nextPos)); // 추가된 여행지를 지우고 다시 진행
                        visited[nextPos] = false; // 추가된 여행지의 여행 기록도 삭제
                        Log.d("JsonProcess(fail1)", travelList.get(currentPos) + " - " + travelList.get(nextPos));
                    }
                else { // 방문을 했거나 해당 방향 진행불가시 false
                    Log.d("JsonProcess(fail2)", travelList.get(currentPos) + " - " + travelList.get(nextPos));
                }
            }
            Log.d("JsonProcess(fail3)", travelList.get(currentPos));
            return false; // 기본적으론 실패한다고 가정!
        }
    }


    private int[][] getDistance() { // distance 배열을 출력
        int[][] result = new int[travelList.size()][travelList.size()];

        for (int i = 0; i < travelList.size(); i++) {
            for (int j = 0; j < travelList.size(); j++) {
                result[i][j] = parsejson(travelList.get(i), travelList.get(j));
            }
        }

        return result;
    }

    private int parsejson(String start, String end) {
        try {
            url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin=" + start + "&destination=" + end + "&mode=transit&key=" + BuildConfig.places_api_key);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
//                Log.i("receiveMsg : ", receiveMsg);

                reader.close();
            } else {
                Log.i("통신 결과", conn.getResponseCode() + "에러");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int time = -1;
        try {
            if (new JSONObject(receiveMsg).get("status").equals("OK")) {
                JSONArray rtarr = new JSONObject(receiveMsg).getJSONArray("routes");
                JSONObject route = (JSONObject) rtarr.get(0);
                JSONArray legsarr = (JSONArray) route.get("legs");
                JSONObject legs = (JSONObject) legsarr.get(0);
                JSONObject duration = (JSONObject) legs.get("duration");
                time = (int) duration.get("value");
            } else
                Log.d("jsonErr", receiveMsg);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("JsonResult", start + ", " + end + " : " + time);

        return time;
    }
}
