package dk.michaelwestergaard.galgeleg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Spil extends AppCompatActivity implements View.OnClickListener {

    Button guessBtn;
    TextView chosenWord;
    TextView wrongLetters;
    ImageView galgeImg;

    TextView usernameTxt;
    TextView scoreTxt;

    Galgelogik galgelogik = new Galgelogik();
    String username = "Anonymous";
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spil);
        Intent intent = getIntent();
        username = intent.getExtras().getString("username");

        if(username.isEmpty())
            username = "Anonymous";

        usernameTxt = findViewById(R.id.usernameTxt);
        scoreTxt = findViewById(R.id.score);

        guessBtn = findViewById(R.id.guessBtn);
        chosenWord = findViewById(R.id.chosenWord);
        wrongLetters = findViewById(R.id.usedLetters);
        galgeImg = findViewById(R.id.galgeImg);

        guessBtn.setOnClickListener(this);

        usernameTxt.setText(username);
        scoreTxt.setText("Score: 0");

        chosenWord.setText(galgelogik.getSynligtOrd());

        galgelogik.logStatus();
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
            makeGuess(guessedLetter);
        }
    }

    public void makeGuess(String guess){
        galgelogik.gætBogstav(guess);

        chosenWord.setText(galgelogik.getSynligtOrd());
        wrongLetters.setText(galgelogik.getBrugteBogstaver().toString().replace("[","").replace("]","")); //Måske en
        calculateScore();
        scoreTxt.setText("Score: " + score);
        galgelogik.logStatus();

        if(galgelogik.getAntalForkerteBogstaver() > 0){
            galgeImg.setImageResource(galgeImg.getContext().getResources().getIdentifier("forkert" + galgelogik.getAntalForkerteBogstaver(), "drawable", galgeImg.getContext().getPackageName()));
        }

        if(galgelogik.erSpilletSlut()) {
            if(galgelogik.erSpilletTabt()){
                //Popup med prøv igen.
                System.out.println("Tabt!");
                reset();
            } else if(galgelogik.erSpilletVundet()){
                //Tillykke, prøv igen.
                System.out.println("Vundet!");
                reset();
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
        if(!galgelogik.erSpilletSlut()) {
            score += galgelogik.erSidsteBogstavKorrekt() ? 10 : -5;
        } else if(galgelogik.erSpilletSlut()){
            if(galgelogik.erSpilletVundet()){
                score += 100;
            } else {
                score -= 50;
            }
        }
    }
}
