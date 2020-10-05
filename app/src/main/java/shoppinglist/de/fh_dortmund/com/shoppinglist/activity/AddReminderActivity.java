package shoppinglist.de.fh_dortmund.com.shoppinglist.activity;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;
import shoppinglist.de.fh_dortmund.com.shoppinglist.R;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.MyLocation;
import shoppinglist.de.fh_dortmund.com.shoppinglist.realmHelper.RealmHelperEvent;
import shoppinglist.de.fh_dortmund.com.shoppinglist.service.GeofenceTransitionsIntentService;

public class AddReminderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        LocationListener,
        ResultCallback<Status>{

    public static final String EXTRA_MESSAGE = "shoppinglist.de.fh_dortmund.com.shoppinglist";
    private static final String TAG = AddReminderActivity.class.getSimpleName();
    Realm realm;
    private EditText eventNameEditText;
    private String eventName;
    private EditText eventDetailsEditText;
    private String eventDetails = "Test";
    private Button addReminder_button;
    private MyLocation selectedLocation;
    private int selectedLocationId;
    private double selectedLatitude;
    private double selectedLongitude;

    private Location lastLocation;

    private GoogleApiClient googleApiClient;
    Spinner spinner;

    SharedPreferences sharedPref ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.reminder_activity);
        sharedPref = getSharedPreferences("myReminderPref", Context.MODE_PRIVATE);

        realm = Realm.getDefaultInstance();

        setTitle("Location Reminder");

        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);

        RealmResults<MyLocation> childList = realm.where(MyLocation.class).findAll();
        MyLocation[] childs = new MyLocation[childList.size()];
        childList.toArray(childs);
        List<MyLocation> locationListe = Arrays.asList(childs);
        // List<MyLocation> locationListe = new ArrayList<MyLocation>(results);
        List<String> locationNameListe = new ArrayList<String>();

        for (int i = 0; i < locationListe.size(); i++) {
            locationNameListe.add(i, locationListe.get(i).getLocationName());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, locationNameListe);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        // create GoogleApiClient
        createGoogleApi();

        addReminder_button = (Button) findViewById(R.id.addReminder_button);
        addReminder_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventNameEditText = (EditText) findViewById(R.id.eventName);
                eventName = eventNameEditText.getText().toString();

                eventDetailsEditText = (EditText) findViewById(R.id.eventDetails);
                eventDetails = eventDetailsEditText.getText().toString();
                RealmHelperEvent.createEvent(realm, selectedLocationId, eventName, eventDetails);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("eventName", eventName);
                editor.putString("eventDetails", eventDetails);
                editor.commit();

                eventDetailsEditText.setText("");
                eventNameEditText.setText("");

                startGeofence(); //startGeofence() method is responsible for starting the geofencing process in the AddReminderActivity class.


            }
        });
        askPermission();

    }
    // Create GoogleApiClient instance
    private void createGoogleApi() {
        Log.d(TAG, "createGoogleApi()");
        if ( googleApiClient == null ) {
            googleApiClient = new GoogleApiClient.Builder( this )
                    .addConnectionCallbacks( this )
                    .addOnConnectionFailedListener( this )
                    .addApi( LocationServices.API )
                    .build();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Call GoogleApiClient connection when starting the Activity
        googleApiClient.connect();
       /* if (!googleApiClient.isConnected()){
            googleApiClient.blockingConnect(1500, TimeUnit.MILLISECONDS);
        }*/

    }
    @Override
    protected void onStop() {
        super.onStop();

        // Disconnect GoogleApiClient when stopping Activity
        googleApiClient.disconnect();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        selectedLocation = realm.where(MyLocation.class).equalTo("locationName",item).findFirst();
        selectedLatitude = selectedLocation.getLatitude();
        selectedLongitude = selectedLocation.getLongitude();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    // GoogleApiClient.ConnectionCallbacks connected
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected()");
        getLastKnownLocation();
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);
    }
    // Get last known location
    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation()");
        if ( checkPermission() ) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if ( lastLocation != null ) {
                Log.i(TAG, "LasKnown location. " +
                        "Long: " + lastLocation.getLongitude() +
                        " | Lat: " + lastLocation.getLatitude());
                //writeLastLocation();
                startLocationUpdates();
            } else {
                Log.w(TAG, "No location retrieved yet");
                startLocationUpdates();
            }
        }
        else askPermission();
    }
    private LocationRequest locationRequest;
    // Defined in mili seconds.
    // This number in extremely low, and should be used only for debug
    private final int UPDATE_INTERVAL =   1000; // 3 minutes
    private final int FASTEST_INTERVAL = 900;  // 30 secs

    // Start location Updates
    private void startLocationUpdates(){
        Log.i(TAG, "startLocationUpdates()");
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if ( checkPermission() )
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged ["+location+"]");
        Log.d(TAG, "onLocationChanged [Latitude: "+location.getLatitude()+"]");
        lastLocation = location;

    }
    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED );
    }
    private final int REQ_PERMISSION = 999;
    // Asks for permission
    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                this,
                new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                REQ_PERMISSION
        );
    }

    // GoogleApiClient.ConnectionCallbacks suspended
    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "onConnectionSuspended()");
    }

    // GoogleApiClient.OnConnectionFailedListener fail
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, "onConnectionFailed()");
    }

    private static final long GEO_DURATION = Geofence.NEVER_EXPIRE;
    //private static final String GEOFENCE_REQ_ID = "My Geofence";
    private static final float GEOFENCE_RADIUS = 50; // in meters
    private static final String GEOFENCE_REQ_ID = "My Geofence";

    // Create a Geofence
    private Geofence createGeofence( double latitude, double longitude, float radius ) {
        Log.d(TAG, "createGeofence");
        return new Geofence.Builder()
               // .setRequestId(eventDetails)
                .setRequestId(GEOFENCE_REQ_ID)
                .setCircularRegion( latitude, longitude, radius)
                .setExpirationDuration( GEO_DURATION )
                .setTransitionTypes( Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_EXIT )
                .build();
    }

    // Create a Geofence Request
    private GeofencingRequest createGeofenceRequest( Geofence geofence ) {
        Log.d(TAG, "createGeofenceRequest");
        return new GeofencingRequest.Builder()
                .setInitialTrigger( GeofencingRequest.INITIAL_TRIGGER_ENTER )
                .addGeofence( geofence )
                .build();
    }

    private PendingIntent geoFencePendingIntent;
    private final int GEOFENCE_REQ_CODE = 0;
    private PendingIntent createGeofencePendingIntent() {
        Log.d(TAG, "createGeofencePendingIntent");
        if ( geoFencePendingIntent != null )
            return geoFencePendingIntent;

        Intent intent = new Intent( this, GeofenceTransitionsIntentService.class);

        return PendingIntent.getService(
                this, GEOFENCE_REQ_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT );
    }

    // Add the created GeofenceRequest to the device's monitoring list
    private void addGeofence(GeofencingRequest request) {
        Log.d(TAG, "addGeofence");
        if (checkPermission())
            LocationServices.GeofencingApi.addGeofences(
                    googleApiClient,
                    request,
                    createGeofencePendingIntent()
            ).setResultCallback(this);
    }

    // Start Geofence creation process
    private void startGeofence() {
        Log.i(TAG, "startGeofence()");
        Geofence geofence = createGeofence(selectedLatitude,selectedLongitude, GEOFENCE_RADIUS);
        GeofencingRequest geofenceRequest = createGeofenceRequest(geofence);
        addGeofence(geofenceRequest);
    }

   /* public static Intent makeNotificationIntent(Context applicationContext, String msg) {
        Intent startIntent = new Intent(applicationContext, GeofenceTransitionsIntentService.class);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startIntent.putExtra(EXTRA_MESSAGE, msg);
        return startIntent;
    }*/

    private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";
    // Create a Intent send by the notification
    public static Intent makeNotificationIntent(Context context, String msg) {
        Intent intent = new Intent( context, AddReminderActivity.class );
        intent.putExtra( NOTIFICATION_MSG, msg );
        return intent;
    }

    @Override
    public void onResult(@NonNull Status status) {
        Log.i(TAG, "onResult: " + status);
        if ( status.isSuccess() ) {
           // saveGeofence();
            //drawGeofence();
        } else {
            // inform about fail
        }
    }
    // Verify user's response of the permission requested
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ( requestCode ) {
            case REQ_PERMISSION: {
                if ( grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    // Permission granted
                    getLastKnownLocation();

                } else {
                    // Permission denied
                    permissionsDenied();
                }
                break;
            }
        }
    }

    // App cannot work without the permissions
    private void permissionsDenied() {
        Log.w(TAG, "permissionsDenied()");
        // TODO close app and warn user
    }
}

