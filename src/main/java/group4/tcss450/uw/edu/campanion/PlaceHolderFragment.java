/**
 * Amanda Aldrich
 *
 * This is just a placeholder
 */

package group4.tcss450.uw.edu.campanion;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * create an instance of this fragment.
 */
public class PlaceHolderFragment extends Fragment implements View.OnClickListener{



    public PlaceHolderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_place_holder, container, false);

        Button b = (Button) v.findViewById(R.id.buttonContinue);
        b.setOnClickListener(this);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();


        if (getArguments() != null) {

            if(getArguments().containsKey(getString(R.string.login_key))){
                User used = (User) getArguments().getSerializable(getString(R.string.login_key));
                updateLoginContent(used);
                updatepasswordContent(used);
            }
            else {
                User used = (User) getArguments().getSerializable(getString(R.string.signup_key));
                updateLoginContent(used);
                updatepasswordContent(used);


            }
        }
    }

    //updates the textView displaying login
    public void updateLoginContent(User used) {
        String login = used.getLogin();
        TextView tv = (TextView) getActivity().findViewById(R.id.displayUsername);
        tv.setText(login);
    }

    //updates the textView displaying password
    public void updatepasswordContent(User used) {
        String pw = used.getPassword();
        TextView tv = (TextView) getActivity().findViewById(R.id.displayPassword);
        tv.setText(pw);
    }


    @Override
    public void onClick(View v) {
        Intent myIntent = new Intent(getActivity(), UseActivity.class);
        getActivity().startActivity(myIntent);
    }
}