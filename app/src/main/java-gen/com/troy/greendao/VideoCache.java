package com.troy.greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "VIDEO_CACHE".
 */
public class VideoCache {

    private Long id;
    private String result;

    public VideoCache() {
    }

    public VideoCache(Long id) {
        this.id = id;
    }

    public VideoCache(Long id, String result) {
        this.id = id;
        this.result = result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
