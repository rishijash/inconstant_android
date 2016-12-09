package com.rishi.mylibrary;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rishi on 26/11/16.
 */
public class PUSHDATA extends AsyncTask {

    public static String ProjectNum = "291796184130";

    JSONObject jo=null;

    String AppId,RegId;

    public PUSHDATA(String AppId, String RegId)
    {
        this.AppId = AppId;
        this.RegId = RegId;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        HttpURLConnection httpcon;
        String url = "https://www.hoodoomail.com/inconstant/addpush_webservice.php";
        JSONObject data = new JSONObject();

        String result = null;
        try {
            //Connect
            httpcon = (HttpURLConnection) ((new URL(url).openConnection()));
            httpcon.setDoOutput(true);
            httpcon.setRequestProperty("Content-Type", "application/json");
            httpcon.setRequestProperty("Accept", "application/json");
            httpcon.setRequestMethod("POST");
            httpcon.connect();

            //Write
            try {
                data.put("a_id",AppId);
                data.put("reg_id",RegId);
                data.put("secret","2wd34wsd3hadkshajksg7t3278te3gdugkdkjhsdg37te2fgydwgdw7");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            OutputStream os = httpcon.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(data.toString());
            writer.close();
            os.close();

            //Read
            BufferedReader br = new BufferedReader(new InputStreamReader(httpcon.getInputStream(),"UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            br.close();
            result = sb.toString();



        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
