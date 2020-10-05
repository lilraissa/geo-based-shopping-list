package shoppinglist.de.fh_dortmund.com.shoppinglist;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.Artikel;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.Kategorie;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.Liste;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.ListeArtikel;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.MyLocation;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.Parent;
import shoppinglist.de.fh_dortmund.com.shoppinglist.realmHelper.RealmHelperKategorie;


public class MyApplication extends Application {

    public static final String PACKAGE = "shoppinglist.de.fh_dortmund.com.shoppinglist";
    public static final String ACTION_REMINDER_EDITED = "ACTION_REMINDER_EDITED";

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder()
                .initialData(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.createObject(Parent.class);
                        RealmHelperKategorie.createUnknow(realm);
                        Number artikelid = realm.where(Artikel.class).max("id");
                        int nextArtId = (artikelid == null) ? 1 : artikelid.intValue();
                        Artikel.INTEGER_COUNTER = new AtomicInteger(nextArtId);

                        Number katid = realm.where(Kategorie.class).max("id");
                        int nextKatId = (katid == null) ? 1 : katid.intValue();
                        Kategorie.INTEGER_COUNTER = new AtomicInteger(nextKatId);

                        Number listid = realm.where(Liste.class).max("id");
                        int nextlistId = (listid == null) ? 1 : listid.intValue();
                        Liste.INTEGER_COUNTER = new AtomicInteger(nextlistId);

                        Number listartid = realm.where(ListeArtikel.class).max("id");
                        int nextlistartId = (listartid == null) ? 1 : listartid.intValue();
                        ListeArtikel.INTEGER_COUNTER = new AtomicInteger(nextlistartId);

                        Number locationid = realm.where(MyLocation.class).max("id");
                        int nextlocationid = (locationid == null) ? 1 : locationid.intValue();
                        MyLocation.INTEGER_COUNTER = new AtomicInteger(nextlocationid);


                    }})
                .build();

       //Realm.deleteRealm(realmConfig); // Delete Realm between app restarts.
        Realm.setDefaultConfiguration(realmConfig);

    }

    public static void broadcastOnReminderCreated(Context context) {
        Intent intent = new Intent(ACTION_REMINDER_EDITED);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}