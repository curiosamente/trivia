package curiosamente.com.app.activities.main.TriviaResult;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import curiosamente.com.app.R;
import curiosamente.com.app.activities.main.MainActivity;
import curiosamente.com.app.activities.prize.prizeslist.PrizesListActivity;
import curiosamente.com.app.utils.ImageUtility;
import pl.droidsonroids.gif.GifTextView;

public class TriviaResultFragment extends Fragment {

    Random colorRandom = new Random();

    boolean isWinner = false;
    private String triviaResultText;
    int viewIndex = 0;
    ArrayList<Integer> colors = new ArrayList<Integer>(Arrays.asList(Color.parseColor("#e33232"), Color.parseColor("#fcee1e"), Color.parseColor("#009fd4"), Color.parseColor("#6a4475"), Color.parseColor("#ed7d2d")));

    private GifTextView gifView;
    private TextView textView;
    private Button buttonView;

    public TriviaResultFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_fragment_trivia_result, container, false);
        textView = (TextView) rootView.findViewById(R.id.trivia_result_text);

        triviaResultText = (isWinner) ? getResources().getString(R.string.trivia_result_won_text) : getResources().getString(R.string.trivia_result_lost_text);

        textView.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        textView.setText(createMultiColorSpannable(triviaResultText));


        gifView = (GifTextView) rootView.findViewById(R.id.trivia_result_gif);
        gifView.setBackgroundResource(R.drawable.facepalm_0);
        gifView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceGif();
            }
        });
        replaceGif();


        buttonView = (Button) rootView.findViewById(R.id.trivia_result_button);
        if (isWinner) {
            buttonView.setText(getResources().getString(R.string.trivia_result_won_button));
            buttonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), PrizesListActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            buttonView.setText(getResources().getString(R.string.trivia_result_lost_button));
            buttonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).startActivity();
                }
            });
        }


        return rootView;
    }

    public void replaceGif() {
        String image = null;
        if (isWinner) {
            image = getActivity().getResources().getString(R.string.trivia_result_thumbup_logo_prefix) + viewIndex;
        } else {
            image = getActivity().getResources().getString(R.string.trivia_result_facepalm_logo_prefix) + viewIndex;
        }

        if (viewIndex <= 2) {
            viewIndex++;
        } else {
            viewIndex = 0;
        }
        gifView.setBackgroundResource(ImageUtility.getResourceID(image, "drawable", getActivity()));
        textView.setText(createMultiColorSpannable(triviaResultText));

    }

    public Spannable createMultiColorSpannable(String text) {
        Spannable spannableText = new SpannableString(text);
        if (isWinner) {
            int colorIndex = colorRandom.nextInt(colors.size());
            for (int i = 0; i < text.length(); i++) {
                spannableText.setSpan(new ForegroundColorSpan(colors.get(colorIndex)), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (colorIndex + 1 < colors.size()) {
                    colorIndex++;
                } else {
                    colorIndex = 0;
                }
            }
        } else {
            spannableText.setSpan(Color.BLACK, 0, spannableText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableText;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }

}
