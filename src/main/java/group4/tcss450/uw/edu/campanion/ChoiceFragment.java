/**
 * Amanda Aldrich
 *
 * The Front Facing Starting Choice Fragment
 */

package group4.tcss450.uw.edu.campanion;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChoiceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ChoiceFragment extends Fragment implements View.OnClickListener{

    public enum Choice {LOGIN, REGISTER};

    private OnFragmentInteractionListener mListener;

    public ChoiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_choice, container, false);
        Button b = (Button) v.findViewById(R.id.loginButton);
        b.setOnClickListener(this);
        b = (Button) v.findViewById(R.id.signupButton);
        b.setOnClickListener(this);

        return v;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            switch (view.getId()) {
                case R.id.loginButton:
                    mListener.onFragmentInteraction(Choice.LOGIN);
                    break;
                case R.id.signupButton:
                    mListener.onFragmentInteraction(Choice.REGISTER);
                    break;

            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Choice choice);

    }
}
