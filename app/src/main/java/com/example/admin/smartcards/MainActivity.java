package com.example.admin.smartcards;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {

    EditText username = null;
    EditText password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase db = openOrCreateDatabase("smartCards", Context.MODE_PRIVATE, null);

//        db.execSQL("DROP TABLE cards");
   //     db.execSQL("DROP TABLE myDecks");

        db.execSQL("CREATE TABLE IF NOT EXISTS myDecks (_id INTEGER PRIMARY KEY, name VARCHAR(255))");

        db.execSQL("CREATE TABLE IF NOT EXISTS cards (_id INTEGER PRIMARY KEY, deckID INTEGER, frontStr VARCHAR(255), backStr VARCHAR(255), FOREIGN KEY (deckID) REFERENCES myDecks(_id))");

        Cursor cursor = db.query("myDecks", null, null, null, null, null, null);

        cursor.moveToFirst();

        if(cursor.getCount() == 0) {

            ContentValues values = new ContentValues();
            values.put("Name", "Test Deck");
            db.insert("myDecks", null, values);

            values.clear();

            values.put("Name", "CSE 100");
            db.insert("myDecks", null, values);

            values.clear();

            values.put("deckID", 1);
            values.put("frontStr", "Test");
            values.put("backStr", "Test string");
            db.insert("cards", null, values);

            values.clear();

            values.put("deckID", 2);
            values.put("frontStr", "Data Structures");
            values.put("backStr", "Insert definition here");
            db.insert("cards", null, values);

            values.clear();
        }

        db.close();
    }


    public void login(View view) {
        username = (EditText)findViewById(R.id.emaillogin);
        password = (EditText)findViewById(R.id.passwordlogin);
        if (username.getText().toString().equals("admin") &&
                password.getText().toString().equals("admin")) {
            Toast.makeText(getApplicationContext(), "Redirecting...",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, DeckList.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(), "...",
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
