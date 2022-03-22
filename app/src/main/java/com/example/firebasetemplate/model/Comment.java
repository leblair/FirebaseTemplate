package com.example.firebasetemplate.model;

import java.util.HashMap;

public class Comment {
    public String id;
    public String authorName;
    public String text;
    public String postId;
    public String date;
    public HashMap<String, Boolean> likes = new HashMap<>();
}
