package curiosamente.com.app.data;

import android.provider.BaseColumns;

public class DbContract {
    public static final String PATH_PRIZES = "prize";

    public static final class PrizesEntry implements BaseColumns {

        public static final String TABLE_NAME = "prizes";

        public static final String COLUMN_BAR_ID = "bar_id";
        public static final String COLUMN_BAR_NAME = "bar_name";
        public static final String COLUMN_PRIZE_DATE = "prize_date";
        public static final String COLUMN_PRIZE_IMAGE = "prize_image";
        public static final String COLUMN_PRIZE_COLLECTED = "prize_collected";


        public static final String QUERY_SELECT_ALL_ROWS = "SELECT * FROM prizes";
        public static final String QUERY_SELECT_ROW_BY_SEQ_ID = "SELECT * FROM prizes p WHERE p." + _ID + " = ";
    }
}
