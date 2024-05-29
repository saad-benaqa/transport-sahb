package com.tp.transport;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mon_application.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_USERS =
            "CREATE TABLE " + Contract.UserEntry.TABLE_NAME + " (" +
                    Contract.UserEntry._ID + " INTEGER PRIMARY KEY," +
                    Contract.UserEntry.COLUMN_NAME_PRENOM + " TEXT," +
                    Contract.UserEntry.COLUMN_NAME_NOM + " TEXT," +
                    Contract.UserEntry.COLUMN_NAME_NUM_TELEPHONE + " TEXT," +
                    Contract.UserEntry.COLUMN_NAME_EMAIL + " TEXT," +
                    Contract.UserEntry.COLUMN_NAME_PASSWORD + " TEXT)";

    private static final String SQL_DELETE_TABLE_USERS =
            "DROP TABLE IF EXISTS " + Contract.UserEntry.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TABLE_USERS);
        onCreate(db);
    }

    public boolean insertUser(String prenom, String nom, String numTelephone, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.UserEntry.COLUMN_NAME_PRENOM, prenom);
        contentValues.put(Contract.UserEntry.COLUMN_NAME_NOM, nom);
        contentValues.put(Contract.UserEntry.COLUMN_NAME_NUM_TELEPHONE, numTelephone);
        contentValues.put(Contract.UserEntry.COLUMN_NAME_EMAIL, email);
        contentValues.put(Contract.UserEntry.COLUMN_NAME_PASSWORD, password);

        long result = db.insert(Contract.UserEntry.TABLE_NAME, null, contentValues);
        return result != -1; // Returns true if insert was successful, false otherwise
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {Contract.UserEntry._ID};
        String selection = Contract.UserEntry.COLUMN_NAME_EMAIL + " = ? AND " + Contract.UserEntry.COLUMN_NAME_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(Contract.UserEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }
}
