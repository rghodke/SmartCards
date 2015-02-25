package com.example.admin.smartcards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.admin.models.Card;
import com.example.admin.models.Deck;

import java.util.ArrayList;


public class CardBack extends Activity {

    TextView cardBackDisplay;
    Deck currDeck;
    String mode;
    Card currCard;
    ArrayList<Card> cardArray;
    int cardIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_back);
        cardBackDisplay = (TextView) findViewById(R.id.BackDisplay);

        Intent intent = getIntent();
        currDeck = (Deck)intent.getSerializableExtra("deck");
        mode = intent.getStringExtra("mode");
        cardIndex = intent.getIntExtra("index", 0);
        cardArray = currDeck.getCardArray();
        currCard = cardArray.get(cardIndex);

        if (mode.equals("front"))
        {
            cardBackDisplay.setText(currCard.getBackStr());
        }
        else
        {
            cardBackDisplay.setText(currCard.getFrontStr());
        }

        cardIndex++;
    }

    public void GoBacktoDeckView(View view)
    {
        Intent intent = new Intent(CardBack.this, ScoreScreen.class);
        startActivity(intent);
    }

    public void cardCorrect(View view)
    {
        int arraySize = cardArray.size();

        currCard.setIsRight(true);
        cardArray.set(cardIndex, currCard);
        currDeck.setCardArray(cardArray);

        if ((cardIndex + 1) == arraySize)
        {
            Intent intent = new Intent(CardBack.this, ScoreScreen.class);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(CardBack.this, CardFront.class);
            startActivity(intent);
        }
    }

    public void cardWrong(View view)
    {
        int arraySize = cardArray.size();

        if ((cardIndex + 1) == arraySize)
        {
            Intent intent = new Intent(CardBack.this, ScoreScreen.class);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(CardBack.this, CardFront.class);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_card_back, menu);
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
