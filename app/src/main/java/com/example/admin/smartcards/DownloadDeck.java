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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.admin.models.Deck;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;


public class DownloadDeck extends Activity {

    private Spinner spinner;
    private static final String[]paths = {"Course", "Deck Name"};
    private String selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_deck);

        final ArrayList<Deck> deckList = new ArrayList();
        final ListView deckListView = (ListView)findViewById(R.id.downloadDeckList);
        final ArrayAdapter<Deck> listAdapter = new ArrayAdapter(DownloadDeck.this, R.layout.cutom_textview1, deckList);

        ParseQuery query = ParseQuery.getQuery("decks");

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> decks, ParseException e) {
        if (e == null) {
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
    public void search(View view) {
        EditText searchString = (EditText) findViewById(R.id.searchBar);
        String ss = searchString.getText().toString();

       // Toast.makeText(DownloadDeck.this, ss, Toast.LENGTH_LONG).show();

        final ArrayList<Deck> deckList1 = new ArrayList();
        final ListView deckListView1 = (ListView)findViewById(R.id.downloadDeckList);
        final ArrayAdapter<Deck> listAdapter1 = new ArrayAdapter(DownloadDeck.this, R.layout.cutom_textview1, deckList1);

        ParseObject gameList = new ParseObject("decks");
        ParseQuery<ParseObject> search_query = ParseQuery.getQuery("decks");
//       / search_query.whereContains("deckName", ss);
    //    search_query.whereMatches("deckName", "("+ss+")", "i");
        search_query.whereMatches("deckCourse", "("+ss+")", "i");

        search_query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> decks, ParseException e) {
                if (e == null) {
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

                        deckList1.add(tempDeck);
                    }

                 //   Toast.makeText(DownloadDeck.this, deckList1.isEmpty() + "---", Toast.LENGTH_LONG).show();
                    if (!deckList1.isEmpty()) {
                        deckListView1.setAdapter(listAdapter1);
                        deckListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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



                });}else{
                        Toast.makeText(DownloadDeck.this, "Deck Not Found", Toast.LENGTH_LONG).show();
                        }
            }

            else

            {
                Log.d("product", "Error: " + e.getMessage());
            }
        }
    });
    }
}
