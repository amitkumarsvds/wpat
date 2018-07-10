package services.networkmanager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONManager {
    private static JSONManager jsonManager = null;

    private JSONManager() {
        if(jsonManager == null) {
            jsonManager = new JSONManager();
        }
    }

    public static String getStringFromJson(JSONObject jsonObject, String key) {
        try {
            return jsonObject.getString(key);
        }catch (JSONException jsonException){
            return null;
        }
    }

    public static int getIntFromJson(JSONObject jsonObject, String key) {
        try {
            return jsonObject.getInt(key);
        }catch (JSONException jsonException) {
            return -1;
        }
    }

    public static double getDoubleFromJson(JSONObject jsonObject, String key) {
        try {
            return jsonObject.getDouble(key);
        }catch (JSONException jsonException) {
            return -1;
        }
    }

    public static boolean getBooleanFromJson(JSONObject jsonObject, String key) {
        try {
            return jsonObject.getBoolean(key);
        }catch (JSONException jsonException){
            return false;
        }
    }

    public static JSONObject getJSONObjectFromJson(JSONObject jsonObject,String key) {
        try {
            return jsonObject.getJSONObject(key);
        }catch(JSONException jsonException) {
            return null;
        }
    }

    public static JSONArray getJSONArrayFromJson(JSONObject jsonObject, String key) {
        try {
            return jsonObject.getJSONArray(key);
        }catch (JSONException jsonException){
            return null;
        }
    }
}
