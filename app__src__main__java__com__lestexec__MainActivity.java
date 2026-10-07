package com.lestexec;

import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.Random;

public class MainActivity extends Activity {

    private RelativeLayout rootLayout;
    private Handler flashHandler;
    private Random random = new Random();

    private static final int FLASH_COUNT = 10;
    private static final int FLASH_INTERVAL_MS = 120;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootLayout = findViewById(R.id.rootLayout);
        Button pressButton = findViewById(R.id.pressButton);
        flashHandler = new Handler(Looper.getMainLooper());

        // Everything below only runs once the user actively taps the button.
        pressButton.setOnClickListener(v -> triggerPrank());
    }

    private void triggerPrank() {
        flashScreen();
        playLoudTone();
        setRandomWallpaper();
    }

    private void flashScreen() {
        flashStep(0);
    }

    private void flashStep(int count) {
        if (count >= FLASH_COUNT) {
            rootLayout.setBackgroundColor(getColor(R.color.bg_dark));
            return;
        }
        int randomColor = Color.rgb(
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256)
        );
        rootLayout.setBackgroundColor(randomColor);
        flashHandler.postDelayed(() -> flashStep(count + 1), FLASH_INTERVAL_MS);
    }

    private void playLoudTone() {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);

        ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME);
        toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, FLASH_COUNT * FLASH_INTERVAL_MS);

        // Release the tone generator after it's done playing.
        flashHandler.postDelayed(toneGenerator::release, FLASH_COUNT * FLASH_INTERVAL_MS + 200);
    }

    private void setRandomWallpaper() {
        int width = 720;
        int height = 1280;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        // Fill with random blocky pattern since no image assets are bundled.
        int blockSize = 80;
        for (int y = 0; y < height; y += blockSize) {
            for (int x = 0; x < width; x += blockSize) {
                paint.setColor(Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
                canvas.drawRect(x, y, x + blockSize, y + blockSize, paint);
            }
        }

        try {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
            wallpaperManager.setBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (flashHandler != null) {
            flashHandler.removeCallbacksAndMessages(null);
        }
    }
}
