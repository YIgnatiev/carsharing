package pro.theboard.models.cards;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 7/4/15.
 */
public class AnswerData {
    private String card_hash;
    private String data;
    private String content_hash;


    public AnswerData (String card_hash) {
        this.card_hash = card_hash;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setContent_hash(String content_hash) {
        this.content_hash = content_hash;
    }

    public String getCard_hash() {
        return card_hash;
    }

    public String getData() {
        return data;
    }

    public String getContent_hash() {
        return content_hash;
    }
}
