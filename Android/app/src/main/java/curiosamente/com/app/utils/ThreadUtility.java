package curiosamente.com.app.utils;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

public class ThreadUtility {

    public static boolean threadCreated = false;

    public static Thread serviceThread;


    public Thread createThread(final long sleepInMillis, final Context context) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    while (!Thread.interrupted()) {
                        Intent intent3 = new Intent(context, IntentService.class);
                        Thread.sleep(sleepInMillis);
                        context.startService(intent3);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        return new Thread(runnable);
    }

}
