package io.github.smartsportapps.app1;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.smartsportapps.app1.Realm.JourEntainement;
import io.github.smartsportapps.app1.Realm.JourEntrainementDB;
import io.github.smartsportapps.app1.adapter.LevelAdapter;
import io.realm.RealmResults;

public class ActiviteChoixDuNiveau extends FragmentActivity {

    private static final String str_JourEnCours = "jourEnCours";
    private static final String str_NiveauEnCours = "niveauEnCours";
    private final int UN = 1;
    private final int ZERO = 0;
    private final float LIMITE_INF = 2f;
    private final float LIMITE_SUP = 18f;
    private final List<Float> tableauDesNiveaux =
            new ArrayList<>(Arrays.asList(1f, 1.2f, 1.3f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f, 12f, 13f, 14f, 15f, 16f, 17f));
    private String str_numeroNouveauJourChoisi;
    private int jourEnCours;
    private int int_numeroNouveauJourChoisi;
    private int indexDuTableau;
    private float niveau;
    private boolean premierChargment = true;
    private boolean selected;
    private TextView txtViewTitreNiveau;
    private TextView textViewNumeroDuJour;
    private ListView listeDesChoixDuNiveau;
    private List<int[]> listeDuNiveau;
    private LevelAdapter levelAdapter;
    private Handler handler;
    private MakeAllLevelList runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_select_level);
        super.onCreate(savedInstanceState);

        listeDesChoixDuNiveau = findViewById(R.id.selectLevelList);
        txtViewTitreNiveau = findViewById(R.id.txtViewLevel);
        TextView txtviewArrowLeft = findViewById(R.id.btnArrowLeft);
        TextView txtviewArrowRight = findViewById(R.id.btnArrowRight);
        Button btn_validate = findViewById(R.id.btnValidateLevel);

        listeDuNiveau = new ArrayList<>();

        if (premierChargment) {
            Intent data = getIntent();
            niveau = data.getFloatExtra(str_NiveauEnCours, UN);
            jourEnCours = data.getIntExtra(str_JourEnCours, UN);
        }

        handler = new Handler();
        runnable = new MakeAllLevelList();

        indexDuTableau = tableauDesNiveaux.indexOf(niveau);

        handler.post(runnable);

        txtviewArrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexDuTableau > ZERO) {
                    indexDuTableau--;
                    affichageDuNiveauSuivant(indexDuTableau);
                }
            }
        });

        txtviewArrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexDuTableau < LIMITE_SUP) {
                    indexDuTableau++;
                    affichageDuNiveauSuivant(indexDuTableau);
                }
            }
        });

        btn_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected) {
                    Intent activiteAvantEntrainement = new Intent(ActiviteChoixDuNiveau.this, ActiviteAvantEntrainement.class);
                    activiteAvantEntrainement.putExtra(str_JourEnCours, int_numeroNouveauJourChoisi);
                    activiteAvantEntrainement.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(activiteAvantEntrainement);
                    finish();
                }
            }
        });
    }

    private void affichageDuNiveauSuivant(int indexDuTableau) {
        String str_niveau;
        Resources res = getResources();
        float niveauSuivant = tableauDesNiveaux.get(indexDuTableau);
        creerLaListeDesJoursDuNiveau(listeDuNiveau, niveauSuivant);
        levelAdapter.updateLevel(listeDuNiveau);
        if (niveauSuivant >= LIMITE_INF) {
            str_niveau = String.format(res.getString(R.string.displayLevelFormat), niveauSuivant);
        } else {
            str_niveau = String.format(res.getString(R.string.displayLevelFormatComa), niveauSuivant);
        }
        txtViewTitreNiveau.setText(str_niveau);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void creerLaListeDesJoursDuNiveau(List<int[]> listeDuNiveau, Float niveau_) {
        if (listeDuNiveau != null) {
            listeDuNiveau.clear();
        }
        JourEntrainementDB joursDuNiveau = new JourEntrainementDB(ActiviteChoixDuNiveau.this);
        RealmResults<JourEntainement> t = joursDuNiveau.getAllTrainingInLevel(niveau_);
        int size = t.size();
        for (int i = ZERO; i < size; i++) {
            listeDuNiveau.add(joursDuNiveau.trouverLesSeriesPourLeChoixDuNiveau(t.get(i)));
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(ActiviteChoixDuNiveau.this, getResources().getString(R.string.no_level_selected), Toast.LENGTH_SHORT).show();

        Intent activiteAvantEntrainement = new Intent(ActiviteChoixDuNiveau.this, ActiviteAvantEntrainement.class);
        activiteAvantEntrainement.putExtra(str_JourEnCours, jourEnCours);
        activiteAvantEntrainement.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(activiteAvantEntrainement);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (runnable != null) {
            handler.removeCallbacksAndMessages(runnable);
            runnable = null;
        }
        finish();
    }

    private class MakeAllLevelList implements Runnable {
        private boolean isCanceled = false;

        @Override
        public void run() {
            if (isCanceled) {
                runnable = null;
            } else {
                premierChargment = false;
                String titreDuNiveau = String.format(getResources().getString(R.string.TitleLevel), niveau);
                txtViewTitreNiveau.setText(titreDuNiveau);
                creerLaListeDesJoursDuNiveau(listeDuNiveau, niveau);

                levelAdapter = new LevelAdapter(ActiviteChoixDuNiveau.this, listeDuNiveau);
                listeDesChoixDuNiveau.setAdapter(levelAdapter);

                listeDesChoixDuNiveau.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        view.setSelected(true);
                        textViewNumeroDuJour = view.findViewById(R.id.numeroDuJour);
                        str_numeroNouveauJourChoisi = textViewNumeroDuJour.getText().toString();
                        int_numeroNouveauJourChoisi = Integer.parseInt(str_numeroNouveauJourChoisi);
                        selected = true;
                    }
                });
            }
            killRunnable();
        }

        private void killRunnable() {
            isCanceled = true;
        }
    }
}
