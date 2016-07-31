package curiosamente.com.app.service;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;

import curiosamente.com.app.activities.main.MainActivityBroadcastReceiver;
import curiosamente.com.app.manager.StatusManager;


public class HttpService extends android.app.IntentService {


    public HttpService() {
        super("HttpService");
    }


    final static public String URL_EXTRA_PROPERTY = "url";
    final static public String CLASS_EXTRA_PROPERTY = "class";
    final static public String CALL_TYPE_ENUM_EXTRA_PROPERTY = "callType";

    static private String returnString = "RETURNED STATUS";
    public static final String LOG_TAG = HttpService.class.getSimpleName();

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(LOG_TAG, "HttpService intent started");

        if (intent.hasExtra(URL_EXTRA_PROPERTY) && intent.hasExtra(CLASS_EXTRA_PROPERTY) && intent.hasExtra(CALL_TYPE_ENUM_EXTRA_PROPERTY)) {

            String url = (String) intent.getExtras().get(URL_EXTRA_PROPERTY);
            Class returnObjectClass = (Class) intent.getExtras().get(CLASS_EXTRA_PROPERTY);
            HttpServiceCallTypeEnum httpServiceCallTypeEnum = (HttpServiceCallTypeEnum) intent.getExtras().get(CALL_TYPE_ENUM_EXTRA_PROPERTY);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Serializable returnObject = null;

            //TODO: ERASE THIS IF BLOCK
            if(httpServiceCallTypeEnum.equals(HttpServiceCallTypeEnum.Bar)){
                returnObject = (Serializable) restTemplate.getForObject(url, returnObjectClass);}
            else {
                returnObject = returnString;
            }



            if (httpServiceCallTypeEnum.equals(HttpServiceCallTypeEnum.Bar)) {
                LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(this);
                Intent returnIntent = new Intent(MainActivityBroadcastReceiver.BROADCAST_RECEIVER_MAINACTIVITY);
                returnIntent.putExtra(MainActivityBroadcastReceiver.BROADCAST_RECEIVER_RETURN_OBJECT, returnObject);
                returnIntent.putExtra(MainActivityBroadcastReceiver.BROADCAST_RECEIVER_TYPE, MainActivityBroadcastReceiver.BROADCAST_RECEIVER_TYPE_BAR_LIST);
                broadcaster.sendBroadcast(returnIntent);
            } else if (httpServiceCallTypeEnum.equals(HttpServiceCallTypeEnum.Status)) {
                StatusManager.statusReceived((String) returnObject, getBaseContext());
            }


        }

    }

}
