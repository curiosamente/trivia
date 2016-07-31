package curiosamente.com.app.activities.prize.prizeslist;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import curiosamente.com.app.R;
import curiosamente.com.app.activities.prize.prizedetail.PrizeDetailActivity;
import curiosamente.com.app.manager.BarManager;
import curiosamente.com.app.model.Bar;
import curiosamente.com.app.model.Prize;
import curiosamente.com.app.manager.PrizeManager;


public class PrizesListFragment extends Fragment {

    private final String LOG_TAG = PrizesListFragment.class.getSimpleName();
    private View rootView;
    private ListView prizeListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.prizes_list_fragment, container, false);
        prizeListView = (ListView) rootView.findViewById(R.id.prizes_list);
        startFragment();
        return rootView;
    }

    public void startFragment(){

        ArrayList<Prize> prizeList = PrizeManager.getPrizesList(getActivity());
        PrizesListAdapter2 prizesListAdapter2 = new PrizesListAdapter2(getActivity(), prizeList);


        TextView tvNoPrizes = (TextView) rootView.findViewById(R.id.prize_list_empty_text);
        if(prizeList.size() > 0){
            tvNoPrizes.setVisibility(View.GONE);
        } else {
            tvNoPrizes.setVisibility(View.VISIBLE);
        }
        prizeListView.setAdapter(prizesListAdapter2);
        prizeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Prize prize = (Prize) adapterView.getItemAtPosition(position);
                if (prize != null) {
                    Intent intent = new Intent(getActivity(), PrizeDetailActivity.class);
                    intent.putExtra(PrizeDetailActivity.INTENT_EXTRA_PRIZE_OBJECT, prize);
                    startActivity(intent);
                }
            }
        });

    };





}