package pro.theboard.fragments.cards_fragments;

import android.databinding.DataBindingUtil;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.gson.Gson;
import com.lapioworks.cards.R;
import com.lapioworks.cards.databinding.FragmentQuestionTextBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

import pro.theboard.constants.Constants;
import pro.theboard.custom_views.RoundedDrawable;
import pro.theboard.models.TextButtonModel;
import pro.theboard.models.cards.Answer;
import pro.theboard.models.cards.ContentModel;
import pro.theboard.utils.Preferences;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 10/27/15.
 */
public class CardWithTextFragment extends BaseFragment<FragmentQuestionTextBinding> {

    @Override
    protected void initCardView(LayoutInflater inflater, ViewGroup container) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_question_text, container, false);


        b.setListener(this);
        setQuestion();
        subscribeToNetwork();
        handleScroll(b.scrollView);
    }


    @Override
    protected void handleNetwork(Boolean isNetworkActive) {
        b.tvSubmit.setEnabled(isNetworkActive);
        if (!isNetworkActive)
            Toast.makeText(mActivity, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
    }





    private void setQuestion() {
        float radius = getResources().getDimension(R.dimen.corner_radius);
        RoundedDrawable drawable = new RoundedDrawable(getResources().getColor(mColorResource), radius);
        if (isPromo()) {
            b.tvSubmit.setBackground(getResources().getDrawable(R.drawable.background_button_promo));
            b.tvSubmit.setText("Delete");
        }
        b.llBottom.setBackground(drawable);
        setData(b.tvQuestion,b.llQuestionLayout);

        List<ContentModel> contentModelList = mCardModel.getContent();

        ContentModel modelWithVideo = null;
        ContentModel modelWithImage = null;
        ContentModel modelWithButtonInfo = null;
        if (contentModelList.size() == 3) {

            modelWithVideo = findVideoLink(contentModelList.get(0).getTitle()) ? contentModelList.get(0) : contentModelList.get(1);
            modelWithImage = findImage(contentModelList.get(0).getTitle()) ? contentModelList.get(0) : contentModelList.get(1);
            modelWithButtonInfo = contentModelList.get(2);
        } else if (contentModelList.size() == 2) {
            modelWithButtonInfo = contentModelList.get(1);
        }

        if (modelWithImage != null && !modelWithImage.getTitle().equals("")) {
            b.ivImage.setVisibility(View.VISIBLE);

            String imageUrl = "http://app.theboard.pro/web/uploads/".concat(modelWithImage.getTitle());
            Log.v("Load image", imageUrl);

            Picasso.with(mActivity).load(imageUrl).into(b.ivImage);
        } else if (modelWithVideo != null && !modelWithVideo.getTitle().equals("")) {
            b.youtubeView.setVisibility(View.VISIBLE);

            Preferences.setVideoLoaded(true);

            String video = modelWithVideo.getTitle();

            if (video.contains("www.youtube.com"))
                playYouTube((video.replace("https://www.youtube.com/watch?v=", "")));
            else if (video.contains("youtu.be"))
                playYouTube((video.replace("https://youtu.be/", "")));
        }

        if (modelWithButtonInfo != null && !isPromo()) {
            TextButtonModel buttonModel = new Gson().fromJson(modelWithButtonInfo.getTitle(), TextButtonModel.class);
            b.tvSubmit.setText(buttonModel.getOkLabel());
        }
    }

    private boolean findVideoLink(String link) {
        return link.contains("youtu.be") || link.contains("youtube.com");
    }

    private boolean findImage(String link) {
        return link.endsWith(".png") || link.endsWith("jpg");
    }

    private void playYouTube(String video) {


        Log.v("Load video", video);

        b.youtubeView.initialize(Constants.YOUTUBE_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.setShowFullscreenButton(false);
                youTubePlayer.loadVideo(video);

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {


            }
        });
    }


    //listener
    public void onAnswer(View view) {
        Answer answer = new Answer(mCardModel.getHash());
        sendAnswer(mCardModel, answer);
    }


}


