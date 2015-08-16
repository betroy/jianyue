package com.troy.jianyue.util;

import com.troy.jianyue.bean.WeiXin;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by chenlongfei on 15/5/10.
 */
public class GsonUtil {
    private static Gson mGson;
    public static int mResultTotalPage;
    public static int mResultPno;

    public static List<WeiXin> jsonToWeiXinList(String json) {
        Gson gson = getGson();
        String resultJson = getResult(json);
        return gson.fromJson(resultJson, new TypeToken<List<WeiXin>>() {
        }.getType());
    }

    public static Gson getGson() {
        if (mGson == null) {
            mGson = new Gson();
        }
        return mGson;
    }

    public static String getResult(String json) {
        String resultJson = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject result = jsonObject.getJSONObject("result");
            JSONArray jsonArray = result.getJSONArray("list");
            setTotalPage(result.getInt("totalPage"));
            setPno(result.getInt("pno"));
            return jsonArray.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultJson;
    }

    //总页数
    public static int getTotalPage() {
        return mResultTotalPage;
    }

    //第几页
    public static int getPno() {
        return mResultPno;
    }

    public static void setTotalPage(int totalPage) {
        GsonUtil.mResultTotalPage=totalPage;
    }

    public static void setPno(int pno) {
        GsonUtil.mResultPno=pno;
    }

}
