package com.example.admin.smartcards;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.admin.daos.CardDBAdapter;

import org.w3c.dom.Text;

public class DeckOpen extends Activity {
    ListView cardListFront, cardListBack;
    TextView DeckID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_open);

        Intent intent = getIntent();
        String idStr = intent.getStringExtra("deckID");
        long deckID = Long.parseLong(intent.getStringExtra("deckID"));
        String deckName = intent.getStringExtra("deckName");

        /*if (deckID == 0)
        {
            Toast.makeText(getApplicationContext(),
                    "0", Toast.LENGTH_LONG)
                    .show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),
                    idStr, Toast.LENGTH_LONG)
                    .show();
        }*/

        CardDBAdapter dbHelper = new CardDBAdapter(this);
        SimpleCursorAdapter dataAdapterFront, dataAdapterBack;

        dbHelper.open();

        Cursor cursorFront = dbHelper.fetchCardFrontsByDeck((int) deckID);
        Cursor cursorBack = dbHelper.fetchCardBacksByDeck((int)deckID);

        String[] columnsFront = new String[] {
                CardDBAdapter.KEY_ROWID,
                CardDBAdapter.KEY_FRONT
        };

        String[] columnsBack = new String[] {
                CardDBAdapter.KEY_ROWID,
                CardDBAdapter.KEY_BACK
        };

        int[] toFront = new int[] {
                R.id.cardIDWord,
                R.id.cardFront
        };

        int[] toBack = new int[] {
                R.id.cardIDDef,
                R.id.cardBack
        };

        dataAdapterFront = new SimpleCursorAdapter(this, R.layout.card_word, cursorFront, columnsFront, toFront, 0);
        dataAdapterBack = new SimpleCursorAdapter(this, R.layout.card_def, cursorBack, columnsBack, toBack, 0);

        cardListFront = (ListView) findViewById(R.id.word);
        cardListBack = (ListView) findViewById(R.id.Definitions);

        cardListFront.setAdapter(dataAdapterFront);
        cardListBack.setAdapter(dataAdapterBack);

        DeckID = (TextView) findViewById(R.id.deckNameDisplay);
        DeckID.setText(deckName);

    }


    public void TransitionFront (View view)
    {
        Intent intent = new Intent(DeckOpen.this, CardFront.class);
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
}
