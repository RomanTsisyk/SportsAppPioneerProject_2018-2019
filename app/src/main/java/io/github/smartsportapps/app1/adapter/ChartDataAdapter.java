package io.github.smartsportapps.app1.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

import java.util.List;

import io.github.smartsportapps.app1.R;

public class ChartDataAdapter extends ArrayAdapter<BarData> {

    private final List<String> titles;

    public ChartDataAdapter(Context context, List<BarData> objects, List<String> titles) {
        super(context, 0, objects);
        this.titles = titles;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BarData data = getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_barchart, null);
            holder.titreGraph = convertView.findViewById(R.id.txtView_Stats_Level);
            holder.chart = convertView.findViewById(R.id.chart);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        YAxisValueFormatter yAxisValueFormatter = new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                return String.format("%.0f", value);
            }
        };

        holder.chart.setDescription("");
        holder.chart.setDrawGridBackground(false);
        holder.chart.setDrawBarShadow(true);
        holder.chart.setDoubleTapToZoomEnabled(false);
        holder.chart.setScaleEnabled(false);
        data.setValueTextColor(Color.WHITE);
        holder.titreGraph.setText(titles.get(position));

        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = holder.chart.getAxisLeft();
        leftAxis.setLabelCount(5, true);
        leftAxis.setValueFormatter(yAxisValueFormatter);
        leftAxis.setSpaceTop(10f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = holder.chart.getAxisRight();
        rightAxis.setEnabled(false);

        holder.chart.setData(data);
        holder.chart.animateY(700);

        return convertView;
    }

    private static class ViewHolder {
        BarChart chart;
        TextView titreGraph;
    }
}