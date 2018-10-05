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

    Galgelogik galgelogik = new Galgelogik();
    String username = "Anonymous";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spil);
        Intent intent = getIntent();
        username = intent.getExtras().getString("username");

        guessBtn = findViewById(R.id.guessBtn);
        chosenWord = findViewById(R.id.chosenWord);
        wrongLetters = findViewById(R.id.usedLetters);
        galgeImg = findViewById(R.id.galgeImg);

        guessBtn.setOnClickListener(this);

        chosenWord.setText(galgelogik.getSynligtOrd());
        galgelogik.logStatus();
        System.out.println(username);
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
            makeGuess(guessedLetter);
        }
    }

    public void makeGuess(String guess){
        galgelogik.gætBogstav(guess);
        chosenWord.setText(galgelogik.getSynligtOrd());
        wrongLetters.setText(galgelogik.getBrugteBogstaver().toString().replace("[","").replace("]","")); //Måske en
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
}
