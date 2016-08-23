package curiosamente.com.app.activities.main.Options;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import curiosamente.com.app.R;
import curiosamente.com.app.manager.QuestionManager;
import curiosamente.com.app.model.Question;
import curiosamente.com.app.service.PushAnswerService;

public class QuestionFragment extends Fragment {

    private List<Button> buttonList = new ArrayList<>();

    private static final int SELECTED_BUTTON_TEXT = R.color.primary;
    private static final int UNSELECTED_BUTTON_TEXT = R.color.icons;

    public QuestionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment_options, container, false);
        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.option_layout);

        Question question = QuestionManager.getQuestion(getActivity());
        List<String> optionsList = question.getOptions();
        for (int i = 0; i < optionsList.size(); i++) {
            Button newButton = createButton(optionsList.get(i), i);
            linearLayout.addView(newButton);
            buttonList.add(newButton);
        }
        setTraslationAnimation(linearLayout);

        return rootView;
    }

    public Button createButton(final String textButton, final int position) {

        final Button button = new Button(getActivity());
        button.setId(position);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.bottomMargin = 10;
        layoutParams.topMargin = 10;
        button.setLayoutParams(layoutParams);

        button.setPadding(10, 10, 10, 10);
        setColor(button, false);
        button.setText(textButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Question question = QuestionManager.getQuestion(getActivity());
                if (question != null) {

                    deselectAllOptions();
                    setColor(button, true);
                    disableAllOptions();

                    Intent intent = new Intent(getActivity(), PushAnswerService.class);

                    intent.putExtra(PushAnswerService.ID_QUESTION_ANSWER, question.getIdQuestion());
                    intent.putExtra(PushAnswerService.ANSWER, textButton);
                    getActivity().startService(intent);

                }
            }
        });

        return button;
    }

    public void deselectAllOptions() {
        for (Button button : buttonList) {
            setColor(button, false);
        }
    }

    public void disableAllOptions() {
        for (Button button : buttonList) {
            button.setEnabled(false);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setColor(Button button, Boolean selected) {
        if (selected) {
            button.setTextColor(getResources().getColor(SELECTED_BUTTON_TEXT));
            button.setBackground(getActivity().getResources().getDrawable(R.drawable.shape_selected_option_button_border));
        } else {
            button.setTextColor(getResources().getColor(UNSELECTED_BUTTON_TEXT));
            button.setBackground(getActivity().getResources().getDrawable(R.drawable.shape_unselected_option_button_border));
        }
    }


    public void setTraslationAnimation(View view) {
        TranslateAnimation anim = new TranslateAnimation(0, -5, 0, 5);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(500);
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
}
