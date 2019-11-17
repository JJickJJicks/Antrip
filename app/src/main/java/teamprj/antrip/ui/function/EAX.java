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
import java.util.Random;

import teamprj.antrip.BuildConfig;

public class EAX {
    ArrayList<String> list;
    ArrayList<Long> pre_cost = new ArrayList<>(), next_cost = new ArrayList<>(), cost_results = new ArrayList<>();
    ArrayList<ArrayList<Integer>> subTour = new ArrayList<>(), oldGene = new ArrayList<>(), nextGene = new ArrayList<>(), results = new ArrayList<>();
    ArrayList<ArrayList<ArrayList<Integer>>> divided = new ArrayList<>();
    ArrayList<Integer> order = new ArrayList<>(), orderA = new ArrayList<>(), orderB = new ArrayList<>(), bestA = new ArrayList<>(),
            bestB = new ArrayList<>();
    static int TotalCities, times = 2000;
    long bestCostA, bestCostB;

    private URL url = null;
    private String str, receiveMsg;

    EAX(ArrayList<String> list) {
        TotalCities = list.size();
        this.list = (ArrayList<String>) list.clone();

        for (int i = 0; i < TotalCities; i++) {
            order.add(i);
        }
        order.add(0);
    }

    private long parseInfo(String json) {
        int time = -1;
        try {
            if (new JSONObject(json).get("status").equals("OK")) {
                JSONArray rtarr = new JSONObject(json).getJSONArray("routes");
                JSONObject route = (JSONObject) rtarr.get(0);
                JSONArray legsarr = (JSONArray) route.get("legs");
                JSONObject legs = (JSONObject) legsarr.get(0);
                JSONObject duration = (JSONObject) legs.get("duration");
                time = (int) duration.get("value");
            } else
                Log.d("jsonErr", json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("jsonTime", String.valueOf(time));

        return Long.valueOf(time);
    }

    private String parsejson(String start, String end) {
        Log.d("JsonCall", start + ", " + end);
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

        return receiveMsg;
    }

    private long calDistance(int a, int b) {
        return parseInfo(parsejson(list.get(a), list.get(b)));
    }

    private long calCost(ArrayList<Integer> arr) {
        long sum = 0;
        for (int i = 0; i < TotalCities; i++) {
            sum += parseInfo(parsejson(list.get(arr.get(i)), list.get(arr.get(i + 1))));
        }
        return sum;
    }

    private void swap(int i, int a, int b) {
        int temp;
        if (i == 0) {
            temp = orderA.get(a);
            orderA.set(a, orderA.get(b));
            orderA.set(b, temp);
        } else {
            temp = orderB.get(a);
            orderB.set(a, orderB.get(b));
            orderB.set(b, temp);
        }
    }

    private void shuffle() {
        orderA = (ArrayList<Integer>) order.clone();
        orderB = (ArrayList<Integer>) order.clone();
        bestCostA = calCost(order);
        bestCostB = bestCostA;
        Random rd = new Random();
        int x, y;
        long cost = 0;
        bestCostA = -1;
        bestCostB = -1;
        for (int i = 0; i < times; i++) {
            x = rd.nextInt(orderA.size() - 2) + 1;
            y = rd.nextInt(orderA.size() - 2) + 1;
            swap(0, x, y);
            cost = calCost(orderA);
            if (cost < bestCostA || bestCostA == -1) {
                bestCostA = cost;
                bestA = (ArrayList<Integer>) orderA.clone();
            }
        }
        for (int i = 0; i < times / 2; i++) {
            x = rd.nextInt(orderB.size() - 2) + 1;
            y = rd.nextInt(orderB.size() - 2) + 1;
            swap(1, x, y);
            cost = calCost(orderB);
            if (cost < bestCostB || bestCostB == -1.0) {
                bestCostB = cost;
                bestB = (ArrayList<Integer>) orderB.clone();
            }
        }
        pre_cost.add(bestCostA);
        pre_cost.add(bestCostB);
        oldGene.add((ArrayList<Integer>) bestA.clone());
        oldGene.add((ArrayList<Integer>) bestB.clone());
    }

    boolean check_subTour(Integer Va, Integer Vb) {
        for (int i = 0; i < subTour.size(); i++)
            for (int j = 0; j < subTour.get(i).size() - 1; j++)
                if (subTour.get(i).get(j).equals(Va))
                    if (subTour.get(i).get(j + 1).equals(Vb))
                        return true;
        return false;
    }

    private boolean mergeCycles() {
        subTour.clear();
        Integer n = bestA.size(), first, nextV, preV, tempA, tempB, count;
        ArrayList<Integer> cycle = new ArrayList<>();
        for (int i = 0; i < n - 1; i++) {
            cycle.clear();

            first = bestA.get(i);
            nextV = bestA.get(i + 1);
            preV = first;

            cycle.add(first);
            cycle.add(nextV);

            tempA = i;
            tempB = 0;
            count = 0;

            while (true) {
                if (first == nextV) break;
                if (count != 0) {
                    for (int j = 0; j < n - 1; j++)
                        if (bestA.get(j) == nextV)
                            tempA = j;
                    preV = nextV;

                    if (tempA == n - 2)
                        nextV = bestA.get(0);
                    else
                        nextV = bestA.get(tempA + 1);

                    if (cycle.contains(nextV) && nextV != first)
                        break;
                    cycle.add(nextV);
                }
                if (first == nextV)
                    break;
                if (check_subTour(preV, nextV))
                    break;
                for (int j = 0; j < n - 1; j++)
                    if (nextV == bestB.get(j))
                        tempB = j;

                if (tempB == 0) {
                    if (bestB.get(n - 2) == preV || bestB.get(tempB + 1) == preV)
                        break;
                    if (tempA + 2 == n) {
                        if (bestA.get(1) == bestB.get(n - 2))
                            break;
                    } else {
                        if (bestA.get(tempA + 2) == bestB.get(n - 2))
                            break;
                        tempB = n - 2;
                    }
                    tempB = n - 2;
                } else {
                    if (bestB.get(tempB - 1) == preV || bestB.get(tempB + 1) == preV)
                        break;
                    if (tempA + 2 == n) {
                        if (bestA.get(1) == bestB.get(tempB - 1))
                            break;
                    } else {
                        if (bestA.get(tempA + 2) == bestB.get(tempB - 1))
                            break;
                    }
                    tempB -= 1;
                }
                nextV = bestB.get(tempB);

                if (cycle.contains(nextV) && nextV != first)
                    break;
                if (check_subTour(cycle.get(cycle.size() - 1), nextV))
                    break;
                cycle.add(nextV);
                count += 1;
            }
            if (cycle.get(0) != cycle.get(cycle.size() - 1) || cycle.size() != 5)
                continue;
            subTour.add((ArrayList<Integer>) cycle.clone());
        }
        for (int i = 0; i < subTour.size(); i++) {
            n = subTour.get(i).size();
            if (n != 5)
                subTour.remove(i);
        }
        return subTour.size() != 0;
    }

    private void select_subTour() {
        divided.clear();
        ArrayList<ArrayList<Integer>> arr = (ArrayList<ArrayList<Integer>>) subTour.clone();
        ArrayList<ArrayList<Integer>> temp = new ArrayList<>();
        int h;
        while (arr.size() != 0) {
            temp.clear();
            temp.add(arr.get(0));
            arr.remove(0);
            for (int i = 0; i < arr.size(); i++) {
                h = 1;
                for (int j = 0; j < arr.get(i).size() - 1; j++) {
                    for (int k = 0; k < temp.size(); k++)
                        for (int l = 0; l < temp.get(k).size() - 1; l++)
                            if (arr.get(i).get(j) == temp.get(k).get(l)) {
                                h = 0;
                                break;
                            }
                    if (h == 0) break;
                }
                if (h == 1) {
                    temp.add(arr.get(i));
                    arr.remove(i);
                }
            }
            divided.add((ArrayList<ArrayList<Integer>>) temp.clone());
        }
    }

    private ArrayList<ArrayList<Integer>> intermediate_solution(ArrayList<Integer> order, ArrayList<Integer> arr, int type) {
        ArrayList<ArrayList<Integer>> inter_sol = new ArrayList<>();
        ArrayList<Integer> temp_sol = new ArrayList<>();
        int temp = -1, nextV;
        if (type == 0) {
            for (int i = 2; i < arr.size(); i += 2) {
                temp_sol.clear();
                temp_sol.add(arr.get(i));
                temp_sol.add(arr.get(i - 1));
                for (int j = 0; j < order.size() - 1; j++)
                    if (order.get(j) == temp_sol.get(1))
                        temp = j;
                nextV = temp_sol.get(1);
                while (nextV != temp_sol.get(0)) {
                    temp = (temp + 1) % (order.size() - 1);
                    nextV = order.get(temp);
                    temp_sol.add(nextV);
                }
                inter_sol.add((ArrayList<Integer>) temp_sol.clone());
            }
        } else if (type == 1) {
            for (int i = 0; i < arr.size() - 1; i += 2) {
                temp_sol.clear();
                temp_sol.add(arr.get(i));
                temp_sol.add(arr.get(i + 1));
                for (int j = 0; j < order.size() - 1; j++)
                    if (order.get(j) == temp_sol.get(1))
                        temp = j;
                nextV = temp_sol.get(1);
                while (nextV != temp_sol.get(0)) {
                    temp = (temp + 1) % (order.size() - 1);
                    nextV = order.get(temp);
                    temp_sol.add(nextV);
                }
                inter_sol.add((ArrayList<Integer>) temp_sol.clone());
            }
        }
        return inter_sol;
    }

    private ArrayList<Integer> new_connection(ArrayList<ArrayList<Integer>> arr) {
        int a = 0, b = 0, c = 0, d = 0, e = -1, temp = -1;
        double temp_cost = 0.0, min_cost = -1.0, x, y;

        long[][] val = new long[TotalCities][TotalCities];

        ArrayList<Integer> list = new ArrayList<>();
        ArrayList<Integer> result = new ArrayList<>();

        for (int i = 0; i < arr.get(0).size() - 1; i++)
            for (int j = 0; j < arr.get(1).size() - 1; j++)
                val[i][j] = calDistance(arr.get(0).get(i), arr.get(1).get(j));

        for (int i = 0; i < arr.get(0).size() - 1; i++)
            for (int j = 0; j < arr.get(1).size() - 1; j++) {
                x = val[i][j] + val[i + 1][j + 1];
                y = val[i][j + 1] + val[i + 1][j];
                if (x > y) temp_cost = y;
                else temp_cost = x;
                if (temp_cost < min_cost || min_cost < 0) {
                    a = i;
                    b = j;
                    min_cost = temp_cost;
                }
            }

        if (min_cost == val[a][b] + val[a + 1][b + 1]) {
            c = a;
            d = b;
            e = 0;
        } else if (min_cost == val[a][b + 1] + val[a + 1][b]) {
            c = a;
            d = b;
            e = 1;
        }

        if (e == 0) {
            for (int i = 0; i < c + 1; i++)
                list.add(arr.get(0).get(i));
            for (int i = 0; i < arr.get(1).size() - 1; i++) {
                temp = d - i;
                if (temp < 0) temp = arr.get(1).size() + temp - 1;
                list.add(arr.get(1).get(temp));
            }
            for (int i = c + 1; i < arr.get(0).size(); i++)
                list.add(arr.get(0).get(i));
        } else if (e == 1) {
            for (int i = 0; i < c + 1; i++)
                list.add(arr.get(0).get(i));
            for (int i = 0; i < arr.get(1).size() - 1; i++) {
                list.add(arr.get(1).get((i + d + 1) % (arr.get(1).size() - 1)));
            }
            for (int i = c + 1; i < arr.get(0).size(); i++)
                list.add(arr.get(0).get(i));
        }

        for (int i = 0; i < list.size() - 1; i++)
            if (list.get(i) == 0) {
                temp = i;
                break;
            }

        for (int i = 0; i < list.size(); i++) {
            result.add(list.get((i + temp) % (list.size() - 1)));
        }

        return result;
    }

    private void next_generation() {
        long temp_costA = 0, temp_costB = 0;
        ArrayList<Integer> tempA = new ArrayList<>();
        ArrayList<Integer> tempB = new ArrayList<>();
        ArrayList<ArrayList<Integer>> interA = new ArrayList<>();
        ArrayList<ArrayList<Integer>> interB = new ArrayList<>();
        ArrayList<ArrayList<Integer>> old = new ArrayList<>();
        for (int i = 0; i < divided.size(); i++) {
            tempA = (ArrayList<Integer>) bestA.clone();
            tempB = (ArrayList<Integer>) bestB.clone();
            interA.clear();
            interB.clear();
            for (int j = 0; j < divided.get(i).size(); j++) {
                interA = intermediate_solution(tempA, divided.get(i).get(j), 0);
                interB = intermediate_solution(tempB, divided.get(i).get(j), 1);
                tempA = new_connection(interA);
                tempB = new_connection(interB);
            }
            temp_costA = calCost(tempA);
            temp_costB = calCost(tempB);
            int x = -1, y = -1;
            for (int k = 0; k < pre_cost.size(); k++) {
                if (tempA.equals(oldGene.get(k))) x++;
                if (temp_costA >= pre_cost.get(k)) x++;
                if (tempB.equals(oldGene.get(k))) y++;
                if (temp_costB >= pre_cost.get(k)) y++;
            }
            if (x == -1) {
                nextGene.add((ArrayList<Integer>) tempA.clone());
                next_cost.add(temp_costA);
            }

            if (y == -1) {
                nextGene.add((ArrayList<Integer>) tempB.clone());
                next_cost.add(temp_costB);
            }
        }
    }

    private void next(int x) {
        for (int i = 0; i < x; i++) {
            oldGene.add((ArrayList<Integer>) nextGene.get(0).clone());
            pre_cost.add(next_cost.get(0));
            nextGene.remove(0);
            next_cost.remove(0);
        }
    }

    void TRY() {
        int ge = 1, x = 0;
        double min = 0.0;
        while (true) {
            if (ge == 1) {
                while (true) {
                    oldGene.clear();
                    pre_cost.clear();
                    shuffle();
                    if (mergeCycles())
                        break;
                }
                select_subTour();
                next_generation();
                ge++;
                x = nextGene.size();
            } else {
                for (int i = 0; i < x; i++)
                    for (int j = 0; j < x; j++) {
                        if (i == j) continue;
                        if (nextGene.get(i).equals(nextGene.get(j))) continue;
                        orderA = (ArrayList<Integer>) nextGene.get(i).clone();
                        orderB = (ArrayList<Integer>) nextGene.get(j).clone();
                        if (mergeCycles()) {
                            select_subTour();
                            next_generation();
                        }
                    }
                next(x);
                x = nextGene.size();
            }
            if (x < 2) break;
        }
        next(nextGene.size());
        for (int i = 0; i < pre_cost.size(); i++) {
            if (min == 0.0 || pre_cost.get(i) < min) {
                min = pre_cost.get(i);
                x = i;
            }
        }
        cost_results.add(pre_cost.get(x));
        results.add((ArrayList<Integer>) oldGene.get(x).clone());
    }

    ArrayList<Integer> run() {
        int x = 0;
        double min = 0.0;

        for (int i = 0; i < 100; i++) {
            pre_cost.clear();
            next_cost.clear();
            oldGene.clear();
            nextGene.clear();
            TRY();
        }

        for (int i = 0; i < cost_results.size(); i++)
            if (min == 0.0 || cost_results.get(i) < min) {
                min = cost_results.get(i);
                x = i;
            }
/*
        double[][] result = new double[TotalCities][2];
        for(int i=0;i<TotalCities;i++) {
            result[i][0] = cities[results.get(x).get(i)][0];
            result[i][1] = cities[results.get(x).get(i)][1];
        }
 */
        return results.get(x);
    }
}