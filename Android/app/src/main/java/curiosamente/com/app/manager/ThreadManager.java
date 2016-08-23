package curiosamente.com.app.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import curiosamente.com.app.model.GameStatus;
import curiosamente.com.app.service.AlarmReceiver;
import curiosamente.com.app.service.QuestionService;
import curiosamente.com.app.service.StatusService;
import curiosamente.com.app.service.WinnerService;

public class ThreadManager {

    private static final String LOG_TAG = ThreadManager.class.getSimpleName();

    private final static long THREAD_FRECUENCY_IN_MILLIS = 1000;
    public static boolean threadCreated = false;
    public static boolean createNewThread = true;
    public static Thread serviceThread;

    public static void stopCheckingStatus() {
        if (threadCreated) {
            serviceThread.interrupt();
            threadCreated = false;
            createNewThread = false;
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
            default: {
                createThread(context);
                break;
            }
        }
    }

    private static void createThread(Context context) {
        if (!threadCreated && createNewThread) {
            serviceThread = createThread(THREAD_FRECUENCY_IN_MILLIS, context);
            serviceThread.start();
            threadCreated = true;
        }
    }


    public static Thread createThread(final long frecuencyInMillis, final Context context) {
        Log.i(LOG_TAG, "Creating New Thread For checking Status");
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

    public static void callCheckStatus(Context context) {
        Intent callIntent = new Intent(context, StatusService.class);
        context.startService(callIntent);
    }

    public static void callGetQuestion(Context context) {
        Intent callIntent = new Intent(context, QuestionService.class);
        context.startService(callIntent);
    }

    public static void callGetWinner(Context context) {
        Intent callIntent = new Intent(context, WinnerService.class);
        context.startService(callIntent);
    }

}
