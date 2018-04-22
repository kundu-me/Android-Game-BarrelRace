package com.utdallas.nxkundu.barrelracegame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.utdallas.nxkundu.barrelracegame.gamecomponents.Component;
import com.utdallas.nxkundu.barrelracegame.gamecomponents.GameComponents;
import com.utdallas.nxkundu.barrelracegame.gamehandler.GameHandler;
import com.utdallas.nxkundu.barrelracegame.gamesettings.GameSettings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentMap;

public class PlayGameActivity extends AppCompatActivity implements SurfaceHolder.Callback, SensorEventListener, View.OnClickListener {

    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;

    private SurfaceView surfaceViewPlayArea;

    private DisplayMetrics displayMetrics;

    private TextView textViewTimer;

    private GameHandler gameHandler;

    private Button buttonPlayPause;
    private Button buttonReset;

    private boolean isGameProgress;

    private long gameStartTimestamp = 0L;
    private long gameProgressTotalTime = 0L;
    private long gameProgressCurrentTime = 0L;
    private long gameProgressTimeBeforePaused = 0L;
    private boolean stopTimer = false;

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        surfaceViewPlayArea = (SurfaceView) findViewById(R.id.play_area);
        surfaceViewPlayArea.getHolder().addCallback(this);

        textViewTimer = (TextView) findViewById(R.id.textViewTimer);

        buttonPlayPause = (Button) findViewById(R.id.buttonPlayPause);

        buttonReset = (Button) findViewById(R.id.buttonReset);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        GameComponents gameComponents = new GameComponents(displayMetrics.widthPixels, displayMetrics.heightPixels);
        ConcurrentMap<String, Component> mapGameComponents = gameComponents.getGameComponents();

        gameHandler = new GameHandler(mapGameComponents);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_menu_main, menu);
        getMenuInflater().inflate(R.menu.help_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.app_settings) {

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.app_help) {

            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        gameHandler.drawGameComponents(surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(sensorManager != null && sensorAccelerometer != null && isGameProgress) {
            sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(sensorManager != null && sensorAccelerometer != null && isGameProgress) {
            sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor currSensorAccelerometer = event.sensor;

        if (currSensorAccelerometer.getType() == Sensor.TYPE_ACCELEROMETER) {

            float accelerationX = -event.values[0];
            float accelerationY = event.values[1];
            float accelerationZ = event.values[2];

            long eventTimestamp = event.timestamp;

            if(isGameProgress) {

                if(gameHandler.isGameCompleted()) {

                    stopTimer = true;
                    System.out.println("****[[[[GAME COMPLETED @ " + textViewTimer.getText() + "]]]]********");
                }
                else if(gameHandler.isBarrelTouched()) {

                    stopTimer = true;
                    System.out.println("****[[[[GAME OVER @ " + textViewTimer.getText() + "]]]]********");
                }
                else {

                    gameHandler.handleHorseMovement(surfaceViewPlayArea, eventTimestamp, accelerationX, accelerationY, accelerationZ);
                }
            }
        }
    }

    private Runnable updateTimerThread = new Runnable() {

        @Override
        public void run() {

            gameProgressCurrentTime = SystemClock.uptimeMillis() - gameStartTimestamp;

            gameProgressTotalTime = gameProgressTimeBeforePaused + gameProgressCurrentTime;

            int secs = (int) (gameProgressTotalTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (gameProgressTotalTime % 1000);
            String localtime = mins + ":" + String.format("%02d", secs) + "." + String.format("%03d", milliseconds);
            textViewTimer.setText(localtime);

            if (gameProgressTotalTime >= GameSettings.MAX_GAME_LONG_TIME) {

                stopTimer = true;
            }
            if (!stopTimer) {
                handler.postDelayed(this, 0);
            }
        }

    };

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.buttonPlayPause:

                if(buttonPlayPause.getText().equals(GameSettings.LABEL_PLAY)) {

                    isGameProgress = true;
                    buttonPlayPause.setText(GameSettings.LABEL_PAUSE);

                    sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_GAME);

                    gameStartTimestamp = SystemClock.uptimeMillis();
                    handler.postDelayed(updateTimerThread, 0);
                }
                else {

                    isGameProgress = false;
                    buttonPlayPause.setText(GameSettings.LABEL_PLAY);

                    sensorManager.unregisterListener(this);

                    gameProgressTimeBeforePaused += gameProgressCurrentTime;
                    handler.removeCallbacks(updateTimerThread);
                }

                break;

            case R.id.buttonReset:

                Intent intentScore = new Intent(this, PlayGameActivity.class);
                startActivity(intentScore);
                break;

        }
    }
}
