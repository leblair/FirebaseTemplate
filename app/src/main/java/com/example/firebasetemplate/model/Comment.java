package com.example.firebasetemplate.model;

import java.util.HashMap;

public class Comment {
    public String id;
    public String authorName;
    public String text;
    public String postId;
    public HashMap<String, Boolean> likes = new HashMap<>();
    //public LocalDateTime day;
}
