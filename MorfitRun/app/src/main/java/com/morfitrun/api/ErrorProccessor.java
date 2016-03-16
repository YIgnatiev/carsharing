package com.morfitrun.api;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.morfitrun.data_models.ErrorModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import retrofit.RetrofitError;

/**
 * Created by Виталий on 16/03/2015.
 */
public class ErrorProccessor {

    public static final void processError(final Context _context, final RetrofitError _error) {
        String error;
        try {
            ErrorModel errorModel = (ErrorModel) _error.getBodyAs(ErrorModel.class);
            error = errorModel.error;
        } catch (RuntimeException exc) {
            error = _error.getMessage();
        } catch (Exception exc) {
            error = _error.getMessage();
        }
        try {
            Toast.makeText(_context, error, Toast.LENGTH_LONG).show();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }
}
