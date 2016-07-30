package curiosamente.com.app.activities.main;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.Fragment;
import java.util.ArrayList;
import java.util.Arrays;
import curiosamente.com.app.R;
import curiosamente.com.app.activities.main.Bar.BarFragment;
import curiosamente.com.app.activities.main.Waiting.WaitingFragment;
import curiosamente.com.app.manager.BarManager;
import curiosamente.com.app.model.Bar;

public class MainActivityBroadcastReceiver extends BroadcastReceiver {

    final static public String BROADCAST_RECEIVER_MAINACTIVITY = "curiosamente.com.app.service.MainActivity.BroadcastReceiver";

    final static public String BROADCAST_RECEIVER_RETURN_OBJECT = "BroadCastReturnObject";
    final static public String BROADCAST_RECEIVER_TYPE = "BroadCastType";

    final static public String BROADCAST_RECEIVER_TYPE_BAR_LIST = "BAR_LIST";
    final static public String BROADCAST_RECEIVER_SELECTED_BAR = "BAR_SELECTED";
    final static public String BROADCAST_RECEIVER_LEAVE_BAR = "BAR_LEAVE";

    private MainActivity mainActivity;
    public MainActivityBroadcastReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Fragment fragment = null;
        if (intent.getExtras().get(BROADCAST_RECEIVER_TYPE) == BROADCAST_RECEIVER_TYPE_BAR_LIST) {
            Bar[] bars = (Bar[]) intent.getExtras().get(BROADCAST_RECEIVER_RETURN_OBJECT);
            fragment = new BarFragment();
            ((BarFragment)fragment).setBars(new ArrayList<>(Arrays.asList(bars)));
        } else if (intent.getExtras().get(BROADCAST_RECEIVER_TYPE) == BROADCAST_RECEIVER_SELECTED_BAR) {
            Bar bar = (Bar) intent.getExtras().get(BROADCAST_RECEIVER_RETURN_OBJECT);
            mainActivity.initDrawer();
            fragment = new WaitingFragment();
        } else if (intent.getExtras().get(BROADCAST_RECEIVER_TYPE) == BROADCAST_RECEIVER_LEAVE_BAR) {
            BarManager.getBars(context);
            fragment = new WaitingFragment();
        }


        if(fragment != null){
            FragmentManager fm = mainActivity.getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.main_layout, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}
