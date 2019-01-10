package dk.michaelwestergaard.galgeleg;

import android.app.ListActivity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HighsocreList extends AppCompatActivity {

    DatabaseReference databaseReference = null;
    ArrayList<PlayerDTO> playerDTOList = new ArrayList<PlayerDTO>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highsocre_list);

        ListView listView = findViewById(R.id.highScoreList);
        final HighScoreAdapter highScoreAdapter = new HighScoreAdapter();
        listView.setAdapter(highScoreAdapter);
        databaseReference = new PlayerDAO().getDatabase();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                playerDTOList.add(new PlayerDTO((String) dataSnapshot.child("username").getValue(), ((Long) dataSnapshot.child("score").getValue()).intValue(), (String) dataSnapshot.child("userID").getValue()));
                sortArrayList();
                highScoreAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                System.out.print(dataSnapshot.child("username").getValue());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                for (PlayerDTO playerDTO : playerDTOList){
                    if(playerDTO.getUserID().equals(dataSnapshot.child("userID").getValue())){
                        playerDTOList.remove(playerDTO);
                        highScoreAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void sortArrayList(){
        Collections.sort(playerDTOList, new Comparator<PlayerDTO>() {
            @Override
            public int compare(PlayerDTO o1, PlayerDTO o2) {
                Long playerScore1 = new Long(o1.getScore());
                Long playerSocre2 = new Long(o2.getScore());
                return playerSocre2.compareTo(playerScore1);
            }
        });
    }

    class HighScoreAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return playerDTOList.size();
        }

        @Override
        public PlayerDTO getItem(int position) {
            return playerDTOList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.highscorelayout, null);
            TextView highScoreName = view.findViewById(R.id.high_score_username);
            TextView highScorePoints = view.findViewById(R.id.high_score_points);

            highScoreName.setText(getItem(position).getUsername());
            highScorePoints.setText("Score: " + getItem(position).getScore());
            return view;
        }
    }

}
