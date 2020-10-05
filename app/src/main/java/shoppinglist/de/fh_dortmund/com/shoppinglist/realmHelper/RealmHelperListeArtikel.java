package shoppinglist.de.fh_dortmund.com.shoppinglist.realmHelper;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.ListeArtikel;

public class RealmHelperListeArtikel {
    public static void  createListeArtikel (Realm realm, final ListeArtikel l){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                ListeArtikel.createListeArtikel(realm, l.getListeId(),l.getListName(),l.getKatId(), l.getArtikelId(),l.getArtName());

            }
        });
    }

    //READ
    public static ArrayList<String> retrieve(Realm realm){
        ArrayList<String> listeNames = new  ArrayList<>();
        RealmResults<ListeArtikel> lists= realm.where(ListeArtikel.class).findAll();
        for (ListeArtikel la:lists){
            //listeNames.add(la.get);
        }
        return listeNames;
    }


}

