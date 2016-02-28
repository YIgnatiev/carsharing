package pro.theboard.models.cards;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 29.03.2015.
 */
public class Answer {
    private List<AnswerData> content;
    @Expose(serialize = false)
    private String card_hash;


//    {"content":[{"data":"test","card_hash":"7ecec3e675","content_hash":""}]}
//    "card_hash": "hash",
//            "content_hash": "hash",
//            "data": "answer_or_base64_encoded_image_string"


    public void setQuestionHash(String questionHash) {
        this.card_hash = questionHash;
    }

    public Answer (){

    }

    public Answer (String _questionId){
        this.card_hash = _questionId;
        this.content = new ArrayList<>();
    }

    public void addAnswer(String _answer) {
        if (content != null ) {
            AnswerData data = new AnswerData(card_hash);
            data.setContent_hash(_answer);
            content.add(data);

        }
    }

    public void addJustOneAnswer(String _answer){
        if(content != null){
            content.clear();
            AnswerData data = new AnswerData(card_hash);
            data.setContent_hash(_answer);
            content.add(data);
        }
    }

    public void addData(String _data){
        AnswerData data = new AnswerData(card_hash);
        data.setData(_data);
        this.content.add(data);

    }

    public boolean isYes(){
        return content
                .get(0)
                .getData()
                .equals("1");

    }

}
