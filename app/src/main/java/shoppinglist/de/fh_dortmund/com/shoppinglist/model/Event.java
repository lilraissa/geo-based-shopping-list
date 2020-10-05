package shoppinglist.de.fh_dortmund.com.shoppinglist.model;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Event extends RealmObject {

    public static final String FIELD_ID = "id";

    public static AtomicInteger INTEGER_COUNTER ;//= new AtomicInteger(0);

    @PrimaryKey
    private int id;
    private String eventName;
    private String  detail;
    private String isDone;
    private int locationId;

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getIsDone() {
        return isDone;
    }

    public void setIsDone(String isDone) {
        this.isDone = isDone;
    }

    public int getId(){
        return id;
    }

    public static void createEvent(Realm realm, int locId, String eventN, String det){

        Event  event = realm.createObject(Event.class, increment(realm));
        //  Liste liste = realm.where(Liste.class).equalTo("name", MainActivity.LIST_NAME).findFirst();
        event.setEventName(eventN);
        event.setDetail(det);

       // MyLocation loc = realm.where(MyLocation.class).findFirst();
        //event.setLocationId(loc.getId());
        event.setLocationId(locId);

    }

    public String getCountString(){
        return Integer.toString(id);
    }
    private static Object increment(Realm realm) {
        if (INTEGER_COUNTER == null){
            Number eventid = realm.where(Event.class).max("id");
            int nextEventId = (eventid == null) ? 1 : eventid.intValue() + 1;
            INTEGER_COUNTER = new AtomicInteger(nextEventId);
        }
        return INTEGER_COUNTER.getAndIncrement();
    }

    public void setId(int id) {
        this.id = id;
    }
}
