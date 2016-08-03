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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.Profile;

import java.io.File;

import curiosamente.com.app.R;
import curiosamente.com.app.activities.main.Waiting.WaitingFragment;
import curiosamente.com.app.activities.prize.prizeslist.PrizesListActivity;
import curiosamente.com.app.manager.ThreadManager;
import curiosamente.com.app.utils.AsyncResponse;
import curiosamente.com.app.manager.BarManager;
import curiosamente.com.app.manager.LogInManager;
import curiosamente.com.app.utils.ImageUtility;


public class MainActivity extends AppCompatActivity implements AsyncResponse {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawerLayout;
    private TextView mDrawerTextView;
    private ImageView mDrawerImage;
    private Button mDrawerLogoutButton;
    private LinearLayout mBarLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private BroadcastReceiver receiver;
    private Button mLeaveBarButton;
    private TextView mBarTextView;
    private Button mGoToPrizesListButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        receiver = new MainActivityBroadcastReceiver(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.activity_main);
        mDrawerTextView = (TextView) findViewById(R.id.facebook_name_text);
        mDrawerLogoutButton = (Button) mDrawerLayout.findViewById(R.id.facebook_logout_button);
        mDrawerImage = (ImageView) findViewById(R.id.profileImage);
        mBarLayout = (LinearLayout) findViewById(R.id.bar_layout);
        mLeaveBarButton = (Button) mBarLayout.findViewById(R.id.leave_bar_button);
        mBarTextView = (TextView) mBarLayout.findViewById(R.id.bar_name);
        mGoToPrizesListButton = (Button) findViewById(R.id.prize_list_button);

    }



    private void startActivity() {

        // Set initial Fragment
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        WaitingFragment waitingFragment = new WaitingFragment();
        waitingFragment.setFragmentMessage("initial message");
        fragmentTransaction.replace(R.id.main_layout, waitingFragment);
        fragmentTransaction.commit();

        initDrawer();

        if (BarManager.isABarSelectedAndValid(this)) {
            BarManager.updateSelectedBarTimeStamp(this);
            ThreadManager.callCheckStatus(this);
        } else {
            BarManager.getBars(this);
        }
    }


    public void initDrawer() {

        mDrawerLogoutButton.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       LogInManager.logOut(MainActivity.this);
                                                   }
                                               }
        );


        mDrawerTextView.setText(Profile.getCurrentProfile().getName());


        Uri imageUrl = Profile.getCurrentProfile().getProfilePictureUri(200, 200);
        ImageUtility downloadImageUtility = new ImageUtility(this, this);
        downloadImageUtility.execute(imageUrl.toString());

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, null, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.actionbar_white_burger_logo);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mDrawerToggle.setHomeAsUpIndicator(R.drawable.actionbar_white_back_logo);
                invalidateOptionsMenu();
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.actionbar_white_burger_logo);


        mLeaveBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BarManager.leaveBar(MainActivity.this);
                initDrawer();
                LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getBaseContext());
                Intent intent = new Intent(BroadcastReceiverConstant.BROADCAST_RECEIVER_MAINACTIVITY);
                intent.putExtra(BroadcastReceiverConstant.BROADCAST_RECEIVER_TYPE, BroadcastReceiverType.LEAVE_BAR);
                broadcaster.sendBroadcast(intent);
                mDrawerLayout.closeDrawers();
            }
        });

        mGoToPrizesListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), PrizesListActivity.class);
                startActivity(intent);
                mDrawerLayout.closeDrawers();
            }
        });


        if (BarManager.isABarSelectedAndValid(this)) {
            mBarLayout.setVisibility(LinearLayout.VISIBLE);
            mBarTextView.setText(BarManager.getBarName(getBaseContext()));
        } else {
            mBarLayout.setVisibility(LinearLayout.GONE);
        }


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
        LogInManager.checkStatus(this);
        startActivity();
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
                new IntentFilter(BroadcastReceiverConstant.BROADCAST_RECEIVER_MAINACTIVITY)
        );
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onStop();
    }

}
