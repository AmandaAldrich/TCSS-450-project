package group4.tcss450.uw.edu.campanion.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wilso on 11/29/2017.
 */

public class TaskDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "task.db";
    private static final int DATABASE_VERSION = 1;

    public TaskDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TASKS_TABLE =
                "CREATE TABLE " + TaskContract.TaskEntry.TABLE_NAME + " (" +
                        TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY," +
                        TaskContract.TaskEntry.COLUMN_TASK + " TEXT NOT NULL" +
                        " );";
        db.execSQL(SQL_CREATE_TASKS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE_NAME);
        onCreate(db);
    }
}