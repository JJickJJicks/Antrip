package teamprj.antrip.data.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;

public class Notice {
    private String author;
    private String title;
    private String date;
    private String content;

    public Notice(String author, String title, String date, String content) {
        this.author = author;
        this.title = title;
        this.date = date;
        this.content = content;
    }

    public Notice(){

    }

    public String getAuthor() {
        return author;
    }

    public void setId(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Exclude
    public HashMap<String, String> toMap(){
        HashMap<String, String> result = new HashMap<>();
        result.put("author", author);
        result.put("title", title);
        result.put("date", date);
        result.put("content", content);

        return result;
    }
}
