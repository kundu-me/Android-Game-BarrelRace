package com.utdallas.nxkundu.barrelracegame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.utdallas.nxkundu.barrelracegame.playerinfo.Player;
import com.utdallas.nxkundu.barrelracegame.scores.Score;

public class ScoreActivity extends AppCompatActivity {

    ListView listViewScores = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        listViewScores = (ListView) findViewById(R.id.list_scores);
    }

    @Override
    protected void onStart() {

        super.onStart();

        Score score = Score.getInstance(this);
        ListAdapter lstAdapter = new ArrayAdapter<Player>(this, android.R.layout.simple_list_item_1, score.getLstScores());
        listViewScores.setAdapter(lstAdapter);
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
}
