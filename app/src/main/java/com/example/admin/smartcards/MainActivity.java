package com.example.admin.smartcards;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import android.content.Context;
import com.parse.Parse;
import java.util.List;

import com.parse.Parse;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseObject;

public class MainActivity extends Activity {

    EditText username = null;
    EditText password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Parse.initialize(this, "oV6qA91rZR1xA35KeC3qu3N2tLqHCUGFEsEvjxY7", "N2Irz0ifsTeSFV0YrU0ayVpOMK29so6a8aW8fb2l");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase db = openOrCreateDatabase("smartCards", Context.MODE_PRIVATE, null);

        //db.execSQL("DROP TABLE cards");
        //db.execSQL("DROP TABLE myDecks");

        db.execSQL("CREATE TABLE IF NOT EXISTS myDecks (_id INTEGER PRIMARY KEY, name VARCHAR(255), course VARCHAR(255))");

        db.execSQL("CREATE TABLE IF NOT EXISTS cards (_id INTEGER PRIMARY KEY, deckID INTEGER, frontStr VARCHAR(255), backStr VARCHAR(255), FOREIGN KEY (deckID) REFERENCES myDecks(_id))");

        Cursor cursor = db.query("myDecks", null, null, null, null, null, null);

        cursor.moveToFirst();

        if(cursor.getCount() == 0) {

            ContentValues values = new ContentValues();
            values.put("name", "Test Deck");
            values.put("course", "CAT 3");
            db.insert("myDecks", null, values);

            values.clear();

            values.put("name", "Data Structs");
            values.put("course", "CSE 100");
            db.insert("myDecks", null, values);

            values.clear();

            values.put("deckID", 1);
            values.put("frontStr", "Test");
            values.put("backStr", "Test string");
            db.insert("cards", null, values);

            values.clear();

            values.put("deckID", 1);
            values.put("frontStr", "Test2");
            values.put("backStr", "Test string 2");
            db.insert("cards", null, values);

            values.clear();

            values.put("deckID", 2);
            values.put("frontStr", "Term 1");
            values.put("backStr", "Definition 1");
            db.insert("cards", null, values);

            values.clear();

            values.put("deckID", 2);
            values.put("frontStr", "Term 2");
            values.put("backStr", "Definition 2");
            db.insert("cards", null, values);

            values.clear();

            values.put("deckID", 2);
            values.put("frontStr", "Term 3");
            values.put("backStr", "Definition 3");
            db.insert("cards", null, values);

            values.clear();
        }

        db.close();

        EditText password = (EditText) findViewById(R.id.passwordlogin);
        password.setTypeface(Typeface.DEFAULT);
        password.setTransformationMethod(new PasswordTransformationMethod());
    }


    public void login(View view) {
        /*
        username = (EditText)findViewById(R.id.emaillogin);
        password = (EditText)findViewById(R.id.passwordlogin);
        if (username.getText().toString().equals("admin") &&
                password.getText().toString().equals("admin")) {
//            Toast.makeText(getApplicationContext(), "Redirecting...",
//                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, DeckList.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(), "...",
                    Toast.LENGTH_SHORT).show();
        }
        */

        EditText username   = (EditText)findViewById(R.id.emaillogin);
        String u = username.getText().toString();


        ParseObject gameList = new ParseObject("Usernames");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Usernames");
        query.whereEqualTo("usernames", u);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> arg0, ParseException arg1) {
                EditText password   = (EditText)findViewById(R.id.passwordlogin);
                String p = password.getText().toString();
                ParseObject user_pass = arg0.get(0) ;
                Context c = MainActivity.this;
                String password_user = user_pass.getString("passwords");

                if(p.equals(password_user))
                {
                    Intent intent = new Intent(MainActivity.this, DeckList.class);
                    startActivity(intent);
                }
                else
                    Toast.makeText(c, "Improper Login" , Toast.LENGTH_LONG).show();
            }
        });
    }

    public void createAccount(View view) {

            Intent intent = new Intent(MainActivity.this, CreateAccount.class);
            startActivity(intent);

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
