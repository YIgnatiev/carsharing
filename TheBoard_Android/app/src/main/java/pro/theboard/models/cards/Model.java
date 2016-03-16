package pro.theboard.models.cards;

import java.util.List;


/**
 * Created by Oleh Makhobey
 *    email : tajcig@ya.ru
 *    on 22/03/15.
 */
public class Model {
    private String hash;
    private String question;
    private String created_at;
    private String promoted;
    private String instant;
    private String ongoing;
    private String type;
    private String type_id;


    private int order;

    private List<ContentModel> content;

    public List<ContentModel> getContent(){
        return this.content;
    }

    public String getHash() {
        return hash;
    }

    public String getQuestion() {
        return question;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getPromoted() {
        return promoted;
    }

    public String getInstant() {
        return instant;
    }

    public String getOngoing() {
        return ongoing;
    }

    public String getType() {
        return type;
    }

    public String getType_id() {
        return type_id;
    }

    public int getOrder(){
        return order;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setPromoted(String promoted) {
        this.promoted = promoted;
    }

    public void setInstant(String instant) {
        this.instant = instant;
    }

    public void setOngoing(String ongoing) {
        this.ongoing = ongoing;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public void setContent(List<ContentModel> content) {
        this.content = content;
    }

    public void setOrder(int order){
        this.order = order;
    }
}

