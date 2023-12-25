package io.github.smartsportapps.app1.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.github.smartsportapps.app1.Alarm.db.PendingIntentsDataSource;

public class ResetAlarmService extends Service {
    private int mId;
    private int mHour;
    private int mMinutes;
    private int mSeconds;
    private int mYear;
    private int mMonth;
    private int mDay;
    private PendingIntentsDataSource mDatasource;
    private List<AlarmScheduleDetails> mAllAlarmList;
    private String nextOccurrenceDate;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatasource.close();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDatasource = new PendingIntentsDataSource(this);
        mDatasource.open();

        mAllAlarmList = mDatasource.getAllPendingIntents();
        if (mAllAlarmList.size() > 0) {
            for (int i = 0; i < mAllAlarmList.size(); i++) {
                mId = (int) mAllAlarmList.get(i).getId();
                mDay = mAllAlarmList.get(i).getDay();
                mHour = mAllAlarmList.get(i).getHour();
                mMinutes = mAllAlarmList.get(i).getMinutes();
                mSeconds = mAllAlarmList.get(i).getSeconds();
                mYear = mAllAlarmList.get(i).getYear();
                mMonth = mAllAlarmList.get(i).getMonth();
                nextOccurrenceDate = mAllAlarmList.get(i).getmNextOccurrence();
                resetAlarmSchedule();
            }
            stopSelf();
        } else {
            stopSelf();
        }
    }

    public void resetAlarmSchedule() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date mNextIsExecute = sdf.parse(nextOccurrenceDate);
            Calendar mNextCal = Calendar.getInstance();
            mNextCal.setTime(mNextIsExecute);
            mDay = mNextCal.get(Calendar.DATE);
            mMonth = mNextCal.get(Calendar.MONTH);
            mYear = mNextCal.get(Calendar.YEAR);
            mHour = mNextCal.get(Calendar.HOUR);
            mMinutes = mNextCal.get(Calendar.MINUTE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        scheduleAlarm(mId, mHour, mMinutes, mDay, mMonth + 1, mYear);
    }

    public void scheduleAlarm(int id, int hour, int minutes, int day, int month, int year) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minutes);
        c.set(Calendar.SECOND, 0);
        c.set(year, month, day);

        Intent i = new Intent(getApplicationContext(), MyReceiver.class);
        Bundle b = new Bundle();
        b.putInt("hour", mHour);
        b.putInt("min", mMinutes);
        b.putInt("id", id);
        i.putExtras(b);

        AlarmManager am = (AlarmManager) getApplicationContext()
                .getSystemService(getApplicationContext().ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),
                id, i, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pi);
        addToDatabase(id, "1");
    }

    private void addToDatabase(int id, String isEnable) {
        boolean isDelete = mDatasource.deletePendingIntent(id);
        String nextOccurrenceDate = mDay + "/" + mMonth + "/" + mYear + " "
                + mHour + ":" + mMinutes;
        AlarmScheduleDetails isInserRow = mDatasource.createPendingIntents(id,
                mHour, mMinutes, mSeconds, mYear, mMonth, mDay, isEnable,
                nextOccurrenceDate);
        try {
            if (!isInserRow.getMessage().equals("")) {
                // Log.d("service", "Reminder set Successfully!");
            } else {
                // Log.d("service", "Reminder Not set Successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Log.d("service", "Reminder Not set Successfully!");
        }
    }

    public String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
