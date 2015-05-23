package com.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.ClientProtocolException;

import android.util.Log;

public class JsonParser 
{
    private static InputStream is = null;
    //private static JSONObject jObj = null;
    private static String json = "";

    public JsonParser() 
    {
    }
    
    public String getJSONFromUrl(String url) 
    {
        // Making HTTP request
        try 
        {
            URL urlnew = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) urlnew.openConnection();
            is = new BufferedInputStream(urlConnection.getInputStream());
        } 
        catch (UnsupportedEncodingException e) 
        {
            e.printStackTrace();
        } 
        catch (ClientProtocolException e) 
        {
            e.printStackTrace();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        try 
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            
            while ((line = reader.readLine()) != null) 
                sb.append(line + "\n");

            json = sb.toString();
            is.close();
        } 
        catch (Exception e) 
        {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        
        //Log.d("JSON_RUTA", json);
        return json;
    }
}