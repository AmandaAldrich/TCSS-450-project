package group4.tcss450.uw.edu.campanion;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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

import static group4.tcss450.uw.edu.campanion.R.id.checkBoxPet;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class HomeFragment extends Fragment implements  AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private static final String apiURL = "http://api.amp.active.com/camping/campgrounds?";
    public String state = "WA";

    CheckBox semiHookups;
    CheckBox fullHookups;
    CheckBox equine;
    CheckBox pet;

    public String myKey = "&api_key=7kgbp8puq2ffuw8rrm4nfg28";

    private boolean petFlag = false;
    private boolean equineFlag = false;
    private boolean semiHookupFlag = false;
    private boolean fullHookupFlag = false;


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

        semiHookups = (CheckBox) v.findViewById(R.id.checkBoxSemiHook);
        semiHookups.setOnCheckedChangeListener(this);

        fullHookups = (CheckBox) v.findViewById(R.id.checkBoxFullHook);
        fullHookups.setOnCheckedChangeListener(this);

        pet = (CheckBox) v.findViewById(checkBoxPet);
        pet.setOnCheckedChangeListener(this);

        Button b = (Button) v.findViewById(R.id.sendButton);
        b.setOnClickListener(this);

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

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void loadPage() {

        new GetXmlTask().execute(apiURL);


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()){

            case R.id.checkBoxPet:
                if(pet.isChecked()) {
                    petFlag = true;
                }else{
                    petFlag = false;
                }
                break;

            case R.id.checkBoxFullHook:
                if(fullHookups.isChecked()) {
                    fullHookupFlag = true;
                }else{
                    fullHookupFlag = false;
                }
                break;

            case R.id.checkBoxSemiHook:
                if(semiHookups.isChecked()) {
                    semiHookupFlag = true;
                }else{
                    semiHookupFlag = false;
                }
                break;

        }

    }

    @Override
    public void onClick(View v) {

        loadPage();

    }


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

            String stateURL = apiURL + "pstate=" + state; //+ myKey;

            if(petFlag){

                stateURL += "&pets=3010";
            }
            if(semiHookupFlag){

                stateURL += "&water=3006&hookups=3004";

            }
            if(fullHookupFlag){

                stateURL += "&water=3006&hookups=3004&sewer=3007";

            }

            String fullURL = stateURL + myKey;


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
            tv.setText(response);
            if(response.equals("")){
                tv.setText("Oops, this search has turned up no results!");
            }
        }

    }
}