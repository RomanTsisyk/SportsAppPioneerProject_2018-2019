package io.github.smartsportapps.app1;

import static android.text.TextUtils.split;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.github.smartsportapps.app1.Realm.JourEntainement;
import io.github.smartsportapps.app1.Realm.JourEntrainementDB;
import io.github.smartsportapps.app1.TextView.FontFitTextView;

public class ActiviteEntrainement extends Activity {

    private final int REQUESTCODEWORKOUT = 1;
    private final int ZERO = 0;
    private final int UN = 1;
    private final int DEUX = 2;
    private final int TROIS = 3;
    private final int QUATRE = 4;
    private final int CINQ = 5;
    private final int DELAIS_ATTENTE = 2000;
    private final int SDK = android.os.Build.VERSION.SDK_INT;
    private final String str_nomDesPreferences = "preferences";
    private final String str_TotalDeTractions = "totalDeTractions";
    private final String str_NombreDeTractions = "nombreDeTractions";
    private final String str_JourEnCours = "jourEnCours";
    private final String str_compteurEntrainement = "compteurEntrainement";
    private final String str_NiveauEnCours = "niveauEnCours";
    private final String str_ad = "compteur_ad";
    private int compteurEntrainement = 1;
    private int compteur_ad;
    private int sommeDesTractionsDuJour;
    private int jourEnCours;
    private int int_jourDuNiveau;
    private boolean doubleBackExit;
    private boolean isCanceled;
    private List<String> listeDesSeriesString;
    private float flt_niveau;
    private IntentExtraRunnable runnable;
    private Handler handler;
    private Button btnComplet;
    private TextView viewSerie1,
            viewSerie2,
            viewSerie3,
            viewSerie4,
            viewSerie5,
            txtNiveau,
            viewNegative,
            txtView_Jour;
    private FontFitTextView viewSerieEnCours;
    private LinearLayout linearLayout;
    @SuppressLint("NewApi")
    private Thread getSizeofRelativeLayout = new Thread(new Runnable() {
        @Override
        public void run() {
            linearLayout = (LinearLayout) findViewById(R.id.relativeLayout2);
            linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                        linearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        linearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }

                    viewSerieEnCours.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

                    int totalWidth = linearLayout.getWidth();
                    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                    float value = totalWidth / TROIS;

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewSerieEnCours.getLayoutParams();
                    params.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, value, displayMetrics);
                    viewSerieEnCours.setLayoutParams(params);
                }
            });
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        btnComplet = (Button) findViewById(R.id.btn_valider);
        viewSerie1 = (TextView) findViewById(R.id.view_nbr1);
        viewSerie2 = (TextView) findViewById(R.id.view_nbr2);
        viewSerie3 = (TextView) findViewById(R.id.view_nbr3);
        viewSerie4 = (TextView) findViewById(R.id.view_nbr4);
        viewSerie5 = (TextView) findViewById(R.id.view_nbr5);
        viewNegative = (TextView) findViewById(R.id.view_negative);
        viewSerieEnCours = (FontFitTextView) findViewById(R.id.txtView_nombre);
        txtView_Jour = (TextView) findViewById(R.id.txtView_Jour);
        txtNiveau = (TextView) findViewById(R.id.txtView_welcome);

        handler = new Handler();
        runnable = new IntentExtraRunnable();
        handler.post(runnable);

        btnComplet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent activiteCompteARebours = new Intent(ActiviteEntrainement.this, ActiviteCompteARebours.class);
                activiteCompteARebours.putExtra(str_compteurEntrainement, compteurEntrainement);
                activiteCompteARebours.putExtra(str_ad, compteur_ad);
                activiteCompteARebours.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(activiteCompteARebours, REQUESTCODEWORKOUT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODEWORKOUT && resultCode == RESULT_OK && data != null) {
            compteurEntrainement = data.getIntExtra(str_compteurEntrainement, UN);

            switch (compteurEntrainement) {
                case DEUX:
                    changerLaCouleurDuFond(viewSerie1, viewSerie2);
                    affichageNegativeOuTractions(listeDesSeriesString.get(UN));
                    break;
                case TROIS:
                    changerLaCouleurDuFond(viewSerie2, viewSerie3);
                    affichageNegativeOuTractions(listeDesSeriesString.get(DEUX));
                    break;
                case QUATRE:
                    changerLaCouleurDuFond(viewSerie3, viewSerie4);
                    affichageNegativeOuTractions(listeDesSeriesString.get(TROIS));
                    break;
                case CINQ:
                    changerLaCouleurDuFond(viewSerie4, viewSerie5);
                    affichageNegativeOuTractions(listeDesSeriesString.get(QUATRE));

                    SharedPreferences.Editor editor = getSharedPreferences(str_nomDesPreferences, MODE_PRIVATE).edit();
                    editor.putInt(str_ad, ++compteur_ad);
                    editor.apply();

                    btnComplet.setText(R.string.end_training);
                    btnComplet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent activiteAvantEntrainement = new Intent();

                            JourEntrainementDB t = new JourEntrainementDB(ActiviteEntrainement.this);
                            t.miseAJourDuTotal(jourEnCours, sommeDesTractionsDuJour);

                            activiteAvantEntrainement.putExtra(str_NombreDeTractions,
                                    t.trouverLeRecordDeTractions(t.trouveJourAvecIdentifiant(jourEnCours)));
                            activiteAvantEntrainement.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            setResult(RESULT_OK, activiteAvantEntrainement);
                            finish();
                        }
                    });
                    break;
                default:
                    viewSerieEnCours.setText(viewSerie1.getText().toString());
                    break;
            }
        }
    }

    private void affichageNegativeOuTractions(String toTest) {
        String[] tabStrView;
        Resources res = getResources();
        if (toTest.contains("n")) {
            tabStrView = split(toTest, "\\s");
            viewSerieEnCours.setText(tabStrView[ZERO]);
            viewNegative.setText(res.getString(R.string.negatives));
        } else {
            viewNegative.setText(R.string.pull_up);
            viewSerieEnCours.setText(toTest);
        }
    }

    @SuppressLint("NewApi")
    private void changerLaCouleurDuFond(TextView vuePrecedente, TextView vueSuivante) {
        Resources res = getResources();
        Drawable cercleNoir;
        Drawable cercleRouge;

        if (SDK >= Build.VERSION_CODES.LOLLIPOP) {
            cercleNoir = ContextCompat.getDrawable(this, R.drawable.circle_black_border);
            cercleRouge = ContextCompat.getDrawable(this, R.drawable.circle_red_border);
        } else {
            cercleNoir = res.getDrawable(R.drawable.circle_black_border);
            cercleRouge = res.getDrawable(R.drawable.circle_red_border);
        }

        if (vuePrecedente == null) {
            vueSuivante.setTypeface(null, Typeface.BOLD);
            if (SDK > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                vueSuivante.setBackground(cercleRouge);
            } else {
                vueSuivante.setBackgroundDrawable(cercleRouge);
            }
        } else {
            if (SDK > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                vuePrecedente.setBackground(cercleNoir);
                vuePrecedente.setTypeface(null, Typeface.NORMAL);
                vueSuivante.setBackground(cercleRouge);
                vueSuivante.setTypeface(null, Typeface.BOLD);
            } else {
                vuePrecedente.setBackgroundDrawable(cercleNoir);
                vuePrecedente.setTypeface(null, Typeface.NORMAL);
                vueSuivante.setBackgroundDrawable(cercleRouge);
                vueSuivante.setTypeface(null, Typeface.BOLD);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (compteurEntrainement == 1) {
            retour();
        } else {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getString(R.string.str_BackTitle))
                    .setConfirmText(getString(R.string.validate))
                    .setCancelText(getString(R.string.annuler))
                    .setContentText(getString(R.string.str_backToMain))
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            retour();
                        }
                    }).show();
        }
    }

    private void retour() {
        Intent intent_preWorkout = new Intent(this, ActiviteAvantEntrainement.class);
        intent_preWorkout.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent_preWorkout);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        getSizeofRelativeLayout.interrupt();
        getSizeofRelativeLayout = null;

        if (runnable != null) {
            handler.removeCallbacks(runnable);
            runnable.killRunnable();
            runnable = null;
        }
    }

    private class IntentExtraRunnable implements Runnable {
        private int[] listeDesSeriesNombres;
        private Resources res = getResources();

        @Override
        public void run() {
            if (isCanceled) {
                runnable = null;
            } else {

                SharedPreferences preferencesPartagees
                        = getSharedPreferences(str_nomDesPreferences, ZERO);
                compteur_ad = preferencesPartagees.getInt(str_ad, ZERO);

                Intent intent = getIntent();
                jourEnCours = intent.getExtras().getInt(str_JourEnCours);
                sommeDesTractionsDuJour = intent.getExtras().getInt(str_NombreDeTractions);

                JourEntrainementDB t = new JourEntrainementDB(ActiviteEntrainement.this);
                JourEntainement JourEntrainement = t.trouveJourAvecIdentifiant(jourEnCours);

                listeDesSeriesString = t.creationTableauDesSeriesPourAffichage(JourEntrainement);
                listeDesSeriesNombres = t.creationTableauDesSeries(JourEntrainement);

                flt_niveau = JourEntrainement.getNiveau();
                int_jourDuNiveau = JourEntrainement.getJourDansLeNiveau();

                viewSerie1.setText(String.valueOf(listeDesSeriesNombres[ZERO]));
                changerLaCouleurDuFond(null, viewSerie1);
                affichageNegativeOuTractions(listeDesSeriesString.get(ZERO));

                viewSerie2.setText(String.valueOf(listeDesSeriesNombres[UN]));
                viewSerie3.setText(String.valueOf(listeDesSeriesNombres[DEUX]));
                viewSerie4.setText(String.valueOf(listeDesSeriesNombres[TROIS]));
                viewSerie5.setText(String.valueOf(listeDesSeriesNombres[QUATRE]));

                String jour = String.format(res.getString(R.string.displayDayActiviteChoixDuNiveau), int_jourDuNiveau);
                String niveau = String.format(res.getString(R.string.displayLevelFormat), flt_niveau);
                txtNiveau.setText(niveau);
                txtView_Jour.setText(jour);

                killRunnable();
            }
        }

        public void killRunnable() {
            isCanceled = true;
        }
    }
}
