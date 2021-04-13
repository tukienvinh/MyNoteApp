package com.example.mynoteapp;

public class MyNote {
    private String title;
    private String tag;
    private String edit_time;
    private String content;
    private String alignment;

    public MyNote(String title, String tag, String edit_time, String content, String alignment) {
        this.title = title;
        this.tag = tag;
        this.edit_time = edit_time;
        this.content = content;
        this.alignment = alignment;
    }

    public MyNote() {
        title = "";
        tag = "";
        edit_time = "";
        content = "";
        alignment = "";
    }

    public String getTitle() {
        return title;
    }

    public String getTag() {
        return tag;
    }

    public String getEdit_time() {
        return edit_time;
    }

    public String getContent() {
        return content;
    }

    public String getAlignment() { return alignment; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setEdit_time(String edit_time) {
        this.edit_time = edit_time;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAlignment(String alignment) { this.alignment = alignment; }
}
