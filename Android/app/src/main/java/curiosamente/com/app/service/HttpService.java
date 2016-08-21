package curiosamente.com.app.service;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.facebook.Profile;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
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
import curiosamente.com.app.model.Bar;
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
    final static public int MAX_RETRY = 3;

    public static final String LOG_TAG = HttpService.class.getSimpleName();

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(LOG_TAG, "HttpService intent started");

        if (intent.hasExtra(CLASS_EXTRA_PROPERTY) && intent.hasExtra(CALL_TYPE_ENUM_EXTRA_PROPERTY)) {

            HttpServiceCallTypeEnum httpServiceCallTypeEnum = (HttpServiceCallTypeEnum) intent.getExtras().get(CALL_TYPE_ENUM_EXTRA_PROPERTY);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ((SimpleClientHttpRequestFactory) restTemplate.getRequestFactory()).setReadTimeout(1000);

            switch (httpServiceCallTypeEnum) {
                case BAR: {

                    ResponseEntity<Bar[]> responseEntityBars = null;
                    int retry = 0;
                    do {
                        try {
                            responseEntityBars = restTemplate.getForEntity(getBaseContext().getResources().getString(R.string.url_bar), Bar[].class);
                        } catch (Exception e) {
                            Log.e(LOG_TAG, "BAR CALL CATCH", e);
                            retry++;
                        }
                    } while (responseEntityBars == null && retry < MAX_RETRY);

                    if (responseEntityBars != null && responseEntityBars.getBody() != null) {
                        LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(this);
                        Intent returnIntent = new Intent(BroadcastReceiverConstant.BROADCAST_RECEIVER_MAINACTIVITY);
                        returnIntent.putExtra(BroadcastReceiverConstant.BROADCAST_RECEIVER_RETURN_OBJECT, responseEntityBars.getBody());
                        returnIntent.putExtra(BroadcastReceiverConstant.BROADCAST_RECEIVER_TYPE, BroadcastReceiverType.BAR_LIST);
                        broadcaster.sendBroadcast(returnIntent);
                    }
                    break;
                }
                case STATUS: {
                    ResponseEntity<GameStatus> gameStatusResponseEntity = null;
                    int retry = 0;
                    do {
                        try {
                            String idBar = BarManager.getBarId(this);
                            Log.i(LOG_TAG, "STATUS CALL");
                            gameStatusResponseEntity = restTemplate.getForEntity(getBaseContext().getResources().getString(R.string.url_game_status), GameStatus.class, idBar);
                            Log.i(LOG_TAG, "STATUS CALL " + gameStatusResponseEntity.getBody());
                        } catch (Exception e) {
                            Log.i(LOG_TAG, "STATUS CALL CATCH");
                            retry++;
                        }
                    } while (gameStatusResponseEntity == null && retry < MAX_RETRY);

                    GameStatus gameStatus;

                    if (gameStatusResponseEntity == null || gameStatusResponseEntity.getBody() == null) {
                        gameStatus = GameStatus.WAITING_TRIVIA;
                    } else {
                        gameStatus = gameStatusResponseEntity.getBody();
                    }

                    Log.i(LOG_TAG, "Status received: " + gameStatus);
                    StatusManager.statusReceived(gameStatus, getBaseContext());
                    break;
                }
                case QUESTION: {
                    ResponseEntity<Question> questionResponseEntity = null;

                    String idBar = BarManager.getBarId(this);
                    int retry = 0;
                    do {
                        try {
                            Log.i(LOG_TAG, "QUESTION CALL");
                            questionResponseEntity = restTemplate.getForEntity(getBaseContext().getResources().getString(R.string.url_game_current_question), Question.class, idBar);
                        } catch (Exception e) {
                            Log.e(LOG_TAG, "QUESTION CALL CATCH", e);
                            retry++;
                        }
                    } while (questionResponseEntity == null && retry < MAX_RETRY);
                    if (questionResponseEntity != null) {
                        QuestionManager.questionReceived(questionResponseEntity.getBody(), getBaseContext());
                    }
                    break;
                }
                case WINNER: {
                    ResponseEntity<Player> playerResponseEntity = null;
                    Player winner;
                    int retry = 0;
                    String idBar = BarManager.getBarId(this);

                    do {
                        try {
                            Log.i(LOG_TAG, "WINNER");
                            playerResponseEntity = restTemplate.getForEntity(getBaseContext().getResources().getString(R.string.url_game_winner), Player.class, idBar);
                        } catch (Exception e) {
                            Log.e(LOG_TAG, "WINNER CALL CATCH", e);
                            retry++;
                        }
                    } while (playerResponseEntity == null && retry < MAX_RETRY);

                    if(playerResponseEntity!=null){
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
                    break;
                }
                case PUSH_ANSWER: {
                    ResponseEntity responseEntityPushAnswer = null;
                    int retry = 0;
                    HttpStatus httpStatus = null;
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
                            Log.i(LOG_TAG, "PUSH ANSWER");
                            responseEntityPushAnswer = restTemplate.postForEntity(getBaseContext().getResources().getString(R.string.url_game_push_answer), answer, Void.class, idBar);
                        } catch (Exception e) {
                            Log.e(LOG_TAG, "PUSH ANSWER CALL CATCH", e);
                            retry++;
                        }
                    }
                    while (responseEntityPushAnswer == null && retry < MAX_RETRY);
                    break;
                }
            }
        }
    }
}
