package curiosamente.com.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtility extends AsyncTask<String, String, Bitmap> {

    Activity mActivity;
    public AsyncResponse delegate = null;

    public ImageUtility(Activity activity, AsyncResponse asyncResponse) {
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
            delegate.processFinish(saveToInternalStorage(result, mActivity));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String saveToInternalStorage(Bitmap bitmapImage, Activity activity) throws IOException {

        ContextWrapper cw = new ContextWrapper(activity.getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, "profile.jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        return mypath.getAbsolutePath();
    }

    public static int getResourceID(final String resName, final String resType, final Context ctx) {
        final int ResourceID =
                ctx.getResources().getIdentifier(resName, resType,
                        ctx.getApplicationInfo().packageName);
        if (ResourceID == 0) {
            throw new IllegalArgumentException
                    ("No resource string found with name " + resName);
        } else {
            return ResourceID;
        }
    }
}
