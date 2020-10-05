package shoppinglist.de.fh_dortmund.com.shoppinglist.realmHelper;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.MyLocation;

public class RealmHelperLocation {

    public static void createLocation(Realm realm, final String locationName, final double longi, final double lat) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                MyLocation.createLocation(realm, locationName,longi, lat);
            }
        });
    }

    //READ
    public static ArrayList<String> retrieve(Realm realm){
        ArrayList<String> locationNames = new  ArrayList<>();
        RealmResults<MyLocation> lists= realm.where(MyLocation.class).findAll();
        for (MyLocation l:lists){
            locationNames.add(l.getLocationName());
        }
        return locationNames;
    }
}
