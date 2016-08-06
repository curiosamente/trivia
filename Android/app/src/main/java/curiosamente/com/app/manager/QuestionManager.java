package curiosamente.com.app.manager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import curiosamente.com.app.R;
import curiosamente.com.app.activities.main.BroadcastReceiverConstant;
import curiosamente.com.app.activities.main.BroadcastReceiverType;
import curiosamente.com.app.model.Question;


public class QuestionManager {

    private static final String LOG_TAG = QuestionManager.class.getSimpleName();

    public static void questionReceived(Question question, Context context) {
        Log.i(LOG_TAG, "Received Question from server");

        if (BarManager.isABarSelectedAndValid(context)) {

            if (question != null && !getQuestion(context).equals(question)) {
                updateQuestion(question, context);
            }
        }
    }

    public static Question getQuestion(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String questionJSON = sharedPreferences.getString(context.getResources().getString(R.string.pref_current_question_key), "{}");
        Question question = null;
        try {
            question = new ObjectMapper().readValue(questionJSON, Question.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return question;
    }

    public static void updateQuestion(Question question, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.pref_current_question_key), question != null ? question.toString() : "{}");
        editor.commit();
    }

}
