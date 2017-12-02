/**
 * Amanda Aldrich
 *
 * Main Activity for Login and Register Capabilities
 */

package group4.tcss450.uw.edu.campanion;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements ChoiceFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener, SignupFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            if (findViewById(R.id.fragmentContainer) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, new ChoiceFragment())
                        .commit();
            }
        }
    }


    //calls login or register depending on button press
    @Override
    public void onFragmentInteraction(ChoiceFragment.Choice choice) {

        switch (choice){
            case  LOGIN:

                LoginFragment loginFragment;
                loginFragment = new LoginFragment();
                Bundle args = new Bundle();
                args.putSerializable(getString(R.string.choice_key), choice);
                loginFragment.setArguments(args);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, loginFragment)
                        .addToBackStack(null);


                // Commit the transaction
                transaction.commit();
                //do things

                break;

            case  REGISTER:

                SignupFragment signupFragment;
                signupFragment = new SignupFragment();
                Bundle args1 = new Bundle();
                args1.putSerializable(getString(R.string.choice_key), choice);
                signupFragment.setArguments(args1);
                FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, signupFragment)
                        .addToBackStack(null);


                // Commit the transaction
                transaction1.commit();

                break;

        }
    }


    //login hands off to our placeholder for now
    @Override
    public void onLoginFragmentInteraction(String login, boolean verified) {

        User user = new User();
        user.setLogin(login);


        PlaceHolderFragment displayFragment;
        displayFragment = new PlaceHolderFragment();
        Bundle args2 = new Bundle();
        args2.putSerializable(getString(R.string.login_key), user);
        displayFragment.setArguments(args2);
        FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, displayFragment)
                .addToBackStack(null);

        // Commit the transaction
        transaction2.commit();



    }

    //signup hands off to our login for now
    @Override
    public void onSignupFragmentInteraction(String login, String password) {

        User user = new User();
        user.setLogin(login);
        user.setPassword(password);

        PlaceHolderFragment displayFragment;
        displayFragment = new PlaceHolderFragment();
        Bundle args3 = new Bundle();
        args3.putSerializable(getString(R.string.signup_key), user);
        displayFragment.setArguments(args3);
        FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, displayFragment)
                .addToBackStack(null);

        // Commit the transaction
        transaction3.commit();

    }
}

