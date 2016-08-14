package curiosamente.com.app.activities.prize.prizedetail;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import curiosamente.com.app.R;
import curiosamente.com.app.manager.PrizeManager;
import curiosamente.com.app.model.Prize;
import curiosamente.com.app.utils.ImageUtility;
import curiosamente.com.app.views.PrizeImageView;


public class PrizeDetailFragment extends Fragment {

    private final String LOG_TAG = PrizeDetailFragment.class.getSimpleName();

    private Prize prize;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.prize_detail_fragment, container, false);

        PrizeImageView prizeImageView = (PrizeImageView) rootView.findViewById(R.id.prize_detail_icon);
        TextView tvBarName = (TextView) rootView.findViewById(R.id.prize_detail_bar_name_text);
        TextView tvPrizeDate = (TextView) rootView.findViewById(R.id.prize_detail_date_text);
        Button btnCollect = (Button) rootView.findViewById(R.id.prize_detail_collect_button);

        prizeImageView.setCollected(prize.isCollected());
        prizeImageView.setImageDrawable(getActivity().getResources().getDrawable(ImageUtility.getResourceID(prize.getImageSrc(), "drawable", getActivity())));

        tvBarName.setText(prize.getName());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String showedDate = format.format(prize.getDate());
        tvPrizeDate.setText(showedDate);


        if (prize.isCollected()) {
            btnCollect.setVisibility(View.INVISIBLE);
        } else {
            btnCollect.setVisibility(View.VISIBLE);
            btnCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setTitle(getActivity().getString(R.string.prize_collect_warning))
                            .setMessage(getActivity().getString(R.string.prize_collect_message))
                            .setPositiveButton(getActivity().getString(R.string.yes), new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Prize cashedPrize = PrizeManager.collectPrize(getActivity(), prize);
                                    if(cashedPrize != null)
                                    {
                                        Intent intent = new Intent(getActivity(), PrizeDetailActivity.class);
                                        intent.putExtra(PrizeDetailActivity.INTENT_EXTRA_PRIZE_OBJECT, cashedPrize);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.prize_collect_error), Toast.LENGTH_LONG);
                                    }
                                }
                            })
                            .setNegativeButton(getActivity().getString(R.string.no), null)
                            .show();
                }
            });
        }


        return rootView;
    }

    public void setPrize(Prize prize) {
        this.prize = prize;
    }

}