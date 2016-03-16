package com.vakoms.meshly.models;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/21/15.
 */
public class Token {

    private String client_id ;
    private String client_secret ;
    private String grant_type ;
    private String refresh_token;
    private String accessToken;
    private String expiresIn;
    private String tokenType ;
    private String error;
    private String access_token;
    private String token_type;
    private String expires_in;
    public  Token (String token,String grandType ,String clienId,String clientSecret) {
        this.grant_type = grandType;
        this.refresh_token = token;
        this.client_id = clienId;
        this.client_secret = clientSecret;
    }

    public Token(){}


    public String getAccessToken() {
        return accessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getRefreshToken(){
        return refresh_token;
    }

    public String getError() {
        return error;
    }



    public String getLoginToken(){
        return access_token;
    }

    public String getLoginExpiresIn(){
        return expires_in;
    }

    public String getLoginTokenType(){
        return  token_type;
    }
}
