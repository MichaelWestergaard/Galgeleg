package dk.michaelwestergaard.galgeleg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Hovedmenu extends AppCompatActivity implements View.OnClickListener {

    Button playBtn;
    Button highscoreBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hovedmenu);

        playBtn = findViewById(R.id.play_button);
        highscoreBtn = findViewById(R.id.highscore_button);

        playBtn.setOnClickListener(this);
        highscoreBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.play_button:
                Intent game = new Intent(this, Spil.class);
                startActivity(game);
                break;


            case R.id.highscore_button:
                Intent highscore = new Intent(this, HighsocreList.class);
                startActivity(highscore);
                break;

            default:
                System.out.println("Denne knap virker ikke endnu..");
                break;
        }
    }
}
