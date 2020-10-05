package shoppinglist.de.fh_dortmund.com.shoppinglist.model;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ListeArtikel extends RealmObject {
    public static final String FIELD_ID = "id";

    public static AtomicInteger INTEGER_COUNTER;// = new AtomicInteger(0);

    @PrimaryKey
    private int id;
    private int listeId;
    private int katId;
    private int artikelId;
    private String listName;
    private String artName;


    public static void createListeArtikel(Realm realm,int listId,String listeName,int kategorieId, int artId, String artikelName){
        ListeArtikel listeArtikel = realm.createObject(ListeArtikel.class, increment(realm));
        listeArtikel.setListeId(listId);
        listeArtikel.setListName(listeName);
        listeArtikel.setKatId(kategorieId);
        listeArtikel.setArtikelId(artId);
        listeArtikel.setArtName(artikelName);


        //  artikel.setList(liste);
    }

    public int getListeId() {
        return listeId;
    }

    public void setListeId(int listeId) {
        this.listeId = listeId;
    }

    public int getKatId() {
        return katId;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getArtName() {
        return artName;
    }

    public void setArtName(String artName) {
        this.artName = artName;
    }

    public void setKatId(int katId) {
        this.katId = katId;
    }

    public int getId(){
        return id;
    }

    public int getArtikelId() {
        return artikelId;
    }

    public void setArtikelId(int artikelId) {
        this.artikelId = artikelId;
    }

    public String getCountString(){
        return Integer.toString(id);
    }
    private static Object increment(Realm realm) {
        if (INTEGER_COUNTER == null){
            Number listartid = realm.where(ListeArtikel.class).max("id");
            int nextlistartId = (listartid == null) ? 1 : listartid.intValue()+1;
            INTEGER_COUNTER = new AtomicInteger(nextlistartId);
        }
        return INTEGER_COUNTER.getAndIncrement();
    }
}
