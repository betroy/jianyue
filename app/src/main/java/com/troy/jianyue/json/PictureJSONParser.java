package com.troy.jianyue.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.troy.jianyue.bean.Picture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenlongfei on 15/8/28.
 */
public class PictureJSONParser {
    private Gson mGson;
    private JsonObject mJsonObject;

    public PictureJSONParser(String json) {
        mGson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(json);
        mJsonObject = jsonElement.getAsJsonObject();
    }

    public List<Picture> jsonToPictureList(String json) {
        return new ArrayList<Picture>();
    }

    public String PictureListToJSON(int page, List<Picture> pictures) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("currentPage", page);
        JsonArray jsonArray = mGson.toJsonTree(pictures).getAsJsonArray();
        JsonObject list = new JsonObject();
        list.add("list", jsonArray);
        jsonObject.add("result", list);
        return jsonObject.toString();
    }

}
