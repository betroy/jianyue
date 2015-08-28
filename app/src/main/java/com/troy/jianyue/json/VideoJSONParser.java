package com.troy.jianyue.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.troy.jianyue.bean.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlongfei on 15/8/28.
 */
public class VideoJSONParser {
    private Gson mGson;
    private JsonObject mJsonObject;

    public void VideoListToJSON(String json) {
        mGson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(json);
        mJsonObject = jsonElement.getAsJsonObject();
    }

    public List<Video> jsonToVideoList() {
        return new ArrayList<Video>();
    }

    public void videoListToJSON() {
        
    }
}
