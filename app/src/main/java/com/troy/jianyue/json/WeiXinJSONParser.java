package com.troy.jianyue.json;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.troy.jianyue.bean.WeiXin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.List;

/**
 * Created by chenlongfei on 15/8/28.
 */
public class WeiXinJSONParser {
    private Gson mGson;
    public static int mResultTotalPage;
    public static int mResultPno;
    public JsonObject mJsonObject;

    public WeiXinJSONParser(String json) {
        mGson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(json);
        mJsonObject = jsonElement.getAsJsonObject();
    }

    public List<WeiXin> getWeiXinList() {
        return mGson.fromJson(getWeiXinArrayJson(), new TypeToken<List<WeiXin>>() {
        }.getType());
    }

    public JsonObject getResult() {
        return mJsonObject.getAsJsonObject("result");
    }

    public String getWeiXinArrayJson() {
        return getResult().getAsJsonArray("list").toString();
    }


    //总页数
    public int getTotalPage() {
        return getResult().get("totalPage").getAsInt();
    }

    //第几页
    public int getPno() {
        return getResult().get("pno").getAsInt();
    }

    //错误代码
    public int getErrorCode() {
        int errorCode = mJsonObject.get("error_code").getAsInt();
        return errorCode;
    }

    public String getReason() {
        String reason = mJsonObject.get("reason").getAsString();
        return reason;
    }

}
