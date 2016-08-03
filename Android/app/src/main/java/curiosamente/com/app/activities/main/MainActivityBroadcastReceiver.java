package curiosamente.com.app.activities.main;

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
import curiosamente.com.app.manager.StatusManager;
import curiosamente.com.app.model.Bar;
import curiosamente.com.app.model.GameStatus;
import curiosamente.com.app.model.Question;

public class MainActivityBroadcastReceiver extends BroadcastReceiver {

    private MainActivity mainActivity;

    public MainActivityBroadcastReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Fragment fragment = null;

        BroadcastReceiverType broadcastReceiverType = (BroadcastReceiverType) intent.getExtras().get(BroadcastReceiverConstant.BROADCAST_RECEIVER_TYPE);

        switch (broadcastReceiverType) {
            case BAR_LIST: {
                Bar[] bars = (Bar[]) intent.getExtras().get(BroadcastReceiverConstant.BROADCAST_RECEIVER_RETURN_OBJECT);
                fragment = new BarFragment();
                ((BarFragment) fragment).setBars(Arrays.asList(bars));
                break;
            }
            case BAR_SELECTED: {
                mainActivity.initDrawer();

                fragment = new WaitingFragment();
                ((WaitingFragment) fragment).setFragmentMessage(GameStatus.WAITING_TRIVIA.name());

                break;
            }
            case LEAVE_BAR: {
                BarManager.getBars(context);
                fragment = new WaitingFragment();
                break;
            }

            case QUESTION: {
                Question question = (Question) intent.getExtras().get(BroadcastReceiverConstant.BROADCAST_RECEIVER_RETURN_OBJECT);
                fragment = new WaitingFragment();
                ((WaitingFragment) fragment).setFragmentMessage(question.getQuestion());
                break;
            }
        }

        FragmentManager fm = mainActivity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.main_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
