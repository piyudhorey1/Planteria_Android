package com.example.planteria.utils

import android.content.Context
import android.content.SharedPreferences

class PrefHelper(context : Context) {

    private val PREFNAME = "PlanteriaPref"
    private var sharedPref: SharedPreferences
    val editor: SharedPreferences.Editor


    companion object {
        const val PREF_IS_LOGIN = "PREF_IS_LOGIN"
        const val USER_DETAIL = "USER_DETAIL"
        const val TOKEN = "TOKEN"
        const val VERIFY_OTP_TOKEN = "VERIFY_OTP_TOKEN"
        const val AVATAR_IMG_URL = "AVATAR_IMG_URL"
        const val USER_CREDIT = "USER_CREDIT"
        const val PREF_USERNAME = "PREF_USERNAME"
        const val ORDER_ID = "ORDER_ID"
        const val CART_COUNT = "CART_COUNT"

    }

    init {
        sharedPref = context.getSharedPreferences(PREFNAME, Context.MODE_PRIVATE)
        editor = sharedPref.edit()
    }


    fun getString(key:String): String ?{
        return sharedPref.getString(key,"")
    }

    fun getInt(key:String): Int {
        return sharedPref.getInt(key,0)
    }

    fun putBoolean(key:String, value:Boolean){
        editor.putBoolean(key,value)
            .apply()
    }

    fun getBoolean(key:String): Boolean {
        return sharedPref.getBoolean(key,false)
    }

    fun clear(){
        editor.clear()
            .apply()
    }

    fun putString(key: String, value: String?) {
        editor.putString(key,value)
            .apply()
    }
    fun putInt(key: String, value: Int) {
        editor.putInt(key,value)
            .apply()
    }

    fun getPref(): SharedPreferences {
        return sharedPref
    }
}