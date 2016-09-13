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
import curiosamente.com.app.manager.QuestionManager;
import curiosamente.com.app.model.Question;

/**
 * Created by Manu on 23/8/16.
 */
public class QuestionService extends IntentService {

    public static final String LOG_TAG = QuestionService.class.getSimpleName();
    final static public int MAX_RETRY = 3;

    public QuestionService() {
        super(LOG_TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(LOG_TAG, "Question Intent started");

        ResponseEntity<Question> questionResponseEntity = null;

        String idBar = BarManager.getBarId(this);
        int retry = 0;
        do {
            try {
                Log.i(LOG_TAG, "QUESTION CALL");


                String url = getBaseContext().getResources().getString(R.string.endpoint_admin_server) + getBaseContext().getResources().getString(R.string.url_game_current_question);
                questionResponseEntity = getRestTemplate().getForEntity(url, Question.class, idBar);
            } catch (Exception e) {
                Log.e(LOG_TAG, "QUESTION CALL CATCH", e);
            }

            retry++;

        } while (questionResponseEntity == null && retry < MAX_RETRY);
        if (questionResponseEntity != null) {
            Log.i(LOG_TAG, "Question Received: " + questionResponseEntity.getBody());
            QuestionManager.questionReceived(questionResponseEntity.getBody(), getBaseContext());
        }

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
