package group4.tcss450.uw.edu.campanion.Data;

import android.provider.BaseColumns;

/**
 * Created by wilso on 11/29/2017.
 */

public class TaskContract {

    //Each of xxxEntry corresponds to a table in the database.
    public class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME ="tasks";
        public static final String COLUMN_TASK = "task";
    }
}
