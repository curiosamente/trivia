package curiosamente.com.app.manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import org.joda.time.DateTime;
import org.joda.time.Hours;

import curiosamente.com.app.R;
import curiosamente.com.app.model.Bar;
import curiosamente.com.app.service.HttpService;
import curiosamente.com.app.service.HttpServiceCallTypeEnum;


public class BarManager {

    private static final int SELECTED_BAR_REFRESH_IN_HOURS = 12;

    public static boolean isABarSelectedAndValid(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);

        String selectedKeyBar = context.getResources().getString(R.string.pref_selected_bar_id_key);
        String idbar = sharedPreferences.getString(selectedKeyBar,null);

        String selectedKeyBarTimeStamp = context.getResources().getString(R.string.pref_selected_bar_timestamp_key);
        DateTime barTimestamp = new DateTime(sharedPreferences.getLong(selectedKeyBarTimeStamp,-1));
        int hours = barTimestamp.isEqual(new DateTime(-1))? SELECTED_BAR_REFRESH_IN_HOURS : Hours.hoursBetween(barTimestamp, DateTime.now()).getHours();

        return idbar != null && hours < SELECTED_BAR_REFRESH_IN_HOURS;
    }

    public static void leaveBar(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(context.getResources().getString(R.string.pref_selected_bar_id_key));
        editor.remove(context.getResources().getString(R.string.pref_selected_bar_timestamp_key));
        editor.commit();
        StatusManager.clearStatus(context);
        ThreadManager.stopCheckingStatus();
    }

    public static String getBarName(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        return sharedPreferences.getString(context.getResources().getString(R.string.pref_selected_bar_name_key),null);
    }

    public static String getBarId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        return sharedPreferences.getString(context.getResources().getString(R.string.pref_selected_bar_id_key),null);
    }

    public static void getBars(Context context){
        Intent intent = new Intent(context, HttpService.class);
        intent.putExtra(HttpService.CLASS_EXTRA_PROPERTY, Bar[].class);
        intent.putExtra(HttpService.CALL_TYPE_ENUM_EXTRA_PROPERTY, HttpServiceCallTypeEnum.BAR);
        context.startService(intent);
    }

    public static void storeSelectedBarPreference(Context context, Bar bar){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.pref_selected_bar_id_key), bar.getIdBar());
        editor.putString(context.getResources().getString(R.string.pref_selected_bar_name_key), bar.getName());
        editor.putLong(context.getResources().getString(R.string.pref_selected_bar_timestamp_key), DateTime.now().getMillis());
        editor.commit();
        ThreadManager.callCheckStatus(context);
    }

    public static void updateSelectedBarTimeStamp(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(context.getResources().getString(R.string.pref_selected_bar_timestamp_key), DateTime.now().getMillis());
        editor.commit();
    }
}
