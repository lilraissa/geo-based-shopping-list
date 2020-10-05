package shoppinglist.de.fh_dortmund.com.shoppinglist.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import shoppinglist.de.fh_dortmund.com.shoppinglist.R;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.Liste;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.MyLocation;
import shoppinglist.de.fh_dortmund.com.shoppinglist.realmHelper.RealmHelperLocation;
import shoppinglist.de.fh_dortmund.com.shoppinglist.ui.Adapter.MyAdapterListe;
import shoppinglist.de.fh_dortmund.com.shoppinglist.realmHelper.RealmHelperListe;
import shoppinglist.de.fh_dortmund.com.shoppinglist.ui.Adapter.ListsAdapter;

public class MainActivity extends AppCompatActivity {
    public static final String LIST_ID_PARAM = "listkeyId";
    public static final String LIST_NAME = "listName";
    Realm realm;
    //ArrayList<String> lists;
    ArrayAdapter adapter;
    ListView lv;
    private EditText addListEdit;
    TextView textView;
    EditText userInputDialogEditText;
    private Liste l ;
    private ListsAdapter listsAdapter;
    private MyAdapterListe mydapterliste;
    ArrayList<Liste> list;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.addList);

        realm = Realm.getDefaultInstance();
        //RealmResults<Liste> results = realm.where(Liste.class).findAllAsync();
       // list = (ArrayList<Liste>) realm.copyFromRealm(results);
        list = new ArrayList<Liste>();
        addListAction();

       // listsAdapter = new ListsAdapter(results);
        lv = (ListView) findViewById(R.id.listView);
        mydapterliste = new MyAdapterListe(this,list);
        //lv.setAdapter(listsAdapter);
        lv.setAdapter(mydapterliste);



        //ITEM CLICKS
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Liste liste  =  mydapterliste.getItem(position);
               // Liste liste = (Liste) mydapterliste.getItem(position);
                //Liste liste = (Liste )clickItemObj;
               // Liste liste =  listsAdapter.getItem(position);
                if (liste != null) {
                    int selectedItemIndex = position;
                    Intent addArtikelIntent = new Intent(MainActivity.this, AddArtikelMainActivity.class);
                    //Liste  liste = realm.where(Liste.class).equalTo("id", position ).findFirst();
                    addArtikelIntent.putExtra(LIST_NAME, liste.getName());
                    addArtikelIntent.putExtra(LIST_ID_PARAM, liste.getId());
                    // addArtikelIntent.putExtra("selectedTemplateIndex", selectedItemIndex);
                    startActivity(addArtikelIntent);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        RealmResults<Liste> results = realm.where(Liste.class).findAllAsync();
        list = (ArrayList<Liste>) realm.copyFromRealm(results);
        //mydapterliste = new MyAdapterListe(this,list);
        mydapterliste.setListe(list);
        // retrieveListId();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public void addListAction() {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflaterAndroid = LayoutInflater.from(MainActivity.this);
                View mView = layoutInflaterAndroid.inflate(R.layout.dialog_create_list, null);
                final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);

                alertDialogBuilderUserInput.setView(mView);

                userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);

                alertDialogBuilderUserInput
                        .setCancelable(false)
                        .setPositiveButton("Erstellen", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                //GET DATA
                                l = new Liste();
                                String s = userInputDialogEditText.getText().toString();
                                l.setName(s);
                                l.setLocationName("");

                                //SAVE
                                RealmResults<Liste> results = realm.where(Liste.class).equalTo("name", s).findAll();
                                if (results.size() == 0){
                                   l = RealmHelperListe.createListeWithRef(realm, s);
                                    list.add(l);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Liste exists already!",
                                            Toast.LENGTH_LONG).show();
                                }


                                userInputDialogEditText.setText("");

                                //RETRIEVE
                                //lists = RealmHelperListe.retrieve(realm);
                               // adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, lists);
                               // adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_2, android.R.id.text1, lists);
                               // lv.setAdapter(adapter);


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


}