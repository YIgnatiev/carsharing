package com.budivnictvo.rssnews.app.utils;

import android.util.Log;
import com.budivnictvo.rssnews.app.data.RssItem;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by Администратор on 16.12.2014.
 */
public class RssParser {



    private Tag currentTag = Tag.DEFAULT;
    private ArrayList<RssItem> items;
    private RssItem currentItem;

    private String mRssTitle;
    private String mRssImageUrl;
    private String mRssLastBuildDate;
    private String mRssLink;
    private XmlPullParser mParser;


    public RssParser (){
        items = new ArrayList<RssItem>();
    }

    public void parseXml(String xml){

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            mParser = factory.newPullParser();
            mParser.setInput(new StringReader(xml));

            int eventType = mParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        checkForStartTag(mParser.getName());
                        break;
                    case XmlPullParser.END_TAG:
                        checkForEndTag(mParser.getName());
                        break;
                    case XmlPullParser.CDSECT:
                        String text = mParser.getText();
                        if (text.contains("/>")){
                            cdataParse(text);
                        }else {
                            write(mParser.getText());
                        }
                        break;

                    case XmlPullParser.TEXT:

                        write(mParser.getText());
                        break;
                }
                eventType = mParser.nextToken();
            }
        }catch(XmlPullParserException e){
            Log.v("test", e.getMessage());
        }catch(IOException e){

        }
    }


    public ArrayList<RssItem> getItems(){
        return items;
    }

    public String getmRssTitle() {
        return mRssTitle;
    }

    public String getmRssImageUrl() {
        return mRssImageUrl;
    }

    public String getmRssLastBuildDate() {
        return mRssLastBuildDate;
    }

    public String getmRssLink() {
        return mRssLink;
    }

    private void checkForStartTag(String _tag) {
        if (_tag.equals("link")) {
            currentTag = Tag.LINK;
        } else if (_tag.equals("title")){
            currentTag = Tag.TITLE;
        } else if (_tag.equals("description")){
            currentTag = Tag.DESCRIPTION;
        } else if (_tag.equals("url")){
            currentTag = Tag.URL;
        } else if (_tag.equals("pubDate")) {
            currentTag = Tag.PUB_DATE;
        } else if (_tag.equals("image")) {
            currentTag = Tag.IMAGE;
        } else if (_tag.equals("lastBuildDate")){
            currentTag = Tag.LAST_BUILD_DATE;
        } else if (_tag.equals("enclosure")) {
            currentItem.setImageUrl(mParser.getAttributeValue(null, "url"));
        } else if (_tag.equals("item")){
            currentTag = Tag.ITEM;
            currentItem = new RssItem();
        } else {
            currentTag = Tag.DEFAULT;
        }
    }

    private void checkForEndTag(String _tag){
        if(_tag.equals("item")){
            currentTag = Tag.DEFAULT;
            items.add(currentItem);
        }else {
            currentTag = Tag.DEFAULT;
        }

    }

    private void write(String _text) {
        if (currentItem == null){
            setHeaders(_text);
            return;
        }
        switch (currentTag){
            case DEFAULT:
                break;
            case LINK:
                currentItem.setLink(_text);
                break;
            case TITLE:
                currentItem.setTitle(_text);
                break;
            case DESCRIPTION:
                currentItem.setDescription(_text);
                break;
            case PUB_DATE:
                currentItem.setPubDate(_text);
                break;
        }
    }

    private void setHeaders(String _text){
        switch (currentTag){
            case TITLE:
                mRssTitle = _text;
                break;
            case URL:
                mRssImageUrl = _text;
                break;
            case LAST_BUILD_DATE:
                mRssLastBuildDate = _text;
                break;
            case LINK:
                mRssLink = _text;
                break;
        }
    }
    private void cdataParse(String cData){
        int httpIndex = cData.indexOf("http://");
        int httpIndexEnd = httpIndex;
        char quote =  '"';
        char data = cData.charAt(httpIndexEnd);
        while (data != quote){
            data = cData.charAt(httpIndexEnd);
            httpIndexEnd++;
        }
        String imageUrl = cData.substring(httpIndex,httpIndexEnd - 1);
        currentItem.setImageUrl(imageUrl);

        char tagSign = '>';
        String onlyCharacters = cData.substring(cData.indexOf(tagSign));

        while(onlyCharacters.indexOf(tagSign) != -1){
            onlyCharacters = onlyCharacters.substring(onlyCharacters.indexOf(tagSign) +1);
        }
        write(onlyCharacters);

    }
    private enum Tag {
        LINK, TITLE, ITEM, DESCRIPTION, PUB_DATE, URL, IMAGE, LAST_BUILD_DATE,ENCLOSURE, DEFAULT,
    }
}
//<p> Оппозиционер Алексей Навальный в понедельник, 5 января, сообщил об отказе соблюдать условия домашнего ареста.</p>
//<p> "Да, я все понимаю и после того, как срок получения текста приговора истек, буду поступать исключительно по закону " +
//        "в соответствии со своим высоким уровнем правосознания. Я отказываюсь выполнять требования моего незаконного содержания" +
//        " под домашним арестом", - написал Навальный.</p> <div class="post-item__image-box post-item__image-box_center" style="max-" +
//        "width: 610px;"> <div class="image-box__img"> <img alt="" src="http://kor.ill.in.ua/m/610x0/1558084.jpg?v=635560733612119882"
//        style="max-width: 610px;" /></div> <div class="articles-box__sdescription">  </div> <div class="articles-box__source"> <a href="">
//         </a></div> </div> <p> Оппозиционер сообщил, что считает меру пресечения в виде домашнего ареста после объявления приговора незаконн
//        ой.</p> <p> "Статья 107 УПК четко говорит, что такая мера пресечения применяется только в отношении подозреваемых и обвиняемых. Собс" +
//        "твенно и сам Замоскворецкий суд признал, что никакого домашнего ареста после приговора быть не может, вернув ФСИНу их жалобу на меня",
// - сообщил Навальный.</p> <p> Также он отметил, что срезал электронный браслет, применяемый для отслеживания перемещений человека, находящегося
// под домашним арестом, и выложил фотографию отрезанного браслета на своем аккаунте в "Твиттере".</p> <p> Навальный сообщил, что не намерен никуда
// уезжать, его "потребности перемещения сводятся к поездкам из дома в офис-обратно и проведению досуга с семьей".</p> <p> Как сообщал <span style="color:#ff0000;">
// <strong>Корреспондент.net</strong></span>, братья Алексей и Олег Навальные обвиняются в хищении 26 миллионов рублей у компании Ив Роше-Восток и еще 4 миллионов рублей
// у Многопрофильной процессинговой компании.</p> <p> <a href="http://korrespondent.net/world/russia/3432324-slova-navalnoho-o-kryme-raskololy-rossyiskuui-oppozytsyui-BBC" target="_self">
// Оппозиционер Алексей Навальный</a> получил 3,5 года заключения условно по уголовному делу о хищении и легализации средств компании <em>Ив Роше</em>. Его брат Олег – 3,5 года лишения свободы.</p>
// <p> После оглашения решения Олег Навальный
//        взят под стражу в зале суда. Суд также взыскал с братьев Навальных более 4 миллионов рублей по иску потерпевшей компании.</p>