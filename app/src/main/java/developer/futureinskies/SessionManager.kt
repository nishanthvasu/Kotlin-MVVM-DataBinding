package developer.futureinskies

import android.app.Activity
import android.content.Context
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fortuner.ai.Login.DataModel.MissionResponseData

/**
 * Created by Nishanth on 20/9/18.
 */

object SessionManager {

    fun saveSession(key: String?, value: String, context: Context) {
        val editor = context.getSharedPreferences("KEY",
                Activity.MODE_PRIVATE).edit()
        editor.putString(key, value)
        editor.commit()
    }

    fun getSession(key: String?, context: Context): String {
        val pref = context.getSharedPreferences("KEY",
                Activity.MODE_PRIVATE)
        return pref.getString(key, "")
    }

    fun saveSessionuUtc(key: String?, value: Long, context: Context) {
        val editor = context.getSharedPreferences("KEY",
                Activity.MODE_PRIVATE).edit()
        editor.putLong(key, value)
        editor.commit()
    }

    fun getSessionUtc(key: String?, context: Context): Long {
        val pref = context.getSharedPreferences("KEY",
                Activity.MODE_PRIVATE)
        return pref.getLong(key, 10000000000)
    }

    fun ClearSession(context: Context) {
        val editor = context.getSharedPreferences("KEY",
                Activity.MODE_PRIVATE).edit()
        editor.clear()
        editor.apply()
    }

    fun savePayLoadsArrayList(key: String, list: ArrayList<MissionResponseData.RocketsData.SecondStage.PayLoadsData>?, cxt: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(cxt)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString(key, json)
        editor.apply()     // This line is IMPORTANT !!!
    }

    fun getPayLoadsArrayList(key: String, cxt: Context): ArrayList<MissionResponseData.RocketsData.SecondStage.PayLoadsData>? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(cxt)
        val gson = Gson()
        val json = prefs.getString(key, null)
        val type = object : TypeToken<ArrayList<MissionResponseData.RocketsData.SecondStage.PayLoadsData>>() {

        }.type
        return gson.fromJson<ArrayList<MissionResponseData.RocketsData.SecondStage.PayLoadsData>>(json, type)
    }

}
