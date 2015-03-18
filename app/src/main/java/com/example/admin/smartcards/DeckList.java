package com.example.admin.smartcards;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.admin.daos.DeckDBAdapter;


public class DeckList extends Activity {
    ListView decklist;
    SimpleCursorAdapter dataAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_list);

        int deckCount = 1;
       // SQLiteDatabase db = openOrCreateDatabase("smartCards", Context.MODE_PRIVATE, null);
        //Cursor cursor = db.query("myDecks", null, null, null, null, null, null);
        //Cursor cursor = db.rawQuery("SELECT deckID, name FROM myDecks", null);

        DeckDBAdapter dbHelper = new DeckDBAdapter(this);

        dbHelper.open();

        Cursor cursor = dbHelper.fetchAllDecks();

        String[] columns = new String[] {
                DeckDBAdapter.KEY_ROWID,
                DeckDBAdapter.KEY_NAME
        };

        int[] to = new int[] {
                R.id.deckID,
                R.id.deckName
        };

        dataAdapter = new SimpleCursorAdapter(this, R.layout.deck_info, cursor, columns, to, 0);

        /*if (cursor.moveToFirst())
        {
            deckCount = cursor.getCount();
            values = new String[deckCount];
            int i = 0;
            do
            {
                values[i] = cursor.getString(1);
                i++;
            } while (cursor.moveToNext());


        }
        else
        {
            values = new String[]{"Android List View",
                    "Adapter implementation",
                    "Simple List View In Android",
                    "Create List View Android",
                    "Android Example",
                    "List View Source Code",
                    "List View Array Adapter",
                    "Android Example List View",
                    "Test"
            };
        }*/


        // Get ListView object from xml
        decklist = (ListView) findViewById(R.id.deckList);
        //decklist.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        // Defined Array values to show in ListView
       /* String[] values = new String[] { "Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"
        };*/

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
           //     R.layout.custom_textview, android.R.id.text1, values);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.custom_textview, values);



        // Assign adapter to ListView
        decklist.setAdapter(dataAdapter);
        dbHelper.close();

        decklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Cursor cursor = (Cursor) parent.getItemAtPosition(position);

                String deckID = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
                String deckName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String deckCourse = cursor.getString(cursor.getColumnIndexOrThrow("course"));
                String deckLocation = cursor.getString(cursor.getColumnIndexOrThrow("location"));
                //long testID = cursor.getColumnIndexOrThrow("_id");

                Intent intent = new Intent(DeckList.this, DeckOpen.class);
                intent.putExtra("deckID", deckID);
                intent.putExtra("deckName", deckName);
                intent.putExtra("deckCourse", deckCourse);
                intent.putExtra("deckLocation", deckLocation);
                startActivity(intent);
                //String textstuff = (TextView) view.findViewById(android.R.id.text1)).getText().toString();

//                Toast.makeText(getApplicationContext(),
  //                      deckID, Toast.LENGTH_LONG)
    //                   .show();

            }
        }
        );

    }


    public void CreateDeck (View view)
    {
        Intent intent = new Intent(DeckList.this, CreateCard.class);
        startActivity(intent);
    }

    public void downloadDecks (View view)
    {
        Intent intent = new Intent(DeckList.this, DownloadDeck.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_deck_list, menu);
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

    @Override
    public void onResume()
    {
        super.onResume();
        DeckDBAdapter dbHelper = new DeckDBAdapter(this);
        dbHelper.open();

        Cursor cursor = dbHelper.fetchAllDecks();

        String[] columns = new String[] {
                DeckDBAdapter.KEY_ROWID,
                DeckDBAdapter.KEY_NAME
        };

        int[] to = new int[] {
                R.id.deckID,
                R.id.deckName
        };

        dataAdapter = new SimpleCursorAdapter(this, R.layout.deck_info, cursor, columns, to, 0);
        dataAdapter.notifyDataSetChanged();
        decklist.setAdapter(dataAdapter);
        dbHelper.close();
    }
}
