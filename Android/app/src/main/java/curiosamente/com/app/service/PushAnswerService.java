package curiosamente.com.app.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.Profile;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import curiosamente.com.app.R;
import curiosamente.com.app.activities.login.LoginActivity;
import curiosamente.com.app.manager.BarManager;
import curiosamente.com.app.manager.LogInManager;
import curiosamente.com.app.model.Answer;
import curiosamente.com.app.model.Player;

/**
 * Created by Manu on 23/8/16.
 */
public class PushAnswerService extends IntentService {

    public static final String LOG_TAG = PushAnswerService.class.getSimpleName();
    final static public int MAX_RETRY = 3;

    final static public String ID_QUESTION_ANSWER = "idQuestion";
    final static public String ANSWER = "answer";

    public PushAnswerService() {
        super(LOG_TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(LOG_TAG, "Push Answer Intent started");

        ResponseEntity responseEntityPushAnswer = null;
        int retry = 0;
        String idBar = BarManager.getBarId(this);
        Answer answer = new Answer();
        Player player = new Player();
        player.setId(LogInManager.getCurrentUserID(getBaseContext()));
        player.setName(LogInManager.getCurrentUserFirstName(getBaseContext()));
        player.setLastName(LogInManager.getCurrentUserLastName(getBaseContext()));
        answer.setPlayer(player);
        answer.setIdQuestion((String) intent.getExtras().get(ID_QUESTION_ANSWER));
        answer.setAnswer((String) intent.getExtras().get(ANSWER));
        do {
            try {
                Log.i(LOG_TAG, "CALL PUSH ANSWER. Player: " + player.getId() + ". Question: " + answer.getIdQuestion());
                String url = getBaseContext().getResources().getString(R.string.endpoint_admin_server) + getBaseContext().getResources().getString(R.string.url_game_push_answer);
                responseEntityPushAnswer = getRestTemplate().postForEntity(url, answer, Void.class, idBar);
            } catch (Exception e) {
                Log.e(LOG_TAG, "PUSH ANSWER CALL CATCH. Player: " + player.getId() + ". Question: " + answer.getIdQuestion(), e);
            }
            retry++;
        }
        while (responseEntityPushAnswer == null && retry < MAX_RETRY);
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
