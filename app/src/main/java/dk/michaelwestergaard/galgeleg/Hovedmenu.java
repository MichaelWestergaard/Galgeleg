package dk.michaelwestergaard.galgeleg;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Hovedmenu extends AppCompatActivity implements View.OnClickListener {

    Button playBtn;
    Button helpBtn;
    Button highscoreBtn;
    AlertDialog.Builder alertBuilder;
    AlertDialog alertDialog;
    EditText usernameInput;

    LocalData localData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hovedmenu);

        localData = new LocalData(this);

        Log.d("SharedPref", localData.toString());

        playBtn = findViewById(R.id.play_button);
        helpBtn = findViewById(R.id.help_button);
        highscoreBtn = findViewById(R.id.highscore_button);

        playBtn.setOnClickListener(this);
        helpBtn.setOnClickListener(this);
        highscoreBtn.setOnClickListener(this);

        alertBuilder = new AlertDialog.Builder(Hovedmenu.this);
        alertBuilder.setTitle("Nyt Spil");
        View dialogView = this.getLayoutInflater().inflate(R.layout.layout_new_game, null);

        usernameInput = dialogView.findViewById(R.id.username);
        usernameInput.setText(localData.getData("username"));
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
        alertDialog = alertBuilder.create();
    }


    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.play_button:
                alertDialog.show();
                break;

            case R.id.help_button:
                Intent help = new Intent(this, Help.class);
                startActivity(help);
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

        localData.saveData("username", username);

        Intent game = new Intent(this, Spil.class);
        game.putExtra("username", username);
        startActivity(game);
    }
}
