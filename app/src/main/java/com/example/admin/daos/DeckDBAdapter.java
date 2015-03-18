package com.example.admin.daos;

/**
 * Created by Kyle on 2/19/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.admin.models.Deck;

public class DeckDBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_COURSE = "course";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_PARSE = "parseID";
    public static final String KEY_CREATOR = "creator";

    private static final String TAG = "com.example.admin.daos.DeckDBAdapter";
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private static final String DATABASE_NAME = "smartCards";
    private static final String SQLITE_TABLE = "myDecks";
    private static final int DATABASE_VERSION = 1;

    private final Context ctx;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY autoincrement," +
                    KEY_NAME + " VARCHAR(255)," +
                    KEY_COURSE + " VARCHAR(255)," +
                    KEY_LOCATION + " VARCHAR(255)," +
                    KEY_PARSE + " VARCHAR(255)," +
                    KEY_COURSE + " VARCHAR(255));";
                    //KEY_COURSE + " VARCHAR(255));";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }
    }

    public DeckDBAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public DeckDBAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(ctx);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    public long createDeck(String name, String course) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_COURSE, course);
        initialValues.put(KEY_LOCATION, "UCSD");

        return db.insert(SQLITE_TABLE, null, initialValues);
    }

    public long createDeck(String name, String course, String location, String parse)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_COURSE, course);
        initialValues.put(KEY_LOCATION, location);
        initialValues.put(KEY_PARSE, parse);

        return db.insert(SQLITE_TABLE, null, initialValues);
    }

    public boolean deleteAllDecks() {

        int doneDelete = 0;
        doneDelete = db.delete(SQLITE_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public boolean deleteDeck(int deckID)
    {
        int doneDelete = 0;
        doneDelete = db.delete(SQLITE_TABLE, KEY_ROWID + " = ?", new String[] {Integer.toString(deckID)});
        return doneDelete > 0;
    }

    public Deck grabDeck(int deckID)
    {
        Deck deck = null;
        Cursor mCursor = null;
        mCursor = db.query(SQLITE_TABLE, new String[] {KEY_NAME, KEY_COURSE},
            KEY_ROWID + " == " + deckID, null,
            null, null, null, null);

        if(mCursor != null)
        {
            mCursor.moveToFirst();

            do {
                String deckName = mCursor.getString(0);
                String deckCourse = mCursor.getString(1);
                deck = new Deck(deckCourse, deckName);
            } while (mCursor.moveToNext());
        }

        return deck;
    }

    public Cursor fetchDecksByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = db.query(SQLITE_TABLE, new String[] {KEY_ROWID, KEY_NAME},
                    null, null, null, null, null);

        }
        else {
            mCursor = db.query(true, SQLITE_TABLE, new String[] {KEY_ROWID, KEY_NAME},
                    KEY_NAME + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchDecksByCourse(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = db.query(SQLITE_TABLE, new String[] {KEY_ROWID, KEY_COURSE},
                    null, null, null, null, null);

        }
        else {
            mCursor = db.query(true, SQLITE_TABLE, new String[] {KEY_ROWID, KEY_COURSE},
                    KEY_NAME + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchAllDecks() {

        Cursor mCursor = db.query(SQLITE_TABLE, null,
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void editDeck(String deckName, String deckCourse, int deckID) throws SQLException {
        ContentValues newVals = new ContentValues();
        newVals.put(KEY_NAME, deckName);
        newVals.put(KEY_COURSE, deckCourse);

        String[] args = new String[] {Integer.toString(deckID)};
        db.update(SQLITE_TABLE, newVals, KEY_ROWID + " = ?", args);
    }

    public void insertTestDecks() {

        createDeck("Test", "CAT3");
        createDeck("Gundam", "SHA3");
        createDeck("Data Structs", "CSE100");

    }

}


