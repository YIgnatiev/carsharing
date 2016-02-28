package com.vakoms.meshly.models;

/**
 * Created by Oleh Makhobey
 * email : tajcig@ya.ru
 * on 9/12/15.
 */
public class Version {



    AndroidVersion android;

    public AndroidVersion getAndroid() {
        return android;
    }




    public class AndroidVersion {


        String version;
        String supportedVersion;


        public String getVersion() {
            return version;
        }

        public String getSupportedVersion() {
            return supportedVersion;
        }

    }
}
