package com.morfitrun.widgets.validators;

import android.content.Context;
import android.widget.EditText;
import com.andreabaccega.formedittextvalidator.Validator;
import com.morfitrun.R;

/**
 * Created by vasia on 16.03.2015.
 */
public class ConfirmValidator extends Validator {

    private EditText etPassword;

    public ConfirmValidator(Context _context, EditText _etPassword) {
        super(_context.getResources().getString(R.string.no_confirm_password));
        etPassword = _etPassword;
    }

    @Override
    public boolean isValid(EditText editText) {
        return editText.getText().toString().equals(etPassword.getText().toString());
    }
}
