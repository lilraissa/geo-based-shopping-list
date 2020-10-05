package shoppinglist.de.fh_dortmund.com.shoppinglist.model;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Liste extends RealmObject {

    public static final String FIELD_ID = "id";

    public static AtomicInteger INTEGER_COUNTER ;//= new AtomicInteger(0);

    @PrimaryKey
    private int id;
    private String name;
    private String locationName;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public int getId(){
        return id;
    }
    public static void createListe(Realm realm, String listeName){
        Liste liste = realm.createObject(Liste.class, increment(realm));
        liste.setName(listeName);
        liste.setLocationName("");
    }
    public static Liste createListeWithRef(Realm realm, String listeName){
        Liste liste = realm.createObject(Liste.class, increment(realm));
        liste.setName(listeName);
        liste.setLocationName("");
        return liste;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountString(){
        return Integer.toString(id);
    }
    private static Object increment(Realm realm) {
        if (INTEGER_COUNTER == null){
            Number listid = realm.where(Liste.class).max("id");
            int nextlistId = (listid == null) ? 1 : listid.intValue()+1;
            INTEGER_COUNTER = new AtomicInteger(nextlistId);
        }
        return INTEGER_COUNTER.getAndIncrement();
    }


}
