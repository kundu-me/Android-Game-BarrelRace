package com.utdallas.nxkundu.barrelracegame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.utdallas.nxkundu.barrelracegame.gamesettings.GameSettings;
import com.utdallas.nxkundu.barrelracegame.playerinfo.Player;
import com.utdallas.nxkundu.barrelracegame.scores.Score;

public class SuccessActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView textViewSuccessRankValue;
    private TextView textViewSuccessScoreValue;
    private EditText textViewSuccessPlayerValue;

    private Player player;
    private int rank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        textViewSuccessRankValue = (TextView) findViewById(R.id.textViewSuccessRankValue);
        textViewSuccessScoreValue = (TextView) findViewById(R.id.textViewSuccessScoreValue);
        textViewSuccessPlayerValue = (EditText) findViewById(R.id.textViewSuccessPlayerValue);

        Intent intent = getIntent();
        String playerScore = "";
        if (intent != null) {

            playerScore = (String) intent.getSerializableExtra(GameSettings.LABEL_PLAYER_SCORE);
        }

        player = new Player(playerScore);
        Score score = Score.getInstance(this);
        score.addScore(player);

        rank = score.getPlayerRank(player);
        textViewSuccessRankValue.setText(String.valueOf(rank));
        textViewSuccessScoreValue.setText(String.valueOf(playerScore));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.button_save:

                String playerName = textViewSuccessPlayerValue.getText().toString().trim();

                if("".equalsIgnoreCase(playerName)) {

                    Toast.makeText(SuccessActivity.this, GameSettings.ERR_PLAYER_NAME, Toast.LENGTH_SHORT).show();
                }
                else {

                    Score score = Score.getInstance(this);
                    player.setPlayerName(playerName);
                    score.updatePlayer(rank, player);

                    Toast.makeText(SuccessActivity.this, GameSettings.LABEL_SAVED_SUCCESSFULLY, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    this.finish();
                }

                break;

            case R.id.button_cancel:

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                this.finish();
                break;
        }
    }

}
