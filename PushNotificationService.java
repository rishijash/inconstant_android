package com.rishi.shareit;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;
import com.rishi.mylibrary.inConstant;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by rishi on 15/10/16.
 */

public class PushNotificationService extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        //createNotification(mTitle, push_msg);

        inConstant inConstant = new inConstant(getApplicationContext());
        String AppId = inConstant.getAppId();

        //GET JSON DATA
        JSONArray ja= null;
        try {
            ja = new JSONArray(message);
            //Check App Id is same or not
            if(AppId.equals(""))
            {
                inConstant.savedata(ja);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}