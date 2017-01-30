package indinasportsnews.com.isnapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SaveSharedPreferences {
    static final String PREF_FLAG = "IsFirstTimeInstalled";
    static final String PREF_SPORTS_LIST = "SelectedSportsList";
    static final String PREF_LAST_ID = "LastFetchedNewsID" ;
    static final String TAG_TOKEN = "tagtoken";
    static final String PREF_LAST_DATE = "LastFetchedNewsDate" ;

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setFlag(Context ctx, String flag)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_FLAG , flag);
        editor.commit();
    }

    public static String getFlag(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_FLAG, "");
    }

    /*
    public static void setLastDate(Context ctx, String newsDate)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_LAST_DATE , newsDate);
        editor.commit();
    }

    public static String getLastDate(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_LAST_DATE , null);
    }
    */

    public static void setLastId(Context ctx, int newsDate)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(PREF_LAST_ID , newsDate);
        editor.commit();
    }

    public static int getLastId(Context ctx)
    {
        return getSharedPreferences(ctx).getInt(PREF_LAST_ID , 0);
    }

    public static void setPrefSportsList(Context ctx, ArrayList<String> list)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        Set<String> set = new HashSet<String>();
        set.addAll(list);
        editor.putStringSet(PREF_SPORTS_LIST , set);
        editor.commit();
    }

    public static Set<String> getPrefSportsList(Context ctx)
    {
        return getSharedPreferences(ctx).getStringSet(PREF_SPORTS_LIST , null);
    }

    //this method will save the device token to shared preferences
    public static void saveDeviceToken(Context ctx , String token){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(TAG_TOKEN, token);
        editor.commit();
    }

    //this method will fetch the device token from shared preferences
    public static String getDeviceToken(Context ctx){
        return getSharedPreferences(ctx).getString(TAG_TOKEN, "");
    }
}
