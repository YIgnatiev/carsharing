package youdrive.today.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;

import youdrive.today.BaseActivity;
import youdrive.today.R;
import youdrive.today.databinding.ActivityOrderCarBinding;
import youdrive.today.models.Car;

public class BookCarActivity extends BaseActivity {

   private ActivityOrderCarBinding b;

    @Override
    public void bindActivity() {
        b = DataBindingUtil.setContentView(this,R.layout.activity_order_car);
        b.setListener(this);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Car mCar = getIntent().getParcelableExtra("car");
        if (mCar != null){
            b.txtModel.setText(getString(R.string.car_model, mCar.getModel()));
            b.txtColor.setText(getString(R.string.car_color, mCar.getColor()));
            b.txtNumber.setText(getString(R.string.car_number, mCar.getNumber()));
            Glide.with(this)
                    .load(mCar.getImg())
                    .centerCrop()
                    .crossFade()
                    .into(b.imgCar);
        }
    }

    //listener
    public void onShowMap(View view) {
        Car mCar = getIntent().getParcelableExtra("car");
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+mCar.getLat()+", "+mCar.getLon()+"&mode=w");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        } else {
            final String appPackageName = "com.google.android.apps.maps";
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
    }

}
