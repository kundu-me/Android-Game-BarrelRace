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
import com.utdallas.nxkundu.barrelracegame.scores.Score;
import com.utdallas.nxkundu.barrelracegame.util.Util;

import java.util.concurrent.ConcurrentMap;
/******************************************************************************
 * Barrel Race Game
 * This is an Android Game Application
 *
 * This is the Main Activity
 * This is where the playing of the game is handled
 *
 * On Play - the sensor is registered and the horse movement is updated
 * Also all the Game constraints are applied from this class
 *
 * Written by Nirmallya Kundu (nxk161830) at The University of Texas at Dallas
 * starting April 20, 2018.
 ******************************************************************************/

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
    private boolean pauseTimer = false;

    AlertDialog alertDialogGamePaused = null;

    private final Handler handler = new Handler();

    /**************************************************************************
     * Method
     * Initializing all the Game Components
     * Drawing the Play Screen and all the Initialized Components
     **************************************************************************/
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

    /**************************************************************************
     * Method
     * All the Menu options are initialized
     **************************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topscore_menu_main, menu);
        getMenuInflater().inflate(R.menu.settings_menu_main, menu);
        getMenuInflater().inflate(R.menu.help_menu_main, menu);
        return true;
    }

    /**************************************************************************
     * Method
     *
     * This method handles the onclick on the icon on the app bar
     * If the Game is in Progress
     * it alerts the User that
     * the game is in progress and whether the user wants to navigate to other page
     * stopping the current Game
     **************************************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id_item = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(isGameProgress == false) {

            startIntentActivity(id_item);
        }
        else {

            gamePaused();

            new AlertDialog.Builder(this)
                    .setMessage("Game in progress. Do you want to still navigate?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if(alertDialogGamePaused != null) {
                                alertDialogGamePaused.cancel();
                            }

                            startIntentActivity(id_item);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

    /**************************************************************************
     * Method
     *
     * This method opens the page based on id
     **************************************************************************/
    private void startIntentActivity(int id) {

        Intent intent = null;

        if(id == R.id.app_topscore) {

            intent = new Intent(this, ScoreActivity.class);

        }
        else if (id == R.id.app_settings) {

            intent = new Intent(this, SettingsActivity.class);
        }
        else if(id == R.id.app_help) {

            intent = new Intent(this, HelpActivity.class);
        }

        if(intent != null) {
            startActivity(intent);
        }
    }

    /**************************************************************************
     * Method
     * This is where the Game Components are drawn
     **************************************************************************/
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

    /**************************************************************************
     * Method
     * If the Game is in progress
     * On resume resumes the Game after 5 sec automatically
     * or give an option to manually resume the game
     **************************************************************************/
    @Override
    protected void onResume() {
        super.onResume();

        if(sensorManager != null && sensorAccelerometer != null) {
            //sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_GAME);

            updateOnStartOrResume();
        }
    }

    /**************************************************************************
     * Method
     * If the Game is in progress
     * On start resumes the Game after 5 sec automatically
     * or give an option to manually resume the game
     **************************************************************************/
    @Override
    protected void onStart() {
        super.onStart();

        if(sensorManager != null && sensorAccelerometer != null) {
            //sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_GAME);

            updateOnStartOrResume();
        }
    }

    /**************************************************************************
     * Method
     *
     * (Game is Resumed)
     * After 5 sec of Delay
     * or a by the manual option given to the player
     * *************************************************************************/
    private void updateOnStartOrResume() {

        if(alertDialogGamePaused != null) {

            handler.postDelayed(resumeGameThread, 5000);
        }
    }

    /**************************************************************************
     * Method
     *
     * Unregistering the Sensor on Pause (Game is Paused)
     **************************************************************************/
    @Override
    protected void onPause() {
        super.onPause();

        if(sensorManager != null && isGameProgress) {
            //sensorManager.unregisterListener(this);

            gamePaused();
        }
    }

    /**************************************************************************
     * Method
     *
     * Unregistering the Sensor on Stop (Game is Paused)
     **************************************************************************/
    @Override
    protected void onStop() {
        super.onStop();

        if(sensorManager != null && isGameProgress) {
            //sensorManager.unregisterListener(this);

            gamePaused();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**************************************************************************
     * Method
     *
     * As we get the values from the sensor
     * All the Game components are redrawn
     * The Horse movement is updated
     * All the Game Constraints are applied
     **************************************************************************/
    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor currSensorAccelerometer = event.sensor;

        if (currSensorAccelerometer.getType() == Sensor.TYPE_ACCELEROMETER) {

            float accelerationX = -event.values[0];
            float accelerationY = event.values[1];
            float accelerationZ = event.values[2];

            //System.out.println("accelerationX=" + accelerationX + ", accelerationY=" + accelerationY);

            long eventTimestamp = System.currentTimeMillis();

            if(isGameProgress) {

                gameHandler.handleHorseMovement(surfaceViewPlayArea, eventTimestamp, accelerationX, accelerationY, accelerationZ);

                updateGameProgress(gameHandler.getGameProgressPercentage());

                if(gameHandler.isGameCompleted()) {

                    pauseTimer = true;

                    updateGameProgress(100);

                    System.out.println("****[[[[GAME COMPLETED @ " + textViewTimer.getText() + "]]]]********");
                    isGameProgress = false;

                    String strScore = textViewTimer.getText().toString();

                    Intent intent = new Intent(this, SuccessActivity.class);
                    intent.putExtra(GameSettings.LABEL_PLAYER_SCORE, strScore);
                    startActivity(intent);
                }
                else if(gameHandler.isBarrelTouched()) {

                    pauseTimer = true;

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

    /**************************************************************************
     * Method
     *
     * This method updates the Game Completion percentage on screen
     *
     **************************************************************************/
    private void updateGameProgress(int gameProgressPercentage) {

        textViewProgress.setText("" + gameProgressPercentage + "% Completed");
    }

    /**************************************************************************
     * Method
     *
     * resumeGameThread
     *
     * Resumes the Game automatically
     *
     **************************************************************************/
    private Runnable resumeGameThread = new Runnable() {

        @Override
        public void run() {

            if(alertDialogGamePaused != null) {

                alertDialogGamePaused.cancel();
                gamePlayorResume();
            }
        }

    };

    /**************************************************************************
     * Method
     *
     * Updates the clock
     * on the bottom of the screen
     * as the Game is in progress
     **************************************************************************/
    private Runnable updateTimerThread = new Runnable() {

        @Override
        public void run() {

            gameProgressCurrentTime = SystemClock.uptimeMillis() - gameStartTimestamp;

            gameProgressTotalTime = gameProgressTimeBeforePaused + gameProgressCurrentTime;

            textViewTimer.setText(Util.getUserReadableTime(gameProgressTotalTime));

            if (pauseTimer == false) {

                handler.postDelayed(this, 0);
            }
            else {

                // Game is Paused
            }
        }

    };

    /**************************************************************************
     * Method
     *
     * This method handles the Game Play pause  reset click
     **************************************************************************/
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.buttonPlayPause:

                if(buttonPlayPause.getText().equals(GameSettings.LABEL_PLAY)) {

                   gamePlayorResume();
                }
                else {

                    gamePaused();

                }

                break;

            case R.id.buttonReset:

                if(isGameProgress) {
                    gamePaused();
                }

                new AlertDialog.Builder(this)
                        .setMessage("Are you sure you want to restart?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if(alertDialogGamePaused != null) {
                                    alertDialogGamePaused.cancel();
                                }

                                restartGame();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                break;

        }
    }

    /**************************************************************************
     * Method
     * This method Starts the Game or Resumes the Paused Game
     **************************************************************************/
    public void gamePlayorResume() {

        System.out.println("Game Started... ");

        isGameProgress = true;
        buttonPlayPause.setText(GameSettings.LABEL_PAUSE);
        textViewProgress.setVisibility(View.VISIBLE);

        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_GAME);

        gameStartTimestamp = SystemClock.uptimeMillis();
        handler.postDelayed(updateTimerThread, 0);
    }

    /**************************************************************************
     * Method
     * This method Pause Game
     **************************************************************************/
    private void gamePaused() {

        isGameProgress = false;
        buttonPlayPause.setText(GameSettings.LABEL_PLAY);

        sensorManager.unregisterListener(this);

        gameProgressTimeBeforePaused += gameProgressCurrentTime;
        handler.removeCallbacks(updateTimerThread);

        alertDialogGamePaused = new AlertDialog.Builder(this)
                .setMessage("Game Paused !")
                .setCancelable(false)
                .setPositiveButton("Resume", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        gamePlayorResume();
                    }
                })
                .show();
    }

    /**************************************************************************
     * Method
     * This method restart the Game
     **************************************************************************/
    public void restartGame() {

        buttonPlayPause.setText(GameSettings.LABEL_PLAY);
        isGameProgress = false;

        gameStartTimestamp = 0L;
        gameProgressTotalTime = 0L;
        gameProgressCurrentTime = 0L;
        gameProgressTimeBeforePaused = 0L;

        pauseTimer = false;

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

    /**************************************************************************
     * Method
     * This method exits the game
     **************************************************************************/
    public void exitGame() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    /**************************************************************************
     * Method
     *
     * On Back Press closes the Screen
     * if the Game is not in Progress then asks the user whether to close
     * else
     * Pauses the Game and then asks the user whether to close
     **************************************************************************/
    @Override
    public void onBackPressed() {

        if(isGameProgress) {
            gamePaused();
        }

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

    /**************************************************************************
     * Method
     *
     * This method shows the Game Over Options
     **************************************************************************/
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
