package curiosamente.com.app.service;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;

import curiosamente.com.app.activities.main.BroadcastReceiverConstant;
import curiosamente.com.app.activities.main.BroadcastReceiverType;
import curiosamente.com.app.manager.BarManager;
import curiosamente.com.app.manager.QuestionManager;
import curiosamente.com.app.manager.StatusManager;
import curiosamente.com.app.model.GameStatus;
import curiosamente.com.app.model.Question;


public class HttpService extends android.app.IntentService {


    public HttpService() {
        super("HttpService");
    }


    final static public String URL_EXTRA_PROPERTY = "url";
    final static public String CLASS_EXTRA_PROPERTY = "class";
    final static public String CALL_TYPE_ENUM_EXTRA_PROPERTY = "callType";
    final static public String ID_BAR_PARAMETER = "idBar";

    public static final String LOG_TAG = HttpService.class.getSimpleName();

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(LOG_TAG, "HttpService intent started");

        if (intent.hasExtra(URL_EXTRA_PROPERTY) && intent.hasExtra(CLASS_EXTRA_PROPERTY) && intent.hasExtra(CALL_TYPE_ENUM_EXTRA_PROPERTY)) {

            String url = (String) intent.getExtras().get(URL_EXTRA_PROPERTY);
            Class returnObjectClass = (Class) intent.getExtras().get(CLASS_EXTRA_PROPERTY);
            HttpServiceCallTypeEnum httpServiceCallTypeEnum = (HttpServiceCallTypeEnum) intent.getExtras().get(CALL_TYPE_ENUM_EXTRA_PROPERTY);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            switch (httpServiceCallTypeEnum) {
                case BAR: {
                    Serializable returnObject = (Serializable) restTemplate.getForObject(url, returnObjectClass);
                    LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(this);
                    Intent returnIntent = new Intent(BroadcastReceiverConstant.BROADCAST_RECEIVER_MAINACTIVITY);
                    returnIntent.putExtra(BroadcastReceiverConstant.BROADCAST_RECEIVER_RETURN_OBJECT, returnObject);
                    returnIntent.putExtra(BroadcastReceiverConstant.BROADCAST_RECEIVER_TYPE, BroadcastReceiverType.BAR_LIST);
                    broadcaster.sendBroadcast(returnIntent);
                    break;
                }
                case STATUS: {
                    GameStatus gameStatus;
                    try {
                        String idBar = BarManager.getBarId(this);
                        gameStatus = restTemplate.getForObject("http://10.0.2.2:8080/game/status?idBar={idBar}", GameStatus.class, idBar);
                    } catch (HttpClientErrorException e) {
                        gameStatus = GameStatus.WAITING_TRIVIA;
                    }
                    StatusManager.statusReceived(gameStatus, getBaseContext());
                    break;
                }
                case QUESTION: {
                    Question question;
                    try {
                        String idBar = BarManager.getBarId(this);
//                        String idBar = (String) intent.getExtras().get(HttpService.ID_BAR_PARAMETER);
                        question = restTemplate.getForObject("http://10.0.2.2:8080/game/currentQuestion?idBar={idBar}", Question.class, idBar);
                    } catch (HttpClientErrorException e) {
                        question = null;
                    }
                    QuestionManager.questionReceived(question, getBaseContext());
                    break;
                }
            }


        }

    }

}
