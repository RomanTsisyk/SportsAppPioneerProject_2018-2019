package io.github.smartsportapps.app1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ActiviteTest extends Activity {

    private final int ZERO = 0;
    private final int UN = 1;
    private final int DEUX = 2;
    private final int SIX = 6;
    private final int INT_MAX_VALUE = 50;
    private final String str_nomDesPreferences = "preferences";
    private final String str_TotalDeTractions = "totalDeTractions";
    private final String str_NombreDeTractions = "nombreDeTractions";
    private final String str_PremierChargement = "premierChargement";
    private final String str_JourEnCours = "jourEnCours";
    private int int_nombreTractions;
    private float flt_niveau;
    private SharedPreferences.Editor editor;
    private SharedPreferences Preferences;
    private EditText editTxt_ResultatDuTestDeTractions;
    private Button btn_valider;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstload_layout);

        editTxt_ResultatDuTestDeTractions = (EditText) findViewById(R.id.editText);
        btn_valider = (Button) findViewById(R.id.btn_valider);

        Preferences = getSharedPreferences(str_nomDesPreferences, ZERO);
        editor = Preferences.edit();

        editTxt_ResultatDuTestDeTractions.requestFocus();

        editTxt_ResultatDuTestDeTractions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTxt_ResultatDuTestDeTractions.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEUX)});
                editTxt_ResultatDuTestDeTractions.setImeOptions(EditorInfo.IME_ACTION_DONE);
            }
        });

        btn_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resultatDuTest = editTxt_ResultatDuTestDeTractions.getText().toString();
                if (resultatDuTest.length() <= DEUX && resultatDuTest.length() > ZERO) {
                    editor.putBoolean(str_PremierChargement, false);
                    int_nombreTractions = Integer.parseInt(resultatDuTest);

                    if (int_nombreTractions <= INT_MAX_VALUE) {
                        editor.putInt(str_JourEnCours, trouverLeNiveauParRapportAuNombreDeTractions(int_nombreTractions));
                        editor.putInt(str_NombreDeTractions, int_nombreTractions);

                        String niveauChoisi = String.format(getString(R.string.niveauSelectionne), flt_niveau);
                        SweetAlertDialog alertNiveau = new SweetAlertDialog(ActiviteTest.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText(getString(R.string.congratulations))
                                .setContentText(niveauChoisi)
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                        sweetAlertDialog.dismissWithAnimation();

                                        Intent accueil = new Intent(ActiviteTest.this, ActiviteAccueil.class);
                                        accueil.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        startActivity(accueil);
                                        finish();
                                    }
                                });
                        alertNiveau.show();

                        editor.putBoolean(str_PremierChargement, false);
                        editor.apply();

                    } else {
                        Toast.makeText(ActiviteTest.this, getResources().getString(R.string.error_edtText_length), Toast.LENGTH_SHORT).show();
                    }
                } else if (resultatDuTest.length() == ZERO || resultatDuTest.equals("")) {
                    Toast.makeText(ActiviteTest.this, getResources().getString(R.string.error_edtText_empty), Toast.LENGTH_SHORT).show();
                } else if (resultatDuTest.length() > DEUX) {
                    Toast.makeText(ActiviteTest.this, getResources().getString(R.string.error_edtText_length), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private int trouverLeNiveauParRapportAuNombreDeTractions(int tractionsDuTest) {
        List<Integer> lst_test = new ArrayList<>(Arrays.asList(0, 1, 2, 5, 7, 9, 11, 15, 19, 22, 25, 27, 30, 33, 35, 40, 44, 47, 50));
        List<Float> lst_intLevel = new ArrayList<Float>(Arrays.asList(1f, 1.2f, 1.3f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f, 11f, 12f, 13f, 14f, 15f, 16f, 17f));

        int day = UN;
        int size = lst_test.size();
        for (int i = ZERO; i < size; i++) {
            if (lst_test.get(i) >= tractionsDuTest && i != ZERO) {
                int index = i - UN;
                day = day + (SIX * index);
                flt_niveau = lst_intLevel.get(index);
                break;
            }
        }
        return day;
    }
}
