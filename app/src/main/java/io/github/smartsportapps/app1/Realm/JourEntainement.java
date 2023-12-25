package io.github.smartsportapps.app1.Realm;

import io.realm.RealmObject;

public class JourEntainement extends RealmObject {

    private int indexDuJour;
    private float niveau;
    private int jourDansLeNiveau;
    private String str_s1;
    private String str_s2;
    private String str_s3;
    private String str_s4;
    private String str_s5;
    private int nombreDeTractions;
    private int totalDeTractions;

    public JourEntainement() {
    }

    public JourEntainement(int int_jour, float flt_level, int int_day_level,
                           String str_s1, String str_s2, String str_s3, String str_s4, String str_s5,
                           int int_max_pullUp, int int_total_pullUp) {
        this.indexDuJour = int_jour;
        this.niveau = flt_level;
        this.jourDansLeNiveau = int_day_level;
        this.str_s1 = str_s1;
        this.str_s2 = str_s2;
        this.str_s3 = str_s3;
        this.str_s4 = str_s4;
        this.str_s5 = str_s5;
        this.nombreDeTractions = int_max_pullUp;
        this.totalDeTractions = int_total_pullUp;
    }

    public int getIndexDuJour() {
        return indexDuJour;
    }

    public void setIndexDuJour(int indexDuJour) {
        this.indexDuJour = indexDuJour;
    }

    public float getNiveau() {
        return niveau;
    }

    public void setNiveau(float niveau) {
        this.niveau = niveau;
    }

    public int getJourDansLeNiveau() {
        return jourDansLeNiveau;
    }

    public void setJourDansLeNiveau(int jourDansLeNiveau) {
        this.jourDansLeNiveau = jourDansLeNiveau;
    }

    public String getStr_s1() {
        return str_s1;
    }

    public void setStr_s1(String str_s1) {
        this.str_s1 = str_s1;
    }

    public String getStr_s2() {
        return str_s2;
    }

    public void setStr_s2(String str_s2) {
        this.str_s2 = str_s2;
    }

    public String getStr_s3() {
        return str_s3;
    }

    public void setStr_s3(String str_s3) {
        this.str_s3 = str_s3;
    }

    public String getStr_s4() {
        return str_s4;
    }

    public void setStr_s4(String str_s4) {
        this.str_s4 = str_s4;
    }

    public String getStr_s5() {
        return str_s5;
    }

    public void setStr_s5(String str_s5) {
        this.str_s5 = str_s5;
    }

    public int getNombreDeTractions() {
        return nombreDeTractions;
    }

    public void setNombreDeTractions(int int_max_pullUp) {
        this.nombreDeTractions = int_max_pullUp;
    }

    public int getTotalDeTractions() {
        return totalDeTractions;
    }

    public void setTotalDeTractions(int totalDeTractions) {
        this.totalDeTractions = totalDeTractions;
    }
}
