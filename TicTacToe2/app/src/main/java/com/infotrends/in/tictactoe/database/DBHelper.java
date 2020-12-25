package com.infotrends.in.tictactoe.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME ="tictactoe.db";
    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_GAMELIST_TABLE="CREATE TABLE " +
                DBContract.PlayGameEntery.TABLE_NAME + " (" + DBContract.PlayGameEntery._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBContract.PlayGameEntery.COLUMN_Player1 + " TEXT NOT NULL, " +
                DBContract.PlayGameEntery.COLUMN_Game_Type + " TEXT NOT NULL, " +
                DBContract.PlayGameEntery.COLUMN_Game_No + " TEXT NOT NULL, " +
                DBContract.PlayGameEntery.COLUMN_Player2 + " TEXT NOT NULL, " +
                DBContract.PlayGameEntery.COLUMN_Player1_SCORE + " INTEGER NOT NULL, " +
                DBContract.PlayGameEntery.COLUMN_Player2_SCORE + " INTEGER NOT NULL, " +
                DBContract.PlayGameEntery.COLUMN_Player1_KEY + " TEXT NOT NULL, " +
                DBContract.PlayGameEntery.COLUMN_Player2_KEY + " TEXT NOT NULL, " +
                DBContract.PlayGameEntery.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        db.execSQL(SQL_CREATE_GAMELIST_TABLE);

        final String SQL_CREATE_GAMELIST_TABLE2="CREATE TABLE " +
                DBContract.LastGameEntry.TABLE_NAME + " (" + DBContract.LastGameEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBContract.LastGameEntry.COLUMN_Player1 + " TEXT NOT NULL, " +
                DBContract.LastGameEntry.COLUMN_Game_Type + " TEXT NOT NULL, " +
                DBContract.PlayGameEntery.COLUMN_Game_No + " TEXT NOT NULL, " +
                DBContract.LastGameEntry.COLUMN_Player2 + " TEXT NOT NULL, " +
                DBContract.LastGameEntry.COLUMN_Player1_SCORE + " INTEGER NOT NULL, " +
                DBContract.LastGameEntry.COLUMN_Player2_SCORE + " INTEGER NOT NULL, " +
                DBContract.LastGameEntry.COLUMN_Player1_SCORE_INITIAL + " INTEGER NOT NULL, " +
                DBContract.LastGameEntry.COLUMN_Player2_SCORE_INITIAL + " INTEGER NOT NULL, " +
                DBContract.LastGameEntry.COLUMN_Player1_KEY + " TEXT NOT NULL, " +
                DBContract.LastGameEntry.COLUMN_Player2_KEY + " TEXT NOT NULL, " +
                DBContract.LastGameEntry.COLUMN_BoardValus  + " TEXT NOT NULL, " +
                DBContract.LastGameEntry.COLUMN_ROUND_COUNT  + " TEXT NOT NULL, " +
                DBContract.LastGameEntry.COLUMN_PLAYER1_TURN + " TEXT NOT NULL, " +
                DBContract.LastGameEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        db.execSQL(SQL_CREATE_GAMELIST_TABLE2);

        final String SQL_CREATE_GAMELIST_TABLE3="CREATE TABLE " +
                DBContract.NoGenerator.TABLE_NAME + " (" + DBContract.LastGameEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DBContract.NoGenerator.COLUMN_NO_GENERATED + " INTEGER NOT NULL, " +
                DBContract.NoGenerator.COLUMN_NO_TYPE + " TEXT NOT NULL, " +
                DBContract.NoGenerator.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        db.execSQL(SQL_CREATE_GAMELIST_TABLE3);

        ContentValues cv = new ContentValues();
        cv.put(DBContract.NoGenerator.COLUMN_NO_GENERATED, 1);
        cv.put(DBContract.NoGenerator.COLUMN_NO_TYPE, "Game_No");
        db.insert(DBContract.NoGenerator.TABLE_NAME, null, cv);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.PlayGameEntery.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.LastGameEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.NoGenerator.TABLE_NAME);
        onCreate(db);
    }
}
