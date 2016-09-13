package curiosamente.com.app.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import curiosamente.com.app.R;
import curiosamente.com.app.manager.BarManager;
import curiosamente.com.app.manager.StatusManager;
import curiosamente.com.app.model.GameStatus;

/**
 * Created by Manu on 23/8/16.
 */
public class StatusService extends IntentService {

    public static final String LOG_TAG = StatusService.class.getSimpleName();
    final static public int MAX_RETRY = 3;

    public StatusService() {
        super(LOG_TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(LOG_TAG, "Status Intent started");

        ResponseEntity<GameStatus> gameStatusResponseEntity = null;
        int retry = 0;
        do {
            try {
                String idBar = BarManager.getBarId(this);
                Log.i(LOG_TAG, "STATUS CALL");
                String url = getBaseContext().getResources().getString(R.string.endpoint_admin_server) + getBaseContext().getResources().getString(R.string.url_game_status);
                gameStatusResponseEntity = getRestTemplate().getForEntity(url, GameStatus.class, idBar);
            } catch (Exception e) {
                Log.i(LOG_TAG, "STATUS CALL CATCH");
            }
            retry++;

        } while (gameStatusResponseEntity == null && retry < MAX_RETRY);

        GameStatus gameStatus;

        if (gameStatusResponseEntity == null || gameStatusResponseEntity.getBody() == null) {
            Log.i(LOG_TAG, "STATUS NOT RECEIVED, SET WAITING_TRIVIA STATUS");
            gameStatus = GameStatus.WAITING_TRIVIA;
        } else {
            gameStatus = gameStatusResponseEntity.getBody();
            Log.i(LOG_TAG, "STATUS RECEIVED: " + gameStatus);
        }

        StatusManager.statusReceived(gameStatus, getBaseContext());

    }

    @NonNull
    private RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setReadTimeout(1000);
        ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setConnectTimeout(1000);
        return restTemplate;
    }

}
