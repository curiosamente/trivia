package curiosamente.com.app.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import curiosamente.com.app.R;
import curiosamente.com.app.activities.main.BroadcastReceiverConstant;
import curiosamente.com.app.activities.main.BroadcastReceiverType;
import curiosamente.com.app.manager.BarManager;
import curiosamente.com.app.manager.LogInManager;
import curiosamente.com.app.manager.PrizeManager;
import curiosamente.com.app.model.Player;

/**
 * Created by Manu on 23/8/16.
 */
public class WinnerService extends IntentService {

    public static final String LOG_TAG = WinnerService.class.getSimpleName();
    final static public int MAX_RETRY = 3;

    public WinnerService() {
        super(LOG_TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(LOG_TAG, "Winner Intent started");

        ResponseEntity<Player> playerResponseEntity = null;
        Player winner;
        int retry = 0;
        String idBar = BarManager.getBarId(this);

        do {
            try {
                Log.i(LOG_TAG, "WINNER");
                playerResponseEntity = getRestTemplate().getForEntity(getBaseContext().getResources().getString(R.string.url_game_winner), Player.class, idBar);
            } catch (Exception e) {
                Log.e(LOG_TAG, "WINNER CALL CATCH", e);
            }
            retry++;
        } while (playerResponseEntity == null && retry < MAX_RETRY);

        if (playerResponseEntity != null) {
            winner = playerResponseEntity.getBody();
            boolean isWinner = winner != null && LogInManager.getCurrentUserID().equals(winner.getId());
            if (isWinner) {
                PrizeManager.createAndStorePrize(getBaseContext());
            }
            LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getBaseContext());
            Intent returnIntent = new Intent(BroadcastReceiverConstant.BROADCAST_RECEIVER_MAINACTIVITY);
            returnIntent.putExtra(BroadcastReceiverConstant.BROADCAST_RECEIVER_RETURN_OBJECT, isWinner);
            returnIntent.putExtra(BroadcastReceiverConstant.BROADCAST_RECEIVER_TYPE, BroadcastReceiverType.TRIVIA_RESULT);
            broadcaster.sendBroadcast(returnIntent);
        }

    }

    @NonNull
    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setReadTimeout(1000);
        return restTemplate;
    }

}
