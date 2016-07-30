package curiosamente.com.app.activities.prizes;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Date;

import curiosamente.com.app.R;
import curiosamente.com.app.activities.main.Bar.BarAdapter;
import curiosamente.com.app.activities.main.MainActivityBroadcastReceiver;
import curiosamente.com.app.manager.BarManager;
import curiosamente.com.app.model.Bar;
import curiosamente.com.app.model.Prize;


public class PrizeListFragment extends Fragment {

    private final String LOG_TAG = PrizeListFragment.class.getSimpleName();
    private ArrayList<Prize> prizes = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.prizes_list_fragment, container, false);

        Prize prize = new Prize();
        prize.setName("aaa");
        prize.setDate(new Date());
        prizes.add(0,prize);
        prizes.add(1,prize);
        prizes.add(2,prize);
        ArrayAdapter<Prize> prizeListAdapter = new PrizeAdapter(getActivity(), prizes);
        ListView prizeListView = (ListView) rootView.findViewById(R.id.prizes_list);
        prizeListView.setAdapter(prizeListAdapter);

        prizeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Prize prize = (Prize) adapterView.getItemAtPosition(position);
                /*BarManager.storeSelectedBarPreference(getActivity(), bar);
                LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getActivity());
                Intent intent = new Intent(MainActivityBroadcastReceiver.BROADCAST_RECEIVER_MAINACTIVITY);
                intent.putExtra(MainActivityBroadcastReceiver.BROADCAST_RECEIVER_RETURN_OBJECT, bar);
                intent.putExtra(MainActivityBroadcastReceiver.BROADCAST_RECEIVER_TYPE, MainActivityBroadcastReceiver.BROADCAST_RECEIVER_SELECTED_BAR);
                broadcaster.sendBroadcast(intent);*/
            }
        });
        return rootView;
    }

    public void setPrizes(ArrayList<Prize> prizes) {
        this.prizes = prizes;
    }
}