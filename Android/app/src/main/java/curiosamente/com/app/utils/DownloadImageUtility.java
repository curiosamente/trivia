package curiosamente.com.app.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import curiosamente.com.app.activities.main.AsyncResponse;


public class DownloadImageUtility extends AsyncTask<String, String, Bitmap> {

    Activity mActivity;
    public AsyncResponse delegate = null;

    public DownloadImageUtility(Activity activity, AsyncResponse asyncResponse) {
        mActivity = activity;
        delegate = asyncResponse;
    }

    protected Bitmap doInBackground(String... urls) {

        String urlDisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urlDisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        try {
            delegate.processFinish(ImageUtility.saveToInternalStorage(result, mActivity));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
