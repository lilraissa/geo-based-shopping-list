package shoppinglist.de.fh_dortmund.com.shoppinglist.model;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MyLocation extends RealmObject {

    public static final String FIELD_ID = "id";

    public static AtomicInteger INTEGER_COUNTER ;//= new AtomicInteger(0);

    @PrimaryKey
    private int id;
    private String locationName;
    private double longitude;
    private double latitude;

    public static String getFieldId() {
        return FIELD_ID;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getId(){
        return id;
    }
    public static void createLocation(Realm realm, String locationName, double longi, double lat){
        MyLocation location = realm.createObject(MyLocation.class, increment(realm));
        location.setLocationName(locationName);
        location.setLongitude(longi);
        location.setLatitude(lat);

    }

    public String getCountString(){
        return Integer.toString(id);
    }
    private static Object increment(Realm realm) {
        if (INTEGER_COUNTER == null){
            Number locationid = realm.where(MyLocation.class).max("id");
            int nextlocationId = (locationid == null) ? 1 : locationid.intValue()+1;
            INTEGER_COUNTER = new AtomicInteger(nextlocationId);
        }

        return INTEGER_COUNTER.getAndIncrement();
    }

}
