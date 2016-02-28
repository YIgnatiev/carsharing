package pro.theboard.models.cards;

/**
 * Created by Admin on 22.03.2015.
 */
public class ContentModel {
    private String content_hash ;
    private String title;
    private String priority;


    public ContentModel(String content_hash, String title) {
        this.content_hash = content_hash;
        this.title = title;
    }

    public ContentModel(){
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getContent_hash() {
        return content_hash;
    }

    public String getTitle() {
        return title;
    }

    public void setContent_hash(String content_hash) {
        this.content_hash = content_hash;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPriority(){
        return priority;
    }
}
