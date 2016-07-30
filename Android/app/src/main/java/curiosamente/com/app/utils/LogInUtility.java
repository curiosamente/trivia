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
package curiosamente.com.app.utils;

import android.app.Activity;
import android.content.Intent;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import curiosamente.com.app.activities.login.LoginActivity;

public class LogInUtility {


    public static void logOut(Activity activity){
        LoginManager.getInstance().logOut();
        Intent login = new Intent(activity, LoginActivity.class);
        activity.startActivity(login);
        activity.finish();
        }

    public static void checkStatus(Activity actitivy){
        if (AccessToken.getCurrentAccessToken() == null) {
            logOut(actitivy);
        }
    }

}