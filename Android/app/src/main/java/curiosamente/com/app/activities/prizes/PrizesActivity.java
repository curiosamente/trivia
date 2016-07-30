package curiosamente.com.app.activities.prizes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import curiosamente.com.app.R;

public class PrizesActivity extends AppCompatActivity {

    private final String LOG_TAG = PrizesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prizes_list_activity);
    }

}
