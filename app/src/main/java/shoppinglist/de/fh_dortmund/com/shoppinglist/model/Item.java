package shoppinglist.de.fh_dortmund.com.shoppinglist.model;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Item extends RealmObject {
    public static final String FIELD_ID = "id";

     private static AtomicInteger INTEGER_COUNTER = new AtomicInteger(0);

     @PrimaryKey private int id;
    private String listName;
    public int getId(){
         return id;
     }
    public String getListName(){
        return listName;
    }
    public void setListName(String name) {
        this.listName = name;
    }

     public String getCountString(){
         return Integer.toString(id);
     }
    //  create() & delete() needs to be called inside a transaction.
    public static void create (Realm realm){
         create(realm, false);
    }

    public static void create(Realm realm, boolean randomlyInsert) {
         Parent parent = realm.where(Parent.class).findFirst();
        RealmList<Item> items = parent.getItemList();
        Item counter = realm.createObject(Item.class, increment());
        if(randomlyInsert && items.size()>0){
            Random rand = new Random();
            items.listIterator(rand.nextInt(items.size())).add(counter);
        }
        else{
            items.add(counter);

        }
    }

   public static void delete(Realm realm, long id){
         Item item = realm.where(Item.class).equalTo(FIELD_ID, id).findFirst();
        // Otherwise it has been deleted already.
        if (item != null) {
            item.deleteFromRealm();
        }
    }
    private static Object increment() {
         return INTEGER_COUNTER.getAndIncrement();
    }
}
