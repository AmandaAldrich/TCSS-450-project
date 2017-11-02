/**
 * Amanda Aldrich
 *
 * This is the Login Fragment, currently no back-end and very rudimentary safety checks
 */

package group4.tcss450.uw.edu.campanion;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{
    //implement that interface again, after you get
    // the first one working

    private OnFragmentInteractionListener lListener;

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
            String login = edit_text.getText().toString();
            String password = edit_text_1.getText().toString();

            if(lListener != null){
                lListener.onLoginFragmentInteraction(login, password);
            }
        }
        else{

            //if the email checks throw false
            if(!atFlag){
                edit_text.setError("This needs to be a valid email");
            }

            //if the rules checks throw false
            else if(!ruleFlag){
                edit_text_1.setError("Passwords must contain a number and a special character" +
                        "and must be longer than three characters");
            }

            //if theh email is blank
            else if(TextUtils.isEmpty(edit_text.getText())){
                edit_text.setError("You left this field blank");

            }

            //if the password is blank
            else{
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
        void onLoginFragmentInteraction(String login, String password);

    }
}
