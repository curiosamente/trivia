package curiosamente.com.app.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import curiosamente.com.app.model.Bar;


public class HttpService extends android.app.IntentService {

    static final public String HTTPSERVICE_RESULT = "curiosamente.com.app.service.HttpServiceResult";

    public HttpService() {
        super("HttpService");
    }

    final static public String URL_EXTRA_PROPERTY = "url";
    final static public String CLASS_EXTRA_PROPERTY = "class";

    final static public String RETURNOBJECT_EXTRA_PROPERTY = "returnObject";


    public static final String LOG_TAG = HttpService.class.getSimpleName();

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(HttpService.LOG_TAG, "HttpService intent started");

        if (intent.hasExtra(URL_EXTRA_PROPERTY) && intent.hasExtra(CLASS_EXTRA_PROPERTY)) {

            String url = (String) intent.getExtras().get(URL_EXTRA_PROPERTY);
            Class returnObjectClass = (Class) intent.getExtras().get(CLASS_EXTRA_PROPERTY);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            Serializable list = (Serializable) restTemplate.getForObject(url, returnObjectClass);


            LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(this);
            Intent returnIntent = new Intent(HTTPSERVICE_RESULT);
            returnIntent.putExtra(RETURNOBJECT_EXTRA_PROPERTY, list);

            broadcaster.sendBroadcast(returnIntent);
        }

    }

}
