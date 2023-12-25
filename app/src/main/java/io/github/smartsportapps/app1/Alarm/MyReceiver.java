package io.github.smartsportapps.app1.Alarm;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.github.smartsportapps.app1.ActiviteAccueil;
import io.github.smartsportapps.app1.Alarm.db.PendingIntentsDataSource;
import io.github.smartsportapps.app1.R;

@SuppressLint("SimpleDateFormat")
public class MyReceiver extends BroadcastReceiver {

    private static final int UN = 1;
    private static final int ZERO = 0;

    private int mToday, mReminderDay;
    private PendingIntentsDataSource mDatasource;
    private int id;
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mDatasource = new PendingIntentsDataSource(context);
        mDatasource.open();
        id = intent.getIntExtra("id", ZERO);
        mContext = context;

        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        Calendar calendar = Calendar.getInstance();
        mToday = calendar.get(Calendar.DAY_OF_WEEK);

        mReminderDay = id;

        updateNextOccurrenceOfEvent(id);

        Calendar c = Calendar.getInstance();
        int mMinitus = intent.getIntExtra("min", ZERO);

        if (c.get(Calendar.MINUTE) == mMinitus) {
            if (mToday == mReminderDay) {
                createNotification();
            }
        }
    }

    private final void createNotification() {
        final NotificationManager mNotification = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        final Intent launchNotificationIntent = new Intent(mContext, ActiviteAccueil.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(mContext,
                ZERO, launchNotificationIntent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder builder = new Notification.Builder(mContext)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(mContext.getResources().getString(R.string.notification_title))
                .setContentText(mContext.getResources().getString(R.string.notification_content))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mNotification.notify(UN, builder.build());
        } else {
            mNotification.notify(UN, builder.getNotification());
        }
    }

    public void updateNextOccurrenceOfEvent(int alarmId) {
        String nextOccurrenceDate = "";
        String dateString = mDatasource.getNextOccurrenceOfEvent(alarmId);
        DateFormat formatter;
        Date date = null;
        formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        if (date != null) {
            c.setTime(date);
            nextOccurrenceDate = c.get(Calendar.DATE) + "/"
                    + ((c.get(Calendar.MONTH)) + UN) + "/"
                    + c.get(Calendar.YEAR) + " " + c.get(Calendar.HOUR) + ":"
                    + c.get(Calendar.MINUTE);
        }
        int affectedRow = mDatasource.updateNextOccurrence(alarmId, nextOccurrenceDate);
        mDatasource.close();
    }
}
