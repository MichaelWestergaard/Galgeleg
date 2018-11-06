package dk.michaelwestergaard.galgeleg;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;

import com.github.jinatonic.confetti.CommonConfetti;

public class EndingScreen extends AppCompatActivity implements View.OnClickListener {

    Button btnExit, btnPlay;
    MediaPlayer mediaPlayer;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ending_screen);

        Intent intent = getIntent();
        String title = intent.getExtras().getString("title");
        String description = intent.getExtras().getString("description");
        Boolean gameWon = intent.getExtras().getBoolean("gameIsWon");

        TextView titleTextView = findViewById(R.id.title);
        TextView descriptionTextView = findViewById(R.id.description);

        btnExit = findViewById(R.id.btn_exit);
        btnPlay = findViewById(R.id.btn_play);

        btnExit.setOnClickListener(this);
        btnPlay.setOnClickListener(this);

        constraintLayout = findViewById(R.id.endingContainer);

        titleTextView.setText(title);
        descriptionTextView.setText(description);

        if(gameWon){
            mediaPlayer = MediaPlayer.create(EndingScreen.this,R.raw.won);
            final ViewTreeObserver viewTreeObserver = constraintLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        CommonConfetti.rainingConfetti((ViewGroup) constraintLayout.getParent(), new int[] { Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW }).stream(5000);

                        constraintLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        } else {
            mediaPlayer = MediaPlayer.create(EndingScreen.this,R.raw.lost);
        }

        mediaPlayer.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_exit:
                setResult(Activity.RESULT_CANCELED, new Intent());
                finish();
                break;

            case R.id.btn_play:
                setResult(Activity.RESULT_OK, new Intent());
                finish();
        }
    }
}
