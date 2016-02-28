package com.example.troubleshooter;
public class CaseInfo {
    private int negativeID;
    private int positiveID;
    private String text;
    private String url;
    private String positiveText;
    private String negativeText;

    public int getNegativeID() {
        return negativeID;
    }

    public void setNegativeID(int negativeID) {
        this.negativeID = negativeID;
    }

    public int getPositiveID() {
        return positiveID;
    }

    public void setPositiveID(int positiveID) {
        this.positiveID = positiveID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPositiveText() {
        return positiveText;
    }

    public void setPositiveText(String positiveText) {
        this.positiveText = positiveText;
    }

    public String getNegativeText() {
        return negativeText;
    }

    public void setNegativeText(String negativeText) {
        this.negativeText = negativeText;
    }
}
