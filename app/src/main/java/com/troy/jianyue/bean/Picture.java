package com.troy.jianyue.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by chenlongfei on 15/5/9.
 */
@AVClassName("Picture")
public class Picture extends AVObject {
    private String url;
    private String summary;

    public Picture() {
    }

    public String getUrl() {
        return getAVFile("url").getUrl();
    }

    public String getSummary() {
        return getString("summary");
    }

}
