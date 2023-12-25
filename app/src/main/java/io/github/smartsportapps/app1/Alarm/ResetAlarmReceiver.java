package io.github.smartsportapps.app1.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ResetAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, ResetAlarmService.class));
    }
}
