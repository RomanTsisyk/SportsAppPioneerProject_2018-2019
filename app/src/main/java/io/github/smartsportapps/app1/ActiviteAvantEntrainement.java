package io.github.smartsportapps.app1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.github.smartsportapps.app1.Realm.JourEntainement;
import io.github.smartsportapps.app1.Realm.JourEntrainementDB;

public class ActiviteAvantEntrainement extends Activity {

    private final int REQUESTCODEWORKOUT = 1;
    private final int ZERO = 0;
    private final int UN = 1;
    private final int QUATRE = 4;
    private final int TROIS = 3;
    private final int DEUX = 2;
    private final int CINQ = 5;
    private final int SIX = 6;
    private final int DERNIER_JOUR_D_ENTRAINEMENT = 115;
    private final String str_NombreDeTractions = "nombreDeTractions";
    private final String str_JourEnCours = "jourEnCours";

    private int jourEnCours;
    private int jourDansLeNiveau;
    private int sommeDesTractionDuJour;
    private float niveauEnCours;

    private creationTableauDesSeriesDuJour tableauDesSeriesDuJour;
    private Handler handler;

    private TextView vueDeLaSerie1,
            vueDeLaSerie2,
            vueDeLaSerie3,
            vueDeLaSerie4,
            vueDeLaSerie5;
    private TextView txtViewNiveauEnCours,
            txtViewEntrainement,
            txtView_TractionsNegatives,
            txtView_Nbrtotal;
    private Button btnEntrainement,
            btnChoixDuNiveau;
    private ProgressBar progressBar;
    private ImageView imgView_level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_workout);

        btnChoixDuNiveau = findViewById(R.id.btn_Level);
        btnEntrainement = findViewById(R.id.btn_entrainement);
        vueDeLaSerie1 = findViewById(R.id.viewSerie1);
        vueDeLaSerie2 = findViewById(R.id.viewSerie2);
        vueDeLaSerie3 = findViewById(R.id.viewSerie3);
        vueDeLaSerie4 = findViewById(R.id.viewSerie4);
        vueDeLaSerie5 = findViewById(R.id.viewSerie5);
        txtViewNiveauEnCours = findViewById(R.id.txtView_level);
        txtViewEntrainement = findViewById(R.id.txtView_Entrainement);
        txtView_Nbrtotal = findViewById(R.id.txt_NbrTotal);
        imgView_level = findViewById(R.id.img_view_levelProgression);
        progressBar = findViewById(R.id.progressBar_level);
        txtView_TractionsNegatives = findViewById(R.id.txtView_info);

        Intent intent = getIntent();
        jourEnCours = intent.getExtras().getInt(str_JourEnCours);

        tableauDesSeriesDuJour = new creationTableauDesSeriesDuJour();
        handler = new Handler();

        progressBar.setVisibility(View.GONE);
        handler.post(tableauDesSeriesDuJour);

        btnEntrainement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activiteEntrainement = new Intent(ActiviteAvantEntrainement.this,
                        ActiviteEntrainement.class);
                activiteEntrainement.putExtra(str_JourEnCours, jourEnCours);
                activiteEntrainement.putExtra(str_NombreDeTractions, sommeDesTractionDuJour);
                activiteEntrainement.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(activiteEntrainement, REQUESTCODEWORKOUT);
            }
        });

        btnChoixDuNiveau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String str_NiveauEnCours = "niveauEnCours";

                Intent activiteChoixDuNiveau = new Intent(ActiviteAvantEntrainement.this, ActiviteChoixDuNiveau.class);
                activiteChoixDuNiveau.putExtra(str_JourEnCours, jourEnCours);
                activiteChoixDuNiveau.putExtra(str_NiveauEnCours, niveauEnCours);
                activiteChoixDuNiveau.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(activiteChoixDuNiveau);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private boolean verificationSeriesNegatives(List<String> tableau) {
        boolean blnNegatives = false;
        for (int i = UN; i < tableau.size() && !blnNegatives; i++) {
            if (tableau.get(i).contains("n")) {
                blnNegatives = true;
                break;
            }
        }
        return blnNegatives;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final SharedPreferences.Editor editeurDePreferences;
        SharedPreferences preferencesPartagees;
        final String str_nomDesPreferences = "preferences";
        final String str_TotalDeTractions = "totalDeTractions";
        final Intent activiteAccueil = new Intent(ActiviteAvantEntrainement.this, ActiviteAccueil.class);

        if (requestCode == REQUESTCODEWORKOUT && resultCode == RESULT_OK && data != null) {

            txtViewEntrainement.setText(R.string.str_entrainement_termine);
            txtView_TractionsNegatives.setText(R.string.str_bravo);
            imgView_level.setVisibility(View.GONE);
            progressBar.setProgress(ZERO);
            progressBar.setProgress(jourDansLeNiveau);
            progressBar.setVisibility(View.VISIBLE);
            btnChoixDuNiveau.setText(R.string.same_day);
            btnEntrainement.setText(R.string.next_day);

            activiteAccueil.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            preferencesPartagees = getSharedPreferences(str_nomDesPreferences, ZERO);
            editeurDePreferences = preferencesPartagees.edit();

            int totalDeTractions = preferencesPartagees.getInt(str_TotalDeTractions, ZERO);
            int maximumDeTractionsEffectuees = preferencesPartagees.getInt(str_NombreDeTractions, ZERO);
            int maxPullUp = data.getIntExtra(str_NombreDeTractions, ZERO);
            if (maximumDeTractionsEffectuees < maxPullUp) {
                editeurDePreferences.putInt(str_NombreDeTractions, maxPullUp);
            }

            int total = sommeDesTractionDuJour + totalDeTractions;
            editeurDePreferences.putInt(str_TotalDeTractions, total);
            btnChoixDuNiveau.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(activiteAccueil);
                    editeurDePreferences.apply();
                    finish();
                }
            });

            btnEntrainement.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (jourEnCours == DERNIER_JOUR_D_ENTRAINEMENT) {
                        Toast.makeText(ActiviteAvantEntrainement.this, getResources().getString(R.string.lastDay), Toast.LENGTH_LONG).show();
                    }
                    jourEnCours++;
                    editeurDePreferences.putInt(str_JourEnCours, jourEnCours);
                    startActivity(activiteAccueil);
                    editeurDePreferences.apply();
                    finish();
                }
            });
        } else {
            Toast.makeText(this, getResources().getString(R.string.lost_progression), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent accueil = new Intent(this, ActiviteAccueil.class);
        accueil.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(accueil);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (tableauDesSeriesDuJour != null) {
            handler.removeCallbacks(tableauDesSeriesDuJour);
            tableauDesSeriesDuJour = null;
            handler = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private class creationTableauDesSeriesDuJour implements Runnable {
        boolean isCanceled = false;

        @Override
        public void run() {
            boolean blnNegatives;
            List<String> tabNbr = new ArrayList<>();
            Bitmap imageArrierePlan = null;

            if (isCanceled) {
                handler.removeCallbacks(tableauDesSeriesDuJour);
                tableauDesSeriesDuJour = null;
            } else {
                Resources res = getResources();
                JourEntrainementDB t = new JourEntrainementDB(ActiviteAvantEntrainement.this);
                JourEntainement jourEntainement = t.trouveJourAvecIdentifiant(jourEnCours);

                niveauEnCours = jourEntainement.getNiveau();
                jourDansLeNiveau = jourEntainement.getJourDansLeNiveau();

                tabNbr = t.creationTableauDesSeriesPourAffichage(jourEntainement);
                blnNegatives = verificationSeriesNegatives(tabNbr);

                sommeDesTractionDuJour = t.sommeDesTractionsDuJour(jourEntainement);

                switch (jourDansLeNiveau) {
                    case UN:
                        imageArrierePlan = BitmapFactory.decodeResource(getResources(), R.drawable.level_progress_1);
                        break;
                    case DEUX:
                        imageArrierePlan = BitmapFactory.decodeResource(getResources(), R.drawable.level_progress_2);
                        break;
                    case TROIS:
                        imageArrierePlan = BitmapFactory.decodeResource(getResources(), R.drawable.level_progress_3);
                        break;
                    case QUATRE:
                        imageArrierePlan = BitmapFactory.decodeResource(getResources(), R.drawable.level_progress_4);
                        break;
                    case CINQ:
                        imageArrierePlan = BitmapFactory.decodeResource(getResources(), R.drawable.level_progress_5);
                        break;
                    case SIX:
                        imageArrierePlan = BitmapFactory.decodeResource(getResources(), R.drawable.level_progress_6);
                        break;
                }

                imgView_level.setImageBitmap(imageArrierePlan);

                vueDeLaSerie1.setText(tabNbr.get(ZERO));
                vueDeLaSerie2.setText(tabNbr.get(UN));
                vueDeLaSerie3.setText(tabNbr.get(DEUX));
                vueDeLaSerie4.setText(tabNbr.get(TROIS));
                vueDeLaSerie5.setText(tabNbr.get(QUATRE));

                String texte = String.format(res.getString(R.string.displayLevelFormatComa), niveauEnCours);
                txtViewNiveauEnCours.setText(texte);

                if (blnNegatives) {
                    txtView_TractionsNegatives.setText(res.getString(R.string.negativeTrue));
                } else {
                    txtView_TractionsNegatives.setText(res.getString(R.string.negativeFalse));
                }
                txtView_Nbrtotal.setText(String.valueOf(sommeDesTractionDuJour));

                this.killRunnable();
            }
        }

        public void killRunnable() {
            isCanceled = true;
        }
    }
}
