package com.troy.jianyue.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by chenlongfei on 15/5/9.
 */
@AVClassName("Video")
public class Video extends AVObject {
    private String videoUrl;
    private String summary;
    private String thumbnails;

    public String getVideoUrl() {
        return getString("videoUrl");
    }

    public String getSummary() {
        return getString("summary");
    }

    public String getThumbnailsUrl() {
        return getAVFile("thumbnails").getUrl();
    }
}
