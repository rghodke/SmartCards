package com.example.admin.daos;

/**
 * Created by Kyle on 2/25/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserDBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_UNAME = "uName";
    public static final String KEY_PWD = "pwd";
    public static final String KEY_EMAIL = "email";

    private static final String TAG = "com.example.admin.daos.UserDBAdapter";
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    private static final String DATABASE_NAME = "smartCards";
    private static final String SQLITE_TABLE = "users";
    private static final int DATABASE_VERSION = 1;

    private final Context ctx;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " INTEGER PRIMARY KEY autoincrement," +
                    KEY_NAME + " VARCHAR(255)," +
                    KEY_UNAME + " VARCHAR(255)," +
                    KEY_PWD + " VARCHAR(255)," +
                    KEY_EMAIL + " VARCHAR(255));";

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

    public UserDBAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public UserDBAdapter open() throws SQLException {
        dbHelper = new DatabaseHelper(ctx);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    public long createUser(String name, String uname, String pwd, String email) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_UNAME, uname);
        initialValues.put(KEY_PWD, pwd);
        initialValues.put(KEY_EMAIL, email);

        return db.insert(SQLITE_TABLE, null, initialValues);
    }
}


