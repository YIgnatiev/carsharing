package com.morfitrun.widgets.validators;

import android.content.Context;
import android.widget.EditText;
import com.andreabaccega.formedittextvalidator.Validator;
import com.morfitrun.R;
import com.morfitrun.global.Constants;

/**
 * Created by vasia on 16.03.2015.
 */
public class PasswordValidator extends Validator {

    public PasswordValidator(Context _context) {
        super(_context.getResources().getString(R.string.min_password_length));
    }

    @Override
    public boolean isValid(EditText _editText) {
        return _editText.getText().toString().length() >= Constants.MIN_PASSWORD_LENGTH;
    }
}
