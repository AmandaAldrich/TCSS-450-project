package group4.tcss450.uw.edu.campanion;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class HomeFragment extends Fragment implements  AdapterView.OnItemSelectedListener{

    private static final String apiURL = "http://api.amp.active.com/camping/campgrounds?";
    public String state = "WA";
    public String myKey = "&api_key=7kgbp8puq2ffuw8rrm4nfg28";

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        Spinner spinner = (Spinner) v.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.state_codes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(1);
        spinner.setOnItemSelectedListener(this);
        // Inflate the layout for this fragment
        return v;
    }

    public void onStart(){
        super.onStart();
        loadPage();



    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        state = (String) parent.getAdapter().getItem(position);
        loadPage();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void loadPage() {

        new GetXmlTask().execute(apiURL);


    }

    // Implementation of AsyncTask used to download XML feed from stackoverflow.com.
    private class GetXmlTask extends AsyncTask<String, Void, String> {
        private Exception exception;
        TextView tv = (TextView) getActivity().findViewById(R.id.textView2);
        ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);

        protected void onPreExecute() {
            tv.setText("");
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... urls) {
            //where state finding and other queries will go
            String requestMethod = "GET";

            String fullURL = apiURL + "pstate=" + state + myKey; //this will need to grow more intelligently
            //as we implement more search criteria.
            InputStream stream = null;
            StringBuilder sb = new StringBuilder();

            try {
                URL url = new URL(fullURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod(requestMethod);

                urlConnection.setInstanceFollowRedirects(true);
                boolean redirect = false;
                if(urlConnection.getResponseCode() >= 300 && urlConnection.getResponseCode() <= 307 && urlConnection.getResponseCode() != 306){
                    do{
                        redirect = false; // reset the value each time
                        String loc = urlConnection.getHeaderField("location"); // get location of the redirect
                        if(loc == null) {
                            redirect = false;
                            continue;
                        }
                        url = new URL(loc);
                        urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod(requestMethod);

                        if(urlConnection.getResponseCode() != 500) { // 500 = fail
                            if(urlConnection.getResponseCode() >=300 && urlConnection.getResponseCode() <= 307 && urlConnection.getResponseCode() != 306) {
                                redirect= true;
                            }

                        }

                    }while(redirect);
                }

                Log.v("potato", "first try");
                try {
                    Log.v("potato", "second try");

                    stream = urlConnection.getInputStream();

                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(stream);

                    NodeList nList = doc.getElementsByTagName("result");

                    for (int temp = 0; temp < nList.getLength(); temp++) {

                        Node nNode = nList.item(temp);

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                            Element eElement = (Element) nNode;


                            sb.append(eElement.getAttribute("facilityID") + ": ");
                            sb.append(eElement.getAttribute("facilityName") + "\n");
                            sb.append(eElement.getAttribute("latitude") + " ");
                            sb.append(eElement.getAttribute("longitude") + "\n\n");
                        }
                    }
                }
                finally{
                    if(stream != null){
                        stream.close();
                    }
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
            return sb.toString();
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);
            tv.setText(response + "\n End Stream");
        }

    }
}