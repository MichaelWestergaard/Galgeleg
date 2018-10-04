package dk.michaelwestergaard.galgeleg;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Spil extends AppCompatActivity implements View.OnClickListener {

    Button guessBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spil);
        guessBtn = findViewById(R.id.guessBtn);
        guessBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.guessBtn:
                makeGuess();
                break;

            default:
                System.out.println("How did u get here?");
                break;
        }
    }

    public void makeGuess() {
        EditText guessText = findViewById(R.id.guess);
        String guessedLetter = guessText.getText().toString();

        if(guessedLetter.isEmpty()){
            Toast.makeText(this, "Du skal gætte på noget..", Toast.LENGTH_LONG).show();
        } else if(guessedLetter.length() > 1){
            Toast.makeText(this, "Du må kun gætte på et bogstav!", Toast.LENGTH_LONG).show();
        } else if(!Character.isLetter(guessedLetter.charAt(0))){
            Toast.makeText(this, "Du kan kun gætte på bogstaver!", Toast.LENGTH_LONG).show();
        }
    }
}
