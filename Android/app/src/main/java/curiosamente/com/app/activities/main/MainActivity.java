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
package curiosamente.com.app.activities.main;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;

import java.io.File;

import curiosamente.com.app.R;
import curiosamente.com.app.model.Bar;
import curiosamente.com.app.service.HttpService;
import curiosamente.com.app.utils.DownloadImageUtility;
import curiosamente.com.app.utils.LogInUtility;


public class MainActivity extends AppCompatActivity implements AsyncResponse {

    private final String LOG_TAG = MainActivity.class.getSimpleName();


    private DrawerLayout mDrawerLayout;
    private TextView mDrawerTextView;
    private ImageView mDrawerImage;
    private Button mDrawerLogoutButton;


    private ActionBarDrawerToggle mDrawerToggle;


    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        MainFragment mainFragment = new MainFragment();
        fragmentTransaction.add(R.id.main_layout, mainFragment);
        fragmentTransaction.commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Bar[] bars = (Bar[]) intent.getExtras().get(HttpService.RETURNOBJECT_EXTRA_PROPERTY);
//                FragmentManager fm = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fm.beginTransaction();
            }
        };

        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_main);
        mDrawerTextView = (TextView) findViewById(R.id.facebook_name_text);
        mDrawerLogoutButton = (Button) mDrawerLayout.findViewById(R.id.facebook_logout_button);
        mDrawerImage = (ImageView) findViewById(R.id.profileImage);


        Intent intent = new Intent(this, HttpService.class);
        intent.putExtra(HttpService.URL_EXTRA_PROPERTY, "http://192.168.0.13:8080/bar");
        intent.putExtra(HttpService.CLASS_EXTRA_PROPERTY, Bar[].class);
        startService(intent);


        initDrawer();
    }



    public void initDrawer() {

        mDrawerLogoutButton.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
//                                          if(StatusClass.serviceThread != null){
//                                              StatusClass.serviceThread.interrupt();
//                                              StatusClass.threadCreated = false;}
//                                            LogInUtility.logOut(MainActivity.this);
                                      }
                                  }
        );


        mDrawerTextView.setText(Profile.getCurrentProfile().getName());


        Uri imageUrl = Profile.getCurrentProfile().getProfilePictureUri(200, 200);
        DownloadImageUtility downloadImageUtility = new DownloadImageUtility(this, this);
        downloadImageUtility.execute(imageUrl.toString());

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }


    @Override
    public void processFinish(String output) {
        File imgFile = new File(output);
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            mDrawerImage.setImageBitmap(myBitmap);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                this.mDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        LogInUtility.checkStatus(this);
    }


    @Override
    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(HttpService.HTTPSERVICE_RESULT)
        );
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }

}
