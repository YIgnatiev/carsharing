package pro.theboard.models;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/27/15.
 */
public class SingleButtonModel {
    private String yesLabel;
    private String noLabel;




    public String getNoLabel() {
        return noLabel;
    }

    public String getYesLabel() {
        return yesLabel;
    }


    public SingleButtonModel(String yesLabel,String noLabel){
        this.yesLabel = yesLabel;
        this.noLabel = noLabel;
    }

}
