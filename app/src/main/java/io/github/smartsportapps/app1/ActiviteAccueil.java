package io.github.smartsportapps.app1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.github.smartsportapps.app1.CaredView.CardFragmentPagerAdapter;
import io.github.smartsportapps.app1.CaredView.CardItem;
import io.github.smartsportapps.app1.CaredView.CardPagerAdapter;
import io.github.smartsportapps.app1.CaredView.ShadowTransformer;
import io.github.smartsportapps.app1.Realm.JourEntrainementDB;

public class ActiviteAccueil extends AppCompatActivity implements
        View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    // Declaration of variables
    private final int ZERO = 0;
    private final int PREMIER_JOUR_D_ENTRAINEMENT = 1;
    private final int DERNIER_JOUR_D_ENTRAINEMENT = 115;
    private final int DELAIS_ATTENTE = 2000;
    private final String str_nomDesPreferences = "preferences";
    private final String str_TotalDeTractions = "totalDeTractions";
    private final String str_NombreDeTractions = "nombreDeTractions";
    private final String str_PremierChargement = "premierChargement";
    private final String str_JourEnCours = "jourEnCours";
    private final String str_ad = "compteur_ad";
    public int int_totalDeTractions;
    private Button mButton;
    private ViewPager mViewPager;
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private CardFragmentPagerAdapter mFragmentCardAdapter;
    private ShadowTransformer mFragmentCardShadowTransformer;
    private boolean mShowingFragments = false;
    // Declaration of elements
    private Button btnEntrainement;
    private TextView txtViewRecordTractions;
    private SharedPreferences preferencesPartagees;
    private int jourEnCours = 1;
    private int pressed = 0;
    private int ad = 0;
    private int int_nombreDeTractions;

    private boolean premierChargement;
    private boolean isCanceled = false;
    private boolean doubleBackExit;
    private String recordDeTractions;

    // Object declaration
    private prefsRunnable prefsRunnable;
    private Handler handler;

    public static float dpToPixels(int dp, Context context) {
        return dp * (context.getResources().getDisplayMetrics().density);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        btnEntrainement = findViewById(R.id.btn_entrainement);
        txtViewRecordTractions = findViewById(R.id.txtView_topRectangle);

        if (preferencesPartagees == null)
            preferencesPartagees = getSharedPreferences(str_nomDesPreferences, ZERO);

        prefsRunnable = new prefsRunnable();
        handler = new Handler();
        handler.post(prefsRunnable);

        btnEntrainement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (premierChargement) {
                    Intent activiteDeTest = new Intent(getApplicationContext(), ActiviteTest.class);
                    startActivity(activiteDeTest);
                } else if (jourEnCours == DERNIER_JOUR_D_ENTRAINEMENT) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.lastDay), Toast.LENGTH_LONG).show();
                } else {
                    Intent activiteAvantEntrainement = new Intent(getApplicationContext(), ActiviteAvantEntrainement.class);
                    activiteAvantEntrainement.putExtra(str_JourEnCours, jourEnCours);
                    startActivity(activiteAvantEntrainement);
                    finish();
                }
            }
        });

        mViewPager = findViewById(R.id.viewPager);
        mButton = findViewById(R.id.cardTypeBtn);
        ((CheckBox) findViewById(R.id.checkBox)).setOnCheckedChangeListener(this);
        mButton.setOnClickListener(this);

        mCardAdapter = new CardPagerAdapter();
        mCardAdapter.addCardItem(new CardItem(R.string.stats, R.drawable.ic_bar_chart));
        mCardAdapter.addCardItem(new CardItem(R.string.alarme, R.drawable.ic_alarm));
        mCardAdapter.addCardItem(new CardItem(R.string.deleteData, R.drawable.ic_trash));

        mFragmentCardAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(),
                dpToPixels(2, this));

        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
        mFragmentCardShadowTransformer = new ShadowTransformer(mViewPager, mFragmentCardAdapter);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.str_LeaveApp))
                .setContentText(getString(R.string.str_confimLeaveApp))
                .setConfirmText(getString(R.string.validate))
                .setCancelText(getString(R.string.annuler))
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        startActivity(intent);
                        finish();
                        Process.killProcess(Process.myPid());
                    }
                }).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = preferencesPartagees.edit();
        editor.putInt(str_NombreDeTractions, int_nombreDeTractions);
        editor.apply();
        handler.removeCallbacksAndMessages(prefsRunnable);
        prefsRunnable = null;
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void resetData(View view) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.deleteData))
                .setConfirmText(getString(R.string.validate))
                .setCancelText(getString(R.string.annuler))
                .setContentText(getString(R.string.reset_confirm))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        SharedPreferences.Editor editor = getSharedPreferences(str_nomDesPreferences, MODE_PRIVATE).edit();
                        JourEntrainementDB t_db = new JourEntrainementDB(getApplicationContext());
                        t_db.resetDataBase();
                        if (preferencesPartagees.contains(str_JourEnCours)) {
                            editor.putInt(str_JourEnCours, PREMIER_JOUR_D_ENTRAINEMENT);
                        }
                        if (preferencesPartagees.contains(str_TotalDeTractions)) {
                            editor.putInt(str_TotalDeTractions, ZERO);
                        }
                        if (preferencesPartagees.contains(str_NombreDeTractions)) {
                            editor.putInt(str_NombreDeTractions, ZERO);
                        }
                        if (preferencesPartagees.contains(str_PremierChargement)) {
                            editor.putBoolean(str_PremierChargement, true);
                        }

                        editor.apply();

                        int_totalDeTractions = preferencesPartagees.getInt(str_TotalDeTractions, ZERO);
                        int_nombreDeTractions = preferencesPartagees.getInt(str_NombreDeTractions, ZERO);
                        premierChargement = preferencesPartagees.getBoolean(str_PremierChargement, true);

                        recordDeTractions = String.format(getResources().getString(R.string.recordDeTraction), int_nombreDeTractions);
                        txtViewRecordTractions.setText(recordDeTractions);

                        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        sweetAlertDialog.setTitleText(getString(R.string.rest))
                                .setContentText(getString(R.string.succesfullreset))
                                .setConfirmText("OK")
                                .showCancelButton(false)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                    }
                                });
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                }).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        premierChargement = preferencesPartagees.getBoolean(str_PremierChargement, true);
    }

    @Override
    public void onClick(View view) {
        if (!mShowingFragments) {
            mButton.setText("Views");
            mViewPager.setAdapter(mFragmentCardAdapter);
            mViewPager.setPageTransformer(false, mFragmentCardShadowTransformer);
        } else {
            mButton.setText("Fragments");
            mViewPager.setAdapter(mCardAdapter);
            mViewPager.setPageTransformer(false, mCardShadowTransformer);
        }

        mShowingFragments = !mShowingFragments;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        mCardShadowTransformer.enableScaling(b);
        mFragmentCardShadowTransformer.enableScaling(b);
    }

    private class prefsRunnable implements Runnable {

        @Override
        public void run() {
            if (isCanceled) {
                prefsRunnable = null;
            } else {
                if (preferencesPartagees.contains(str_JourEnCours)) {
                    jourEnCours = preferencesPartagees.getInt(str_JourEnCours, PREMIER_JOUR_D_ENTRAINEMENT);
                }
                if (preferencesPartagees.contains(str_TotalDeTractions)) {
                    int_totalDeTractions = preferencesPartagees.getInt(str_TotalDeTractions, ZERO);
                }
                if (preferencesPartagees.contains(str_NombreDeTractions)) {
                    int_nombreDeTractions = preferencesPartagees.getInt(str_NombreDeTractions, ZERO);
                }
                if (preferencesPartagees.contains(str_ad)) {
                    ad = preferencesPartagees.getInt(str_ad, ZERO);
                }

                recordDeTractions = String.format(getResources().getString(R.string.recordDeTraction), int_nombreDeTractions);
                txtViewRecordTractions.setText(recordDeTractions);

                killRunnable();
            }
        }

        public void killRunnable() {
            isCanceled = true;
        }
    }
}
