package io.github.smartsportapps.app1.Alarm;

import io.github.smartsportapps.app1.R;

public class Const {

    public static final int MONDAY_REMINDER_ID = 2;
    public static final int TUESDAY_REMINDER_ID = 3;
    public static final int WEDNESDAY_REMINDER_ID = 4;
    public static final int THURSDAY_REMINDER_ID = 5;
    public static final int FRIDAY_REMINDER_ID = 6;
    public static final int SATURDAY_REMINDER_ID = 7;
    public static final int SUNDAY_REMINDER_ID = 1;

    public static int getDay(int id) {
        switch (id) {
            case 2:
                return R.string.monday;
            case 3:
                return R.string.thuesday;
            case 4:
                return R.string.wednesday;
            case 5:
                return R.string.thursday;
            case 6:
                return R.string.friday;
            case 7:
                return R.string.saturday;
            case 1:
                return R.string.sunday;
        }
        return 0;
    }
}
