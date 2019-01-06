package dk.michaelwestergaard.galgeleg;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

public class NewWord extends AppCompatActivity implements View.OnClickListener {

    ArrayList<String> words = new ArrayList<String>();
    String chosenWord = "";

    ArrayAdapter<String> adapter;
    ListView listView;

    CardView wordListCardView, newWordCardView;

    Button randomWordBtn, selectWordBtn, newWordBtn, startGameBtn;
    TextView chosenWordTxt;
    EditText newWordTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);

        listView = findViewById(R.id.word_list);

        randomWordBtn = findViewById(R.id.word_random_btn);
        selectWordBtn = findViewById(R.id.word_select_btn);
        newWordBtn = findViewById(R.id.word_new_btn);
        startGameBtn = findViewById(R.id.start_game_btn);

        randomWordBtn.setOnClickListener(this);
        selectWordBtn.setOnClickListener(this);
        newWordBtn.setOnClickListener(this);
        startGameBtn.setOnClickListener(this);

        wordListCardView = findViewById(R.id.word_list_cardview);
        newWordCardView = findViewById(R.id.word_new_cardview);
        chosenWordTxt = findViewById(R.id.chosen_word_txt);
        newWordTxt = findViewById(R.id.new_word_field);

        newWordTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String word = newWordTxt.getText().toString();
                if(word.length() > 0) {
                    if (word.matches("[a-zæøåA-ZÆØÅ]+")) {
                        chosenWord = newWordTxt.getText().toString();
                        chosenWordTxt.setText(chosenWord);
                    } else {
                        Toast.makeText(NewWord.this, "Du må kun bruge bogstaver!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Intent intent = getIntent();

        words.addAll(new HashSet<String>(Arrays.asList(intent.getExtras().getString("words").toString().split(", "))));

        //Collections.sort(words, String.CASE_INSENSITIVE_ORDER);

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, words);
        listView.setAdapter(adapter);
        listView.setClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosenWord = words.get(position);
                chosenWordTxt.setText(chosenWord);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.equals(randomWordBtn)){
            //Start spil med et random ord

            chosenWord = words.get(new Random(System.currentTimeMillis()).nextInt(words.size()));

            Intent result = new Intent();
            result.putExtra("NewWord", chosenWord);
            setResult(Activity.RESULT_OK, result);
            finish();

        } else if(v.equals(selectWordBtn)){
            //vis vælg ord
            wordListCardView.setVisibility(View.VISIBLE);
            newWordCardView.setVisibility(View.GONE);

        } else if(v.equals(newWordBtn)){
            //new word
            wordListCardView.setVisibility(View.GONE);
            newWordCardView.setVisibility(View.VISIBLE);

        } else if(v.equals(startGameBtn)){

            if(!chosenWord.isEmpty()){
                Intent result = new Intent();
                result.putExtra("NewWord", chosenWord);
                setResult(Activity.RESULT_OK, result);
                finish();
            } else {
                if(wordListCardView.getVisibility() == View.VISIBLE){
                    Toast.makeText(NewWord.this, "Du skal først vælge et ord i listen", Toast.LENGTH_LONG).show();
                } else if(newWordCardView.getVisibility() == View.VISIBLE){
                    Toast.makeText(NewWord.this, "Du skal skrive et ord først", Toast.LENGTH_LONG).show();
                } else if(newWordCardView.getVisibility() == View.GONE && wordListCardView.getVisibility() == View.GONE){
                    Toast.makeText(NewWord.this, "Tryk på tilfædigt ord, vælg fra liste eller skriv et ord..", Toast.LENGTH_LONG).show();
                }
            }

        }
    }
}
