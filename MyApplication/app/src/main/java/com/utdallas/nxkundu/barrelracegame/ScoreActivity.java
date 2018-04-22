package com.utdallas.nxkundu.barrelracegame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.utdallas.nxkundu.barrelracegame.playerinfo.Player;
import com.utdallas.nxkundu.barrelracegame.scores.Score;

import java.util.List;

public class ScoreActivity extends AppCompatActivity {

    TableLayout tableViewScores = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        tableViewScores = (TableLayout) findViewById(R.id.table_view_scores);
    }

    @Override
    protected void onStart() {

        super.onStart();

        Score score = Score.getInstance(this);
        List<Player> lstScores = score.getLstScores();

        int rank = 0;
        for(Player player : lstScores) {

            ++rank;

            TableRow tr = new TableRow(ScoreActivity.this);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            TextView textViewRank = new TextView(this);
            textViewRank.setText(String.valueOf(rank));
            textViewRank.setWidth(80);
            textViewRank.setPadding(0, 10, 0, 10);
            textViewRank.setTextSize(20);
            tr.addView(textViewRank);

            TextView textViewScore = new TextView(this);
            textViewScore.setText(player.getScore());
            textViewScore.setWidth(120);
            textViewScore.setPadding(0, 10, 0, 10);
            textViewScore.setTextSize(20);
            tr.addView(textViewScore);

            TextView textViewPlayerName = new TextView(this);
            textViewPlayerName.setText(player.getPlayerName());
            textViewPlayerName.setWidth(180);
            textViewPlayerName.setPadding(0, 10, 0, 10);
            textViewPlayerName.setTextSize(20);
            tr.addView(textViewPlayerName);

            tableViewScores.addView(tr);
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
