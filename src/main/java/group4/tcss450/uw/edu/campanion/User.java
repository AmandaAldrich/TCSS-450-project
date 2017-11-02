/**
 * Amanda Aldrich
 *
 * A Simple User class that allows the easy passing of email and password
 * between fragments.
 */

package group4.tcss450.uw.edu.campanion;

import java.io.Serializable;


public class User implements Serializable{

    String login;
    String password;
    String results;

    private static final long serialVersionUID = 0L;

    //getters
    public String getLogin() {
        return login.toString();
    }

    public String getPassword() {
        return password.toString();
    }

    public String getResults() {
        return results.toString();
    }


    //setters
    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setresults(String results) {
        this.results = results;
    }

}
