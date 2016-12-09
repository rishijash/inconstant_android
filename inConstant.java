package com.rishi.mylibrary;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;

/**
 * Created by rishi on 21/09/16.
 */
public class inConstant {

    private String AppId, AppSecret;
    private  Boolean valid;
    private inConstant_DB inConstant_db;
    Context context;

    public inConstant(Context context, String AppId, String AppSecret)
    {
        this.AppId = AppId;
        this.AppSecret = AppSecret;
        this.context = context;
        inConstant_db = new inConstant_DB(context);

        //Authenticate
        SharedPreferences sf = context.getSharedPreferences("com.hoodoomail.inconstant",Context.MODE_APPEND);
        valid = sf.getBoolean("authorized",false);
        Boolean blocked = sf.getBoolean("blocked", false);
        Boolean push = sf.getBoolean("push", false);
        if(valid==false && blocked==false)
        {
            setAuthentication();
            setPush();
        }
        if(valid==true && push==false)
        {
            setPush();
        }
    }

    public inConstant(Context context)
    {
        this.context = context;
        inConstant_db = new inConstant_DB(context);
        //Authenticate
        SharedPreferences sf = context.getSharedPreferences("com.hoodoomail.inconstant",Context.MODE_APPEND);
        valid = sf.getBoolean("authorized",false);

    }

    private boolean UserAuthentication()
    {
        //Check whether user is legit or not

        //Here if conection is 200 and still not authenticated set block yes.
        SharedPreferences.Editor sf2 = context.getSharedPreferences("com.hoodoomail.inconstant",Context.MODE_PRIVATE).edit();
        sf2.putBoolean("blocked",true);

        return true;
    }

    public void savedata(JSONArray ja)
    {
        inConstant_db.addConstants(ja);
    }

    public String getAppId()
    {
        SharedPreferences sf = context.getSharedPreferences("com.hoodoomail.inconstant", Context.MODE_PRIVATE);
        String AppId = sf.getString("AppId","");
        return AppId;
    }

    private void setAuthentication()
    {
        if(UserAuthentication()==true)
        {
            inConstant_db.addConstants(getData());
            SharedPreferences.Editor sf2 = context.getSharedPreferences("com.hoodoomail.inconstant",Context.MODE_PRIVATE).edit();
            sf2.putBoolean("authorized",true);
            sf2.putString("AppId", AppId);
            sf2.putString("AppSecret",AppSecret);
            sf2.commit();
            valid = true;
        }
        else
        {
            SharedPreferences.Editor sf2 = context.getSharedPreferences("com.hoodoomail.inconstant",Context.MODE_PRIVATE).edit();
            sf2.putBoolean("authorized",false);
            sf2.commit();
            valid = false;
        }
    }

    public void setPush()
    {
        String PROJECT_NUMBER = "291796184130";
        inConstant_GCMClientManager pushClientManager = new inConstant_GCMClientManager((Activity) context, PROJECT_NUMBER);
        pushClientManager.registerIfNeeded(new inConstant_GCMClientManager.RegistrationCompletedHandler() {
            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {
                Log.d("Registration id",registrationId);
                //send this registrationId to your server

                //Set push enabled
                SharedPreferences.Editor sf2 = context.getSharedPreferences("com.hoodoomail.inconstant",Context.MODE_PRIVATE).edit();
                sf2.putBoolean("push",true);
                sf2.commit();
            }
            @Override
            public void onFailure(String ex) {
                super.onFailure(ex);
            }
        });
    }

    //fetch data from server
    private JSONArray getData()
    {
        //Fetch Data from web service
        //Put it on JSONArray
        JSONArray ja = new JSONArray();
        return ja;
    }

    public String getString(String key)
    {
        if(valid==false)
        {
            return null;
        }
        String value = inConstant_db.getConstant(key);
        return value;
    }

    public int getInt(String key)
    {
        if(valid==false)
        {
            return 0;
        }
        Integer value = Integer.valueOf(inConstant_db.getConstant(key));
        return value;
    }

    public Boolean getBool(String key)
    {
        if(valid==false)
        {
            return false;
        }
        Boolean value = Boolean.valueOf(inConstant_db.getConstant(key));
        return value;
    }

    public long getLong(String key)
    {
        if(valid==false)
        {
            return 0;
        }
        Long value = Long.valueOf(inConstant_db.getConstant(key));
        return value;
    }

    public double getDouble(String key)
    {
        if(valid==false)
        {
            return 0;
        }
        Double value = Double.valueOf(inConstant_db.getConstant(key));
        return value;

    }

    public String[] getArrayString(String key)
    {
        if(valid==false)
        {
            return null;
        }
        String value = inConstant_db.getConstant(key);
        String[] separated = value.split("~");
        return separated;
    }

    public int[] getArrayInt(String key)
    {
        if(valid==false)
        {
            return null;
        }
        String value = inConstant_db.getConstant(key);
        String[] separated = value.split("~");
        int[] numbers = new int[separated.length];
        for(int i = 0;i < separated.length;i++)
        {
            // Note that this is assuming valid input
            // If you want to check then add a try/catch
            // and another index for the numbers if to continue adding the others
            numbers[i] = Integer.parseInt(separated[i]);
        }
        return numbers;
    }

    public Boolean[] getArrayBool(String key)
    {
        if(valid==false)
        {
            return null;
        }
        String value = inConstant_db.getConstant(key);
        String[] separated = value.split("~");
        Boolean[] numbers = new Boolean[separated.length];
        for(int i = 0;i < separated.length;i++)
        {
            // Note that this is assuming valid input
            // If you want to check then add a try/catch
            // and another index for the numbers if to continue adding the others
            numbers[i] = Boolean.parseBoolean(separated[i]);
        }
        return numbers;
    }

    public Long[] getArrayLong(String key)
    {
        if(valid==false)
        {
            return null;
        }
        String value = inConstant_db.getConstant(key);
        String[] separated = value.split("~");
        Long[] numbers = new Long[separated.length];
        for(int i = 0;i < separated.length;i++)
        {
            // Note that this is assuming valid input
            // If you want to check then add a try/catch
            // and another index for the numbers if to continue adding the others
            numbers[i] = Long.parseLong(separated[i]);
        }
        return numbers;

    }

    public Double[] getArrayDouble(String key)
    {
        if(valid==false)
        {
            return null;
        }
        String value = inConstant_db.getConstant(key);
        String[] separated = value.split("~");
        Double[] numbers = new Double[separated.length];
        for(int i = 0;i < separated.length;i++)
        {
            // Note that this is assuming valid input
            // If you want to check then add a try/catch
            // and another index for the numbers if to continue adding the others
            numbers[i] = Double.parseDouble(separated[i]);
        }
        return numbers;
    }
}
