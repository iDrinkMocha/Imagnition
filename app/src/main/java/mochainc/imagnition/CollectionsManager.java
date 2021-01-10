package mochainc.imagnition;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;


public class CollectionsManager {
    public CollectionsManager(Context context, ArrayList<String> imagePathArray){
        SharedPreferences prefs  = PreferenceManager.getDefaultSharedPreferences(context);
        int state = prefs.getInt("firstruncollections", 1);
        if (state == 1){
            //TODO Initial run
            prefs.edit().putInt("firstruncollections", 0).apply();
            //PsuedoCollections pc = new PsuedoCollections(context);
            //pc.setImagePathArray(imagePathArray);
        } else{
            //TODO Subsequent run
            prefs.edit().putInt("firstruncollections", 0).apply();
            //Create class here

        }
    }
}