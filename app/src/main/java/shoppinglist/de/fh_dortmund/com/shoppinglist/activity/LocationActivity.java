package shoppinglist.de.fh_dortmund.com.shoppinglist.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import shoppinglist.de.fh_dortmund.com.shoppinglist.R;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.Liste;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.MyLocation;
import shoppinglist.de.fh_dortmund.com.shoppinglist.realmHelper.RealmHelperLocation;

public class LocationActivity  extends Activity {
    public static final int MAPS_ACTIVITY = 0;
    String  locName;
    double latitude;
    double longitude;
    Button addLocation_button;
    Realm realm;
    EditText editText_location;
    public int listkeyId;
    private String listeName;
    Button  setLocation_button;

    ArrayList<String> lists;
    ArrayAdapter adapter;
    ListView lv;
    Liste l;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.location_activity);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            listkeyId = extras.getInt(AddArtikelMainActivity.LIST_ID_PARAM, 0);
            listeName = extras.getString(AddArtikelMainActivity.LIST_NAME);
            //selectedTemplateIndex = extras.getInt("selectedTemplateIndex");
        }

        realm = Realm.getDefaultInstance();
        //getGPS(this);
        //launchMap();

        setLocation_button = (Button)findViewById(R.id.setLocation_button);
        setLocation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent launchMapIntent = new Intent(LocationActivity.this, MapsActivity2.class);
                startActivityForResult(launchMapIntent, MAPS_ACTIVITY);
            }

        });

        editText_location = (EditText)findViewById(R.id.editText_location);
        addLocation_button = (Button) findViewById(R.id.addLocation_button);

        addLocation();

    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
            data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MAPS_ACTIVITY){
            if (resultCode == LocationActivity.RESULT_OK){
                latitude = data.getStringExtra(MapsActivity2.LAT_KEY);
                longitude = data.getStringExtra(MapsActivity2.LONG_KEY);
            }

        }
    }*/

    public void addLocation(){

        addLocation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = LocationActivity.this.getSharedPreferences("myPreference", Context.MODE_PRIVATE);
                latitude = Double.longBitsToDouble(sharedPref.getLong("mylatitude", -1));
                longitude = Double.longBitsToDouble(sharedPref.getLong("mylongitude", -1));

                //SAVE if Location not exists
                locName = editText_location.getText().toString();
                RealmResults<MyLocation> results = realm.where(MyLocation.class).equalTo("locationName",locName).findAll();
                if (results.size() == 0){
                    RealmHelperLocation.createLocation(realm,locName,longitude, latitude);

                }
                else {
                    Toast.makeText(getBaseContext(), "LocationName exists already!",
                            Toast.LENGTH_LONG).show();
                }

                l = realm.where(Liste.class).equalTo("id", listkeyId).findFirst();
                realm.beginTransaction();
                l.setLocationName(locName);
                realm.commitTransaction();
                editText_location.setText("");

                //Log.d("latitude", "hi");
            }
        });
    }

   /* public LatLong getLocationFromAddress(Context context, String inputtedAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLong resLatLng = null;

        try {
            address = coder.getFromLocationName(inputtedAddress, 1);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            resLatLng = new LatLong();
            resLatLng.setLatitude(lat);
            resLatLng.setLongitude(lng);
        } catch (Exception e) {
            return null;
        }

        return resLatLng;
    }*/
  /* public void launchMap(){
       final AlertDialog.Builder adb = new AlertDialog.Builder(this);
       editText_location = (EditText)findViewById(R.id.editText_location);
       final Button  setLocation_button = (Button)findViewById(R.id.setLocation_button);
       setLocation_button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {
               try{
                   locationName = editText_location.getText().toString();
                   //locationName = locationName.replace(' ', '+');
                   LatLong latlong = new LatLong();
                   latlong = getLocationFromAddress(v.getContext(),locationName);
                   latitude = latlong.getLatitude()+"";
                   longitude = latlong.getLongitude()+"";

                   Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + locationName);
                   Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                   mapIntent.setPackage("com.google.android.apps.maps");
                   if (mapIntent.resolveActivity(getPackageManager()) != null) {
                       startActivity(mapIntent);
                   }
               } catch (Exception e) {
                   AlertDialog ad = adb.create();
                   ad.setMessage("Failed to Launch Map");
                   ad.show();
               }
           }
       });
   }*/
}
