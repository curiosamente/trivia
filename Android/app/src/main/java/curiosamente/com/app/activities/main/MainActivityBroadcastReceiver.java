package curiosamente.com.app.activities.main;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.Fragment;
import android.widget.Toast;
import org.joda.time.LocalDateTime;
import java.util.Arrays;
import curiosamente.com.app.R;
import curiosamente.com.app.activities.main.Bar.BarFragment;
import curiosamente.com.app.activities.main.Options.QuestionFragment;
import curiosamente.com.app.activities.main.TriviaResult.TriviaResultFragment;
import curiosamente.com.app.activities.main.Waiting.WaitingFragment;
import curiosamente.com.app.manager.BarManager;
import curiosamente.com.app.manager.QuestionManager;
import curiosamente.com.app.model.Bar;
import curiosamente.com.app.model.GameStatus;
import curiosamente.com.app.model.Question;
import curiosamente.com.app.service.HttpService;
import curiosamente.com.app.service.HttpServiceCallTypeEnum;

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
                ((WaitingFragment) fragment).setFragmentMessage(context.getResources().getString(R.string.waiting_fragment_connecting_server));
                break;
            }
            case BAR_LEAVE: {
                BarManager.getBars(context);
                //TODO review initDrawer
                mainActivity.initDrawer();
                fragment = new WaitingFragment();
                ((WaitingFragment) fragment).setFragmentMessage(context.getResources().getString(R.string.waiting_fragment_loading_bars_message));
                break;
            }
            case QUESTION: {
                fragment = new QuestionFragment();
                break;
            }
            case TRIVIA_RESULT:{
                boolean isWinner = (boolean) intent.getExtras().get(BroadcastReceiverConstant.BROADCAST_RECEIVER_RETURN_OBJECT);
                TriviaResultFragment triviaResultFragment = new TriviaResultFragment();
                triviaResultFragment.setWinner(isWinner);
                fragment = triviaResultFragment;
                break;
            }
            case SHOW_TOAST:{
                String string = (String) intent.getExtras().get(BroadcastReceiverConstant.BROADCAST_RECEIVER_RETURN_OBJECT);
                Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
                break;
            }

            case PUSH_ANSWER:{

                Question question = QuestionManager.getQuestion(context);
                Intent intent2 = new Intent(context, HttpService.class);
                intent2.putExtra(HttpService.ID_QUESTION_ANSWER, question.getIdQuestion());
                intent2.putExtra(HttpService.ANSWER, "Catamarca");
                intent2.putExtra(HttpService.CALL_TYPE_ENUM_EXTRA_PROPERTY, HttpServiceCallTypeEnum.PUSH_ANSWER);
                context.startService(intent2);
                break;
            }

            case SHOWING_WAITING_MESSAGE: {
                //TODO switch for GameStatus enum and get String
                String string = intent.getExtras().get(BroadcastReceiverConstant.BROADCAST_RECEIVER_RETURN_OBJECT).toString();
                WaitingFragment waitingFragment = new WaitingFragment();
                waitingFragment.setFragmentMessage(string);
                fragment = waitingFragment;
                break;
            }
        }

        if (fragment != null) {
            long millis = LocalDateTime.now().getMillisOfDay() - mainActivity.fragmentReplacementTimeStamp.getMillisOfDay();
            final Fragment fFragment = fragment;
            if (millis < 500) {
                Thread thread= new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
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
        fragmentTransaction.replace(R.id.main_layout, fragment);
        fragmentTransaction.commit();
        mainActivity.fragmentReplacementTimeStamp = LocalDateTime.now();
        fm.popBackStack();
    }
}
