package com.yach.projetoagua.data;

import com.google.gson.annotations.SerializedName;

public class Post {
    private int tl_id;

    private String tl_name;

    private String tl_type;

    private String tl_title;

    private String tl_text;

    private String tl_location;

    private String tl_date;


    private String user;

    private String name;

    private String location;

    private String desc;

    private String date;

    public Post(String user, String name, String location, String desc, String date) {
        this.user = user;
        this.name = name;
        this.location = location;
        this.desc = desc;
        this.date = date;
    }


    public int getId() {
        return tl_id;
    }

    public String getName() {
        return tl_name;
    }

    public String getType() {
        return tl_type;
    }

    public String getTitle() {
        return tl_title;
    }

    public String getText() {
        return tl_text;
    }

    public String getLocation() {
        return tl_location;
    }

    public String getDate() {
        return tl_date;
    }
}
