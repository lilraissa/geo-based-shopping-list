package shoppinglist.de.fh_dortmund.com.shoppinglist.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import shoppinglist.de.fh_dortmund.com.shoppinglist.R;
import shoppinglist.de.fh_dortmund.com.shoppinglist.realmHelper.DataHelper;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.Item;
import shoppinglist.de.fh_dortmund.com.shoppinglist.ui.Adapter.MyListAdapter;

public class ListViewExampleActivity extends AppCompatActivity{

    private Realm realm;
    private Menu menu;
    private MyListAdapter adapter;
    private Button createBut = null;
    private EditText addListEdit;
    ArrayList<String> itemNames;

    protected  void onCreate(Bundle savedInstanceState){
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_listview);
      realm = Realm.getDefaultInstance();



        // RealmResults are "live" views, that are automatically kept up to date, even when changes happen
        // on a background thread. The RealmBaseAdapter will automatically keep track of changes and will
        // automatically refresh when a change is detected.
        RealmResults<Item> counters = realm.where(Item.class).findAll().sort(Item.FIELD_ID);
        adapter = new MyListAdapter(counters);

        //RETRIEVE
       // itemNames=DataHelper.retrieve(realm);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item counter = adapter.getItem(i);
                if (counter == null){
                    return true;
                }

                final int id = counter.getId();
                realm.executeTransactionAsync(new Realm.Transaction(){
                    @Override
                    public void execute(Realm realm){
                        realm.where(Item.class).equalTo(Item.FIELD_ID, id).findAll().deleteAllFromRealm();
                    }

                });
                return true;

            }
        });

        addListAction();

    }
    public void addList()
    {
        TextView tv= (TextView) findViewById(R.id.addList_view);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataHelper
                        .addItemAsync(realm);
            }
        });

    }

    public void addListAction() {
        TextView textView= (TextView) findViewById(R.id.addList_view);
        addListEdit   = (EditText)findViewById(R.id.userInputDialog);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(ListViewExampleActivity.this);
                View mView = layoutInflaterAndroid.inflate(R.layout.dialog_create_list, null);
                final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(ListViewExampleActivity.this);
                alertDialogBuilderUserInput.setView(mView);

                final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Erstellen", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                // ToDo get user input here
                               // Item item = new Item();
                                //item.setListName(userInputDialogEditText.getText().toString());

                                DataHelper
                                        .addItemAsync(realm);
                                //SAVE
                               // DataHelper.save(realm,item);
                                //addListEdit.setText("");

                                //RETRIEVE
                               // itemNames=DataHelper.retrieve(realm);


                            }
                        })

                        .setNegativeButton("Abbrechen",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialogBox, int id) {
                                        dialogBox.cancel();
                                    }
                                });

                AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
                alertDialogAndroid.show();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.listview_options, menu);
        menu.setGroupVisible(R.id.group_normal_mode, true);
        menu.setGroupVisible(R.id.group_delete_mode, false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add:
                DataHelper
                        .addItemAsync(realm);
                return true;
            case R.id.action_random:
                DataHelper.randomAddItemAsync(realm);
                return true;
            case R.id.action_start_delete_mode:
                adapter.enableDeletionMode(true);
                menu.setGroupVisible(R.id.group_normal_mode, false);
                menu.setGroupVisible(R.id.group_delete_mode, true);
                return true;
            case R.id.action_end_delete_mode:
                DataHelper.deleteItemsAsync(realm, adapter.getCountersToDelete());
                // Fall through
            case R.id.action_cancel_delete_mode:
                adapter.enableDeletionMode(false);
                menu.setGroupVisible(R.id.group_normal_mode, true);
                menu.setGroupVisible(R.id.group_delete_mode, false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
