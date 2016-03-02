package youdrive.today.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import youdrive.today.BaseActivity;
import youdrive.today.models.Check;
import youdrive.today.R;
import youdrive.today.databinding.ActivityCompleteBinding;

/**
 * Created by psuhoterin on 17.05.15.
 */
public class CompleteActivity extends BaseActivity {

    private ActivityCompleteBinding b;


    @Override
    public void bindActivity() {
        b = DataBindingUtil.setContentView(this,R.layout.activity_complete);
        b.setListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Check check = getIntent().getParcelableExtra("check");

        if (check != null){
            b.txtTotalUsage.setText(convertRub(check.getUsageWeekendCost()
                    + check.getUsageWorkdayCost()));
            b.txtParking.setText(convertRub(check.getParkingCost()));
            b.txtTotal.setText(convertRub(check.getParkingCost()
                    + check.getUsageWorkdayCost()
                    + check.getUsageWeekendCost()));
            if (check.getDiscountPercent() != 0 && check.getDiscountPrice() != 0) {
                b.llDiscount.setVisibility(View.VISIBLE);
                b.txtDiscount.setText("-"+convertRub(check.getDiscountPrice()));
                b.txtTitleDiscount.setText(getString(R.string.discount) + " " + check.getDiscountPercent() + "%:");
            } else {
                b.llDiscount.setVisibility(View.GONE);
            }
        }

    }


    public void onClose(View view) {
        finish();
    }

    private String convertRub(long kopeck) {
        return String.format("%.2f", (float) kopeck / 100) + " руб.";
    }
}
