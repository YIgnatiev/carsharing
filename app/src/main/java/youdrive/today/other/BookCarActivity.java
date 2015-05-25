package youdrive.today.other;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import youdrive.today.BaseActivity;
import youdrive.today.Car;
import youdrive.today.R;

public class BookCarActivity extends BaseActivity {

    @InjectView(R.id.txtModel)
    TextView txtModel;

    @InjectView(R.id.txtNumber)
    TextView txtNumber;

    @InjectView(R.id.txtColor)
    TextView txtColor;

    @InjectView(R.id.imgCar)
    ImageView imgCar;

    @OnClick(R.id.btnShowOnMap)
    public void show(View view) {
        finish();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_order_car;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        Car mCar = getIntent().getParcelableExtra("car");
        if (mCar != null){
            txtModel.setText(mCar.getModel());
            txtColor.setText(mCar.getColor());
            txtNumber.setText(mCar.getNumber());
            Glide.with(this)
                    .load(mCar.getImg())
                    .centerCrop()
                    .crossFade()
                    .into(imgCar);
        }
    }
}
