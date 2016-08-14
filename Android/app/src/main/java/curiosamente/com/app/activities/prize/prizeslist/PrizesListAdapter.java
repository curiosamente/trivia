package curiosamente.com.app.activities.prize.prizeslist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import curiosamente.com.app.R;
import curiosamente.com.app.model.Prize;
import curiosamente.com.app.utils.ImageUtility;
import curiosamente.com.app.views.PrizeImageView;

public class PrizesListAdapter extends ArrayAdapter<Prize> {

    public PrizesListAdapter(Context context, ArrayList<Prize> prizes) {
        super(context, 0, prizes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Prize prize = (Prize) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.prizes_list_fragment_prize_listitem, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.prizes_bar_name_list_item_text);
        TextView tvDate = (TextView) convertView.findViewById(R.id.prizes_date_list_item_text);
        PrizeImageView prizeImageView = (PrizeImageView) convertView.findViewById(R.id.prizes_list_item_icon);

        tvName.setText(prize.getName());
        Date date = prize.getDate();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String showedDate = format.format(date);
        tvDate.setText(showedDate);

        String imagePath = prize.getImageSrc();
        boolean prizeCashed = prize.isCollected();
        prizeImageView.setCollected(prizeCashed);
        prizeImageView.setImageDrawable(getContext().getResources().getDrawable(ImageUtility.getResourceID(imagePath, "drawable", getContext())));

        return convertView;
    }
}
