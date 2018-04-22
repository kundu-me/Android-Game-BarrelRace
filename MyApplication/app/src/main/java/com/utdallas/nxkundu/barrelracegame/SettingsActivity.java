package com.utdallas.nxkundu.barrelracegame;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.utdallas.nxkundu.barrelracegame.gamesettings.GameSettings;
import com.utdallas.nxkundu.barrelracegame.gamesettings.ReadWriteGameSettings;

public class SettingsActivity extends AppCompatActivity {

    private RadioGroup radioGroupHorse;
    private RadioGroup radioGroupBarrel;
    private RadioGroup radioGroupLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        radioGroupHorse = (RadioGroup) findViewById(R.id.radioButtonHorse);
        radioGroupBarrel = (RadioGroup) findViewById(R.id.radioButtonBarrel);
        radioGroupLevel = (RadioGroup) findViewById(R.id.radioButtonLevel);

        checkCurrentHorse();
        checkCurrentBarrel();
        checkCurrentGameLevel();

        radioGroupHorse.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                updateHorseColor();
                Toast.makeText(getBaseContext(), GameSettings.LABEL_SAVED_SUCCESSFULLY, Toast.LENGTH_SHORT).show();
            }
        });

        radioGroupBarrel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                updateBarrelColor();
                ReadWriteGameSettings.getInstance(SettingsActivity.this).writeGameSettings();
                Toast.makeText(getBaseContext(), GameSettings.LABEL_SAVED_SUCCESSFULLY, Toast.LENGTH_SHORT).show();
            }
        });

        radioGroupHorse.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                updateHorseColor();
                ReadWriteGameSettings.getInstance(SettingsActivity.this).writeGameSettings();
                Toast.makeText(getBaseContext(), GameSettings.LABEL_SAVED_SUCCESSFULLY, Toast.LENGTH_SHORT).show();
            }
        });

        radioGroupLevel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                updateGameLevel();
                ReadWriteGameSettings.getInstance(SettingsActivity.this).writeGameSettings();
                Toast.makeText(getBaseContext(), GameSettings.LABEL_SAVED_SUCCESSFULLY, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateHorseColor() {

        final String value = ((RadioButton)findViewById(radioGroupHorse.getCheckedRadioButtonId())).getText().toString();

        if("green".equalsIgnoreCase(value)) {
            GameSettings.HORSE_COLOR = Color.GREEN;
        }
        else if("yellow".equalsIgnoreCase(value)){
            GameSettings.HORSE_COLOR = Color.YELLOW;
        }
        else if("magneta".equalsIgnoreCase(value)) {
            GameSettings.HORSE_COLOR = Color.MAGENTA;
        }
    }

    private void updateBarrelColor() {

        final String value = ((RadioButton)findViewById(radioGroupBarrel.getCheckedRadioButtonId())).getText().toString();

        if("red".equalsIgnoreCase(value)) {
            GameSettings.BARREL_COLOR = Color.RED;
        }
        else if("blue".equalsIgnoreCase(value)){
            GameSettings.BARREL_COLOR = Color.BLUE;
        }
        else if("white".equalsIgnoreCase(value)) {
            GameSettings.BARREL_COLOR = Color.WHITE;
        }
    }

    private void updateGameLevel() {

        final String value = ((RadioButton)findViewById(radioGroupLevel.getCheckedRadioButtonId())).getText().toString();

        if("easy".equalsIgnoreCase(value)) {
            GameSettings.ACCELEROMETER_RATE = 1.45f;
        }
        else if("medium".equalsIgnoreCase(value)){
            GameSettings.ACCELEROMETER_RATE = 2.50f;
        }
        else if("difficult".equalsIgnoreCase(value)) {
            GameSettings.ACCELEROMETER_RATE = 2.90f;
        }
    }

    private  void checkCurrentHorse() {

        if(Color.GREEN == GameSettings.HORSE_COLOR) {
            radioGroupHorse.check(R.id.radioButtonHorseGreen);
        }
        else if(Color.YELLOW == GameSettings.HORSE_COLOR) {
            radioGroupHorse.check(R.id.radioButtonHorseYellow);
        }
        else if(Color.MAGENTA == GameSettings.HORSE_COLOR) {
            radioGroupHorse.check(R.id.radioButtonHorseMagneta);
        }
    }

    private  void checkCurrentBarrel() {

        if(Color.RED == GameSettings.BARREL_COLOR) {
            radioGroupBarrel.check(R.id.radioButtonBarrelRed);
        }
        else if(Color.BLUE == GameSettings.BARREL_COLOR) {
            radioGroupBarrel.check(R.id.radioButtonBarrelBlue);
        }
        else if(Color.WHITE == GameSettings.BARREL_COLOR) {
            radioGroupBarrel.check(R.id.radioButtonBarrelWhite);
        }
    }

    private void checkCurrentGameLevel() {

        if(GameSettings.ACCELEROMETER_RATE < 1.5) {
            radioGroupLevel.check(R.id.radioButtonEasy);
        }
        else if(GameSettings.ACCELEROMETER_RATE > 2.0 && GameSettings.ACCELEROMETER_RATE < 2.6) {
            radioGroupLevel.check(R.id.radioButtonMedium);
        }
        else if(GameSettings.ACCELEROMETER_RATE > 2.6) {
            radioGroupLevel.check(R.id.radioButtonDifficult);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}
