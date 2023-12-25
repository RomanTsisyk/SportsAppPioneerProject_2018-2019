package io.github.smartsportapps.app1.Alarm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class PendingIntentsSqlite extends SQLiteOpenHelper {

    public static final String TABLE_PENDINGINTENT = "pendingintents";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_HOUR = "hour";
    public static final String COLUMN_MINUTES = "minutes";
    public static final String COLUMN_SECONDS = "seconds";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_MONTH = "month";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_RECEIVER_NAME = "receivername";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_NEXT_OCCURRENCE = "nextoccurrence";

    private static final String DATABASE_NAME = "pendingintents.db";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE = "create table "
            + TABLE_PENDINGINTENT + "(" + COLUMN_ID + " integer primary key , "
            + COLUMN_HOUR + " integer, " + COLUMN_MINUTES + " integer, "
            + COLUMN_SECONDS + " integer, " + COLUMN_YEAR + " integer, "
            + COLUMN_MONTH + " integer, " + COLUMN_DAY + " integer, "
            + COLUMN_RECEIVER_NAME + " String, " + COLUMN_MESSAGE + " String, "
            + COLUMN_NEXT_OCCURRENCE + " String" + ");";

    public PendingIntentsSqlite(Context context, String name, CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PENDINGINTENT);
        onCreate(db);
    }
}
