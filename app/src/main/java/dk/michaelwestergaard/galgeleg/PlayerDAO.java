package dk.michaelwestergaard.galgeleg;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class PlayerDAO {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("players");

    public boolean create(PlayerDTO player){
        String userID = database.push().getKey();
        player.setUserID(userID);
        database.child(userID).setValue(player);
        return true;
    }

    public void update(PlayerDTO player){
        database.child(player.getUserID()).setValue(player);
    }

    public List<PlayerDTO> list(){
        return null;
    }

    public DatabaseReference getDatabase(){
        return database;
    }

}
