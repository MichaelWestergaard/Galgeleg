package dk.michaelwestergaard.galgeleg;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
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

    SharedPreferences sharedPreferences;
    static String PREF_NAME = "Username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hovedmenu);

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
        usernameInput.setText(getSharedPreferences().getString(PREF_NAME, ""));
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

    public SharedPreferences getSharedPreferences() {
        return this.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
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

        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(PREF_NAME, username);
        editor.commit();

        Intent game = new Intent(this, Spil.class);
        game.putExtra("username", username);
        startActivity(game);
    }
}
