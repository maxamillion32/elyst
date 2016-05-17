package com.elystapp.elyst;

/**
 * Created by roxanagheorghe on 5/16/16.
 *
 */
public class Post {

    private String postTitle;


    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public Post(String postTitle) {
        this.postTitle = postTitle;

    }
}
