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
            if (!gameStatus.equals(getStatus(context))) {

                if (!GameStatus.SHOWING_QUESTION.equals(gameStatus) && !GameStatus.SHOWING_OPTIONS.equals(gameStatus)) {
                    QuestionManager.clearQuestion(context);
                }

                updateStatus(gameStatus, context);
                switch (gameStatus) {
                    case SHOWING_QUESTION:
                        ThreadManager.callGetQuestion(context);
                        break;
                    case SHOWING_OPTIONS: {
                        if (QuestionManager.getQuestion(context) == null) {
                            ThreadManager.callGetQuestion(context);
                            StatusManager.updateStatus(GameStatus.SHOWING_QUESTION, context);
                        } else {
                            LocalBroadcastManager broadcaster3 = LocalBroadcastManager.getInstance(context);
                            Intent returnIntent2 = new Intent(BroadcastReceiverConstant.BROADCAST_RECEIVER_MAINACTIVITY);
                            returnIntent2.putExtra(BroadcastReceiverConstant.BROADCAST_RECEIVER_TYPE, BroadcastReceiverType.QUESTION);
                            broadcaster3.sendBroadcast(returnIntent2);
                        }
                        break;
                    }
                    case SHOWING_FINAL_WINNERS: {
                        ThreadManager.callGetWinner(context);

                        callWaitingMessage(gameStatus, context);

                        break;
                    }
                    default: {
                        callWaitingMessage(gameStatus, context);
                        break;
                    }
                }
            }
            ThreadManager.newStatusReceived(gameStatus, context);
        }
    }

    private static void callWaitingMessage(GameStatus gameStatus, Context context) {
        LocalBroadcastManager broadcaster2 = LocalBroadcastManager.getInstance(context);
        Intent intent = new Intent(BroadcastReceiverConstant.BROADCAST_RECEIVER_MAINACTIVITY);
        intent.putExtra(BroadcastReceiverConstant.BROADCAST_RECEIVER_RETURN_OBJECT, gameStatus);
        intent.putExtra(BroadcastReceiverConstant.BROADCAST_RECEIVER_TYPE, BroadcastReceiverType.SHOWING_WAITING_MESSAGE);
        broadcaster2.sendBroadcast(intent);
    }

    public static GameStatus getStatus(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        String status = sharedPreferences.getString(context.getResources().getString(R.string.pref_current_status_key), null);
        return (status != null) ? GameStatus.valueOf(status) : null;
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
