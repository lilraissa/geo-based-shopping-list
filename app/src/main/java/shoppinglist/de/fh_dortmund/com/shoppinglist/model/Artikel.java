package shoppinglist.de.fh_dortmund.com.shoppinglist.model;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Artikel extends RealmObject {
    public static final String FIELD_ID = "id";

    public static AtomicInteger INTEGER_COUNTER ;//= new AtomicInteger(0);

    @PrimaryKey
    private int id;

    private String name = "";
    private int  kategorie;
    private Liste list;

    public Liste getList() {
        return list;
    }

    public void setList(Liste list) {
        this.list = list;
    }

    public int getId(){
        return id;
    }

    public static Artikel createArtikel(Realm realm, String artikelName){
        Kategorie kat = realm.where(Kategorie.class).equalTo(FIELD_ID,Kategorie.UNKNOWN_KATEGORIE_ID ).findFirst();
        Artikel artikel = realm.createObject(Artikel.class, increment(realm));

        //  Liste liste = realm.where(Liste.class).equalTo("name", MainActivity.LIST_NAME).findFirst();
        artikel.setName(artikelName);
        artikel.setKategorie(kat.getId());
        //  artikel.setList(liste);
        return artikel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKategorie() {
        return kategorie;
    }

    public void setKategorie(int kategorie) {
        this.kategorie = kategorie;
    }

    public String getCountString(){
        return Integer.toString(id);
    }
    private static Object increment(Realm realm) {
        if (INTEGER_COUNTER == null){
            Number artikelid = realm.where(Artikel.class).max("id");
            int nextArtId = (artikelid == null) ? 1 : artikelid.intValue() + 1;
            INTEGER_COUNTER = new AtomicInteger(nextArtId);
        }
        return INTEGER_COUNTER.getAndIncrement();
    }

    public void setId(int id) {
        this.id = id;
    }
}
