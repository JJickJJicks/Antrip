package teamprj.antrip.data.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import teamprj.antrip.R;

public class RecommandCity {
    static ArrayList<String> cityList = new ArrayList<>(Arrays.asList("런던", "파리", "뉴욕", "암스테르담", "나폴리", "베니스", "로마", "베이징", "도쿄", "후쿠오카", "이스탄불", "블라디보스토크", "모스크바",
            "부다페스트", "비엔나", "아부다비", "두바이"));

    public static HashMap<String, Integer> getRandomCity(int size) { // 랜덤으로 5개 출력시켜줌
        ArrayList<Integer> history = new ArrayList<>();
        HashMap<String, Integer> result = new HashMap<>();
        while (result.size() < size) {
            Integer ran = new Integer((int) (Math.random() * cityList.size()));
            if (!history.contains(ran)) {
                history.add(ran); // 중복이 아니면 랜덤값 ArrayList추가
                String city = cityList.get(ran);
                result.put(city, setPicture(city));
            }
        }
        return result;
    }

    static int setPicture(String city) {
        switch (city) {
            case "런던":
                return R.drawable.london;
            case "파리":
                return R.drawable.paris;
            case "뉴욕":
                return R.drawable.newyork;
            case "암스테르담":
                return R.drawable.amsterdam;
            case "나폴리":
                return R.drawable.napoli;
            case "베니스":
                return R.drawable.venis;
            case "로마":
                return R.drawable.rome;
            case "베이징":
                return R.drawable.beijing;
            case "도쿄":
                return R.drawable.tokyo;
            case "후쿠오카":
                return R.drawable.fukuoka;
            case "이스탄불":
                return R.drawable.istanbul;
            case "블라디보스토크":
                return R.drawable.vladivostok;
            case "모스크바":
                return R.drawable.moscow;
            case "부다페스트":
                return R.drawable.budafest;
            case "비엔나":
                return R.drawable.vienna;
            case "아부다비":
                return R.drawable.abudabi;
            default:
                return R.drawable.dubai;
        }
    }
}
