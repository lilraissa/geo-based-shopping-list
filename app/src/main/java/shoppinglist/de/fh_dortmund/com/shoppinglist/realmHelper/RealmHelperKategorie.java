package shoppinglist.de.fh_dortmund.com.shoppinglist.realmHelper;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.Kategorie;

public abstract class RealmHelperKategorie {


    //WRITE
  /*  public void  save (final Kategorie kat){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                Kategorie  k= realm.copyToRealm(kat);
            }
        });
    }*/

    public static void createKat(Realm realm, final String katName) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Kategorie.createKat(realm, katName);
            }
        });
    }

    public static void createUnknow(Realm realm) {
        Kategorie.createUnknow(realm);
    }

    //READ
    public static ArrayList<String> retrieve(Realm realm) {
        ArrayList<String> listeNames = new ArrayList<>();
        RealmResults<Kategorie> lists = realm.where(Kategorie.class).findAll();
        for (Kategorie k : lists) {
            listeNames.add(k.getName());
        }
        return listeNames;
    }
}
