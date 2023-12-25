package io.github.smartsportapps.app1.Alarm;

public class AlarmeObject {

    private int int_id;
    private int int_id_day;
    private String str_day;
    private String str_Heure;
    private int int_hour;
    private int int_min;
    private boolean bln_alarmeSet;

    public AlarmeObject(int id, int int_id_day, String day, int hour, int min, String heure, boolean alarmeOn) {
        this.int_id = id;
        this.int_id_day = int_id_day;
        this.str_day = day;
        this.int_hour = hour;
        this.int_min = min;
        this.str_Heure = heure;
        this.bln_alarmeSet = alarmeOn;
    }

    public void setAlarme(boolean isAlarmeOn) {
        this.bln_alarmeSet = isAlarmeOn;
    }

    public void setHourAndMinutes(int hour, int min) {
        this.int_hour = hour;
        this.int_min = min;
    }

    public int getInt_id() {
        return this.int_id;
    }

    public String getDayName() {
        return this.str_day;
    }

    public boolean getAlarmeStatus() {
        return this.bln_alarmeSet;
    }

    public String getAlarmeTime() {
        return this.str_Heure;
    }

    public void setAlarmeTime(String time) {
        this.str_Heure = time;
    }

    public int getAlarmeHour() {
        return this.int_hour;
    }

    public int getAlarmeMin() {
        return this.int_min;
    }

    public int getInt_id_day() {
        return this.int_id_day;
    }
}
