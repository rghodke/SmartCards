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

import com.example.admin.models.Card;

import java.util.ArrayList;

public class CardDBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_DECK = "deckID";
    public static final String KEY_FRONT = "frontStr";
    public static final String KEY_BACK = "backStr";
    public static final String KEY_PARSE = "parseID";

    private static final String TAG = "com.example.admin.daos.CardDBAdapter";
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private static final String DATABASE_NAME = "smartCards";
    private static final String SQLITE_TABLE = "cards";
    private static final int DATABASE_VERSION = 1;

    private final Context ctx;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY autoincrement," +
                    KEY_DECK + " INTEGER," +
                    KEY_FRONT + " VARCHAR(255)," +
                    KEY_BACK + " VARCHAR(255), " +
                    KEY_PARSE + " VARCHAR(255), " +
                    "FOREIGN KEY (" + KEY_PARSE + ") REFERENCES myDecks(parseID)," +
                    "FOREIGN KEY (" + KEY_DECK + ") REFERENCES myDecks(_id));";

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

    public CardDBAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public CardDBAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(ctx);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    public long createCard(int deck, String front, String back) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DECK, deck);
        initialValues.put(KEY_FRONT, front);
        initialValues.put(KEY_BACK, back);

        return db.insert(SQLITE_TABLE, null, initialValues);
    }

    public long createCard(int deck, String front, String back, String parse) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DECK, deck);
        initialValues.put(KEY_FRONT, front);
        initialValues.put(KEY_BACK, back);
        initialValues.put(KEY_PARSE, parse);

        return db.insert(SQLITE_TABLE, null, initialValues);
    }

    public boolean deleteAllCards() {

        int doneDelete = 0;
        doneDelete = db.delete(SQLITE_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public boolean deleteCardByDeck(int deckID)
    {
        int doneDelete = 0;
        doneDelete = db.delete(SQLITE_TABLE, KEY_DECK + " = ?", new String[] {Integer.toString(deckID)});
        return doneDelete > 0;
    }

    public boolean deleteCard(int cardID)
    {
        int doneDelete = 0;
        doneDelete = db.delete(SQLITE_TABLE, KEY_ROWID + " = ?", new String[] {Integer.toString(cardID)});
        return doneDelete > 0;
    }

    public int getCardCountByDeck(int deckID)
    {
        Cursor mCursor = null;
        mCursor = db.query(true, SQLITE_TABLE, new String[] {KEY_ROWID},
            KEY_DECK + " == " + deckID, null,
            null, null, null, null);
        int count = mCursor.getCount();
        return count;
    }

    public Cursor fetchCardFrontsByDeck(int deck) throws SQLException {
        Cursor mCursor = null;
        mCursor = db.query(true, SQLITE_TABLE, new String[] {KEY_ROWID, KEY_FRONT},
            KEY_DECK + " == " + deck, null,
            null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchCardBacksByDeck(int deck) throws SQLException {
        Cursor mCursor = null;
        mCursor = db.query(true, SQLITE_TABLE, new String[] {KEY_ROWID, KEY_BACK},
                KEY_DECK + " == " + deck, null,
                null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public ArrayList<Card> fetchCards(int deck) throws SQLException {
        ArrayList<Card> cardList = new ArrayList();

        Cursor mCursor;
        mCursor = db.query(true, SQLITE_TABLE, new String[] {KEY_ROWID, KEY_FRONT, KEY_BACK},
                KEY_DECK + " == " + deck, null,
                null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();

            do {
                String frontStr = mCursor.getString(1);
                String backStr = mCursor.getString(2);
                Card newCard = new Card(frontStr, backStr, deck);
                cardList.add(newCard);
            } while (mCursor.moveToNext());
        }

        return cardList;
    }

    public Card fetchCardById(int cardID) throws SQLException {
        Card newCard = null;
        Cursor mCursor;
        mCursor = db.query(true, SQLITE_TABLE, new String[] {KEY_ROWID, KEY_FRONT, KEY_BACK, KEY_DECK},
                    KEY_ROWID + " == " + cardID, null,
                    null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();

            do {
                String frontStr = mCursor.getString(1);
                String backStr = mCursor.getString(2);
                int deck = mCursor.getInt(3);
                newCard = new Card(frontStr, backStr, deck);
            } while (mCursor.moveToNext());
        }

        return newCard;
    }

    public void editCard(String frontStr, String backStr, int cardID) throws SQLException {
        ContentValues newVals = new ContentValues();
        newVals.put(KEY_FRONT, frontStr);
        newVals.put(KEY_BACK, backStr);

        String[] args = new String[] {Integer.toString(cardID)};
        db.update(SQLITE_TABLE, newVals, KEY_ROWID + " = ?", args);
    }

    /*public void insertTestDecks() {

        createDeck("Test");
        createDeck("Gundam");
        createDeck("CSE 100");

    }*/

}


