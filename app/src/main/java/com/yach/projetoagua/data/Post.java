package com.yach.projetoagua.data;

import com.google.gson.annotations.SerializedName;

public class Post {
    @SerializedName("tl_id")
    private int id;

    @SerializedName("tl_name")
    private String name;

    @SerializedName("tl_type")
    private String type;

    @SerializedName("tl_title")
    private String title;

    @SerializedName("tl_text")
    private String text;

    @SerializedName("tl_location")
    private String location;

    @SerializedName("tl_date")
    private String date;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }
}
