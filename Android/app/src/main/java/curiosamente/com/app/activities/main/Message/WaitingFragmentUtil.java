package curiosamente.com.app.activities.main.Message;

import android.content.Context;

import curiosamente.com.app.R;
import curiosamente.com.app.model.GameStatus;

public class WaitingFragmentUtil {

    public static String getWaitingMessageForStatus(GameStatus gameStatus, Context context) {
        String messageString = context.getResources().getString(R.string.waiting_fragment_default_message);

        switch (gameStatus) {
            case STARTING_TRIVIA: {
                messageString = context.getResources().getString(R.string.waiting_fragment_starting_trivia);
                break;
            }
            case WAITING_TRIVIA: {
                messageString = context.getResources().getString(R.string.waiting_fragment_waiting_trivia);
                break;
            }
            case SHOWING_QUESTION: {
                messageString = context.getResources().getString(R.string.waiting_fragment_showing_question);
                break;
            }
            case WAITING_CORRECT_ANSWER: {
                messageString = context.getResources().getString(R.string.waiting_fragment_waiting_game);
                break;
            }
            case SHOWING_CORRECT_ANSWER: {
                messageString = context.getResources().getString(R.string.waiting_fragment_waiting_game);
                break;
            }
            case SHOWING_DESCRIPTION: {
                messageString = context.getResources().getString(R.string.waiting_fragment_waiting_game);
                break;
            }
            case SHOWING_PARTIAL_WINNERS: {
                messageString = context.getResources().getString(R.string.waiting_fragment_waiting_game);
                break;
            }
            case SHOWING_BANNER: {
                messageString = context.getResources().getString(R.string.waiting_fragment_waiting_game);
                break;
            }
            case SHOWING_FINAL_WINNERS: {
                messageString = context.getResources().getString(R.string.waiting_fragment_waiting_result);
                break;
            }
        }

        return messageString;
    }

}
