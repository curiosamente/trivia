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
import curiosamente.com.app.model.Bar;

/**
 * Created by Manu on 23/8/16.
 */
public class BarService extends IntentService {

    public static final String LOG_TAG = BarService.class.getSimpleName();
    final static public int MAX_RETRY = 3;

    public BarService() {
        super(LOG_TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(LOG_TAG, "BAR Intent started");

        ResponseEntity<Bar[]> responseEntityBars = null;

        int retry = 0;
        String barUrl = getBaseContext().getResources().getString(R.string.url_bar);
        do {
            try {
                Log.i(LOG_TAG, "BAR CALL");
                responseEntityBars = getRestTemplate().getForEntity(barUrl, Bar[].class);
            } catch (Exception e) {
                Log.e(LOG_TAG, "BAR CALL CATCH", e);
            }
            retry++;
        } while (responseEntityBars == null && retry < MAX_RETRY);

        if (responseEntityBars != null && responseEntityBars.getBody() != null) {
            Log.i(LOG_TAG, "Bar List Received: " + responseEntityBars.getBody());
            LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(this);
            Intent returnIntent = new Intent(BroadcastReceiverConstant.BROADCAST_RECEIVER_MAINACTIVITY);
            returnIntent.putExtra(BroadcastReceiverConstant.BROADCAST_RECEIVER_RETURN_OBJECT, responseEntityBars.getBody());
            returnIntent.putExtra(BroadcastReceiverConstant.BROADCAST_RECEIVER_TYPE, BroadcastReceiverType.BAR_LIST);
            broadcaster.sendBroadcast(returnIntent);
        } else {
            Log.e(LOG_TAG, "Error getting Bar List, received null response");
            LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(this);
            Intent returnIntent = new Intent(BroadcastReceiverConstant.BROADCAST_RECEIVER_MAINACTIVITY);
            returnIntent.putExtra(BroadcastReceiverConstant.BROADCAST_RECEIVER_TYPE, BroadcastReceiverType.SHOWING_ERROR_MESSAGE);
            broadcaster.sendBroadcast(returnIntent);
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
