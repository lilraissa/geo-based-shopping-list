package shoppinglist.de.fh_dortmund.com.shoppinglist.realmHelper;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.Liste;

public class RealmHelperListe {


   /* //WRITE
    public void  save (final Liste liste){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Liste  l= realm.copyToRealm(liste);
            }
        });
    }*/

    public static void createListe(Realm realm, final String listeName) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Liste.createListe(realm, listeName);
            }
        });
    }
    public static Liste createListeWithRef(Realm realm, final String listeName) {
        realm.beginTransaction();
        Liste l = Liste.createListeWithRef(realm, listeName);
        realm.commitTransaction();
        return l;
    }

    //READ
    public static ArrayList<String> retrieve(Realm realm){
        ArrayList<String> listeNames = new  ArrayList<>();
        RealmResults<Liste> lists= realm.where(Liste.class).findAll();
        for (Liste l:lists){
            listeNames.add(l.getName());
        }
        return listeNames;
    }
}
