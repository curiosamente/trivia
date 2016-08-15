package curiosamente.com.app.service;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.facebook.Profile;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;

import curiosamente.com.app.R;
import curiosamente.com.app.activities.main.BroadcastReceiverConstant;
import curiosamente.com.app.activities.main.BroadcastReceiverType;
import curiosamente.com.app.manager.BarManager;
import curiosamente.com.app.manager.LogInManager;
import curiosamente.com.app.manager.PrizeManager;
import curiosamente.com.app.manager.QuestionManager;
import curiosamente.com.app.manager.StatusManager;
import curiosamente.com.app.model.Answer;
import curiosamente.com.app.model.GameStatus;
import curiosamente.com.app.model.Player;
import curiosamente.com.app.model.Question;


public class HttpService extends android.app.IntentService {

    public HttpService() {
        super("HttpService");
    }

    final static public String CLASS_EXTRA_PROPERTY = "class";
    final static public String CALL_TYPE_ENUM_EXTRA_PROPERTY = "callType";
    final static public String ID_BAR_PARAMETER = "idBar";
    final static public String ID_QUESTION_ANSWER = "idQuestion";
    final static public String ANSWER = "answer";
    final static public int MAX_RETRY = 2;

    public static final String LOG_TAG = HttpService.class.getSimpleName();

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(LOG_TAG, "HttpService intent started");

        if (intent.hasExtra(CLASS_EXTRA_PROPERTY) && intent.hasExtra(CALL_TYPE_ENUM_EXTRA_PROPERTY)) {
            Class returnObjectClass = (Class) intent.getExtras().get(CLASS_EXTRA_PROPERTY);
            HttpServiceCallTypeEnum httpServiceCallTypeEnum = (HttpServiceCallTypeEnum) intent.getExtras().get(CALL_TYPE_ENUM_EXTRA_PROPERTY);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setReadTimeout(1000);
            switch (httpServiceCallTypeEnum) {
                case BAR: {
                    Serializable returnObject = (Serializable) restTemplate.getForObject(getBaseContext().getResources().getString(R.string.url_bar), returnObjectClass);
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
                        Log.i(LOG_TAG, "STATUS CALL");
                        gameStatus = restTemplate.getForObject(getBaseContext().getResources().getString(R.string.url_game_status), GameStatus.class, idBar);
                    } catch (HttpClientErrorException | ResourceAccessException e) {
                        gameStatus = GameStatus.WAITING_TRIVIA;
                    }
                    Log.i(LOG_TAG, "Status received: " + gameStatus);
                    StatusManager.statusReceived(gameStatus, getBaseContext());
                    break;
                }
                case QUESTION: {
                    Question question;
                    String idBar = BarManager.getBarId(this);
                    int retry = 0;
                    do {
                        try {
                            Log.i(LOG_TAG, "QUESTION CALL");
                            question = restTemplate.getForObject(getBaseContext().getResources().getString(R.string.url_game_current_question), Question.class, idBar);
                        } catch (HttpClientErrorException | ResourceAccessException e) {
                            Log.e(LOG_TAG, "QUESTION CALL CATCH", e);
                            question = null;
                            retry++;
                        }
                    } while (question == null && retry < MAX_RETRY);
                    QuestionManager.questionReceived(question, getBaseContext());
                    break;
                }
                case WINNER: {
                    Player winner;
                    int retry = 0;
                    String idBar = BarManager.getBarId(this);

                    do {
                        try {
                            Log.i(LOG_TAG, "WINNER");
                            winner = restTemplate.getForObject(getBaseContext().getResources().getString(R.string.url_game_winner), Player.class, idBar);
                        } catch (HttpClientErrorException | ResourceAccessException e) {
                            Log.e(LOG_TAG, "WINNER CALL CATCH", e);
                            retry++;
                            winner = null;
                        }
                    } while (winner == null && retry < MAX_RETRY);

                    boolean isWinner = winner != null && LogInManager.getCurrentUserID().equals(winner.getId());
                    if (isWinner) {
                        PrizeManager.createAndStorePrize(getBaseContext());
                    }
                    LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getBaseContext());
                    Intent returnIntent = new Intent(BroadcastReceiverConstant.BROADCAST_RECEIVER_MAINACTIVITY);
                    returnIntent.putExtra(BroadcastReceiverConstant.BROADCAST_RECEIVER_RETURN_OBJECT, isWinner);
                    returnIntent.putExtra(BroadcastReceiverConstant.BROADCAST_RECEIVER_TYPE, BroadcastReceiverType.TRIVIA_RESULT);
                    broadcaster.sendBroadcast(returnIntent);
                    break;
                }
                case PUSH_ANSWER: {
                    int retry = 0;
                    HttpStatus httpStatus;
                    String idBar = BarManager.getBarId(this);
                    Answer answer = new Answer();
                    Player player = new Player();
                    player.setId(Profile.getCurrentProfile().getId());
                    player.setName(Profile.getCurrentProfile().getFirstName());
                    player.setLastName(Profile.getCurrentProfile().getLastName());
                    answer.setPlayer(player);
                    answer.setIdQuestion((String) intent.getExtras().get(HttpService.ID_QUESTION_ANSWER));
                    answer.setAnswer((String) intent.getExtras().get(HttpService.ANSWER));
                    do {
                        try {
                            Log.i(LOG_TAG, "PUSH");
                            httpStatus = restTemplate.postForEntity(getBaseContext().getResources().getString(R.string.url_game_push_answer), answer, Void.class, idBar).getStatusCode();
                        } catch (HttpClientErrorException  e) {
                            retry++;
                            httpStatus = e.getStatusCode();
                        } catch (ResourceAccessException e ){
                            Log.e(LOG_TAG, "PUSH CALL TIMEOUT", e);
                            httpStatus = HttpStatus.REQUEST_TIMEOUT;
                        }
                    } while (!httpStatus.equals(HttpStatus.OK) && !httpStatus.equals(HttpStatus.ACCEPTED) && retry < MAX_RETRY);
                    break;
                }
            }
        }
    }
}
