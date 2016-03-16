package pro.theboard.models.retrofit;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 7/11/15.
 */
public class Customer {
    private String customer_hash;
    private int status;
    private int cards_unaswered;

    public String getCustomer_hash() {
        return customer_hash;
    }

    public int getStatus() {
        return status;
    }

    public int getCards_unaswered() {
        return cards_unaswered;
    }
}
