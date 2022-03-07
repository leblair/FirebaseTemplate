package com.example.firebasetemplate.model;

import java.time.LocalDateTime;
import java.util.HashMap;

public class Comment {
    public String authorName;
    public String text;
    public HashMap<String, Boolean> likes = new HashMap<>();
    public LocalDateTime day;
}
