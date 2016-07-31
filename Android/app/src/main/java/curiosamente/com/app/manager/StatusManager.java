package curiosamente.com.app.manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import curiosamente.com.app.R;
import curiosamente.com.app.activities.main.MainActivityBroadcastReceiver;


public class StatusManager {

    private static final String LOG_TAG = StatusManager.class.getSimpleName();

    public static void statusReceived(String status, Context context) {
        Log.i(LOG_TAG, "Received Status From Server");

        if (BarManager.isABarSelectedAndValid(context)) {

            //TODO SHOULD CHECK THAT MAIN ACITIVITY HAS RECIEVED FIRST STATUS CHANGE (IF NOT, WILL NEVER BE UPDATED)
            if (getStatus(context) != status) {
                updateStatus(status, context);

                LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(context);
                Intent returnIntent = new Intent(MainActivityBroadcastReceiver.BROADCAST_RECEIVER_MAINACTIVITY);
                returnIntent.putExtra(MainActivityBroadcastReceiver.BROADCAST_RECEIVER_RETURN_OBJECT, status);
                returnIntent.putExtra(MainActivityBroadcastReceiver.BROADCAST_RECEIVER_TYPE, MainActivityBroadcastReceiver.BROADCAST_RECEIVER_TYPE_TRIVIA_STATUS);
                broadcaster.sendBroadcast(returnIntent);
            }
            StatusCheckManager.newStatusReceived(status, context);
        }
    }

    public static String getStatus(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        return sharedPreferences.getString(context.getResources().getString(R.string.pref_current_status_key), null);
    }

    public static void updateStatus(String status, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.pref_current_status_key), status);
        editor.commit();
    }
}
