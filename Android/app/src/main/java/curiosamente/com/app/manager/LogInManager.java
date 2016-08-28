/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package curiosamente.com.app.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import com.facebook.AccessToken;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import curiosamente.com.app.R;
import curiosamente.com.app.activities.login.LoginActivity;

public class LogInManager {

    private static GoogleApiClient googleApiClient = null;

    public static void logOut(Context context) {
        BarManager.leaveBar(context);
        if (isConnectedToFacebook()) {
            LoginManager.getInstance().logOut();
        } else {
            if (googleApiClient.isConnected()) {
                Auth.GoogleSignInApi.signOut(googleApiClient);
            }
            logOutGoogleAccount(context);
        }
        Intent login = new Intent(context, LoginActivity.class);
        context.startActivity(login);
        ((Activity) context).finish();
    }

    public static void checkStatus(Activity actitivy) {
        if (!isConnectedToFacebook() && !isConnectedToGoogleAccount(actitivy)) {
            logOut(actitivy);
        }
    }

    public static boolean isConnected(Context context) {
        return isConnectedToFacebook() || isConnectedToGoogleAccount(context);
    }

    public static boolean isConnectedToFacebook() {
        return Profile.getCurrentProfile() != null || Profile.getCurrentProfile() != null;
    }

    public static String getCurrentUserID(Context context) {

        if (isConnectedToFacebook()) {
            return "F" + Profile.getCurrentProfile().getId();
        } else {
            return "G" + getGoogleId(context);
        }
    }

    public static Uri getCurrentUserPhoto(Context context, int size) {

        if (isConnectedToFacebook()) {
            return Profile.getCurrentProfile().getProfilePictureUri(size, size);
        } else {
            return getGoogleImageURL(context);
        }
    }

    public static String getCurrentUserCompleteName(Context context) {
        if (isConnectedToFacebook()) {
            return Profile.getCurrentProfile().getName();
        } else {
            return getGoogleCompleteName(context);
        }
    }

    public static String getCurrentUserFirstName(Context context) {
        if (isConnectedToFacebook()) {
            return Profile.getCurrentProfile().getFirstName();
        } else {
            return getGoogleFirstName(context);
        }
    }

    public static String getCurrentUserLastName(Context context) {
        if (isConnectedToFacebook()) {
            return Profile.getCurrentProfile().getLastName();
        } else {
            return getGoogleLastName(context);
        }
    }

    public static GoogleApiClient getGoogleApiClient(Object loginFragment) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile()
                .build();
        googleApiClient = new GoogleApiClient.Builder((LoginActivity) loginFragment)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        return googleApiClient;
    }

    public static boolean isConnectedToGoogleAccount(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        return (sharedPreferences.getString(context.getResources().getString(R.string.pref_google_logged_account_id_key), null) != null);
    }

    public static void storeGoogleSingInAccount(GoogleSignInAccount googleSignInAccount, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.pref_google_logged_account_id_key), googleSignInAccount.getId());
        editor.putString(context.getResources().getString(R.string.pref_google_logged_account_name), googleSignInAccount.getGivenName());
        editor.putString(context.getResources().getString(R.string.pref_google_logged_account_lastname), googleSignInAccount.getFamilyName());
        editor.putString(context.getResources().getString(R.string.pref_google_logged_account_email), googleSignInAccount.getEmail());
        editor.putString(context.getResources().getString(R.string.pref_google_logged_account_image_url), String.valueOf(googleSignInAccount.getPhotoUrl()));
        editor.apply();
    }

    public static void logOutGoogleAccount(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(context.getResources().getString(R.string.pref_google_logged_account_id_key));
        editor.apply();
    }

    public static String getGoogleFirstName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        String name = sharedPreferences.getString(context.getResources().getString(R.string.pref_google_logged_account_name), "");
        return name;
    }

    public static String getGoogleLastName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        return sharedPreferences.getString(context.getResources().getString(R.string.pref_google_logged_account_lastname), "");
    }

    public static String getGoogleId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        return sharedPreferences.getString(context.getResources().getString(R.string.pref_google_logged_account_id_key), "");
    }

    public static String getGoogleCompleteName(Context context) {
        return getGoogleFirstName(context) + " " + getGoogleLastName(context);
    }

    public static Uri getGoogleImageURL(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPref", context.MODE_PRIVATE);
        String uri = sharedPreferences.getString(context.getResources().getString(R.string.pref_google_logged_account_image_url), null);
        if (uri.equals("null")) {
            return null;
        } else {
            return Uri.parse(uri);
        }
    }

}