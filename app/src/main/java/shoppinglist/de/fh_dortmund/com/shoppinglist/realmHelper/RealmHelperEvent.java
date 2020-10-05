package shoppinglist.de.fh_dortmund.com.shoppinglist.realmHelper;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.Event;

public class RealmHelperEvent {

    public static void createEvent(Realm realm, final int locId,final String eventN, final String det) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Event.createEvent(realm, locId,eventN, det);
            }
        });
    }

    //READ
    public static ArrayList<String> retrieve(Realm realm){
        ArrayList<String> eventNames = new  ArrayList<>();
        RealmResults<Event> lists= realm.where(Event.class).findAll();
        for (Event e:lists){
            eventNames.add(e.getEventName());
        }
        return eventNames;
    }
}
