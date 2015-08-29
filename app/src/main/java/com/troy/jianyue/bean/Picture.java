package com.troy.jianyue.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;

/**
 * Created by chenlongfei on 15/5/9.
 */
@AVClassName("Picture")
public class Picture extends AVObject {
    private String imageUrl;
    private String summary;

    public Picture() {
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl == null ? getAVFile("image").getUrl() : imageUrl;
    }

    public String getSummary() {
        return summary == null ? getString("summary") : summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

}
