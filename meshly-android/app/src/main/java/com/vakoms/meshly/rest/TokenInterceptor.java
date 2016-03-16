package com.vakoms.meshly.rest;

import com.vakoms.meshly.models.Token;
import com.vakoms.meshly.utils.Logger;
import com.vakoms.meshly.utils.P;
import com.vakoms.meshly.utils.TokenValidator;

import java.io.IOException;

import retrofit.RequestInterceptor;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/9/15.
 */
public class TokenInterceptor implements RequestInterceptor {


    @Override
    public void intercept(RequestFacade request) {

        String header = String.format("%s %s", P.getTokenType(), P.getAccessToken());
        if(TokenValidator.accessTokenExpiresSoon())
            refreshToken(header);

        request.addHeader("Authorization", header);


    }



    private void refreshToken(String header){
        try {
            Token token = RetrofitApi.getInstance().meshly().refreshToken(P.getRefreshToken(), header);
            P.saveTokens(token.getLoginToken(), token.getRefreshToken(), token.getLoginExpiresIn(), token.getLoginTokenType());

        }catch (IOException e){
            if(e.getMessage()!= null) {
                if (e.getMessage().contains("Forbidden")) {
                    Logger.e("token", "403: Forbidden! Probably, need to re-login or I don't know. Will logout you, sorry.");
                        //TODO force logout
                }

                if (e.getMessage().contains("You are not permitted to perform this action.")) {
                        //TODO show dialog
                }

            }
        }
    }
}

