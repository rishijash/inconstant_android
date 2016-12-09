package com.rishi.mylibrary;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.scottyab.aescrypt.AESCrypt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.GeneralSecurityException;

/**
 * Created by rishi on 3/7/16.
 */
public class inConstant_DB extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "inConstant_privatedb_21331221343";

    private static final String Constant_Table = "Constants";
    private static final String Array_Table = "Arrays";

    // Constant Table Columns names
    private static final String C_Id = "Constant_Id";
    private static final String C_Key = "Constant_Key";
    private static final String C_Value = "Constant_Value";
    private static final String C_Array = "Constant_Array";

    // Array Table Columns names
    private static final String A_Id = "Array_Id";
    private static final String A_CID = "Constant_Id";
    private static final String A_CV = "Constant_Array_Value";

    Context context;

    public void setCredential(String appid, String appsecret)
    {
        SharedPreferences.Editor sf = context.getSharedPreferences("com.hoodoomail.inconstant", Context.MODE_PRIVATE).edit();
        sf.putString("AppID",appid);
        sf.putString("AppSecret",appsecret);
        sf.commit();


    }

    public inConstant_DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create Account Table
        String CREATE_CONSTANT_TABLE = "CREATE TABLE " + Constant_Table + "("
                + C_Id + " INTEGER PRIMARY KEY AUTOINCREMENT," + C_Key + " TEXT,"
                + C_Value + " TEXT," + C_Array +" INTEGER)";
        db.execSQL(CREATE_CONSTANT_TABLE);
        String CREATE_ARRAY_TABLE = "CREATE TABLE " + Array_Table + "("
                + A_Id + " INTEGER PRIMARY KEY AUTOINCREMENT," + A_CID + " INTEGER,"
                + A_CV +" TEXT)";
        db.execSQL(CREATE_ARRAY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Constant_Table);
        db.execSQL("DROP TABLE IF EXISTS " + Array_Table);
        // Create tables again
        onCreate(db);
    }

    public void addConstants(JSONArray ja) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;

        deleteConstants();
        for(int i=0;i<ja.length();i++)
        {
            JSONObject tempjo = new JSONObject();
            String temp_key = null;
            boolean temp_array;
            String temp_value = null;
            try {
                tempjo = ja.getJSONObject(i);
                temp_key = tempjo.getString("key") + "";
                temp_array = tempjo.getBoolean("array");
                temp_value = tempjo.getString("value");



                //Decrypt using key AppSecret
                SharedPreferences sf = context.getSharedPreferences("com.hoodoomail.inconstant", Context.MODE_PRIVATE);
                String AppSecret = sf.getString("AppSecret","");

                try {
                    temp_value = AESCrypt.decrypt(AppSecret, temp_value);
                }catch (GeneralSecurityException e){
                    Log.e("error",e.toString());
                    //handle error - could be due to incorrect password or tampered encryptedMsg
                }





                if(temp_array==true)
                {
                    //If constant is an array
                    ContentValues values = new ContentValues();
                    values.put(C_Id, "");
                    values.put(C_Key, temp_key);
                    values.put(C_Array, 1);
                    values.put(C_Value, "");

                    // Inserting Row
                    long cid = db.insert(Constant_Table, null, values);

                    values = new ContentValues();
                    values.put(A_Id, "");
                    values.put(A_CID, cid);
                    values.put(A_CV, temp_value);


                }
                else
                {
                    //If constant is not an array
                    /*
                    ContentValues values = new ContentValues();
                    values.put(C_Id, "");
                    values.put(C_Key, temp_key);
                    values.put(C_Array, 0);
                    values.put(C_Value, temp_value);
                    // Inserting Row
                    long store_id = db.insert(Constant_Table, null, values);

                    */
                    String q = "insert into `"+Constant_Table+"` values (null, '" + temp_key + "','" + temp_value + "',0)";
                    db.execSQL(q);
                }
                //db.close(); // Closing database connection
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //
    public boolean deleteConstants()
    {
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            String delete_constant_query = "delete FROM '" + Constant_Table + "'";
            db.execSQL(delete_constant_query);
            String delete_array_query = "delete FROM '" + Array_Table + "'";
            db.execSQL(delete_array_query);
        }
        catch (Exception e)
        {
            System.out.print(e.toString());
        }
        return true;
    }


    //Get Constant Value
    public String getConstant(String key)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String value=null;

        Cursor cursor = null;
        try
        {
            String query = "select * from '" + Constant_Table + "' where " + C_Key + "='" + key + "'";

            cursor = db.rawQuery(query, null);
            if(cursor.moveToFirst())
            {
                long cid = cursor.getLong(0);
                value = cursor.getString(2);
                int array = cursor.getInt(3);
                if(array>0)
                {
                    //It is an array
                    value = "";
                    query = "select * from " + Array_Table + "' where " + A_CID + "='" + cid + "'";
                    cursor = db.rawQuery(query, null);
                    if(cursor.moveToFirst()) {
                        do {
                            String temp = cursor.getString(2);
                            value = value + temp + "~";
                        } while (cursor.moveToNext());
                    }
                    return value;
                }
                else
                {
                    return value;
                }
            }
        }
        catch (Exception e)
        {
            System.out.print(e.toString());
        }
        return value;
    }

    //Constant Table Size
    public boolean getConstantSize()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = null;
        String query = "select * from " + Constant_Table;
        cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst())
        {
            return true;
        }
        return false;
    }
}
