package group4.tcss450.uw.edu.campanion;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import group4.tcss450.uw.edu.campanion.Data.TaskContract;
import group4.tcss450.uw.edu.campanion.Data.TaskDBHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class PackingListFragment extends Fragment {

    TaskAdapter mTaskAdapter;

    // These indices are tied to TASKS_COLUMNS.  If TASKS_COLUMNS changes, these must change.
    static final int COL_TASK_ID = 0;
    static final int COL_TASK_NAME = 1;

    public PackingListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_packing_list, container, false);

        //Find the listView
        ListView listView = (ListView) rootView.findViewById(R.id.listview_tasks);

        //Get TaskDBHelper to read from database
        TaskDBHelper helper = new TaskDBHelper(getActivity());
        SQLiteDatabase sqlDB = helper.getReadableDatabase();

        //Query database to get any existing data
        Cursor cursor = sqlDB.query(TaskContract.TaskEntry.TABLE_NAME,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COLUMN_TASK},
                null, null, null, null, null);

        //Create a new TaskAdapter and bind it to ListView
        mTaskAdapter = new TaskAdapter(getActivity(), cursor);
        listView.setAdapter(mTaskAdapter);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tasks_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_add_task:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Add an Item");
                builder.setMessage("What do you want to pack?");
                final EditText inputField = new EditText(getActivity());
                builder.setView(inputField);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Get user input
                        String inputTask = inputField.getText().toString();

                        //Get TaskDBHelper to write to database
                        TaskDBHelper helper = new TaskDBHelper(getActivity());
                        SQLiteDatabase db = helper.getWritableDatabase();

                        //Put in the values within a ContentValues.
                        ContentValues values = new ContentValues();
                        values.clear();
                        values.put(TaskContract.TaskEntry.COLUMN_TASK, inputTask);

                        //Insert the values into the Table for Tasks
                        db.insertWithOnConflict(
                                TaskContract.TaskEntry.TABLE_NAME,
                                null,
                                values,
                                SQLiteDatabase.CONFLICT_IGNORE);

                        //Query database again to get updated data
                        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE_NAME,
                                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COLUMN_TASK},
                                null, null, null, null, null);

                        //Swap old data with new data for display
                        mTaskAdapter.swapCursor(cursor);
                    }
                }).setNegativeButton("Cancel", null);
                builder.create().show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
