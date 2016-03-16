package com.maryjorapini.soulintention.global;

import java.util.ArrayList;

/**
 * Created by Администратор on 02.12.2014.
 */
public abstract  class SocialNetworkVariables {
    private static ArrayList<Integer> facebookPostInfo = new ArrayList<Integer>();
    private static ArrayList<Integer> twitterPostInfo = new ArrayList<Integer>();


    public static ArrayList<Integer> getFacebookPostInfo() {
        return facebookPostInfo;
    }

    public static void setFacebookPostInfo(ArrayList<Integer> facebookPostInfo) {
        SocialNetworkVariables.facebookPostInfo = facebookPostInfo;
    }

    public static ArrayList<Integer> getTwitterPostInfo() {
        return twitterPostInfo;
    }

    public static void setTwitterPostInfo(ArrayList<Integer> twitterPostInfo) {
        SocialNetworkVariables.twitterPostInfo = twitterPostInfo;
    }
}
