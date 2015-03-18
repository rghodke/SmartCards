package com.example.admin.smartcards;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.admin.daos.CardDBAdapter;
import com.example.admin.daos.DeckDBAdapter;
import com.example.admin.models.Card;
import com.example.admin.models.Deck;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;


public class DeckOpen extends Activity {
    ListView cardListFront, cardListBack;
    TextView deckNameDisp;
    Deck currentDeck;

    String deckName, deckCourse, deckLocation;
    long deckID;
    int uploadLooper;
    String idStr;

    SimpleCursorAdapter dataAdapterFront, dataAdapterBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_open);

        Intent intent = getIntent();
        idStr = intent.getStringExtra("deckID");
        deckID = Long.parseLong(intent.getStringExtra("deckID"));
        deckName = intent.getStringExtra("deckName");
        deckCourse = intent.getStringExtra("deckCourse");
        deckLocation = intent.getStringExtra("deckLocation");

        deckNameDisp = (TextView) findViewById(R.id.deckNameDisplay);
        deckNameDisp.setText(deckName);

        //currentDeck = new Deck(deckCourse, deckName);

        CardDBAdapter dbHelper = new CardDBAdapter(this);

        dbHelper.open();

        currentDeck = new Deck(deckCourse, deckName, "UCSD");
        currentDeck.setCardArray(dbHelper.fetchCards((int) deckID));

        Cursor cursorFront = dbHelper.fetchCardFrontsByDeck((int) deckID);
        Cursor cursorBack = dbHelper.fetchCardBacksByDeck((int) deckID);

        String[] columnsFront = new String[]{
                CardDBAdapter.KEY_ROWID,
                CardDBAdapter.KEY_FRONT
        };

        String[] columnsBack = new String[]{
                CardDBAdapter.KEY_ROWID,
                CardDBAdapter.KEY_BACK
        };

        int[] toFront = new int[]{
                R.id.cardIDWord,
                R.id.cardFront
        };

        int[] toBack = new int[]{
                R.id.cardIDDef,
                R.id.cardBack
        };

        dataAdapterFront = new SimpleCursorAdapter(this, R.layout.card_word, cursorFront, columnsFront, toFront, 0);
        dataAdapterBack = new SimpleCursorAdapter(this, R.layout.card_def, cursorBack, columnsBack, toBack, 0);

        cardListFront = (ListView) findViewById(R.id.word);
        cardListBack = (ListView) findViewById(R.id.Definitions);

        cardListFront.setAdapter(dataAdapterFront);
        cardListBack.setAdapter(dataAdapterBack);

        cardListFront.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Cursor cursor = (Cursor) parent.getItemAtPosition(position);

            String cardID = cursor.getString(cursor.getColumnIndexOrThrow("_id"));

            Intent intent = new Intent(DeckOpen.this, EditCard.class);
            intent.putExtra("deckID", idStr);
            intent.putExtra("cardID", cardID);
            intent.putExtra("deckName", deckName);
            intent.putExtra("deckCourse", deckCourse);
            startActivity(intent);
            }
        });

        cardListBack.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                String cardID = cursor.getString(cursor.getColumnIndexOrThrow("_id"));

                Intent intent = new Intent(DeckOpen.this, EditCard.class);
                intent.putExtra("deckID", idStr);
                intent.putExtra("cardID", cardID);
                intent.putExtra("deckName", deckName);
                intent.putExtra("deckCourse", deckCourse);
                startActivity(intent);
            }
        });
    }

    public void TransitionFront(View view) {
        Intent intent = new Intent(DeckOpen.this, CardFront.class);
        intent.putExtra("deck", currentDeck);
        intent.putExtra("mode", "front");
        startActivity(intent);
    }

    public void TransitionBack(View view) {
        Intent intent = new Intent(DeckOpen.this, CardFront.class);
        intent.putExtra("deck", currentDeck);
        intent.putExtra("mode", "back");
        startActivity(intent);
    }

    public void goBack(View view) {
        super.finish();
    }

    public void addCard(View view)
    {
        Intent intent = new Intent(DeckOpen.this, CreateCardFront.class);
        intent.putExtra("title", deckName);
        intent.putExtra("course", deckCourse);
        intent.putExtra("newDeck", (int) deckID);
        startActivity(intent);
    }

    public void uploadDeck(View view)
    {
        new AlertDialog.Builder(this)
            .setTitle("Upload")
            .setMessage("Do you want to upload this deck to the Smartcard servers?")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    //Toast.makeText(DownloadConfirm.this, deckParseID + " | " + deckName + " | " + deckCourse + " | " + deckLocation, Toast.LENGTH_SHORT).show();

                    final ParseObject uploadDeck = new ParseObject("decks");
                    uploadDeck.put("deckName", deckName);
                    uploadDeck.put("deckCourse", deckCourse);
                    uploadDeck.put("location", deckLocation);
                    uploadDeck.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            //Toast.makeText(DeckOpen.this, uploadDeck.getObjectId(), Toast.LENGTH_SHORT).show();
                            String objectID = uploadDeck.getObjectId();
                            ArrayList<Card> cardArray = currentDeck.getCardArray();
                            ArrayList<ParseObject> parseCards = new ArrayList();

                            for (int i = 0; i < cardArray.size(); i++){
                                String frontStr = cardArray.get(i).getFrontStr();
                                String backStr = cardArray.get(i).getBackStr();

                                final ParseObject uploadCard = new ParseObject("cards");
                                uploadCard.put("frontStr", frontStr);
                                uploadCard.put("backStr", backStr);
                                uploadCard.put("deckID", objectID);
                                parseCards.add(uploadCard);

                            }

                            ParseObject.saveAllInBackground(parseCards, new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    new AlertDialog.Builder(DeckOpen.this)
                                        .setTitle("Success")
                                        .setMessage("Upload Complete!")
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                finish();
                                            }
                                        }).show();
                                }
                            });
                        }
                    });
                }
            })
            .setNegativeButton(android.R.string.no, null)
            .show();
    }

    public void deleteDeck(View view)
    {
        new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Do you want to delete this deck?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Toast.makeText(DownloadConfirm.this, deckParseID + " | " + deckName + " | " + deckCourse + " | " + deckLocation, Toast.LENGTH_SHORT).show();
                        CardDBAdapter cardDbHelper = new CardDBAdapter(DeckOpen.this);
                        DeckDBAdapter deckDbHelper = new DeckDBAdapter(DeckOpen.this);

                        cardDbHelper.open();
                        deckDbHelper.open();

                        cardDbHelper.deleteCardByDeck((int) deckID);
                        deckDbHelper.deleteDeck((int) deckID);

                        cardDbHelper.close();
                        deckDbHelper.close();

                        new AlertDialog.Builder(DeckOpen.this)
                                .setTitle("Success")
                                .setMessage("Delete Complete!")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        finish();
                                    }
                                }).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    public void editDeck(View view)
    {
        Intent intent = new Intent(DeckOpen.this, EditDeck.class);
        intent.putExtra("deckID", idStr);
        intent.putExtra("deckName", deckName);
        intent.putExtra("deckCourse", deckCourse);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_deck_open, menu);
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

    @Override
    public void onResume()
    {
        super.onResume();
        CardDBAdapter cardDbHelper = new CardDBAdapter(this);
        cardDbHelper.open();

        int count = cardDbHelper.getCardCountByDeck((int) deckID);

        if (count == 0)
        {
            super.finish();
        }
        else
        {
            DeckDBAdapter deckDbHelper = new DeckDBAdapter(this);
            deckDbHelper.open();

            currentDeck = deckDbHelper.grabDeck((int) deckID);

            deckCourse = currentDeck.getCourse();
            deckName = currentDeck.getDeckName();

            deckDbHelper.close();

            deckNameDisp.setText(deckName);

            currentDeck = new Deck(deckCourse, deckName, "UCSD");
            currentDeck.setCardArray(cardDbHelper.fetchCards((int) deckID));

            Cursor cursorFront = cardDbHelper.fetchCardFrontsByDeck((int) deckID);
            Cursor cursorBack = cardDbHelper.fetchCardBacksByDeck((int) deckID);

            String[] columnsFront = new String[]{
                    CardDBAdapter.KEY_ROWID,
                    CardDBAdapter.KEY_FRONT
            };

            String[] columnsBack = new String[]{
                    CardDBAdapter.KEY_ROWID,
                    CardDBAdapter.KEY_BACK
            };

            int[] toFront = new int[]{
                    R.id.cardIDWord,
                    R.id.cardFront
            };

            int[] toBack = new int[]{
                    R.id.cardIDDef,
                    R.id.cardBack
            };

            dataAdapterFront = new SimpleCursorAdapter(this, R.layout.card_word, cursorFront, columnsFront, toFront, 0);
            dataAdapterBack = new SimpleCursorAdapter(this, R.layout.card_def, cursorBack, columnsBack, toBack, 0);

            dataAdapterFront.notifyDataSetChanged();
            dataAdapterBack.notifyDataSetChanged();

            cardListFront.setAdapter(dataAdapterFront);
            cardListBack.setAdapter(dataAdapterBack);

            cardDbHelper.close();
        }
    }
}
