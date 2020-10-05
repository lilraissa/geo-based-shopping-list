package shoppinglist.de.fh_dortmund.com.shoppinglist;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import io.realm.Realm;
import io.realm.RealmResults;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.Artikel;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.Event;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.MyLocation;
import shoppinglist.de.fh_dortmund.com.shoppinglist.realmHelper.RealmHelperLocation;
import shoppinglist.de.fh_dortmund.com.shoppinglist.service.UserTransitionIntentService;

public class LocationManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    private String longitude;
    private String latitude;
    Realm realm;
    private boolean location_matched = false;
    private boolean event_with_location_associated = false;

    public LocationManager(Context context) {
        mContext = context;
        realm = Realm.getDefaultInstance();
        //
        if (checkIfGooglePlayServicesAreAvailable()) {
            //Get Access to the google service api
            buildGoogleApiClient();
            mGoogleApiClient.connect();
        } else {
            //Use Android Location Services
            //TODO:
        }
    }

    public Location getCoarseLocation() {
        if (mLastLocation != null) {
            return mLastLocation;
        } else return null;
    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private boolean checkIfGooglePlayServicesAreAvailable() {
        int errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
        if (errorCode != ConnectionResult.SUCCESS) {
            // GooglePlayServicesUtil.getErrorDialog(errorCode, (RecentSightings) mContext, 0).show();
            return false;
        }
        return true;
    }

   // to start making requests when connected
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            mLastLocation = location;
            longitude = String.valueOf(location.getLongitude());
            latitude =  String.valueOf(location.getLatitude());

            //lookup in Table  MyLocation
            RealmResults <MyLocation> results  = realm.where(MyLocation.class).equalTo("longitude",longitude).and().equalTo("latitude", latitude).findAll();
            MyLocation optLocation  = realm.where(MyLocation.class).equalTo("longitude",longitude).and().equalTo("latitude", latitude).findFirst();
            int locationID  = optLocation.getId();
            if (results.size() == 1){
                location_matched = true;
            }

            // if true lookup in Event Table if a Reminder with this cordinates exists
            if(location_matched){
                RealmResults <Event> results2  = realm.where(Event.class).equalTo("locationId",locationID).findAll();
                if (results.size() == 1){
                    event_with_location_associated = true;
                }

            }
            // trigger reminder if event is associated with  location
            if (event_with_location_associated){
              /*  Geofence cinemaFence = new Geofence.Builder()
                        //.setRequestId(entry) // some id for you
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                        .setCircularRegion(latitude, longitude, mRadius)
                        .build();*/
            }


           // location matched?? look in table MyLocation


            Toast.makeText(mContext, location.getLongitude() + " , " + location.getLatitude() + " : " + location.getAccuracy(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}
