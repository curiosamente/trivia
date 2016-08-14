package curiosamente.com.app.activities.prize.prizedetail;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import curiosamente.com.app.R;
import curiosamente.com.app.model.Prize;

public class PrizeDetailActivity extends AppCompatActivity {

    private final String LOG_TAG = PrizeDetailActivity.class.getSimpleName();

    public final static String INTENT_EXTRA_PRIZE_OBJECT = "prize";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prize_detail_activity);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        PrizeDetailFragment prizeDetailFragment = new PrizeDetailFragment();
        Prize prize = (Prize) getIntent().getExtras().get(INTENT_EXTRA_PRIZE_OBJECT);
        if (prize != null) {
            prizeDetailFragment.setPrize(prize);
            fragmentTransaction.add(R.id.prize_detail_container, prizeDetailFragment);
            fragmentTransaction.commit();
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.actionbar_white_back_logo);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
