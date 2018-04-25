package com.utdallas.nxkundu.barrelracegame;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
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
import com.utdallas.nxkundu.barrelracegame.gamesettings.ReadWriteGameSettings;
import com.utdallas.nxkundu.barrelracegame.playerinfo.Player;
import com.utdallas.nxkundu.barrelracegame.scores.Score;
import com.utdallas.nxkundu.barrelracegame.util.Util;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback, SensorEventListener, View.OnClickListener {

    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;

    private SurfaceView surfaceViewPlayArea;

    private DisplayMetrics displayMetrics;

    private TextView textViewProgress;
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
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Score score = Score.getInstance(this);

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        surfaceViewPlayArea = (SurfaceView) findViewById(R.id.play_area);
        surfaceViewPlayArea.getHolder().addCallback(this);

        textViewProgress = (TextView) findViewById(R.id.textViewProgress);

        textViewTimer = (TextView) findViewById(R.id.textViewTimer);

        buttonPlayPause = (Button) findViewById(R.id.buttonPlayPause);

        buttonReset = (Button) findViewById(R.id.buttonReset);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        GameComponents gameComponents = new GameComponents(displayMetrics.widthPixels, displayMetrics.heightPixels);
        ConcurrentMap<String, Component> mapGameComponents = gameComponents.getGameComponents();

        gameHandler = new GameHandler(mapGameComponents);

        ReadWriteGameSettings.getInstance(this).readGameSettings();

        textViewProgress.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topscore_menu_main, menu);
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
        if(id == R.id.app_topscore) {

            Intent intent = new Intent(this, ScoreActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.app_settings) {

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

                gameHandler.handleHorseMovement(surfaceViewPlayArea, eventTimestamp, accelerationX, accelerationY, accelerationZ);

                updateGameProgress(gameHandler.getGameProgressPercentage());

                if(gameHandler.isGameCompleted()) {

                    stopTimer = true;

                    updateGameProgress(100);

                    System.out.println("****[[[[GAME COMPLETED @ " + textViewTimer.getText() + "]]]]********");
                    isGameProgress = false;

                    String strScore = textViewTimer.getText().toString();

                    Intent intent = new Intent(this, SuccessActivity.class);
                    intent.putExtra(GameSettings.LABEL_PLAYER_SCORE, strScore);
                    startActivity(intent);
                }
                else if(gameHandler.isBarrelTouched()) {

                    stopTimer = true;
                    System.out.println("****[[[[GAME OVER @ " + textViewTimer.getText() + "]]]]********");
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (vibrator.hasVibrator()) {
                        vibrator.vibrate(500);
                    }
                    isGameProgress = false;

                    gameover();
                }
                else if((gameHandler.isCourseTouched()) &&
                        (System.currentTimeMillis() - gameHandler.getTimeStampCourseTouched() < 200)) {

                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    if (vibrator.hasVibrator()) {
                        vibrator.vibrate(100);
                    }
                }
            }
        }
    }

    private void updateGameProgress(int gameProgressPercentage) {

        textViewProgress.setText("" + gameProgressPercentage + "% Completed");
    }

    private Runnable updateTimerThread = new Runnable() {

        @Override
        public void run() {

            gameProgressCurrentTime = SystemClock.uptimeMillis() - gameStartTimestamp;

            gameProgressTotalTime = gameProgressTimeBeforePaused + gameProgressCurrentTime;

            textViewTimer.setText(Util.getUserReadableTime(gameProgressTotalTime));

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
                    textViewProgress.setVisibility(View.VISIBLE);

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

                restartGame();

                break;

        }
    }

    public void restartGame() {

        buttonPlayPause.setText(GameSettings.LABEL_PLAY);
        isGameProgress = false;

        gameStartTimestamp = 0L;
        gameProgressTotalTime = 0L;
        gameProgressCurrentTime = 0L;
        gameProgressTimeBeforePaused = 0L;
        stopTimer = false;

        textViewTimer.setText("00:00.000");

        sensorManager.unregisterListener(this);
        handler.removeCallbacks(updateTimerThread);

        GameComponents gameComponents = new GameComponents(displayMetrics.widthPixels, displayMetrics.heightPixels);
        ConcurrentMap<String, Component> mapGameComponents = gameComponents.getGameComponents();

        gameHandler = new GameHandler(mapGameComponents);

        gameHandler.drawGameComponents(surfaceViewPlayArea.getHolder());

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void exitGame() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        exitGame();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void gameover() {

        new AlertDialog.Builder(this)
                .setMessage("Game Over! You touched the barrel")
                .setCancelable(false)
                .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        restartGame();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        exitGame();

                    }
                })
                .show();
    }
    
}
