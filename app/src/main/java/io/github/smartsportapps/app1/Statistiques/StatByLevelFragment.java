package io.github.smartsportapps.app1.Statistiques;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;

import io.github.smartsportapps.app1.R;
import io.github.smartsportapps.app1.Realm.JourEntainement;
import io.github.smartsportapps.app1.Realm.JourEntrainementDB;
import io.github.smartsportapps.app1.adapter.ChartDataAdapter;
import io.realm.RealmResults;

public class StatByLevelFragment extends Fragment {

    final private float tab[] = {1, 1.2f, 1.3f, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17};
    private int compteur;
    private ChartDataAdapter cda;
    private ArrayList<BarData> list;
    private ArrayList<String> titreGraph;
    private WeakReference<RunDataBase> runDataBaseWeakReference;
    private JourEntrainementDB t;
    private TextView txtViewTitreNiveauGraphique;
    private ArrayList<String> xValues = new ArrayList<String>(Arrays.asList("1", "2", "3", "4", "5", "6"));

    public static StatByLevelFragment newInstance(String text) {
        StatByLevelFragment f = new StatByLevelFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_stats, container, false);

        TextView titre = (TextView) v.findViewById(R.id.txtViewTitreStats);

        ListView lv = (ListView) v.findViewById(R.id.listView2);
        list = new ArrayList<>();
        titreGraph = new ArrayList<>();

        setRetainInstance(true);
        startRunDataBase();

        cda = new ChartDataAdapter(container.getContext(), list, titreGraph);
        lv.setAdapter(cda);

        return v;
    }

    private void startRunDataBase() {
        RunDataBase runDataBase = new RunDataBase(this, this.getContext());
        this.runDataBaseWeakReference = new WeakReference<RunDataBase>(runDataBase);
        runDataBase.execute();
    }

    private boolean isRunDataBasePendingOrRunning() {
        return this.runDataBaseWeakReference != null
                && this.runDataBaseWeakReference.get() != null
                && !this.runDataBaseWeakReference.get().getStatus().equals(AsyncTask.Status.FINISHED);
    }

    class RunDataBase extends AsyncTask<Context, Void, Integer[]> {
        Context context;
        private WeakReference<StatByLevelFragment> fragmentWeakReference;

        public RunDataBase(StatByLevelFragment statsActivity, Context context) {
            this.fragmentWeakReference = new WeakReference<>(statsActivity);
            this.context = context;
        }

        @Override
        protected Integer[] doInBackground(Context... params) {
            try {
                t = new JourEntrainementDB(this.context);
                RealmResults r = t.getAllDB();
                int nbr = r.size();
                Integer[] tabInt = new Integer[nbr];

                for (int i = 0; i < nbr; i++) {
                    JourEntainement trainingDay = t.trouveJourAvecIdentifiant(i + 1);
                    tabInt[i] = trainingDay.getTotalDeTractions();
                }
                return tabInt;
            } catch (Exception e) {
                Integer[] tabInteger = new Integer[0];
                return tabInteger;
            }
        }

        @Override
        protected void onPostExecute(Integer[] result) {
            super.onPostExecute(result);
            if (this.fragmentWeakReference.get() != null) {
                if (result.length > 0) {
                    int k = 0;
                    for (int i = 0; i < tab.length; i++) {
                        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
                        int indexOftabAllPullUp = 0;
                        for (int j = 1; j < 7; j++) {
                            entries.add(new BarEntry(result[k], indexOftabAllPullUp));
                            indexOftabAllPullUp++;
                            k++;
                        }

                        BarDataSet d = new BarDataSet(entries, context.getString(R.string.statsTotalDeTractions));
                        d.setBarSpacePercent(20f);
                        d.setBarShadowColor(Color.rgb(203, 203, 203));

                        if (compteur == 1) {
                            d.setColor(getResources().getColor(R.color.action_bar));
                            compteur = 0;
                        } else {
                            d.setColor(getResources().getColor(R.color.bg));
                            compteur = 1;
                        }

                        ArrayList<BarDataSet> sets = new ArrayList<BarDataSet>();
                        sets.add(d);

                        String titreDuGraphique = String.format(getResources().getString(R.string.displayLevelFormatComa), tab[i]);
                        titreGraph.add(titreDuGraphique);

                        BarData cd = new BarData(xValues, sets);
                        list.add(cd);
                        cda.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(this.context, R.string.erreurDansAffichageStatistiques, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
