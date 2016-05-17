package com.elystapp.elyst.testing.code;

/**
 * Created by ERNEL on 5/16/2016.
 */
public class TestEventAdapter {

    public TestEventClass[] listEvents = new TestEventClass[3];

    public TestEventAdapter() {

        listEvents[0] = (new TestEventClass("Happy Jumping", "May 2016",
                "32 Pine Street, Hanover, NH 03755", "12:50 PM",
                "We just do it..."));

        listEvents[1] = (new TestEventClass("DJ Snoopy", "May 2016",
                "32 Main Street, Hanover, NH 03755", "11:30 PM",
                "Let's go cray cray"));

        listEvents[2] = (new TestEventClass("Wine Tasting", "May 2016",
                "KKG", "2:00 PM",
                "Cool, pretentious people only..."));
    }

    public TestEventClass get(int pos) {
        return listEvents[pos];
    }
}
