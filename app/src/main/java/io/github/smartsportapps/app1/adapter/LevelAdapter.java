package io.github.smartsportapps.app1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import io.github.smartsportapps.app1.R;

public class LevelAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private final Context context;
    private List<int[]> list;

    public LevelAdapter(Context context, List<int[]> listeNiveau) {
        this.mInflater = LayoutInflater.from(context);
        this.list = listeNiveau;
        this.context = context;
    }

    public void updateLevel(List<int[]> niveau) {
        ThreadPreconditions.checkOnMainThread();
        this.list = niveau;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView numeroDuJour;
        TextView series;
        TextView indexDuJour;
        View view;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.selectlevel_list_item, parent, false);
            numeroDuJour = view.findViewById(R.id.title);
            series = view.findViewById(R.id.series);
            indexDuJour = view.findViewById(R.id.numeroDuJour);
            view.setTag(new ViewHolder(numeroDuJour, series, indexDuJour));
        } else {
            view = convertView;
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            numeroDuJour = viewHolder.dayNbr;
            series = viewHolder.series;
            indexDuJour = viewHolder.date;
        }

        int tabOfSeries[] = list.get(position);
        int SIX = 6;
        String str_jour = String.format(context.getString(R.string.displayDayActiviteChoixDuNiveau), tabOfSeries[SIX]);
        numeroDuJour.setText(str_jour);
        int ZERO = 0;
        int UN = 1;
        int DEUX = 2;
        int TROIS = 3;
        int QUATRE = 4;
        series.setText(tabOfSeries[ZERO] + " / " + tabOfSeries[UN] + " / " + tabOfSeries[DEUX] + " / " + tabOfSeries[TROIS] + " / " + tabOfSeries[QUATRE]);
        int CINQ = 5;
        indexDuJour.setText(String.valueOf(tabOfSeries[CINQ]));

        return view;
    }

    private static class ViewHolder {
        public final TextView dayNbr;
        public final TextView series;
        public final TextView date;

        public ViewHolder(TextView dayNbr, TextView series, TextView date) {
            this.dayNbr = dayNbr;
            this.date = date;
            this.series = series;
        }
    }
}
