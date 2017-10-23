package com.andarpoblacion.andrade.andar;

/**
 * Created by Alyssa-PC on 9/23/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "mysharedpref12";
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_ADDRESS = "address";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_PHONE = "phone";
    private static final String KEY_USER_BDAY = "bday";
    private static final String KEY_USER_STATUS = "status";
    private static final String KEY_USER_USERTYPE = "usertype";
    private static final String KEY_USER_OTHERS = "others";


    private SharedPrefManager(Context context) {
        mCtx = context;

    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean userLogin(int id, String name, String phone, String bday, String address, String email, String password, String status, String userType ){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_USER_ID, id);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_PHONE, phone);
        editor.putString(KEY_USER_BDAY, bday);
        editor.putString(KEY_USER_ADDRESS, address);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_PASSWORD, password);
        editor.putString(KEY_USER_STATUS, status);
        editor.putString(KEY_USER_USERTYPE, userType);
        editor.apply();

        return true;
    }

    public boolean isAddedOthers(String others ){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USER_OTHERS, others);
        editor.apply();

        return true;
    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if(sharedPreferences.getString(KEY_USER_EMAIL, null) != null){
            return true;
        }
        return false;
    }

    public boolean logout(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    public String getUserId(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    public String getUserName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_NAME, null);
    }

    public String getUserPhone(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_PHONE, null);
    }

    public String getUserBday(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_BDAY, null);
    }

    public String getUserAddress(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ADDRESS, null);
    }

    public String getUserEmail(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }

    public String getUserPassword(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_PASSWORD, null);
    }

    public String getUserStatus(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_STATUS, null);
    }

    public String getUserType(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_USERTYPE, null);
    }

    public String getUserOthers(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_OTHERS, null);
    }
}

