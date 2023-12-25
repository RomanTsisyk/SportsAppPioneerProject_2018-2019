package io.github.smartsportapps.app1.Statistiques;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.github.smartsportapps.app1.R;
import io.github.smartsportapps.app1.Realm.JourEntainement;
import io.github.smartsportapps.app1.Realm.JourEntrainementDB;
import io.realm.RealmResults;

public class StatByDayFragment extends Fragment {

    private final int ZERO = 0;
    private final int UN = 1;
    private final int CINQ = 5;
    private final int ANIMATE = 700;

    private int compteur;
    private ArrayList<BarData> list;
    private BarData barData;
    private BarChart chart;
    private JourEntrainementDB t;
    private ArrayList<String> xValues;
    private ArrayList<BarEntry> entries;
    private AsyncRunnable runDataBase;

    public static StatByDayFragment newInstance(String text) {
        StatByDayFragment f = new StatByDayFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_stats_tab, container, false);

        chart = (BarChart) v.findViewById(R.id.chart_fullpage);

        chart.setDescription("");
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(true);
        chart.setDoubleTapToZoomEnabled(false);
        chart.setScaleEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setLabelCount(CINQ, true);
        leftAxis.setSpaceTop(10f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        runDataBase = new AsyncRunnable(StatByDayFragment.this, v.getContext());
        runDataBase.execute();

        return v;
    }

    class AsyncRunnable extends AsyncTask<Context, Void, ArrayList<BarDataSet>> {
        private WeakReference<StatByDayFragment> mTestDb = null;
        private SweetAlertDialog pDialog;
        private ArrayList<BarDataSet> sets = null;
        private Context context;

        public AsyncRunnable(StatByDayFragment BarChartActivity, Context context) {
            link(BarChartActivity);
            this.context = context;
        }

        public void link(StatByDayFragment BarChartActivity) {
            mTestDb = new WeakReference<>(BarChartActivity);
        }

        @Override
        protected void onPreExecute() {
            pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.setTitleText(context.getResources().getString(R.string.recuperationStatistiques));
            pDialog.setCancelable(false);
            pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.bg));
            pDialog.show();
        }

        @Override
        protected ArrayList<BarDataSet> doInBackground(Context... context) {
            try {
                barData = new BarData();
                entries = new ArrayList<>();
                xValues = new ArrayList<>();
                Resources res = this.context.getResources();
                t = new JourEntrainementDB(this.context);
                RealmResults r = t.getAllDB();
                int nbr = r.size();
                int pullUpDone;
                compteur = ZERO;

                for (int i = ZERO; i < nbr; i++) {
                    JourEntainement trainingDay = t.trouveJourAvecIdentifiant(i + UN);
                    pullUpDone = trainingDay.getTotalDeTractions();
                    if (pullUpDone != ZERO) {
                        entries.add(new BarEntry(pullUpDone, compteur));
                        compteur++;
                        xValues.add(compteur + "");
                    }
                }

                BarDataSet dataSet = new BarDataSet(entries, res.getString(R.string.nbr_tractions));
                dataSet.setBarSpacePercent(10f);
                dataSet.setColor(res.getColor(R.color.bg));
                sets = new ArrayList<>();
                sets.add(dataSet);
                barData = new BarData(xValues, sets);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return sets;
        }

        @Override
        protected void onPostExecute(ArrayList<BarDataSet> sets) {
            pDialog.dismiss();
            chart.setData(barData);
            chart.animateXY(ANIMATE, ANIMATE);
            chart.invalidate();
        }
    }
}
