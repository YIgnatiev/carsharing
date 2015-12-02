package youdrive.today.models;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 12/2/15.
 */
public class CreditCardModel {
    private String registration_id;
    private String amount;
    private String card_holder ;
    private String crypto_packet ;

    public CreditCardModel(String registration_id, String amount, String card_holder, String crypto_packet) {
        this.registration_id = registration_id;
        this.amount = amount;
        this.card_holder = card_holder;
        this.crypto_packet = crypto_packet;
    }
}
