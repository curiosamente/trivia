package curiosamente.com.app.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import curiosamente.com.app.R;
import curiosamente.com.app.model.GameStatus;
import curiosamente.com.app.service.HttpServiceCallTypeEnum;
import curiosamente.com.app.service.AlarmReceiver;
import curiosamente.com.app.service.HttpService;

public class StatusCheckManager {

    private static final String LOG_TAG = StatusCheckManager.class.getSimpleName();

    private static Map<String, GameStatus> GAME_STATUS_MAP = new HashMap<>();
    private final static long THREAD_FRECUENCY_IN_MILLIS = 1 * 1000;
    private final static long ALARM_FRECUENCY_IN_MILLIS = 60 * 1000;

    public static boolean threadCreated = false;
    public static Thread serviceThread;

    public static void stopCheckingStatus(){
        if(threadCreated){
            serviceThread.interrupt();
            threadCreated = false;
        }
    }

    public static void newStatusReceived(String status, Context context) {
        Log.i(LOG_TAG, "Checking Status For Future Checks Strategy");
        if (GAME_STATUS_MAP.size() == 0) {
            populateGameStatusMap();
        }

        if (GAME_STATUS_MAP.containsKey(status)) {
            if (!threadCreated) {
                serviceThread = createThread(THREAD_FRECUENCY_IN_MILLIS, context);
                serviceThread.start();
                threadCreated = true;
            }
        } else {
            if (threadCreated) {
                serviceThread.interrupt();
                threadCreated = false;
            }
            createAlarmIntent(ALARM_FRECUENCY_IN_MILLIS, context);
        }
    }


    public static Thread createThread(final long frecuencyInMillis, final Context context) {
        Log.i("LOG_TAG", "Creating New Thread For checking Status");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Thread.interrupted()) {
                        Log.i("LOG_TAG", "Status Checked through Thread");
                        StatusCheckManager.callCheckStatus(context);
                        Thread.sleep(frecuencyInMillis);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        return new Thread(runnable);
    }

    public static void createAlarmIntent(final long frecuencyInMillis, final Context context) {
        Log.i(LOG_TAG, "Created Alarm");
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + frecuencyInMillis, alarmIntent);
    }

    public static void populateGameStatusMap() {
        for (GameStatus gameStatus : GameStatus.values()) {
            GAME_STATUS_MAP.put(gameStatus.name(), gameStatus);
        }
    }

    public static void callCheckStatus(Context context) {
        Intent callIntent = new Intent(context, HttpService.class);
        callIntent.putExtra(HttpService.URL_EXTRA_PROPERTY, context.getResources().getString(R.string.url_bar));
        callIntent.putExtra(HttpService.CLASS_EXTRA_PROPERTY, String.class);
        callIntent.putExtra(HttpService.CALL_TYPE_ENUM_EXTRA_PROPERTY, HttpServiceCallTypeEnum.Status);
        context.startService(callIntent);
    }



}
