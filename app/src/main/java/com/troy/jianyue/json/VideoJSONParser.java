package com.troy.jianyue.json;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.troy.jianyue.bean.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlongfei on 15/8/28.
 */
public class VideoJSONParser {
    private Gson mGson;
    private JsonObject mJsonObject;
    private JsonParser mJsonParser;

    public VideoJSONParser() {
        mGson = new Gson();
        mJsonParser = new JsonParser();
    }

    public List<Video> jsonToVideoList(String json) {
        JsonElement jsonElement = mJsonParser.parse(json);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<Video> videoList = new ArrayList<Video>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String summary = jsonObject.get("summary").getAsString();
            String videoUrl = jsonObject.get("videoUrl").getAsString();
            String thumbnailsUrl = jsonObject.get("thumbnailsUrl").getAsString();
            Video video = new Video();
            video.setSummary(summary);
            video.setVideoUrl(videoUrl);
            video.setThumbnailsUrl(thumbnailsUrl);
            videoList.add(video);
        }
        return videoList;
    }

    public String videoListToJSON(List<Video> videos) {
        List<Video> videoList = new ArrayList<Video>();
        for (Video video : videos) {
            Video newVideo = new Video();
            newVideo.setSummary(video.getSummary());
            newVideo.setVideoUrl(video.getVideoUrl());
            newVideo.setThumbnailsUrl(video.getThumbnailsUrl());
            videoList.add(newVideo);
        }
        return mGson.toJson(videoList);
    }
}
