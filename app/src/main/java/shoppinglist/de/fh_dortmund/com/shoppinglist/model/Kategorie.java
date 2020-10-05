package shoppinglist.de.fh_dortmund.com.shoppinglist.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Kategorie extends RealmObject {

    public static final String FIELD_ID = "id";

    public static AtomicInteger INTEGER_COUNTER; //= new AtomicInteger(2);
    public static final int UNKNOWN_KATEGORIE_ID = 1;

    @PrimaryKey
    private int id;
    private String name;
    private RealmList<Artikel> productList = new RealmList<Artikel>();

    @Ignore
    private List<Artikel> dieArtikel;

    public int getId(){
        return id;
    }
    public String getName() {
        return name;
    }


    public static void createKat(Realm realm, String kategorie){
        Kategorie kat = realm.createObject(Kategorie.class, increment(realm));
        kat.setName(kategorie);

    }

    public static void createUnknow(Realm realm){
        Kategorie kat = realm.createObject(Kategorie.class, UNKNOWN_KATEGORIE_ID);
        kat.setName("Unkategorisiert");
    }
    public void setName(String name) {
        this.name = name;
    }

    public RealmList<Artikel> getProductList() {
        return productList;
    }
    public void setProductList(RealmList<Artikel> productList) {
        this.productList = productList;
    }
    public String getCountString(){
        return Integer.toString(id);
    }
    public static Object increment(Realm realm) {
        if (INTEGER_COUNTER == null){
            Number katid = realm.where(Kategorie.class).max("id");
            int nextKatId = (katid == null) ? 1 : katid.intValue()+1;
            INTEGER_COUNTER = new AtomicInteger(nextKatId);
        }

        return INTEGER_COUNTER.getAndIncrement();
    }

    public List<Artikel> getDieArtikel() {
        return dieArtikel;
    }

    public void setDieArtikel(List<Artikel> dieArtikel) {
        this.dieArtikel = dieArtikel;
    }
}
