package se.promedia.nyhetsapp.trash;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.MalformedJsonException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by martenolsson on 15-12-27.
 */
public class GSONParser {
    JsonObject jObj = null;
    protected Context context;
    public JsonObject getGSONFromUrl(String... strings) {
        // Connect to the URL using java's native library
        URL getUrl;
        try {
            getUrl = new URL(strings[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        HttpURLConnection request;
        try {
            request = (HttpURLConnection) getUrl.openConnection();
            if(strings.length >= 3) {
                request.setRequestProperty("X-APPID", strings[1]);
                if(!strings[2].isEmpty()) {
                    request.setRequestProperty("X-USERID", strings[2]);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            request.connect();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // Convert to a JSON object
        JsonParser jp = new JsonParser(); //from gson
        JsonElement root; //Convert the input stream to a json element

        try {
            root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            if (root.isJsonObject()) {
                //if OBJECT
                jObj = root.getAsJsonObject();
                Log.e("GSON2",jObj.toString());
            }
            else{
                //if ARRAY
                JsonObject jsonObject = new JsonObject();
                jsonObject.add("result", root);
                jObj = jsonObject;
            }
        }
        catch (MalformedJsonException e){
            return null;
        }
        catch (IOException e) {
            return null;
        }
        request.disconnect();
        return jObj;
    }

}
