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

import com.example.admin.daos.DeckDBAdapter;


public class EditDeck extends Activity {

    String deckID;
    String deckName;
    String deckCourse;

    EditText deckValName;
    EditText deckValCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_deck);

        deckValName = (EditText) findViewById(R.id.editDeckName);
        deckValCourse = (EditText) findViewById(R.id.editDeckCourse);

        Intent intent = getIntent();
        deckID = intent.getStringExtra("deckID");
        deckName = intent.getStringExtra("deckName");
        deckCourse = intent.getStringExtra("deckCourse");

        deckValName.setText(deckName, TextView.BufferType.EDITABLE);
        deckValCourse.setText(deckCourse, TextView.BufferType.EDITABLE);
    }

    public void goBack(View view)
    {
        super.finish();
    }

    public void editConfirm(View view)
    {
        new AlertDialog.Builder(this)
            .setTitle("Edit")
            .setMessage("Do you want to edit this deck?")
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    //Toast.makeText(DownloadConfirm.this, deckParseID + " | " + deckName + " | " + deckCourse + " | " + deckLocation, Toast.LENGTH_SHORT).show();

                    DeckDBAdapter deckDbHelper = new DeckDBAdapter(EditDeck.this);
                    deckDbHelper.open();

                    String newValName = deckValName.getText().toString();
                    String newValDeck = deckValCourse.getText().toString();

                    deckDbHelper.editDeck(newValName, newValDeck, Integer.parseInt(deckID));

                    deckDbHelper.close();

                    new AlertDialog.Builder(EditDeck.this)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_deck, menu);
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
