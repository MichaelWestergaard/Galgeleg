package dk.michaelwestergaard.galgeleg;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalData {

    private Context context;

    public LocalData(Context context) {
        this.context = context;
    }

    public SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences("Galgeleg", Context.MODE_PRIVATE);
    }

    public void saveData(String key, String value){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getData(String key){
        return getSharedPreferences().getString(key, "");
    }

    public String toString(){
        return getSharedPreferences().getAll().toString();
    }
}
