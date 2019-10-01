package teamprj.antrip.data.model;

import java.util.HashMap;

public class Language {
    HashMap<String, String> lang_code = new HashMap<>();
    String[][] lang_data = {{"한국어", "ko"}, {"일본어", "ja"}, {"중국어 간체", "zh-cn"}, {"영어", "en"}, {"자동 감지", "user"}};

    public Language() {
        for (String[] i : lang_data)
            lang_code.put(i[0], i[1]);
    }

    public String getLangCode(String langName) {
        return lang_code.get(langName);
    }
}
