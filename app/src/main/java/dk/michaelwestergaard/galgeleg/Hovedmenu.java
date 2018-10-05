package dk.michaelwestergaard.galgeleg;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Hovedmenu extends AppCompatActivity implements View.OnClickListener {

    Button playBtn;
    Button highscoreBtn;
    AlertDialog.Builder alertBuilder;
    EditText usernameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hovedmenu);

        playBtn = findViewById(R.id.play_button);
        highscoreBtn = findViewById(R.id.highscore_button);

        playBtn.setOnClickListener(this);
        highscoreBtn.setOnClickListener(this);

        alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Nyt Spil");
        View dialogView = this.getLayoutInflater().inflate(R.layout.layout_new_game, null);
        usernameInput = dialogView.findViewById(R.id.username);
        alertBuilder.setView(dialogView);

        alertBuilder.setPositiveButton("Spil!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startGame();
            }
        });
        alertBuilder.setNegativeButton("Annuller", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.play_button:
                alertBuilder.show();
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

    public void startGame(){
        String username = usernameInput.getText().toString();
        Intent game = new Intent(this, Spil.class);
        game.putExtra("username", username);
        startActivity(game);
    }

;
}
