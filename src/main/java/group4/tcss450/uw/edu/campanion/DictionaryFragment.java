package group4.tcss450.uw.edu.campanion;

import android.content.Context;
import android.content.Intent;
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
 * to handle interaction events.
 */
public class DictionaryFragment extends Fragment {

    private static final String PARTIAL_URL
            = "http://cssgate.insttech.washington.edu/~aldrich7/";



    public DictionaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_dictionary, container, false);

        /*AsyncTask<String, Void, String> getPNTask = new getPN();
        getPNTask.execute(PARTIAL_URL);*/

        return view;
    }



    //TO-DO: add number thing and add information...
    //update fragment as such
    //add itinerary


    /*private class getPN extends AsyncTask<String, Void, String>{

        String response = "";
        private final String SERVICE = "login.php";

        @Override
        protected void onPreExecute(){

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
            String phoNum;
            String phoNum2;
            boolean verified = true;
            try{

                myJSON2 = new JSONObject(result);
                test = myJSON2.getInt("code");
                phoNum = myJSON2.getString("phoN");
                phoNum2 = myJSON2.getString("phoN2");

                if(test == 100){ //added
                    TextView tv = (TextView) getActivity().findViewById(R.id.ForestryService);
                    tv.setText(phoNum);
                    TextView tv2 = (TextView) getActivity().findViewById(R.id.naturalResourceService);
                    tv2.setText(phoNum2);


                    Log.v("code", "" + test);
                }
                else if(test == 200){

                    Log.v("code", "" + test);
                }

                else{

                }
            }catch(JSONException e){
                Log.wtf("phoNum", "Things went doubly hinky");
            }



        }
    }*/


}
