package curiosamente.com.app.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import curiosamente.com.app.manager.StatusCheckManager;

public class AlarmReceiver extends BroadcastReceiver {
    private final String LOG_TAG = AlarmReceiver.class.getSimpleName();

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(LOG_TAG, "Status Checked through Alarm Receiver");
        StatusCheckManager.callCheckStatus(context);
    }
}