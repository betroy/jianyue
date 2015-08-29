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
    private String thumbnailsUrl;

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoUrl() {
        return videoUrl == null ? getString("videoUrl") : videoUrl;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return summary == null ? getString("summary") : summary;
    }

    public void setThumbnailsUrl(String thumbnailsUrl) {
        this.thumbnailsUrl = thumbnailsUrl;
    }

    public String getThumbnailsUrl() {
        return thumbnailsUrl == null ? getAVFile("thumbnailsUrl").getUrl() : thumbnailsUrl;
    }
}
