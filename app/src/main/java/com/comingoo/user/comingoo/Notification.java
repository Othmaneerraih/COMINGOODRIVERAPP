package com.comingoo.user.comingoo;

public class Notification {
    String title, content, code;

    public Notification() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Notification(String title, String content, String code) {

        this.title = title;
        this.content = content;

        this.code = code;
    }
}
