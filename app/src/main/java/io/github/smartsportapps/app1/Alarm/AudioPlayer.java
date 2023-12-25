package io.github.smartsportapps.app1.Alarm;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

public class AudioPlayer {

    private String fileName;
    private Context context;
    private MediaPlayer mediaPlayer;

    // Constructor
    public AudioPlayer(String name, Context context) {
        fileName = name;
        this.context = context;
        playAudio();
    }

    // Play Audio
    private void playAudio() {
        mediaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor descriptor = context.getAssets().openFd(fileName);
            mediaPlayer.setDataSource(descriptor.getFileDescriptor(),
                    descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            mediaPlayer.setVolume(3, 3);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Stop Audio
    public void stop() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}
