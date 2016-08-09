package curiosamente.com.app.manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import curiosamente.com.app.R;
import curiosamente.com.app.activities.main.BroadcastReceiverConstant;
import curiosamente.com.app.activities.main.BroadcastReceiverType;
import curiosamente.com.app.model.GameStatus;
import curiosamente.com.app.model.Question;


public class StatusManager {

    private static final String LOG_TAG = StatusManager.class.getSimpleName();

    public static void statusReceived(GameStatus gameStatus, Context context) {
        Log.i(LOG_TAG, "Received Status From Server");
        if (BarManager.isABarSelectedAndValid(context)) {

            //TODO SHOULD CHECK THAT MAIN ACITIVITY HAS RECIEVED FIRST STATUS CHANGE (IF NOT, WILL NEVER BE UPDATED)

            if (!gameStatus.equals(getStatus(context))) {
                updateStatus(gameStatus, context);
                switch (gameStatus) {
                    case SHOWING_QUESTION:
                        ThreadManager.callGetQuestion(context);
                        break;
                    case SHOWING_OPTIONS:
                        LocalBroadcastManager broadcaster3 = LocalBroadcastManager.getInstance(context);
                        Intent returnIntent2 = new Intent(BroadcastReceiverConstant.BROADCAST_RECEIVER_MAINACTIVITY);
                        returnIntent2.putExtra(BroadcastReceiverConstant.BROADCAST_RECEIVER_TYPE, BroadcastReceiverType.QUESTION);
                        broadcaster3.sendBroadcast(returnIntent2);
                        break;
                    case SHOWING_FINAL_WINNERS:
                        LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(context);
                        Intent returnIntent = new Intent(BroadcastReceiverConstant.BROADCAST_RECEIVER_MAINACTIVITY);
                        returnIntent.putExtra(BroadcastReceiverConstant.BROADCAST_RECEIVER_RETURN_OBJECT, true);
                        returnIntent.putExtra(BroadcastReceiverConstant.BROADCAST_RECEIVER_TYPE, BroadcastReceiverType.TRIVIA_RESULT);
                        broadcaster.sendBroadcast(returnIntent);
                        break;
                    default: {
                        LocalBroadcastManager broadcaster2 = LocalBroadcastManager.getInstance(context);
                        Intent intent = new Intent(BroadcastReceiverConstant.BROADCAST_RECEIVER_MAINACTIVITY);
                        intent.putExtra(BroadcastReceiverConstant.BROADCAST_RECEIVER_RETURN_OBJECT, gameStatus);
                        intent.putExtra(BroadcastReceiverConstant.BROADCAST_RECEIVER_TYPE, BroadcastReceiverType.SHOWING_WAITING_MESSAGE);
                        broadcaster2.sendBroadcast(intent);
                        break;
                    }
                }
            }
            ThreadManager.newStatusReceived(gameStatus, context);
        }
    }

    public static GameStatus getStatus(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        String status = sharedPreferences.getString(context.getResources().getString(R.string.pref_current_status_key), null);
        return (status != null)? GameStatus.valueOf(status) : null;
    }

    public static void updateStatus(GameStatus gameStatus, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.pref_current_status_key), gameStatus.name());
        editor.apply();
    }

    public static void clearStatus(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(context.getResources().getString(R.string.pref_current_status_key));
        editor.apply();
    }

}
