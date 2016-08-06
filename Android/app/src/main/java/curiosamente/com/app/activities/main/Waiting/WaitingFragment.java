package curiosamente.com.app.activities.main.Waiting;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import curiosamente.com.app.R;

public class WaitingFragment extends Fragment {

    private String fragmentMessage = null;

    public WaitingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment_waiting, container, false);

        final TextView textView = (TextView) rootView.findViewById(R.id.mainText);
        textView.setText(fragmentMessage);

        final LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.loadingPanel);

        setTraslationAnimation(linearLayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translate(linearLayout);
            }
        });

        return rootView;
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
                if (numberOfRepetition == 6) {
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
        view.clearAnimation();

        TranslateAnimation anim = new TranslateAnimation(0, -5, 0, 5);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(6);
        anim.setDuration(5);
        anim.setFillAfter(true);
        anim.setAnimationListener(new Animation.AnimationListener() {
            int numberOfRepetition = 0;
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                setTraslationAnimation(view);

            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        view.startAnimation(anim);
    }

    public String getFragmentMessage() {
        return fragmentMessage;
    }

    public void setFragmentMessage(String fragmentMessage) {
        this.fragmentMessage = fragmentMessage;
    }
}
