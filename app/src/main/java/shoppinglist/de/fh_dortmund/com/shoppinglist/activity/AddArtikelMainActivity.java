package shoppinglist.de.fh_dortmund.com.shoppinglist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import shoppinglist.de.fh_dortmund.com.shoppinglist.activity.viewPager.CustomTabActivity;
import shoppinglist.de.fh_dortmund.com.shoppinglist.R;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.Artikel;
import shoppinglist.de.fh_dortmund.com.shoppinglist.ui.Adapter.ExpandableListAdapter;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.Kategorie;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.ListeArtikel;
import shoppinglist.de.fh_dortmund.com.shoppinglist.realmHelper.RealmHelperKategorie;


public class AddArtikelMainActivity extends AppCompatActivity implements View.OnClickListener {

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expandableListView;
    private ArrayList<Kategorie> SectionList = new ArrayList<>();
    private LinkedHashMap<String, Kategorie> mySection = new LinkedHashMap<>();
    private TextView artikeladd;
    private Realm realm;
    public int listkeyId;
    private String activityName;
    int selectedTemplateIndex;
    public static final String LIST_ID_PARAM = "listkeyId";
    public static final String LIST_NAME = "listeName";
    public static final String LIST_NAME2 = "listeName";
    private TextView addLocationTextView;
    TextView textview_config;
    private TextView textview_addReminder;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_artikel_main);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            listkeyId = extras.getInt(MainActivity.LIST_ID_PARAM, 0);
            activityName = extras.getString(MainActivity.LIST_NAME);
            //selectedTemplateIndex = extras.getInt("selectedTemplateIndex");
        }

        this.setTitle(activityName);

        realm = Realm.getDefaultInstance();

        //Just add some data to start with
        // AddProduct();
        // get the listview
        expandableListView = (ExpandableListView) findViewById(R.id.lvExp);

        listAdapter = new ExpandableListAdapter(this, SectionList);
        //attach the adapter to the list
        expandableListView.setAdapter(listAdapter);
        //expand all Groups
        //expandAll();

        artikeladd = (TextView) findViewById(R.id.textview_addArtikel);
        artikeladd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addArtikelIntent = new Intent(AddArtikelMainActivity.this, AddArtikelActivity.class);
                addArtikelIntent.putExtra(LIST_ID_PARAM, listkeyId);
                addArtikelIntent.putExtra(LIST_NAME, activityName);
                startActivity(addArtikelIntent);
            }
        });

        addLocationTextView = (TextView) findViewById(R.id.textview_addLocations);
        addLocationTextView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent addLocationIntent = new Intent(AddArtikelMainActivity.this, LocationActivity.class);
                addLocationIntent.putExtra(LIST_ID_PARAM, listkeyId);
                addLocationIntent.putExtra(LIST_NAME, activityName);
                startActivity(addLocationIntent);

            }
        });

        textview_addReminder = (TextView)findViewById(R.id.textview_addReminder);
        textview_addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent addReminderIntent = new Intent(AddArtikelMainActivity.this, AddReminderActivity.class);
                startActivity(addReminderIntent);
            }

        });

        textview_config = (TextView)findViewById(R.id.textview_config) ;
        textview_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent customTabIntent = new Intent(AddArtikelMainActivity.this, CustomTabActivity.class);
                customTabIntent.putExtra(LIST_NAME2, activityName);
                startActivity(customTabIntent);
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

        initExpandableList();
        // retrieveListId();
    }

    void retrieveListId() {
        String newString;
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            newString = null;
        } else {
            newString = extras.getString("LIST_ID_PARAM");
        }
    }

    void initExpandableList() {
        //listAdapter.
        ArrayList sectionList = new ArrayList<>();
        // RealmResults<Kategorie> resultskat = realm.where(Kategorie.class).findAll();
        Kategorie k = realm.where(Kategorie.class).findFirst();
        RealmResults<ListeArtikel> res = realm.where(ListeArtikel.class).equalTo("listeId", listkeyId).findAll();
        Kategorie kat = new Kategorie();
        kat.setName(k.getName());
        List<Artikel> list = new ArrayList<>();

        for (ListeArtikel l : res) {
            Artikel dieArtikel = realm.where(Artikel.class).equalTo(Artikel.FIELD_ID, l.getArtikelId()).findFirst();
            list.add(dieArtikel);
        }
        kat.setDieArtikel(list);
        sectionList.add(kat);

        listAdapter.setDeptList(sectionList);
        //expand all Groups
        expandAll();
    }

    @Override
    public void onClick(View v) {

    }

    //load some initial data into out list
    private void AddProduct() {

        addProduct("Unkategorisiert", "Salz");

    }

    //method to expand all groups
    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            expandableListView.expandGroup(i);
        }
    }

    //method to collapse all groups
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            expandableListView.collapseGroup(i);
        }
    }

    //here we maintain our products in various departments
    public int addProduct(String katName, String product) {

        int groupPosition = 0;

        //check the hash map if the group already exists
        Kategorie kategorie = mySection.get(katName);
        //add the group if doesn't exists
        if (kategorie == null) {
            kategorie = new Kategorie();
            kategorie.setName(katName);
            RealmHelperKategorie.createKat(realm, katName);
            mySection.put(katName, kategorie);
            SectionList.add(kategorie);
        }

        //get the children for the group
        RealmList<Artikel> productList = kategorie.getProductList();
        //size of the children list
        int listSize = productList.size();
        //add to the counter
        listSize++;

        //create a new child and add that to the group
        Artikel artikel = new Artikel();
        artikel.setName(product);
        productList.add(artikel);
        kategorie.setProductList(productList);

        //find the group position inside the list
        groupPosition = SectionList.indexOf(kategorie);
        return groupPosition;
    }


}
