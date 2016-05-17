package com.elystapp.elyst.testing.code;

/**
 * Created by ERNEL on 5/16/2016.
 */
public class TestEventClass {

    public static String[] features = new String[5];

    public TestEventClass(String title, String date, String address,
                          String time, String description) {
        features[0] = title;
        features[1] = date;
        features[2] = address;
        features[3] = time;
        features[4] = description;
    }

    public String getFeature(int pos) {
        return features[pos];
    }
}
