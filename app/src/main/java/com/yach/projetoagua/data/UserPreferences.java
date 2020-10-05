package com.yach.projetoagua.data;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {
    private SharedPreferences mSharedPreferences;

    public UserPreferences(Context mContext) {
        this.mSharedPreferences = (SharedPreferences) mContext.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
    }

    public void storeString(String key, String value) {
        this.mSharedPreferences.edit().putString(key, value).apply();
    }

    public String getStorageString(String key) {
        return this.mSharedPreferences.getString(key, "");
    }
}
