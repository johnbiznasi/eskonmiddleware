package conf.middleware.models;

public class CoversationTopics {
    String title;
    String id;

    public CoversationTopics(String title, String id) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }
}
