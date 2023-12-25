package io.github.smartsportapps.app1.Alarm;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import io.github.smartsportapps.app1.R;

public class AlarmPopup extends Activity {

    private int id;
    private TextView tvMsg;
    private AudioPlayer playMp3;
    private Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminderdailog);

        WindowManager.LayoutParams windowManager = getWindow().getAttributes();
        windowManager.dimAmount = 0.75f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        id = getIntent().getIntExtra("id", 0);
        tvMsg = findViewById(R.id.tvMsgForReminder);
        tvMsg.setText(Const.getDay(id));

        btnOk = findViewById(R.id.btnOkForReminderDialog);
        btnOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                playMp3.stop();
                AlarmPopup.this.finish();
            }
        });

        playMp3 = new AudioPlayer("beep.mp3", AlarmPopup.this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode != KeyEvent.KEYCODE_BACK)
            return super.onKeyDown(keyCode, event);
        else {
            return false;
        }
    }
}
