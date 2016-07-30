package curiosamente.com.app.activities.prizes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import curiosamente.com.app.R;
import curiosamente.com.app.model.Bar;
import curiosamente.com.app.model.Prize;
import curiosamente.com.app.utils.ImageUtility;
import curiosamente.com.app.views.RoundedAndCrossedImageView;
import curiosamente.com.app.views.RoundedImageView;

public class PrizeAdapter extends ArrayAdapter<Prize> {

    final Random rnd = new Random();

    public PrizeAdapter(Context context, ArrayList<Prize> prizes) {
        super(context, 0, prizes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Prize prize = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.prize_list_fragment_prize_listitem, parent
                    , false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.prize_bar_name_list_item_text);
        TextView tvDate = (TextView) convertView.findViewById(R.id.prize_date_list_item_text);
        RoundedAndCrossedImageView roundedImageView= (RoundedAndCrossedImageView) convertView.findViewById(R.id.prize_list_item_icon);

        tvName.setText(prize.getName());
        tvDate.setText(prize.getDate().toString());

        String resource = "beer_logo" + rnd.nextInt(5);
        roundedImageView.setImageDrawable(getContext().getResources().getDrawable(ImageUtility.getResourceID(resource, "drawable", getContext())));

        return convertView;
    }



}
