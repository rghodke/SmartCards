package com.example.admin.smartcards;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.admin.daos.CardDBAdapter;
import com.example.admin.daos.DeckDBAdapter;
import com.example.admin.models.Card;


public class EditCard extends Activity {

    String cardID;
    String deckID;
    String cardStr;
    String frontStr;
    String backStr;
    String deckName;
    String deckCourse;
    String mode;

    long id;

    TextView ViewTitle;
    TextView ViewCourse;

    EditText cardValFront;
    EditText cardValBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        ViewTitle = (TextView) findViewById(R.id.editViewTitle);
        ViewCourse = (TextView) findViewById(R.id.editViewCourse);
        cardValFront = (EditText) findViewById(R.id.editCardStrFront);
        cardValBack = (EditText) findViewById(R.id.editCardStrBack);

        Intent intent = getIntent();
        cardID = intent.getStringExtra("cardID");
        deckID = intent.getStringExtra("deckID");
        deckName = intent.getStringExtra("deckName");
        deckCourse = intent.getStringExtra("deckCourse");
        id = Long.parseLong(cardID);

        CardDBAdapter dbHelper = new CardDBAdapter(this);
        dbHelper.open();

        Card currCard = dbHelper.fetchCardById(Integer.parseInt(cardID));

        dbHelper.close();

        frontStr = currCard.getFrontStr();
        backStr = currCard.getBackStr();

        ViewTitle.setText(deckName);
        ViewCourse.setText(deckCourse);

        cardValFront.setText(frontStr, TextView.BufferType.EDITABLE);
        cardValBack.setText(backStr, TextView.BufferType.EDITABLE);
    }

    public void goBack(View view)
    {
        super.finish();
    }

    public void editConfirm(View view)
    {
        new AlertDialog.Builder(this)
            .setTitle("Edit")
            .setMessage("Do you want to edit this value?")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    //Toast.makeText(DownloadConfirm.this, deckParseID + " | " + deckName + " | " + deckCourse + " | " + deckLocation, Toast.LENGTH_SHORT).show();

                    CardDBAdapter cardDbHelper = new CardDBAdapter(EditCard.this);
                    cardDbHelper.open();

                    String newValFront = cardValFront.getText().toString();
                    String newValBack = cardValBack.getText().toString();

                    cardDbHelper.editCard(newValFront, newValBack, (int) id);

                    cardDbHelper.close();

                    new AlertDialog.Builder(EditCard.this)
                            .setTitle("Success")
                            .setMessage("Edit Successful!")
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

    public void deleteCard(View view)
    {
        new AlertDialog.Builder(this)
            .setTitle("Delete")
            .setMessage("Do you want to delete this card?")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    //Toast.makeText(DownloadConfirm.this, deckParseID + " | " + deckName + " | " + deckCourse + " | " + deckLocation, Toast.LENGTH_SHORT).show();

                    CardDBAdapter cardDbHelper = new CardDBAdapter(EditCard.this);
                    cardDbHelper.open();

                    cardDbHelper.deleteCard((int) id);
                    int count = cardDbHelper.getCardCountByDeck(Integer.parseInt(deckID));

                    cardDbHelper.close();

                    if (count == 0)
                    {
                        DeckDBAdapter deckDbHelper = new DeckDBAdapter(EditCard.this);
                        deckDbHelper.open();

                        deckDbHelper.deleteDeck(Integer.parseInt(deckID));

                        deckDbHelper.close();
                    }

                    new AlertDialog.Builder(EditCard.this)
                            .setTitle("Success")
                            .setMessage("Delete Successful!")
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_card, menu);
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
