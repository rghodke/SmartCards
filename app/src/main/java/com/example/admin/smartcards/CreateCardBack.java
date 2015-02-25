package com.example.admin.smartcards;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.daos.CardDBAdapter;
import com.example.admin.daos.DeckDBAdapter;

import java.util.ArrayList;


public class CreateCardBack extends Activity {

    TextView ViewTitle;
    EditText enterCardBack;

    String title;
    String course;
    String cardFront;
    String cardBack;
    int newDeck = 0;

    public  boolean isConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        if (net!=null && net.isAvailable() && net.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            match_text_dialog = new Dialog(CreateCardBack.this);
            match_text_dialog.setContentView(R.layout.dialog_matches_frag);
            match_text_dialog.setTitle("Select Matching Text");
            textlist = (ListView)match_text_dialog.findViewById(R.id.list);
            matches_text = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            ArrayAdapter<String> adapter =    new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, matches_text);
            textlist.setAdapter(adapter);
            textlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Speech.setText(matches_text.get(position));
                    match_text_dialog.hide();
                }
            });
            match_text_dialog.show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private static final int REQUEST_CODE = 1234;
    Button Start;
    EditText Speech;
    Dialog match_text_dialog;
    ListView textlist;
    ArrayList<String> matches_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card_back);

        Start = (Button)findViewById(R.id.button5);

        EditText as = (EditText)findViewById(R.id.enterCardBack);
        as.requestFocus();

        Speech = as;


        //Toast.makeText(getApplicationContext(),
        //                          deck, Toast.LENGTH_LONG)
        //                       .show();

        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Speech == null && isConnected())
                {
                    Toast.makeText(getApplicationContext(), "Please select field", Toast.LENGTH_SHORT).show();
                }
                else if(isConnected()){
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    startActivityForResult(intent, REQUEST_CODE);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Plese Connect to Internet", Toast.LENGTH_LONG).show();
                }}
        });


        ViewTitle = (TextView) findViewById(R.id.viewTitle);
        enterCardBack = (EditText)findViewById(R.id.enterCardBack);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        course = intent.getStringExtra("course");
        cardFront = intent.getStringExtra("cardFront");
        newDeck = intent.getIntExtra("newDeck", 0);

        ViewTitle.setText(course);
    }

    public void goDone (View view)
    {
        cardBack = enterCardBack.getText().toString();

        DeckDBAdapter deckHelper = new DeckDBAdapter(this);
        CardDBAdapter cardHelper = new CardDBAdapter(this);

        deckHelper.open();

        if (newDeck == 0) {
            newDeck = (int) deckHelper.createDeck(title, course);
        }

        cardHelper.open();

        cardHelper.createCard(newDeck, cardFront, cardBack);

        Intent intent = new Intent(CreateCardBack.this, DeckList.class);
        startActivity(intent);
    }

    public void nextCard (View view)
    {
        cardBack = enterCardBack.getText().toString();

        DeckDBAdapter deckHelper = new DeckDBAdapter(this);
        CardDBAdapter cardHelper = new CardDBAdapter(this);

        deckHelper.open();

        if (newDeck == 0) {
            newDeck = (int) deckHelper.createDeck(title, course);
        }

        cardHelper.open();

        cardHelper.createCard(newDeck, cardFront, cardBack);

        Intent intent = new Intent(CreateCardBack.this, CreateCard.class);
        intent.putExtra("title", title);
        intent.putExtra("course", course);
        intent.putExtra("newDeck", newDeck);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_card_back, menu);
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
