package shoppinglist.de.fh_dortmund.com.shoppinglist.realmHelper;

import java.util.ArrayList;
import java.util.Collection;

import io.realm.Realm;
import io.realm.RealmResults;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.Item;

public class DataHelper {

    // Create 3 counters and insert them into random place of the list.
    public static void randomAddItemAsync(Realm realm) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int i = 0; i < 3; i++) {
                    Item.create(realm, true);
                }
            }
        });
    }

    public static void addItemAsync(Realm realm) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Item.create(realm);
            }
        });
    }

    public static void deleteItemAsync(Realm realm, final long id) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Item.delete(realm, id);
            }
        });
    }

    public static void deleteItemsAsync(Realm realm, Collection<Integer> ids) {
        // Create an new array to avoid concurrency problem.
        final Integer[] idsToDelete = new Integer[ids.size()];
        ids.toArray(idsToDelete);
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (Integer id : idsToDelete) {
                    Item.delete(realm, id);
                }
            }
        });
    }

    //READ
    public static ArrayList<String> retrieve(Realm realm)
    {
        ArrayList<String> itemNames=new ArrayList<>();
        RealmResults<Item> items= realm.where(Item.class).findAll();

        for(Item i:items)
        {
            itemNames.add(i.getListName());
        }

        return itemNames;
    }


    //WRITE
    public static void save(Realm realm, final Item item)
    {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                Item i=realm.copyToRealm(item);

            }
        });
    }

}