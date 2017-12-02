/**
 * Amanda Aldrich
 *
 * This is the sign up fragment
 */
package group4.tcss450.uw.edu.campanion;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
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
 * to handle interaction events.
 */
public class SignupFragment extends Fragment implements View.OnClickListener{

    private static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/~aldrich7/";

    protected String login;
    protected String password;

    private OnFragmentInteractionListener kListener;

    public SignupFragment() {
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
            kListener = (SignupFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        kListener = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_signup, container, false);
        Button b = (Button) v.findViewById(R.id.acceptSignupButton);
        b.setOnClickListener(this);
        return v;
    }


    @Override
    public void onClick(View v) {


        boolean flag = true;

        EditText edit_text = (EditText) getActivity().findViewById(R.id.selectAUsername);
        EditText edit_text_1 = (EditText) getActivity().findViewById(R.id.selectAPassword);
        EditText edit_text_2 = (EditText) getActivity().findViewById(R.id.matchAPassword);

        //if the passwords dont match
        if(!((edit_text_1.getText().toString()).equals(edit_text_2.getText().toString()))){
            edit_text_2.setError("Passwords don't match");
            flag = false;

        }

        //if the helper methods, fields arent empty, and password matching flags are good
        if(!TextUtils.isEmpty(edit_text.getText()) && !TextUtils.isEmpty(edit_text_1.getText())
                && atHelper(edit_text) && ruleHelper(edit_text_1) && ruleHelper(edit_text_2)
                && !TextUtils.isEmpty(edit_text_2.getText()) && flag){
            login = edit_text.getText().toString();
            password = edit_text_1.getText().toString();

            if(kListener != null){
                //call task here
                AsyncTask<String, Void, String> signupTaskCall= new SignupFragment.signupTask();
                signupTaskCall.execute(PARTIAL_URL, login, password);
            }



        }
        else{
            if(!atHelper(edit_text)){
                edit_text.setError("This needs to be a valid email");
            }

            else if(!ruleHelper(edit_text_1)){
                edit_text_1.setError("Passwords must contain a number and a special character" +
                        "and must be longer than three characters");
            }

            else if(!ruleHelper(edit_text_2)){
                edit_text_2.setError("Passwords must contain a number and a special character" +
                        "and must be longer than three characters");
            }

            else if(TextUtils.isEmpty(edit_text.getText())){
                edit_text.setError("You left this field blank");

            }
            else if(TextUtils.isEmpty(edit_text_1.getText())){
                edit_text_1.setError("You left this field blank");
            }
            else{
                edit_text_2.setError("You left this field blank");
            }
        }

    }

    //checks for @ in email
    public boolean atHelper(EditText tested){
        if(tested.getText().toString().length() > 3 && tested.getText().toString().contains("@")){
            return true;
        }
        return false;
    }

    //checks for password rules
    public boolean ruleHelper(EditText tested){
        if(tested.getText().toString().length() > 3 && tested.getText().toString().matches(".*[1234567890].*")
                && tested.getText().toString().matches(".*[!@#$%^&*?~].*")){
            return true;
        }
        return false;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSignupFragmentInteraction(String login, String password);

    }

    private class signupTask extends AsyncTask<String, Void, String>{

        String response = "";
        private final String SERVICE = "register.php";
        Button b = (Button) getActivity().findViewById(R.id.acceptSignupButton);

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
            Log.e("signup", result);
            b.setEnabled(true);
            int test = 0;
            JSONObject myJSON3;
            try{
                myJSON3 = new JSONObject(result);
                test = myJSON3.getInt("code");
                if(test == 200){
                    if(kListener != null){
                        //call task here
                        kListener.onSignupFragmentInteraction(login, password);
                    }
                    Log.v("code", "" + test);
                }
                else if(test == 100){
                    EditText userText = (EditText) getActivity().findViewById(R.id.selectAUsername);
                    userText.setError("Username taken");
                    Log.v("code", "" + test);
                }
                else{
                    EditText userText = (EditText) getActivity().findViewById(R.id.selectAUsername);
                    userText.setError("Connection Currently Unavailable");
                    Log.v("codeElse", "" + test);
                }
            }catch(JSONException e){
                Log.wtf("signup", "Things went hinky");
                Log.v("codeElse", "" + test);
            }



        }
    }
}
