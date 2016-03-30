package youdrive.today.fragments;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.yandex.metrica.YandexMetrica;

import java.util.ArrayList;
import java.util.List;

import youdrive.today.App;
import youdrive.today.BaseActivity;
import youdrive.today.R;
import youdrive.today.activities.InviteActivity;
import youdrive.today.databinding.FragmentReferalBinding;
import youdrive.today.helpers.AppUtils;
import youdrive.today.models.ReferralRules;

public class ReferalFragment extends BaseFragment<BaseActivity> {

    private ReferralRules referralRules;
    private FragmentReferalBinding b;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_referal, container, false);
        b.setListener(this);
        referralRules = getActivity().getIntent().getParcelableExtra("referralData");
        setData();
        return b.getRoot();
    }

    private void setData() {
        b.tvCode.setText(referralRules.referral_code);
    }

    @Override
    public void onStart() {
        super.onStart();
        YandexMetrica.reportEvent("referal_fragment");

        App.tracker().setScreenName("referal_fragment");
        App.tracker().send(new HitBuilders.ScreenViewBuilder().build());

    }

    public void onMore(View view){
        String referralDescription=getString(R.string.referral_dialog_description,
                referralRules.invitee_bonus_part,
                referralRules.max_invitee_bonus/100,
                referralRules.inviter_bonus_part,
                referralRules.max_inviter_bonus/100);

        AlertDialog dialog = new AlertDialog.Builder(mActivity, R.style.ReferralDialog)
                .setMessage(referralDescription)
                .setPositiveButton(android.R.string.ok, (num, window) -> {
                })
                .create();
        dialog.show();
    }
    public void onCodeClick(View view){
        View popupView=LayoutInflater.from(getActivity()).inflate(R.layout.popup_copy, (ViewGroup)view.getParent(), false);
        popupView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        PopupWindow popupWindow=new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupView.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(referralRules.referral_code, referralRules.referral_code);
            clipboard.setPrimaryClip(clip);
            popupWindow.dismiss();
        });
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        //System.out.printf("offset %d %d %d\n", view.getTop(), view.getHeight(), view.getTop() + view.getHeight() - size.y / 2);
        //popupWindow.showAtLocation(view, Gravity., 0, view.getTop()+view.getHeight()-size.y/2);
        popupWindow.showAsDropDown(view, view.getWidth()/2-popupView.getMeasuredWidth()/2, 0);
    }
    public void onShareSocial(View view){
        PackageManager pm = getActivity().getPackageManager();
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        Intent openInChooser = null;

        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
        List<LabeledIntent> intentList = new ArrayList<>();
        for (int i = 0; i < resInfo.size(); i++) {
            // Extract the label, append it, and repackage it in a LabeledIntent
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
            if(packageName.contains("twitter")
                    || packageName.contains("facebook")
                    || packageName.contains("vkontakte")
                    || packageName.contains("vkfree")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                if(packageName.contains("vkontakte")||packageName.contains("vkfree")) {
                    intent.putExtra(Intent.EXTRA_TEXT, AppUtils.getShareText(getActivity(), referralRules));
                }
                if(packageName.contains("twitter")) {
                    intent.putExtra(Intent.EXTRA_TEXT, AppUtils.getShareText(getActivity(), referralRules));
                }
                else if(packageName.contains("facebook")) {
                    // Warning: Facebook IGNORES our text. They say "These fields are intended for users to express themselves. Pre-filling these fields erodes the authenticity of the user voice."
                    // One workaround is to use the Facebook SDK to post, but that doesn't allow the user to choose how they want to share. We can also make a custom landing page, and the link
                    // will show the <meta content ="..."> text from that page with our link in Facebook.
                    intent.putExtra(Intent.EXTRA_TEXT, referralRules.registration_link);
                }

                if(openInChooser==null) openInChooser=Intent.createChooser(intent, getString(R.string.share_social));
                else intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
            }
        }

        // convert intentList to array
        LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size() ]);

        if(openInChooser!=null) {
            openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
            startActivity(openInChooser);
        }
        else Toast.makeText(getActivity(), R.string.no_social_detected, Toast.LENGTH_LONG).show();
    }
    public void onShareEmail(View view){
        mActivity.getReadContactsPermission(() -> {
            Intent intent = new Intent(getActivity(), InviteActivity.class);
            intent.putExtra("mode", InviteActivity.MODE_EMAIL);
            startActivity(intent);
        });
    }

    public void onShareSMS(View view)
    {
        mActivity.getReadContactsPermission(() -> {
            Intent intent = new Intent(getActivity(), InviteActivity.class);
            intent.putExtra("referralData", referralRules);
            intent.putExtra("mode", InviteActivity.MODE_SMS);
            startActivity(intent);
        });
    }
}
