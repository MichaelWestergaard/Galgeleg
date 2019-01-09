package dk.michaelwestergaard.galgeleg;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
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

    Galgelogik galgelogik = Galgelogik.getInstance();
    LocalData localData;

    String chosenWord = "";

    ArrayAdapter<String> adapter;
    ListView listView;

    CardView wordListCardView, newWordCardView;

    Button randomWordBtn, selectWordBtn, updateWordsBtn, newWordBtn, startGameBtn;
    TextView chosenWordTxt;
    EditText newWordTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_word);

        localData = new LocalData(this);

        galgelogik.loadWords(localData.getData("words"));
        listView = findViewById(R.id.word_list);

        randomWordBtn = findViewById(R.id.word_random_btn);
        selectWordBtn = findViewById(R.id.word_select_btn);
        updateWordsBtn = findViewById(R.id.update_words);
        newWordBtn = findViewById(R.id.word_new_btn);
        startGameBtn = findViewById(R.id.start_game_btn);

        randomWordBtn.setOnClickListener(this);
        selectWordBtn.setOnClickListener(this);
        updateWordsBtn.setOnClickListener(this);
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
                if (word.matches("[a-zæøåA-ZÆØÅ]+")) {
                    chosenWord = newWordTxt.getText().toString();
                    chosenWordTxt.setText(chosenWord);
                } else {
                    Toast.makeText(NewWord.this, "Du må kun bruge bogstaver!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, galgelogik.muligeOrd);
        listView.setAdapter(adapter);
        listView.setClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosenWord = galgelogik.muligeOrd.get(position);
                chosenWordTxt.setText(chosenWord);
            }
        });
    }

    @Override
    public void onClick(View v) {

        if(v.equals(randomWordBtn)){
            //Start spil med et random ord
            galgelogik.randomWord();
            finish();
        } else if(v.equals(selectWordBtn)){
            //vis vælg ord
            wordListCardView.setVisibility(View.VISIBLE);
            newWordCardView.setVisibility(View.GONE);
        } else if(v.equals(updateWordsBtn)){
            new UpdateWords().execute();
        } else if(v.equals(newWordBtn)){
            //new word
            wordListCardView.setVisibility(View.GONE);
            newWordCardView.setVisibility(View.VISIBLE);
        } else if(v.equals(startGameBtn)){
            if(!chosenWord.isEmpty()){
                galgelogik.setWord(chosenWord);
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

    private class UpdateWords extends AsyncTask<String, Void, String> {

        AlertDialog.Builder builder = new AlertDialog.Builder(NewWord.this);

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
            adapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            builder.setCancelable(false);
            builder.setView(R.layout.layout_loading_dialog);
            progressDialog = builder.create();
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
