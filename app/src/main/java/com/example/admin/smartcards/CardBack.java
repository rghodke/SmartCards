package com.example.admin.smartcards;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.admin.models.Card;
import com.example.admin.models.Deck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


public class CardBack extends Activity implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener{

    TextView cardBackDisplay;
    Deck currDeck;
    String mode;
    Card currCard;
    ArrayList<Card> cardArray;
    int cardIndex;

    TextToSpeech tts;

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

        tts = new TextToSpeech(this,this);
        tts.setLanguage(Locale.US);

        if (mode.equals("front"))
        {
            cardBackDisplay.setText(currCard.getBackStr());
        }
        else
        {
            cardBackDisplay.setText(currCard.getFrontStr());
        }

    }

    @Override
    public void onInit(int status)
    {
        HashMap<String, String> myHashAlarm = new HashMap<String, String>();
        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_NOTIFICATION));
        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "test");
        if (status == TextToSpeech.SUCCESS)
        {
            int result = tts.setOnUtteranceCompletedListener(this);
            if (mode.equals("front"))
            {
                tts.speak(currCard.getBackStr(), TextToSpeech.QUEUE_ADD, myHashAlarm);
            }
            else
            {
                tts.speak(currCard.getFrontStr(), TextToSpeech.QUEUE_ADD, myHashAlarm);
            }


        }
        else
        {}

    }

    @Override
    public void onUtteranceCompleted(String utteranceId)
    {
        // Toast.makeText(this, "onUtteranceCompleted", Toast.LENGTH_SHORT).show();
    }

    public void GoBacktoDeckView(View view)
    {
        Intent intent = new Intent(CardBack.this, ScoreScreen.class);
        tts.stop();
				intent.putExtra("deck", currDeck);
        startActivity(intent);
    }

    public void cardCorrect(View view)
    {
        cardIndex++;
        int arraySize = cardArray.size();

        if (cardIndex == arraySize)
        {
            currCard.setIsRight(true);
            cardArray.set((cardIndex-1), currCard);
            currDeck.setCardArray(cardArray);

            Intent intent = new Intent(CardBack.this, ScoreScreen.class);
            intent.putExtra("deck", currDeck);
            tts.stop();
            startActivity(intent);
        }
        else
        {
            currCard.setIsRight(true);
            cardArray.set((cardIndex-1), currCard);
            currDeck.setCardArray(cardArray);

            Intent intent = new Intent(CardBack.this, CardFront.class);
            intent.putExtra("deck", currDeck);
            intent.putExtra("mode", mode);
            intent.putExtra("index", cardIndex);
            tts.stop();
            startActivity(intent);
        }
    }

    public void cardWrong(View view)
    {
        cardIndex++;
        int arraySize = cardArray.size();

        if (cardIndex == arraySize)
        {
            Intent intent = new Intent(CardBack.this, ScoreScreen.class);
            intent.putExtra("deck", currDeck);
            tts.stop();
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(CardBack.this, CardFront.class);
            intent.putExtra("deck", currDeck);
            intent.putExtra("mode", mode);
            intent.putExtra("index", cardIndex);
            tts.stop();
            startActivity(intent);
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
