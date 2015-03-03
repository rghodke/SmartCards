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


public class ScoreScreen extends Activity {

    TextView score;
    TextView time;
    TextView right;
    TextView wrong;
    Deck currDeck;
    ArrayList<Card> cardArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_screen);

        score = (TextView)findViewById(R.id.Score);
        right = (TextView)findViewById(R.id.Right);
        wrong = (TextView)findViewById(R.id.Wrong);

        Intent intent = getIntent();
        currDeck = (Deck)intent.getSerializableExtra("deck");
        cardArray = currDeck.getCardArray();

        float total = cardArray.size();
        float correct = 0;

        for (int i = 0; i < total; i++)
        {
            Card tempCard = cardArray.get(i);

            if(tempCard.getIsRight())
            {
                correct++;
            }
        }

        right.setText(Integer.toString((int)correct));
        wrong.setText(Integer.toString((int)(total - correct)));
        score.setText(String.format("%.2f", (correct/total) * 100.0f)+"%");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_score_screen, menu);
        return true;
    }

    public void TryDoneButton (View view)
    {
        Intent intent = new Intent(ScoreScreen.this, DeckList.class);
        startActivity(intent);
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
