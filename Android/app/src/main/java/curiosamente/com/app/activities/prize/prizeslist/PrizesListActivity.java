package curiosamente.com.app.activities.prize.prizeslist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import curiosamente.com.app.R;

public class PrizesListActivity extends AppCompatActivity {

    private final String LOG_TAG = PrizesListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prizes_list_activity);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        PrizesListFragment prizesListFragment = (PrizesListFragment) getFragmentManager().findFragmentById(R.id.prizes_list_fragment);
        prizesListFragment.startFragment();
    }
}
