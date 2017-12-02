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
import android.widget.EditText;
import android.widget.TextView;

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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 *
 * create an instance of this fragment.
 */
public class PlaceHolderFragment extends Fragment implements View.OnClickListener{

    private static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/~aldrich7/";

    User used;
    String login;
    String theCode;

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
        b.setEnabled(false);

        Button b1 = (Button) v.findViewById(R.id.verifyButton);
        b1.setOnClickListener(this);

        Button b2 = (Button) v.findViewById(R.id.buttonResend);
        b2.setOnClickListener(this);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();


        if (getArguments() != null) {

            if(getArguments().containsKey(getString(R.string.login_key))){
                used = (User) getArguments().getSerializable(getString(R.string.login_key));
                updateLoginContent(used);
            }
            else {
                used = (User) getArguments().getSerializable(getString(R.string.signup_key));
                updateLoginContent(used);


            }
        }
    }

    //updates the textView displaying login
    public void updateLoginContent(User used) {
        login = used.getLogin();
        EditText edit_text = (EditText) getActivity().findViewById(R.id.editNumberForCode);
        try {
            theCode = edit_text.getText().toString();
        }
        catch(NumberFormatException e){
            edit_text.setError("Hi! This has to be the number we sent you.");

        }


    }




    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.buttonContinue:

                Bundle bundle = new Bundle();
                bundle.putSerializable("login", used);


                Intent myIntent = new Intent(getActivity(), UseActivity.class);
                myIntent.putExtra("Login", bundle);
                getActivity().startActivity(myIntent);
                break;

            case R.id.verifyButton:

                AsyncTask<String, Void, String> checkVerify = new verifyTask();
                checkVerify.execute(PARTIAL_URL, login, theCode);

                break;

            case R.id.buttonResend:

                AsyncTask<String, Void, String> resendEmail = new resendEmailTask();
                resendEmail.execute();

                break;
        }

    }

    private class verifyTask extends AsyncTask<String, Void, String>{

        String response = "";
        private final String SERVICE = "verify.php";
        Button b = (Button) getActivity().findViewById(R.id.buttonContinue);
        Button b1 = (Button) getActivity().findViewById(R.id.verifyButton);

        @Override
        protected void onPreExecute() {
            b.setEnabled(false);
            b1.setEnabled(false);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            String partURL = params[0];
            URL urlObject = null;
            try {
                urlObject = new URL(partURL + SERVICE);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);

                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                String data = URLEncoder.encode("email", "UTF-8")
                        + "=" + URLEncoder.encode(params[1], "UTF-8") + "&" +
                        URLEncoder.encode("code", "UTF-8")
                        + "=" + URLEncoder.encode(params[2], "UTF-8");
                wr.write(data);
                wr.flush();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }

            return response;
        }

        protected void onPostExecute(String result) {

            b1.setEnabled(true);
            int test = 0;
            JSONObject myJSON2;
            try{
                myJSON2 = new JSONObject(result);
                Log.v("very","made it here");
                test = myJSON2.getInt("code");
                if(test == 300){

                    b.setEnabled(true);
                    Log.v("verifyCode", "" + test);
                }

                else{

                    Log.v("verifyCode", "" + test);
                }
            }catch(JSONException e){
                Log.wtf("verify", "" + test);
            }



        }
    }

    private class resendEmailTask extends AsyncTask<String, Void, String>{

        String response = "";
        private final String SERVICE = "resendEmail.php";
        Button b = (Button) getActivity().findViewById(R.id.buttonContinue);


        @Override
        protected void onPreExecute() {
            b.setEnabled(false);
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            String partURL = params[0];
            URL urlObject = null;
            try {
                urlObject = new URL(partURL + SERVICE);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);

                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                String data = URLEncoder.encode("email", "UTF-8")
                        + "=" + URLEncoder.encode(params[1], "UTF-8");
                wr.write(data);
                wr.flush();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }

            return response;
        }

        protected void onPostExecute(String result) {

            int test = 0;
            JSONObject myJSON2;
            try{

                myJSON2 = new JSONObject(result);
                test = myJSON2.getInt("code");
                if(test == 300){

                    Log.v("code", "" + test);
                }

                else{

                    Log.v("codeElse", "" + test);
                }
            }catch(JSONException e){
                Log.wtf("login", "Things went doubly hinky");
            }



        }
    }

}