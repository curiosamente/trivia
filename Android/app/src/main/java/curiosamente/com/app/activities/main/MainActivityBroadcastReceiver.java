package curiosamente.com.app.activities.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.joda.time.LocalDateTime;

import java.util.Arrays;

import curiosamente.com.app.R;
import curiosamente.com.app.activities.main.Bar.BarFragment;
import curiosamente.com.app.activities.main.Message.WaitingFragment;
import curiosamente.com.app.activities.main.Message.WaitingFragmentUtil;
import curiosamente.com.app.activities.main.Options.QuestionFragment;
import curiosamente.com.app.activities.main.TriviaResult.TriviaResultFragment;
import curiosamente.com.app.manager.BarManager;
import curiosamente.com.app.model.Bar;
import curiosamente.com.app.model.GameStatus;

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
                if(bars != null) {
                    ((BarFragment) fragment).setBars(Arrays.asList(bars));
                }
                break;
            }
            case BAR_SELECTED: {
                mainActivity.initDrawer();
                fragment = new WaitingFragment();
                ((WaitingFragment) fragment).setFragmentMessage(context.getResources().getString(R.string.waiting_fragment_connecting_server));
                break;
            }
            case BAR_LEAVE: {
                BarManager.getBars(context);
                mainActivity.initDrawer();
                fragment = new WaitingFragment();
                ((WaitingFragment) fragment).setFragmentMessage(context.getResources().getString(R.string.waiting_fragment_loading_bars_message));
                break;
            }
            case QUESTION: {
                fragment = new QuestionFragment();
                break;
            }
            case TRIVIA_RESULT: {

                boolean isWinner = (boolean) intent.getExtras().get(BroadcastReceiverConstant.BROADCAST_RECEIVER_RETURN_OBJECT);
                TriviaResultFragment triviaResultFragment = new TriviaResultFragment();
                triviaResultFragment.setWinner(isWinner);
                fragment = triviaResultFragment;
                break;
            }
            case SHOW_TOAST: {
                String string = (String) intent.getExtras().get(BroadcastReceiverConstant.BROADCAST_RECEIVER_RETURN_OBJECT);
                Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
                break;
            }

            case SHOWING_WAITING_MESSAGE: {
                GameStatus gameStatus = (GameStatus) intent.getExtras().get(BroadcastReceiverConstant.BROADCAST_RECEIVER_RETURN_OBJECT);
                String waitingFragmentMessage = WaitingFragmentUtil.getWaitingMessageForStatus(gameStatus, context);

                Fragment currentFragment = mainActivity.getFragmentManager().findFragmentByTag(MainActivity.FRAGMENT_TAG);
                if (currentFragment instanceof WaitingFragment && ((WaitingFragment) currentFragment).getFragmentMessage().equals(waitingFragmentMessage)) {
                    fragment = null;
                } else {
                    WaitingFragment waitingFragment = new WaitingFragment();
                    waitingFragment.setFragmentMessage(waitingFragmentMessage);
                    fragment = waitingFragment;
                }

                break;
            }
        }

        if (fragment != null) {
            long millis = LocalDateTime.now().getMillisOfDay() - mainActivity.fragmentReplacementTimeStamp.getMillisOfDay();
            final Fragment fFragment = fragment;
            if (millis < 1000) {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            replaceFragment(fFragment);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            } else {
                replaceFragment(fFragment);
            }


        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = mainActivity.getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_right);
        fragmentTransaction.replace(R.id.main_layout, fragment, MainActivity.FRAGMENT_TAG);
        fragmentTransaction.commit();
        mainActivity.fragmentReplacementTimeStamp = LocalDateTime.now();
        fm.popBackStack();
    }
}
