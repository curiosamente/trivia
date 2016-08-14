package curiosamente.com.app.activities.main.Bar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import curiosamente.com.app.R;
import curiosamente.com.app.model.Bar;
import curiosamente.com.app.utils.ImageUtility;
import curiosamente.com.app.views.RoundedImageView;

public class BarAdapter extends ArrayAdapter<Bar> {

    public BarAdapter(Context context, List<Bar> bars) {
        super(context, 0, bars);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Bar bar = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.main_fragment_bar_listitem, parent
                    , false);
        }


        TextView tvName = (TextView) convertView.findViewById(R.id.bar_name_list_item_text);
        TextView tvAddress = (TextView) convertView.findViewById(R.id.bar_address_list_item_text);
        RoundedImageView roundedImageView = (RoundedImageView) convertView.findViewById(R.id.bar_list_item_icon);

        tvName.setText(bar.getName());
        tvAddress.setText(bar.getAddress());

        String resource = getContext().getResources().getString(R.string.bar_icon_logo_prefix) + 0;
        roundedImageView.setImageDrawable(getContext().getResources().getDrawable(ImageUtility.getResourceID(resource, "drawable", getContext())));

        return convertView;
    }
}