package youdrive.today.models;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/31/15.
 */
public class RegistrationUser {


    private String online_contract_signed;
    private String email;
    private String password;
    private String password_confirm;
    private String phone;
    private String first_name;
    private String middle_name;
    private String last_name;
    private String documents_storage_url;
    private String card_number;
    private String promocode;


    public String getOnline_contract_signed() {
        return online_contract_signed;
    }

    public void setOnline_contract_signed(String online_contract_signed) {
        this.online_contract_signed = online_contract_signed;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_confirm() {
        return password_confirm;
    }

    public void setPassword_confirm(String password_confirm) {
        this.password_confirm = password_confirm;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDocuments_storage_url() {
        return documents_storage_url;
    }

    public void setDocuments_storage_url(String documents_storage_url) {
        this.documents_storage_url = documents_storage_url;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }
}
