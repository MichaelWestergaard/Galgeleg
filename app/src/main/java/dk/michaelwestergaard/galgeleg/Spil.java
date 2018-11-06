package dk.michaelwestergaard.galgeleg;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Spil extends AppCompatActivity implements View.OnClickListener {

    AlertDialog.Builder alertBuilder;
    AlertDialog alertDialog;

    Button guessBtn;
    TextView chosenWord;
    TextView wrongLetters;
    ImageView galgeImg;

    TextView usernameTxt;
    TextView scoreTxt;

    Galgelogik galgelogik = new Galgelogik();
    PlayerDAO playerDAO = new PlayerDAO();
    PlayerDTO player = null;
    String username = "Anonymous";
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spil);
        Intent intent = getIntent();
        String username = intent.getExtras().getString("username");

        if(username.isEmpty())
            username = "Anonymous";

        player = new PlayerDTO(username, 0, null);
        playerDAO.create(player);

        usernameTxt = findViewById(R.id.usernameTxt);
        scoreTxt = findViewById(R.id.score);

        guessBtn = findViewById(R.id.guessBtn);
        chosenWord = findViewById(R.id.chosenWord);
        wrongLetters = findViewById(R.id.usedLetters);
        galgeImg = findViewById(R.id.galgeImg);

        guessBtn.setOnClickListener(this);

        usernameTxt.setText(player.getUsername());
        scoreTxt.setText("Score: " + player.getScore());

        new NewWords().execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.guessBtn:
                newGuess();
                break;

            default:
                System.out.println("How did u get here?");
                break;
        }
    }

    public void newGuess() {
        EditText guessText = findViewById(R.id.guess);
        String guessedLetter = guessText.getText().toString();

        if(guessedLetter.isEmpty()){
            Toast.makeText(this, "Du skal gætte på noget..", Toast.LENGTH_LONG).show();
        } else if(guessedLetter.length() > 1){
            Toast.makeText(this, "Du må kun gætte på et bogstav!", Toast.LENGTH_LONG).show();
        } else if(!Character.isLetter(guessedLetter.charAt(0))){
            Toast.makeText(this, "Du kan kun gætte på bogstaver!", Toast.LENGTH_LONG).show();
        } else {
            guessText.setText("");
            if(!galgelogik.getBrugteBogstaver().contains(guessedLetter)){
                makeGuess(guessedLetter);
            } else {
                Toast.makeText(this, "Du har allerede gættet på dette bogstav!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void makeGuess(String guess){
        galgelogik.gætBogstav(guess);

        chosenWord.setText(galgelogik.getSynligtOrd());
        wrongLetters.setText(galgelogik.getBrugteBogstaver().toString().replace("[","").replace("]","")); //Måske en
        calculateScore();
        scoreTxt.setText("Score: " + player.getScore());
        galgelogik.logStatus();

        playerDAO.update(player);

        if(galgelogik.getAntalForkerteBogstaver() > 0){
            galgeImg.setImageResource(galgeImg.getContext().getResources().getIdentifier("forkert" + galgelogik.getAntalForkerteBogstaver(), "drawable", galgeImg.getContext().getPackageName()));
        }

        if(galgelogik.erSpilletSlut()) {
            chosenWord.setText("Ordet var: " + galgelogik.getOrdet());

            if(galgelogik.erSpilletVundet()){
                showEndingScreen("Du har vundet!", "Antal forsøg: " + galgelogik.getBrugteBogstaver().size());
            } else {
                showEndingScreen("Du har tabt!", "Ordet var: " + galgelogik.getOrdet());
            }
        }
    }

    public void reset(){
        galgelogik.nulstil();
        chosenWord.setText(galgelogik.getSynligtOrd());
        wrongLetters.setText("");
        galgelogik.logStatus();
        galgeImg.setImageResource(R.drawable.galge);
    }

    public void calculateScore(){
        int score = 0;
        if(!galgelogik.erSpilletSlut()) {
            score = galgelogik.erSidsteBogstavKorrekt() ? 10 : -5;
        } else if(galgelogik.erSpilletSlut()){
            if(galgelogik.erSpilletVundet()){
                score = 100;
            } else {
                score = -50;
            }
        }
        player.increaseScore(score);
    }

    public void showEndingScreen(String title, String description){
        Intent endingScreen = new Intent(this, EndingScreen.class);
        endingScreen.putExtra("title", title);
        endingScreen.putExtra("description", description);
        endingScreen.putExtra("gameIsWon", galgelogik.erSpilletVundet());
        startActivityForResult(endingScreen, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                reset();
            } else {
                finish();
            }
        }
    }

    private class NewWords extends AsyncTask<String, Void, String> {

        AlertDialog.Builder builder = new AlertDialog.Builder(Spil.this);
        AlertDialog progressDialog;

        @Override
        protected String doInBackground(String... params) {
            try {
                galgelogik.hentOrdFraDr();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            chosenWord.setText(galgelogik.getSynligtOrd());
            galgelogik.logStatus();
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            builder.setCancelable(false); // if you want user to wait for some process to finish,
            builder.setView(R.layout.layout_loading_dialog);
            progressDialog = builder.create();
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
