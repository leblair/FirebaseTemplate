package com.example.firebasetemplate.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Post implements Serializable {
    public String postid;
    public String content;
    public String authorName;
    public String date;
    public String imageUrl;


    public HashMap<String, Boolean> likes = new HashMap<>();
    public List<Comment> comments = new ArrayList<>();


}
