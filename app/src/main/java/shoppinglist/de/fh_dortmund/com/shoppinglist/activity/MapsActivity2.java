package shoppinglist.de.fh_dortmund.com.shoppinglist.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import shoppinglist.de.fh_dortmund.com.shoppinglist.GeocodeJSONParser;
import shoppinglist.de.fh_dortmund.com.shoppinglist.R;

public class MapsActivity2 extends FragmentActivity {
   // public static final String LAT_KEY = "LAT_KEY";
   // public static final String LONG_KEY = "LONG_KEY";
    private GoogleMap mMap;
    Button mBtnFind;
    EditText etPlace;
    double lat;
    double lng;
    SharedPreferences sharedPref ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        sharedPref = getSharedPreferences("myPreference", Context.MODE_PRIVATE);

        // Getting reference to the find button
        mBtnFind = (Button) findViewById(R.id.btn_show);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        // Getting reference to the Google Map
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                // Getting reference to the Google Map
                mMap = googleMap;
            }
        });

        // Getting reference to EditText
        etPlace = (EditText) findViewById(R.id.et_place);

        // Setting click event listener for the find button
        mBtnFind.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // Getting the place entered
                String location = etPlace.getText().toString();

                if(location==null || location.equals("")){
                    Toast.makeText(getBaseContext(), "No Place is entered", Toast.LENGTH_SHORT).show();
                    return;
                }

                String url = "https://maps.googleapis.com/maps/api/geocode/json?";


                try {
                    // encoding special characters like space in the user input place
                    location = URLEncoder.encode(location, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String address = "address=" + location;

                String sensor = "sensor=false";

                // url , from where the geocoding data is fetched
                url = url + address + "&" + sensor;

                // Instantiating DownloadTask to get places from Google Geocoding service
                // in a non-ui thread
                DownloadTask downloadTask = new DownloadTask();

                // Start downloading the geocoding places
                downloadTask.execute(url);


            }
        });
        /*Intent intent = new Intent();
        intent.putExtra(LAT_KEY, lat+"");
        intent.putExtra(LONG_KEY, lng+"");
        setResult(MapsActivity2.RESULT_OK, intent);
        //finish();*/
    }

    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }

/** A class, to download Places from Geocoding webservice */
class DownloadTask extends AsyncTask<String, Integer, String>{

    String data = null;

    // Invoked by execute() method of this object
    @Override
    protected String doInBackground(String... url) {
        try{
            data = downloadUrl(url[0]);
        }catch(Exception e){
            Log.d("Background Task",e.toString());
        }
        return data;
    }

    // Executed after the complete execution of doInBackground() method
    @Override
    protected void onPostExecute(String result){

        // Instantiating ParserTask which parses the json data from Geocoding webservice
        // in a non-ui thread
        ParserTask parserTask = new ParserTask();

        // Start parsing the places in JSON format
        // Invokes the "doInBackground()" method of the class ParseTask
        parserTask.execute(result);
    }
}
/** A class to parse the Geocoding Places in non-ui thread */
class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

    JSONObject jObject;

    // Invoked by execute() method of this object
    @Override
    protected List<HashMap<String,String>> doInBackground(String... jsonData) {

        List<HashMap<String, String>> places = null;
        GeocodeJSONParser parser = new GeocodeJSONParser();

        try{
            jObject = new JSONObject(jsonData[0]);

            /** Getting the parsed data as a an ArrayList */
            places = parser.parse(jObject);

        }catch(Exception e){
            Log.d("Exception",e.toString());
        }
        return places;
    }

    // Executed after the complete execution of doInBackground() method
    @Override
    protected void onPostExecute(List<HashMap<String,String>> list){

        // Clears all the existing markers
        mMap.clear();

        for(int i=0;i<list.size();i++){

            // Creating a marker
            MarkerOptions markerOptions = new MarkerOptions();

            // Getting a place from the places list
            HashMap<String, String> hmPlace = list.get(i);

            // Getting latitude of the place
            lat = Double.parseDouble(hmPlace.get("lat"));

            // Getting longitude of the place
            lng = Double.parseDouble(hmPlace.get("lng"));

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putLong("mylatitude", Double.doubleToRawLongBits(lat));
            editor.putLong("mylongitude", Double.doubleToRawLongBits(lng));
            editor.commit();

            //finish();
            // Getting name
            String name = hmPlace.get("formatted_address");

            LatLng latLng = new LatLng(lat, lng);

            // Setting the position for the marker
            markerOptions.position(latLng);

            // Setting the title for the marker
            markerOptions.title(name);

            // Locate the first location
            //if(i==0)
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

            // Placing a marker on the touched position
            mMap.addMarker(markerOptions);

        }
    }
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
   // @Override
   /* public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        // Setting a click event handler for the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);
            }
        });
    }*/



