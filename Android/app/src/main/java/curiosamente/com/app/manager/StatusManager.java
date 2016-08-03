package curiosamente.com.app.manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import curiosamente.com.app.R;
import curiosamente.com.app.activities.main.BroadcastReceiverConstant;
import curiosamente.com.app.activities.main.BroadcastReceiverType;
import curiosamente.com.app.model.GameStatus;


public class StatusManager {

    private static final String LOG_TAG = StatusManager.class.getSimpleName();

    public static void statusReceived(GameStatus gameStatus, Context context) {
        Log.i(LOG_TAG, "Received Status From Server");

        if (BarManager.isABarSelectedAndValid(context)) {

            if (!getStatus(context).equals(gameStatus)) {
                updateStatus(gameStatus, context);
                switch (gameStatus) {
                    case SHOWING_OPTIONS:
                        ThreadManager.callGetQuestion(context);
                        break;
                }
            }
            ThreadManager.newStatusReceived(gameStatus, context);
        }
    }

    public static GameStatus getStatus(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        return GameStatus.valueOf(sharedPreferences.getString(context.getResources().getString(R.string.pref_current_status_key), GameStatus.WAITING_TRIVIA.name()));
    }

    public static void updateStatus(GameStatus gameStatus, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.pref_current_status_key), gameStatus.name());
        editor.commit();
    }

    public static void gameStatus(GameStatus gameStatus, Context context) {

        LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(context);
        Intent returnIntent = new Intent(BroadcastReceiverConstant.BROADCAST_RECEIVER_MAINACTIVITY);
        returnIntent.putExtra(BroadcastReceiverConstant.BROADCAST_RECEIVER_RETURN_OBJECT, gameStatus);
        returnIntent.putExtra(BroadcastReceiverConstant.BROADCAST_RECEIVER_TYPE, BroadcastReceiverType.TRIVIA_STATUS);
        broadcaster.sendBroadcast(returnIntent);
    }

}
