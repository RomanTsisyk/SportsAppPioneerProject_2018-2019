package io.github.smartsportapps.app1.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import io.github.smartsportapps.app1.Alarm.AlarmeObject;
import io.github.smartsportapps.app1.R;

public class AlarmeAdapter extends BaseAdapter implements TimePickerDialog.OnTimeSetListener {

    private final ArrayList<AlarmeObject> alarmeObjectArrayList;
    private final LayoutInflater mInflater;
    private Calendar calendar;
    private Context context;

    public AlarmeAdapter(Context context, ArrayList<AlarmeObject> alarmeObjectArrayList) {
        this.mInflater = LayoutInflater.from(context);
        this.alarmeObjectArrayList = alarmeObjectArrayList;
    }

    @Override
    public int getCount() {
        return alarmeObjectArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return alarmeObjectArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        String TIME_PATTERN = "HH:mm";
        SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());
        calendar = Calendar.getInstance();
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.alarme_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.chkBox_day = convertView.findViewById(R.id.checkBox_str_Jour);
            viewHolder.txtView_hour = convertView.findViewById(R.id.txtView_int_Heure);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.chkBox_day.setText(alarmeObjectArrayList.get(position).getDayName());
        viewHolder.txtView_hour.setText(alarmeObjectArrayList.get(position).getAlarmeTime());
        context = parent.getContext();

        viewHolder.txtView_hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fmng = ((Activity) context).getFragmentManager();
                TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(
                        AlarmeAdapter.this,
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true,
                        position
                );
                if (!viewHolder.chkBox_day.isChecked()) {
                    viewHolder.chkBox_day.performClick();
                }
                timePickerDialog.show(fmng, "");
            }
        });

        viewHolder.chkBox_day.setChecked(alarmeObjectArrayList.get(position).getAlarmeStatus());

        viewHolder.chkBox_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmeObjectArrayList.get(position).setAlarme(viewHolder.chkBox_day.isChecked());
            }
        });

        return convertView;
    }

    public void updateTime(String time, int id, int hour, int min) {
        ThreadPreconditions.checkOnMainThread();
        this.alarmeObjectArrayList.get(id).setAlarmeTime(time);
        this.alarmeObjectArrayList.get(id).setHourAndMinutes(hour, min);
        notifyDataSetChanged();
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute, int position) {
        String time = formatHHMMValue(hourOfDay) + ":" + formatHHMMValue(minute);
        updateTime(time, position, hourOfDay, minute);
    }

    private String formatHHMMValue(int value) {
        String retour;
        if (value < 10) {
            retour = "0" + value;
        } else {
            retour = String.valueOf(value);
        }
        return retour;
    }

    private class ViewHolder {
        public CheckBox chkBox_day;
        public TextView txtView_hour;
    }
}