package curiosamente.com.app.activities.question;

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

public class QuestionFragment extends Fragment {

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

        View rootView = inflater.inflate(R.layout.main_fragment_waiting, container, false);

        final TextView textView = (TextView) rootView.findViewById(R.id.mainText);
        textView.setText(getResources().getString(R.string.main_activity_loading));

        return rootView;
    }

}
