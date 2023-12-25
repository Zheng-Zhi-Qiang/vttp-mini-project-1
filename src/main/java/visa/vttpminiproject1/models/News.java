package visa.vttpminiproject1.models;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import static visa.vttpminiproject1.Utils.*;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class News {
    private String title;
    private String url;
    private String summary;
    private String sentiment;
    private String img;
    private LocalDateTime datetime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSentiment() {
        return sentiment;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public static News toNews(String payload) {
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject data = reader.readObject();

        return toNews(data);
    }

    public static News toNews(JsonObject data) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
        News news = new News();
        news.setTitle(data.getString(ATTR_TITLE));
        news.setUrl(data.getString(ATTR_URL));
        news.setSentiment(data.getString(ATTR_SENTIMENT));
        news.setSummary(data.getString(ATTR_SUMMARY));
        news.setImg(data.getString(ATTR_NEWSIMG,
                "https://digitalfinger.id/wp-content/uploads/2019/12/no-image-available-icon-6.png"));
        news.setDatetime(LocalDateTime.parse(data.getString(ATTR_DATETIME), formatter));
        return news;
    }

    public static String toJsonString(News news) {
        String jsonString = Json.createObjectBuilder()
                .add(ATTR_TITLE, news.getTitle())
                .add(ATTR_URL, news.getUrl())
                .add(ATTR_NEWSIMG, news.getImg())
                .add(ATTR_SUMMARY, news.getSummary())
                .add(ATTR_SENTIMENT, news.getSentiment())
                .build().toString();

        return jsonString;
    }

}
