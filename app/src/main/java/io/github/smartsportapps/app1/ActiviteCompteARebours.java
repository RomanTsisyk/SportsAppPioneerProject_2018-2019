package io.github.smartsportapps.app1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;


public class ActiviteCompteARebours extends AdMobActivity {

    private final int PERCENT = 120000;
    private final int INT_MILLIS = 1000;
    private final int INTERVALLE = 100;
    private final int AJOUTE_30_SEC = 30000;
    private final String str_compteurEntrainement = "compteurEntrainement";
    private final String str_ad = "compteur_ad";
    private int compteurTimer, int_rowTime_display, int_total_time_in_millis;
    private int compteur_ad;
    private int pressed = 0;
    private boolean doubleBackExit;
    private boolean bln_addTime = false;
    private Handler handler;
    private TimerRunnable runnable;
    private Button btn_passer;
    private TextView txt_countDown;
    private ProgressBar progressBar;
    private Handler handlertappxInterAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        progressBar = findViewById(R.id.progressBar);
        btn_passer = findViewById(R.id.btn_entrainement);
        txt_countDown = findViewById(R.id.txtView_countDown);

        int_total_time_in_millis = PERCENT;
        int_rowTime_display = PERCENT;

        progressBar.setMax(PERCENT);
        progressBar.setProgress(PERCENT);

        Intent intent = getIntent();
        compteurTimer = intent.getIntExtra(str_compteurEntrainement, 1);
        compteur_ad = intent.getIntExtra(str_ad, 0);

        txt_countDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runnable.addTime();
                handler.removeCallbacks(runnable);

                int_total_time_in_millis =
                        runnable.getTimeInMillis() + AJOUTE_30_SEC;
                bln_addTime = false;
                int_rowTime_display = int_total_time_in_millis;
                progressBar.setMax(int_total_time_in_millis);
                progressBar.setProgress(int_total_time_in_millis);
                handler.postDelayed(runnable, INTERVALLE);
            }
        });

        btn_passer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (runnable != null || runnable.isRunning()) {
                    runnable.killRunnable();
                }
            }
        });

        runnable = new TimerRunnable();
        handler = new Handler();
        handler.postDelayed(runnable, 100);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!runnable.isRunning()) {
            runnable = new TimerRunnable();
            handler = new Handler();
            handler.postDelayed(runnable, 100);
        }
    }

    void workoutIntent() {
        Intent workout = new Intent(ActiviteCompteARebours.this, ActiviteEntrainement.class);
        compteurTimer++;
        workout.putExtra(str_compteurEntrainement, compteurTimer);
        workout.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        setResult(RESULT_OK, workout);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (runnable != null) {
            runnable.killRunnable();
            handler.removeCallbacks(runnable);
            runnable = null;
        }
    }

    @Override
    public void onBackPressed() {

        if (doubleBackExit) {
            btn_passer.performClick();
        }

        this.doubleBackExit = true;

        String str_backtoTraining = getResources().getString(R.string.str_backtoTraining);
        if (pressed == 0) {
            Toast.makeText(this, str_backtoTraining, Toast.LENGTH_SHORT).show();
            pressed = 1;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackExit = false;
                pressed = 0;
            }
        }, 2500);
    }

    private class TimerRunnable implements Runnable {

        final Vibrator vibreur = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        private final int INT_SEC = 60;
        private boolean isCanceled = false;
        private int int_progress_value;

        @Override
        public void run() {
            if (bln_addTime) {
                handler.removeCallbacksAndMessages(runnable);
                runnable = null;
            } else if (isCanceled || int_total_time_in_millis == int_progress_value) {
                runnable = null;
                vibreur.vibrate(200);
                workoutIntent();
                return;
            } else {
                int_progress_value += INTERVALLE;
                int_rowTime_display -= INTERVALLE;
                progressBar.setSecondaryProgress(int_progress_value);

                String temps = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(int_rowTime_display),
                        TimeUnit.MILLISECONDS.toSeconds(int_rowTime_display) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(int_rowTime_display)));

                txt_countDown.setText(temps);

                if (int_rowTime_display == 300 || int_rowTime_display == 200) {
                    vibreur.vibrate(100);
                }
            }

            handler.postDelayed(this, INTERVALLE);
        }

        public void killRunnable() {
            isCanceled = true;
        }

        public boolean isRunning() {
            return !isCanceled;
        }

        public int getTimeInMillis() {
            return int_rowTime_display;
        }

        public void addTime() {
            bln_addTime = true;
        }
    }
}
