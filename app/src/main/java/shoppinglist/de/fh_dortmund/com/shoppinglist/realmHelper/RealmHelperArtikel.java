package shoppinglist.de.fh_dortmund.com.shoppinglist.realmHelper;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmResults;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.Artikel;

public abstract class RealmHelperArtikel {
    //Realm realm;


    //WRITE
   /* public void  save (final Artikel artikel){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Artikel  a= realm.copyToRealm(artikel);
            }
        });
    }*/

    public static Artikel createArtikel (Realm realm, final String artikelName){
        realm.beginTransaction();
        Artikel a =  Artikel.createArtikel(realm, artikelName);
        realm.commitTransaction();
        /*
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
          a =  Artikel.createArtikel(realm, artikelName);
            }
        });*/
        return a;
    }

    //READ
    public static ArrayList<String> retrieve(Realm realm){
        ArrayList<String> listeNames = new  ArrayList<>();
        RealmResults<Artikel> lists= realm.where(Artikel.class).findAll();
        for (Artikel a:lists){
            listeNames.add(a.getName());
        }
        return listeNames;
    }


    public static void increment(Realm realm) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if(Artikel.INTEGER_COUNTER == null) {
                    Number artikelid = realm.where(Artikel.class).max("id");
                    int nextArtId = (artikelid == null) ? 1 : artikelid.intValue();
                    Artikel.INTEGER_COUNTER = new AtomicInteger(nextArtId);
                } else {

                }

            }
        });
    }
}
