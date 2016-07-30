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

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import curiosamente.com.app.R;


public class MainFragment extends Fragment {

    private final String LOG_TAG = MainFragment.class.getSimpleName();

    private static final String ROTATION_MOVEMENT_ENABLED = "ROTATION_MOVEMENT_ENABLED";
    private static boolean ROTATION_MOVEMENT_ENABLED_BOOLEAN = true;

    RotateAnimation rotateAnim;

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        final ImageView imgView = (ImageView) rootView.findViewById(R.id.arrow);
        setRotationAnimation(imgView, ROTATION_MOVEMENT_ENABLED_BOOLEAN);

        final String IMAGEVIEW_TAG = "icon bitmap";
        imgView.setTag(IMAGEVIEW_TAG);


        final TextView textView = (TextView) rootView.findViewById(R.id.mainText);
        textView.setText("NO INICIO NINGUNA TRIVIA, SELECCIONE UNA EN EL MENU SUPERIOR IZQUIERDO");

        ViewTreeObserver vto = textView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                textView.setY(textView.getY() + (textView.getHeight() / 2));
                ViewTreeObserver obs = textView.getViewTreeObserver();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    obs.removeOnGlobalLayoutListener(this);
                } else {
                    obs.removeGlobalOnLayoutListener(this);
                }
            }

        });

        setTraslationAnimation(textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translate(textView);
            }
        });

        return rootView;
    }

    public void setRotationAnimation(View view, Boolean enabled) {
        if (enabled) {
            this.rotateAnim = new RotateAnimation(-5, 5, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            rotateAnim.setRepeatMode(Animation.REVERSE);
            rotateAnim.setRepeatCount(-1);
            rotateAnim.setDuration(200);
            rotateAnim.setFillAfter(true);
            view.startAnimation(rotateAnim);
        } else {
            view.clearAnimation();
        }
    }

    public void setTraslationAnimation(View view) {
        TranslateAnimation anim = new TranslateAnimation(0, -5, 0, 5);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(-1);
        anim.setDuration(20);
        anim.setFillAfter(true);
        anim.setAnimationListener(new Animation.AnimationListener() {
            int numberOfRepetition = 0;

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                numberOfRepetition = numberOfRepetition + 1;
                if (numberOfRepetition == 5) {
                    animation.setStartOffset(3000);
                    numberOfRepetition = -1;
                }
                if (numberOfRepetition == 0) {
                    animation.setStartOffset(0);
                }
            }
        });
        view.startAnimation(anim);

    }

    private void translate(final View view) {

        TranslateAnimation anim = new TranslateAnimation(0, -5, 0, 5);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(5);
        anim.setDuration(5);
        anim.setFillAfter(true);

        view.startAnimation(anim);
    }

}