package teamprj.antrip.data.model;

import java.util.HashMap;

public class Language {
    HashMap<String, String> lang_code = new HashMap<>();
    String[][] lang_data = {{"한국어", "ko"}, {"일본어", "ja"}, {"중국어 간체", "zh-cn"}, {"중국어 번체", "zh-tw"}, {"힌디어", "hi"}, {"영어", "en"}, {"스페인어", "es"}, {"프랑스어", "fr"}, {"독일어", "de"}, {"포르투갈어", "pt"}, {"베트남어", "vi"}, {"인도네시아어", "id"}, {"페르시아어", "fa"}, {"아랍어", "ar"}, {"미얀마어", "mm"}, {"태국어", "th"}, {"러시아어", "ru"}, {"이탈리아어", "it"}, {"알수없음", "unk"}, {"자동 감지", "user"}};

    public Language() {
        for (String[] i : lang_data)
            lang_code.put(i[0], i[1]);
    }

    public String getLangCode(String langName) {
        return lang_code.get(langName);
    }
}
