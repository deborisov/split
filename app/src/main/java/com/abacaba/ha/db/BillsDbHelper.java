package com.abacaba.ha.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.abacaba.ha.db.BillsContract;

public class BillsDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "bills.db";

    public BillsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_COMPANIES);
        db.execSQL(SQL_CREATE_PERSONS);
        db.execSQL(SQL_CREATE_TRANSACTIONS);
        db.execSQL(SQL_CREATE_TRANSACTIONS_LIST);
        db.execSQL(SQL_CREATE_PAYMENTS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_COMPANIES);
        db.execSQL(SQL_DELETE_PERSONS);
        db.execSQL(SQL_DELETE_TRANSACTIONS);
        db.execSQL(SQL_DELETE_TRANSACTIONS_LIST);
        db.execSQL(SQL_DELETE_PAYMENTS);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    private static final String SQL_CREATE_COMPANIES =
            "CREATE TABLE IF NOT EXISTS " + BillsContract.Company.TABLE_NAME + " (" +
                    BillsContract.Company._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    BillsContract.Company.COLUMN_NAME_NAME + " TEXT)";

    private static final String SQL_CREATE_PERSONS =
            "CREATE TABLE IF NOT EXISTS " + BillsContract.Person.TABLE_NAME + " (" +
                    BillsContract.Person._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    BillsContract.Person.COLUMN_NAME_NAME + " TEXT," +
                    BillsContract.Person.COLUMN_NAME_COMPANYID + " INTEGER,"+
                    BillsContract.Person.COLUMN_NAME_DEBT + " REAL)";

    private static final String SQL_CREATE_TRANSACTIONS =
            "CREATE TABLE IF NOT EXISTS " + BillsContract.Transaction.TABLE_NAME + " (" +
                    BillsContract.Transaction._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    BillsContract.Transaction.COLUMN_NAME_TO_PERSON + " INTEGER,"+
                    BillsContract.Transaction.COLUMN_NAME_FROM_PERSON + " INTEGER,"+
                    BillsContract.Transaction.COLUMN_NAME_TRANSACTION_ID + " INTEGER,"+
                    BillsContract.Transaction.COLUMN_NAME_SUM + " REAL)";

    private static final String SQL_CREATE_TRANSACTIONS_LIST =
            "CREATE TABLE IF NOT EXISTS " + BillsContract.TransactionList.TABLE_NAME + " (" +
                    BillsContract.TransactionList._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    BillsContract.TransactionList.COLUMN_NAME_NAME + " TEXT,"+
                    BillsContract.TransactionList.COLUMN_NAME_COMPANY_ID + " INTEGER)";

    private static final String SQL_CREATE_PAYMENTS =
            "CREATE TABLE IF NOT EXISTS " + BillsContract.Payment.TABLE_NAME + " (" +
                    BillsContract.Payment._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    BillsContract.Payment.COLUMN_NAME_PERSON_ID + " INTEGER,"+
                    BillsContract.Payment.COLUMN_NAME_SUM + " REAL,"+
                    BillsContract.Payment.COLUMN_NAME_TYPE + " INTEGER,"+
                    BillsContract.Payment.COLUMN_NAME_TRANSACTION_ID + " INTEGER)";

    public static final String SQL_DELETE_COMPANIES =
            "DROP TABLE IF EXISTS " + BillsContract.Company.TABLE_NAME;

    public static final String SQL_DELETE_PERSONS =
            "DROP TABLE IF EXISTS " + BillsContract.Person.TABLE_NAME;

    public static final String SQL_DELETE_TRANSACTIONS =
            "DROP TABLE IF EXISTS " + BillsContract.Transaction.TABLE_NAME;

    public static final String SQL_DELETE_TRANSACTIONS_LIST =
            "DROP TABLE IF EXISTS " + BillsContract.TransactionList.TABLE_NAME;

    public static final String SQL_DELETE_PAYMENTS =
            "DROP TABLE IF EXISTS " + BillsContract.Payment.TABLE_NAME;

}
