package pro.theboard.adapters;

import android.app.Activity;

import java.util.List;

import pro.theboard.constants.Constants;
import pro.theboard.fragments.cards_fragments.BaseFragment;
import pro.theboard.fragments.cards_fragments.CardWithCheckboxesFragment;
import pro.theboard.fragments.cards_fragments.CardWithEditTextFragment;
import pro.theboard.fragments.cards_fragments.CardWithMultipleButtonsFragment;
import pro.theboard.fragments.cards_fragments.CardWithRadioButtonsFragment;
import pro.theboard.fragments.cards_fragments.CardWithSliderFragment;
import pro.theboard.fragments.cards_fragments.CardWithTextFragment;
import pro.theboard.fragments.cards_fragments.CardWithUploadPhotoFragment;
import pro.theboard.models.cards.Model;


/**
 * Created by Alex on 3/22/2015.
 */
public class  CustomPageAdapter <T extends Activity> {
    private int mColorResource;
    private boolean isPromo;
    private List<Model> mModelList;
    private int position;
    public CustomPageAdapter(List<Model> modelList,int color, boolean isPromo) {
        this.mModelList = modelList;
        this.mColorResource = color;
        this.isPromo = isPromo;
    }


    private int getPosition() {

        if (position >= getCount()) position = 0;
        return position++;
    }


    public BaseFragment getItem() {

        if (getCount() == 0) return null;

        BaseFragment fragment = null;
        int i = getPosition();

        Model currentCard = mModelList.get(i);


        switch (currentCard.getType()) {
            case Constants.TYPE_IMAGE:
                fragment = new CardWithUploadPhotoFragment();
                break;
            case Constants.TYPE_CHECKBOX:
                fragment = new CardWithCheckboxesFragment();
                break;
            case Constants.TYPE_RADIO:
                fragment = new CardWithRadioButtonsFragment();
                break;
            case Constants.TYPE_RANGE:
                fragment = new CardWithSliderFragment();
                break;
            case Constants.TYPE_SINGLE:
                fragment = new CardWithMultipleButtonsFragment();
                break;
            case Constants.TYPE_BASIC:
                fragment = new CardWithEditTextFragment();
                break;
            case Constants.TYPE_TEXT:
                fragment= new CardWithTextFragment();
                break;
            case Constants.TYPE_PASSWORD:
                fragment = new CardWithEditTextFragment();
        }

        if (fragment != null) {
            fragment.setCardModel(currentCard);
            fragment.setColor(mColorResource);
            fragment.setIsPromo(isPromo);
            fragment.setAdapter(this);
        }  else
            throw new RuntimeException("Unknown model type ");
        return fragment;
    }


    public int getCount() {
        if (mModelList != null)
            return mModelList.size();
        else return 0;
    }

    public boolean isEmpty() {
        return getCount() == 0;
    }

    public void remove(Model _model) {
        mModelList.remove(_model);

        if (position == 0) position = mModelList.size() - 1;
        else position--;
    }

}
