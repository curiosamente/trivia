package curiosamente.com.app.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import curiosamente.com.app.model.GameStatus;
import curiosamente.com.app.model.Player;
import curiosamente.com.app.model.Question;
import curiosamente.com.app.service.AlarmReceiver;
import curiosamente.com.app.service.HttpService;
import curiosamente.com.app.service.HttpServiceCallTypeEnum;

public class ThreadManager {

    private static final String LOG_TAG = ThreadManager.class.getSimpleName();

    private final static long THREAD_FRECUENCY_IN_MILLIS = 1000;
    private final static long ALARM_FRECUENCY_IN_MILLIS = 60 * 1000;
    public static boolean threadCreated = false;
    public static Thread serviceThread;

    public static void stopCheckingStatus() {
        if (threadCreated) {
            serviceThread.interrupt();
            threadCreated = false;
        }
    }

    public static void newStatusReceived(GameStatus gameStatus, Context context) {
        Log.i(LOG_TAG, "Checking Status For Future Checks Strategy");
        switch (gameStatus) {
            case SHOWING_FINAL_WINNERS:
                if (threadCreated) {
                    serviceThread.interrupt();
                    threadCreated = false;
                }
                break;
//            case WAITING_TRIVIA: {
//                if (threadCreated) {
//                    serviceThread.interrupt();
//                    threadCreated = false;
//                }
//                createAlarmIntent(ALARM_FRECUENCY_IN_MILLIS, context);
//                break;
//            }
            default: {
                createThread(context);
                break;
            }
        }
    }

    private static void createThread(Context context) {
        if (!threadCreated) {
            serviceThread = createThread(THREAD_FRECUENCY_IN_MILLIS, context);
            serviceThread.start();
            threadCreated = true;
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
                        ThreadManager.callCheckStatus(context);
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

    public static void callCheckStatus(Context context) {
        Intent callIntent = new Intent(context, HttpService.class);
        callIntent.putExtra(HttpService.CLASS_EXTRA_PROPERTY, String.class);
        callIntent.putExtra(HttpService.ID_BAR_PARAMETER, BarManager.getBarId(context));
        callIntent.putExtra(HttpService.CALL_TYPE_ENUM_EXTRA_PROPERTY, HttpServiceCallTypeEnum.STATUS);
        context.startService(callIntent);
    }

    public static void callGetQuestion(Context context) {
        Intent callIntent = new Intent(context, HttpService.class);
        callIntent.putExtra(HttpService.CLASS_EXTRA_PROPERTY, Question.class);
        callIntent.putExtra(HttpService.ID_BAR_PARAMETER, BarManager.getBarId(context));
        callIntent.putExtra(HttpService.CALL_TYPE_ENUM_EXTRA_PROPERTY, HttpServiceCallTypeEnum.QUESTION);
        context.startService(callIntent);
    }

    public static void callGetWinner(Context context) {
        Intent callIntent = new Intent(context, HttpService.class);
        callIntent.putExtra(HttpService.CLASS_EXTRA_PROPERTY, Player.class);
        callIntent.putExtra(HttpService.ID_BAR_PARAMETER, BarManager.getBarId(context));
        callIntent.putExtra(HttpService.CALL_TYPE_ENUM_EXTRA_PROPERTY, HttpServiceCallTypeEnum.WINNER);
        context.startService(callIntent);
    }

}
