package shoppinglist.de.fh_dortmund.com.shoppinglist.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import shoppinglist.de.fh_dortmund.com.shoppinglist.R;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.Artikel;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.Kategorie;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.ListeArtikel;
import shoppinglist.de.fh_dortmund.com.shoppinglist.model.MyLocation;
import shoppinglist.de.fh_dortmund.com.shoppinglist.realmHelper.RealmHelperArtikel;
import shoppinglist.de.fh_dortmund.com.shoppinglist.realmHelper.RealmHelperListeArtikel;
import shoppinglist.de.fh_dortmund.com.shoppinglist.realmHelper.RealmHelperLocation;
import shoppinglist.de.fh_dortmund.com.shoppinglist.ui.Adapter.ArtikelAdapter;

public class AddArtikelActivity extends Activity implements View.OnClickListener {
    Button artikelButton;
    Realm realm;
    ArrayList<String> lists;
    ArrayAdapter adapter;
    ArtikelAdapter artikelAdapter;
    ListView lv;
    AutoCompleteTextView addEdit;

   // ExpandableListAdapter listAdapter;
   // ExpandableListView expListView;
   // ArrayList<String> listDataHeader;
   // HashMap<String, List<String>> listDataChild;
    TextView artikeladd;
    List<String> listeArtikel;
    public int listkeyId;
    public String listeName;

    public ListView mList;
    public Button speakButton;
    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_artikel);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            listkeyId = extras.getInt(AddArtikelMainActivity.LIST_ID_PARAM, 0);
            listeName = extras.getString(AddArtikelMainActivity.LIST_NAME);
            //selectedTemplateIndex = extras.getInt("selectedTemplateIndex");
        }

        lv= (ListView) findViewById(R.id.listViewArtikel);

        realm = Realm.getDefaultInstance();
        RealmResults<Artikel> results = realm.where(Artikel.class).findAllAsync();
        artikelAdapter = new ArtikelAdapter(results);
        lv.setAdapter(artikelAdapter);

        addArtikel();


        speakButton = (Button) findViewById(R.id.btn_speak);
        speakButton.setOnClickListener(this);

        voiceinputbuttons();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private void addArtikel() {
        artikelButton = (Button)findViewById(R.id.buttonArtikel);
        artikelButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onClick(View v) {
                addEdit = (AutoCompleteTextView)findViewById(R.id.artikelTextview);
                //GET DATA
                Artikel a = new Artikel();
                String s = addEdit.getText().toString();
                a.setName(s);
                a.setKategorie(Kategorie.UNKNOWN_KATEGORIE_ID);
                //SAVE if Artikel not exists
                RealmResults<Artikel> results = realm.where(Artikel.class).equalTo("name",s).findAll();
                if (results.size() == 0){
                   a = RealmHelperArtikel.createArtikel(realm,s);
                    addEdit.setText("");
                    ListeArtikel listeArtikel = new ListeArtikel();
                    listeArtikel.setListeId(listkeyId);
                    listeArtikel.setListName(listeName);
                    listeArtikel.setKatId(Kategorie.UNKNOWN_KATEGORIE_ID);
                    listeArtikel.setArtikelId(a.getId());
                    listeArtikel.setArtName(a.getName());
                    RealmHelperListeArtikel.createListeArtikel(realm, listeArtikel);
                }
                else {
                    Toast.makeText(getBaseContext(), "Artikel exists already!",
                            Toast.LENGTH_LONG).show();
                }

              /*  //SAVE if Artikel not exists
                RealmResults<Artikel> results = realm.where(Artikel.class).findAllAsync();
                for(Artikel artikel: results){
                    if ((a.getName().equals(artikel.getName()))){
                        return;
                    }

                }

                a = RealmHelperArtikel.createArtikel(realm,s);
                addEdit.setText("");
                ListeArtikel listeArtikel = new ListeArtikel();
                listeArtikel.setListeId(listkeyId);
                listeArtikel.setListName(listeName);
                listeArtikel.setKatId(Kategorie.UNKNOWN_KATEGORIE_ID);
                listeArtikel.setArtikelId(a.getId());
                listeArtikel.setArtName(a.getName());

                RealmHelperListeArtikel.createListeArtikel(realm, listeArtikel);*/

                //RETRIEVE
                lists = RealmHelperArtikel.retrieve(realm);
                adapter=new ArrayAdapter(AddArtikelActivity.this,android.R.layout.simple_list_item_1,lists);
                lv.setAdapter(adapter);

            }
        });
    }


    @Override
    public void onClick(View v) {
        startVoiceRecognitionActivity();
    }

    public void voiceinputbuttons() {
        speakButton = (Button) findViewById(R.id.btn_speak);
        mList = (ListView) findViewById(R.id.list);
    }

    public void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Artikelname sprechen");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Fill the list view with the strings the recognizer thought it
            // could have heard
            ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mList.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, matches));
            // matches is the result of voice input. It is a list of what the
            // user possibly said.
            // Using an if statement for the keyword you want to use allows the
            // use of any activity if keywords match
            // it is possible to set up multiple keywords to use the same
            // activity so more than one word will allow the user
            // to use the activity (makes it so the user doesn't have to
            // memorize words from a list)
            // to use an activity from the voice input information simply use
            // the following format;
            // if (matches.contains("keyword here") { startActivity(new
            // Intent("name.of.manifest.ACTIVITY")

            if (matches.contains("information")) {
                informationMenu();
            }
        }
    }

    public void informationMenu() {
        startActivity(new Intent("android.intent.action.INFOSCREEN"));
    }
}

