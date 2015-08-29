package com.troy.jianyue.json;

import android.util.Log;

import com.baidu.cyberplayer.utils.J;
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
    private JsonParser mJsonParser;

    public PictureJSONParser() {
        mGson = new Gson();
        mJsonParser = new JsonParser();
    }

    public List<Picture> jsonToPictureList(String json) {
        JsonElement jsonElement = mJsonParser.parse(json);
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<Picture> pictureList = new ArrayList<Picture>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject=jsonArray.get(i).getAsJsonObject();
            String imageUrl=jsonObject.get("imageUrl").getAsString();
            String summary=jsonObject.get("summary").getAsString();
            Picture picture = new Picture();
            picture.setImageUrl(imageUrl);
            picture.setSummary(summary);
            pictureList.add(picture);
        }
        return pictureList;
    }

    public String PictureListToJSON(List<Picture> pictures) {
        List<Picture> pictureList = new ArrayList<Picture>();
        for (Picture picture : pictures) {
            Picture newPicture = new Picture();
            newPicture.setImageUrl(picture.getImageUrl());
            newPicture.setSummary(picture.getSummary());
            pictureList.add(newPicture);
        }
        return mGson.toJson(pictureList);
    }

}
