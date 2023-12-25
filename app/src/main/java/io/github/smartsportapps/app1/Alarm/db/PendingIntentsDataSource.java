package io.github.smartsportapps.app1.Alarm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import io.github.smartsportapps.app1.Alarm.AlarmScheduleDetails;

public class PendingIntentsDataSource {

    private final PendingIntentsSqlite dbHelper;
    private final String[] allColumns = {PendingIntentsSqlite.COLUMN_ID,
            PendingIntentsSqlite.COLUMN_HOUR,
            PendingIntentsSqlite.COLUMN_MINUTES,
            PendingIntentsSqlite.COLUMN_SECONDS,
            PendingIntentsSqlite.COLUMN_YEAR,
            PendingIntentsSqlite.COLUMN_MONTH, PendingIntentsSqlite.COLUMN_DAY,
            PendingIntentsSqlite.COLUMN_RECEIVER_NAME,
            PendingIntentsSqlite.COLUMN_MESSAGE,
            PendingIntentsSqlite.COLUMN_NEXT_OCCURRENCE};
    private SQLiteDatabase database;

    public PendingIntentsDataSource(Context context) {
        dbHelper = new PendingIntentsSqlite(context, "pendingintents.db", null,
                2);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public AlarmScheduleDetails createPendingIntents(int id, int hour, int minutes,
                                                     int seconds, int year, int month, int day,
                                                     String message, String nextOccurrence) {

        ContentValues values = new ContentValues();
        values.put(PendingIntentsSqlite.COLUMN_ID, id);
        values.put(PendingIntentsSqlite.COLUMN_HOUR, hour);
        values.put(PendingIntentsSqlite.COLUMN_MINUTES, minutes);
        values.put(PendingIntentsSqlite.COLUMN_SECONDS, seconds);
        values.put(PendingIntentsSqlite.COLUMN_YEAR, year);
        values.put(PendingIntentsSqlite.COLUMN_MONTH, month);
        values.put(PendingIntentsSqlite.COLUMN_DAY, day);
        values.put(PendingIntentsSqlite.COLUMN_MESSAGE, message);
        values.put(PendingIntentsSqlite.COLUMN_NEXT_OCCURRENCE, nextOccurrence);

        database.insert(PendingIntentsSqlite.TABLE_PENDINGINTENT, null, values);

        Cursor cursor = database.query(
                PendingIntentsSqlite.TABLE_PENDINGINTENT, allColumns,
                PendingIntentsSqlite.COLUMN_ID + " = " + id, null, null, null,
                null);

        cursor.moveToFirst();
        AlarmScheduleDetails newPendingIntent = cursorToPendingIntent(cursor);
        cursor.close();
        return newPendingIntent;

    }

    public List<AlarmScheduleDetails> getAllPendingIntents() {
        List<AlarmScheduleDetails> pendingIntents = new ArrayList<>();
        Cursor cursor = database.query(
                PendingIntentsSqlite.TABLE_PENDINGINTENT, allColumns, null,
                null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            AlarmScheduleDetails pendingIntent = cursorToPendingIntent(cursor);
            pendingIntents.add(pendingIntent);
            cursor.moveToNext();
        }
        cursor.close();
        return pendingIntents;
    }

    private AlarmScheduleDetails cursorToPendingIntent(Cursor cursor) {
        AlarmScheduleDetails pendingIntent = new AlarmScheduleDetails();
        pendingIntent.setId(cursor.getLong(0));
        pendingIntent.setHour(cursor.getInt(1));
        pendingIntent.setMinutes(cursor.getInt(2));
        pendingIntent.setSeconds(cursor.getInt(3));
        pendingIntent.setYear(cursor.getInt(4));
        pendingIntent.setMonth(cursor.getInt(5));
        pendingIntent.setDay(cursor.getInt(6));
        pendingIntent.setReceiverName(cursor.getString(7));
        pendingIntent.setMessage(cursor.getString(8));
        pendingIntent.setmNextOccurrence(cursor.getString(9));
        return pendingIntent;
    }

    public boolean deletePendingIntent(int id) {
        return database.delete(PendingIntentsSqlite.TABLE_PENDINGINTENT,
                PendingIntentsSqlite.COLUMN_ID + "=" + id, null) > 0;
    }

    public String getNextOccurrenceOfEvent(int id) {
        String mNextOccurrence = "";
        Cursor cursor = database.query(
                PendingIntentsSqlite.TABLE_PENDINGINTENT,
                new String[] { PendingIntentsSqlite.COLUMN_NEXT_OCCURRENCE },
                PendingIntentsSqlite.COLUMN_ID + "=" + id, null, null, null,
                null);

        cursor.moveToFirst();
        if (cursor != null) {
            AlarmScheduleDetails pendingIntent = cursorToPendingIntentForUpdate(cursor);
            mNextOccurrence = pendingIntent.getmNextOccurrence();
        }
        cursor.close();
        return mNextOccurrence;
    }

    public int updateNextOccurrence(int id, String mNextOccurrence) {
        ContentValues values = new ContentValues();
        values.put(PendingIntentsSqlite.COLUMN_NEXT_OCCURRENCE, mNextOccurrence);
        return database.update(PendingIntentsSqlite.TABLE_PENDINGINTENT,
                values, PendingIntentsSqlite.COLUMN_ID + "=" + id, null);
    }

    private AlarmScheduleDetails cursorToPendingIntentForUpdate(Cursor cursor) {
        AlarmScheduleDetails pendingIntent = new AlarmScheduleDetails();
        pendingIntent.setmNextOccurrence(cursor.getString(0));
        return pendingIntent;
    }

}
