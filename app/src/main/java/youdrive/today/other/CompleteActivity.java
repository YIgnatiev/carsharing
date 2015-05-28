package youdrive.today.other;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import youdrive.today.BaseActivity;
import youdrive.today.Check;
import youdrive.today.R;

/**
 * Created by psuhoterin on 17.05.15.
 */
public class CompleteActivity extends BaseActivity {

    @OnClick(R.id.btnClose)
    public void close() {
        finish();
    }

    @InjectView(R.id.txtTotalUsage)
    TextView txtTotalUsage;

    @InjectView(R.id.txtParking)
    TextView txtParking;

    @InjectView(R.id.txtTotal)
    TextView txtTotal;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_complete;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        Check check = getIntent().getParcelableExtra("check");
        if (check != null){
            txtTotalUsage.setText(String.valueOf(check.getUsageWeekendCost()
                    + check.getUsageWorkdayCost()));
            txtParking.setText(String.valueOf(check.getParkingCost()));
            txtTotal.setText(String.valueOf(check.getParkingCost()
                    + check.getUsageWorkdayCost()
                    + check.getUsageWeekendCost()));
        }

    }
}
