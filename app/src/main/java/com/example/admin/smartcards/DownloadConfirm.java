package com.example.admin.smartcards;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.daos.CardDBAdapter;
import com.example.admin.daos.DeckDBAdapter;
import com.example.admin.models.Card;
import com.example.admin.models.Deck;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class DownloadConfirm extends Activity {
    String deckParseID;
    String deckName;
    String deckCourse;
    String deckLocation;

    ArrayList<Card> cardList;

    DeckDBAdapter deckDbHelper = new DeckDBAdapter(this);
    CardDBAdapter cardDbHelper = new CardDBAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_confirm);

        Intent intent = getIntent();
        deckParseID = intent.getStringExtra("deckParseID");
        deckName = intent.getStringExtra("deckName");
        deckCourse = intent.getStringExtra("deckCourse");
        deckLocation = intent.getStringExtra("deckLocation");

        //Toast.makeText(getApplicationContext(), deckParseID + " | " + deckName + " | " + deckCourse + " | " + deckLocation, Toast.LENGTH_LONG).show();

        TextView deckNameDisp = (TextView) findViewById(R.id.downloadDeckNameDisplay);
        deckNameDisp.setText(deckName + " | " + deckCourse + " | " + deckLocation);

        cardList = new ArrayList();
        final ListView cardListView = (ListView)findViewById(R.id.downloadCardDef);
        final ArrayAdapter<Deck> listAdapter = new ArrayAdapter(DownloadConfirm.this, R.layout.custom_textview, cardList);

        ParseQuery query = ParseQuery.getQuery("cards");
        query.whereEqualTo("deckID", deckParseID);
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> cards, ParseException e) {
                if (e == null) {

                    for (int i = 0; i < cards.size(); i++) {
                        Card tempCard = new Card();
                        String cardFront = cards.get(i).getString("frontStr");
                        String cardBack = cards.get(i).getString("backStr");

                        tempCard.setFrontStr(cardFront);
                        tempCard.setBackStr(cardBack);

                        cardList.add(tempCard);
                    }

                    //ArrayAdapter<String> listAdapter = new ArrayAdapter(DownloadDeck.this, android.R.layout.simple_list_item_1, deckList);
                    //deckListView.setAdapter(listAdapter);

                    cardListView.setAdapter(listAdapter);
                }
                else {
                    Log.d("product", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void goBack(View view) {
        super.finish();
    }

    public void downloadDeck(View view)
    {
        new AlertDialog.Builder(this)
            .setTitle("Download")
            .setMessage("Do you want to download this deck?")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    //Toast.makeText(DownloadConfirm.this, deckParseID + " | " + deckName + " | " + deckCourse + " | " + deckLocation, Toast.LENGTH_SHORT).show();
                    deckDbHelper.open();
                    cardDbHelper.open();

                    int newDeckID = (int)deckDbHelper.createDeck(deckName, deckCourse, deckLocation, deckParseID);

                    for (int j = 0; j < cardList.size(); j++)
                    {
                        String cardFront = cardList.get(j).getFrontStr();
                        String cardBack = cardList.get(j).getBackStr();

                        cardDbHelper.createCard(newDeckID, cardFront, cardBack, deckParseID);
                    }

                    new AlertDialog.Builder(DownloadConfirm.this)
                        .setTitle("Success")
                        .setMessage("Download Complete!")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int whichButton) {
                               deckDbHelper.close();
                               cardDbHelper.close();
                               finish();
                           }
                        }).show();
                }
            })
            .setNegativeButton(android.R.string.no, null)
            .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_download_confirm, menu);
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
