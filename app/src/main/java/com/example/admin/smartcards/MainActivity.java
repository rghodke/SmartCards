package com.example.admin.smartcards;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

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

        db.execSQL("CREATE TABLE IF NOT EXISTS myDecks (_id INTEGER PRIMARY KEY, name VARCHAR(255), course VARCHAR(255), location VARCHAR(255), parseID VARCHAR(255), creator VARCHAR(255))");

        db.execSQL("CREATE TABLE IF NOT EXISTS cards (_id INTEGER PRIMARY KEY, deckID INTEGER, parseID VARCHAR(255), frontStr VARCHAR(255), backStr VARCHAR(255), FOREIGN KEY (deckID) REFERENCES myDecks(_id), FOREIGN KEY (parseID) REFERENCES myDecks(parseID))");

        Cursor cursor = db.query("myDecks", null, null, null, null, null, null);

        cursor.moveToFirst();

        if(cursor.getCount() == 0) {

            ContentValues values = new ContentValues();
            values.put("name", "Test Deck");
            values.put("course", "CAT 3");
            values.put("location", "UCSD");
            values.put("creator", "NZZpCqjuPs");
            db.insert("myDecks", null, values);

            values.clear();

            values.put("name", "Data Structs");
            values.put("course", "CSE 100");
            values.put("location", "UCSD");
            values.put("creator", "NZZpCqjuPs");
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
        copyAssets();
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
                Context c = MainActivity.this;


                ParseObject user_pass = arg0.get(0) ;
                String password_user = user_pass.getString("passwords");

                //Toast.makeText(MainActivity.this, user_pass.getString("usernames").isEmpty()+"--", Toast.LENGTH_LONG).show();
                if(!(user_pass.getString("usernames").isEmpty())) {
                    if (p.equals(password_user)) {
                        Intent intent = new Intent(MainActivity.this, DeckList.class);
                        startActivity(intent);
                    } else
                        Toast.makeText(c, "Improper Login", Toast.LENGTH_LONG).show();
                }else{

                    Toast.makeText(c, "Username does not exist.", Toast.LENGTH_LONG).show();
                }
            }

        });
    }


    private void copyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        for(String filename : files) {
            //Toast.makeText(getApplicationContext(), filename,
            //      Toast.LENGTH_SHORT).show();
            //Log.e("tag", filename);
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                //Log.e("tag",getExternalFilesDir("tessdata").toString());
                File outFile = new File(getExternalFilesDir("tessdata"), filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    public void createAccount(View view) {
        copyAssets();
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
