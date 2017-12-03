/**
 * Amanda Aldrich
 *
 * This is the Login Fragment, currently no back-end and very rudimentary safety checks
 */

package group4.tcss450.uw.edu.campanion;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
 * to handle interaction events.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    private static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/~aldrich7/";

    private OnFragmentInteractionListener lListener;
    String login;
    String password;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            String choice = getArguments().getString(getString(R.string.choice_key));


        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ChoiceFragment.OnFragmentInteractionListener) {
            lListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        lListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        Button b = (Button) v.findViewById(R.id.acceptLoginButton);
        b.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {

        //Getting the information fron the text fields
        EditText edit_text = (EditText) getActivity().findViewById(R.id.loginEdit);
        EditText edit_text_1 = (EditText) getActivity().findViewById(R.id.paswordEdit);

        //helper methods
        boolean atFlag = atHelper(edit_text);
        boolean ruleFlag = ruleHelper(edit_text_1);

        //if the fields are not empty and the helper methods return true
        if(!TextUtils.isEmpty(edit_text.getText()) && !TextUtils.isEmpty(edit_text_1.getText()) && atFlag && ruleFlag){
            login = edit_text.getText().toString();
            password = edit_text_1.getText().toString();

            if(lListener != null){
                //call task here
                AsyncTask<String, Void, String> loginTaskCall= new loginTask();
                loginTaskCall.execute(PARTIAL_URL, login, password);
            }
        }
        else{

            //if the email checks throw false
            if(!atFlag){
                edit_text.setError("This needs to be a valid email");
            }

            //if the rules checks throw false
            if(!ruleFlag){
                edit_text_1.setError("Passwords must contain a number and a special character" +
                        "and must be longer than three characters");
            }

            //if theh email is blank
            if(TextUtils.isEmpty(edit_text.getText())){
                edit_text.setError("You left this field blank");

            }

            //if the password is blank
            if(TextUtils.isEmpty(edit_text_1.getText())){
                edit_text_1.setError("You left this field blank");
            }
        }
    }

    //checks for @ sign and for length longer than 3
    //could probably use a better check but this will owrk for now
    public boolean atHelper(EditText tested){
        if(tested.getText().toString().length() > 3 && tested.getText().toString().contains("@")){
            return true;
        }
        return false;
    }

    //checks for length longer than 3, a special character and a number
    //If you can think of other rules they go here
    public boolean ruleHelper(EditText tested){
        if(tested.getText().toString().length() > 3 && tested.getText().toString().matches(".*[!@#$%^&*?~].*")
                && tested.getText().toString().matches(".*[1234567890].*")){
            return true;
        }
        return false;
    }

    public interface OnFragmentInteractionListener {
        void onLoginFragmentInteraction(String login, boolean verified);

    }

    private class loginTask extends AsyncTask<String, Void, String>{

        String response = "";
        private final String SERVICE = "login.php";
        Button b = (Button) getActivity().findViewById(R.id.acceptLoginButton);

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
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);

                OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                String data = URLEncoder.encode("userTest", "UTF-8")
                        + "=" + URLEncoder.encode(params[1], "UTF-8") + "&" +
                        URLEncoder.encode("passTest", "UTF-8")
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
            b.setEnabled(true);
            int test = 0;
            JSONObject myJSON2;
            boolean verified = true;
            try{

                myJSON2 = new JSONObject(result);
                test = myJSON2.getInt("code");
                if(test == 400){ //verified

                    //call task here
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("login", login);

                    Intent myIntent = new Intent(getActivity(), UseActivity.class);
                    myIntent.putExtra("Login", bundle);
                    getActivity().startActivity(myIntent);


                    Log.v("code", "" + test);
                }
                else if(test == 200){
                    EditText userText = (EditText) getActivity().findViewById(R.id.loginEdit);
                    userText.setError("Username or Password Incorrect");
                    Log.v("code", "" + test);
                }
                else if(test == 500){ //not verified
                    Log.v("code", ""+test);
                    lListener.onLoginFragmentInteraction(login, verified);

                }
                else{
                    EditText userText = (EditText) getActivity().findViewById(R.id.loginEdit);
                    userText.setError("Connection Currently Unavailable");
                    Log.v("codeElse", "" + test);
                }
            }catch(JSONException e){
                Log.wtf("login", "Things went doubly hinky");
            }



        }
    }
}
