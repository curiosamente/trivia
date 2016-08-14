package curiosamente.com.app.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import curiosamente.com.app.R;
import curiosamente.com.app.model.Player;

/**
 * Created by Manu on 13/8/16.
 */
public class WinnerManager {

    private static final String LOG_TAG = WinnerManager.class.getSimpleName();

    public static void winnerReceived(Player player, Context context) {
        Log.i(LOG_TAG, "Received Player from server");

        if (BarManager.isABarSelectedAndValid(context)) {
            if (player != null) {
                updateWinner(player, context);
            }
        }
    }

    public static Player getWinner(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String winnerJSON = sharedPreferences.getString(context.getResources().getString(R.string.pref_winner_key), "{}");
        Player player = null;
        try {
            player = new ObjectMapper().readValue(winnerJSON, Player.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return player;
    }

    public static void updateWinner(Player player, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.pref_winner_key), player != null ? player.toString() : "{}");
        editor.commit();
    }


}
