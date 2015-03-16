package com.example.admin.smartcards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.admin.models.Deck;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class DownloadDeck extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Parse.initialize(this, "oV6qA91rZR1xA35KeC3qu3N2tLqHCUGFEsEvjxY7", "N2Irz0ifsTeSFV0YrU0ayVpOMK29so6a8aW8fb2l");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_deck);

        //ArrayList deckList = new ArrayList<String>();

        //final ArrayAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        final ArrayList<Deck> deckList = new ArrayList();
        final ListView deckListView = (ListView)findViewById(R.id.downloadDeckList);
        final ArrayAdapter<Deck> listAdapter = new ArrayAdapter(DownloadDeck.this, R.layout.custom_textview, deckList);
        //deckListView.setAdapter(listAdapter);

        ParseQuery query = ParseQuery.getQuery("decks");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> decks, ParseException e) {
                if (e == null) {
                    //ArrayAdapter listAdapter = new ArrayAdapter<String>(DownloadDeck.this, android.R.layout.simple_list_item_1);
                    //ListView deckListView = (ListView)findViewById(R.id.downloadDeckList);
                    //deckListView.setAdapter(listAdapter);

                    //ArrayList<String> deckList = new ArrayList();

                    //Toast.makeText(getApplicationContext(), "TEST", Toast.LENGTH_LONG).show();
                    //deckList.add(decks.get(0).getString("deckName"));

                    for (int i = 0; i < decks.size(); i++) {
                        Deck tempDeck = new Deck();
                        String deckName = decks.get(i).getString("deckName");
                        String deckCourse = decks.get(i).getString("deckCourse");
                        String deckLocation = decks.get(i).getString("location");
                        String deckParseId = decks.get(i).getObjectId();

                        tempDeck.setDeckName(deckName);
                        tempDeck.setCourse(deckCourse);
                        tempDeck.setLocation(deckLocation);
                        tempDeck.setParseID(deckParseId);

                        deckList.add(tempDeck);
                    }

                    //ArrayAdapter<String> listAdapter = new ArrayAdapter(DownloadDeck.this, android.R.layout.simple_list_item_1, deckList);
                    //deckListView.setAdapter(listAdapter);

                    deckListView.setAdapter(listAdapter);
                    deckListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {

                            Deck selectDeck = (Deck) parent.getItemAtPosition(position);

                            String deckParseID = selectDeck.getParseID();
                            String deckName = selectDeck.getDeckName();
                            String deckCourse = selectDeck.getCourse();
                            String deckLocation = selectDeck.getLocation();

                            Intent intent = new Intent(DownloadDeck.this, DownloadConfirm.class);
                            intent.putExtra("deckParseID", deckParseID);
                            intent.putExtra("deckName", deckName);
                            intent.putExtra("deckCourse", deckCourse);
                            intent.putExtra("deckLocation", deckLocation);
                            startActivity(intent);
                        }
                    });
                }
                else {
                    Log.d("product", "Error: " + e.getMessage());
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_download_deck, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
