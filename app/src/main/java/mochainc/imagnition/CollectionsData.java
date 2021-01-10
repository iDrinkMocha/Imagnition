package mochainc.imagnition;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * Created by ZSJTraits on 3/7/16.
 */

public class CollectionsData {
    private static ArrayList<String> imageGroupKeysStatic;
    private static Map<String, ArrayList<String>> retrieveCollectionStatic;
    private static ArrayList<String> imageGroupKeysAll;
    private static Map<String, ArrayList<String>> retrieveCollectionAll;

    public void putData(ArrayList<String> imageGroupKeys, Map<String, ArrayList<String>> retrieveCollection) {
        imageGroupKeysStatic = imageGroupKeys;
        retrieveCollectionStatic = retrieveCollection;
    }

    public ArrayList<String> getImageGroupKeysStatic() {
        return imageGroupKeysStatic;
    }

    public Map<String, ArrayList<String>> getRetrieveCollectionStatic() {
        return retrieveCollectionStatic;
    }

    public void getPreferences(SharedPreferences prefs) {
        String temp1 = prefs.getString("collectiondata", "");
        String[] results = temp1.split(Pattern.quote("\n"));
        //System.out.println("Deleted" + results[0]);
        String imageGroupString = prefs.getString("imageGroupKeys", "");
        List<String> imageGroupList = Arrays.asList(imageGroupString.split(","));
        imageGroupKeysStatic = new ArrayList<>();
        retrieveCollectionStatic = new TreeMap<String, ArrayList<String>>();
        imageGroupKeysStatic.clear();
        imageGroupKeysStatic.addAll(imageGroupList);
        for (int i = 0; i < results.length; ++i) {
            if (i % 2 != 0) {
                String tempo = results[i].toString().substring(1, results[i].length() - 2);
                String[] segregate;
                ArrayList<String> tArr = new ArrayList<String>();
                segregate = tempo.split(Pattern.quote("*, "));
                for (int b = 0; b < segregate.length; ++b) {
                    tArr.add(segregate[b]);
                }
                if (tArr.size() >= 10) {
                    retrieveCollectionStatic.put(results[i - 1], tArr);
                    //System.out.println("tArr" + results[i - 1]);
                } else {
                    imageGroupKeysStatic.remove(results[i - 1]);
                }
            }
        }
        //TODO problem avoided by simply removing the last one since its somehow bugging out
        //imageGroupKeys.remove(imageGroupKeys.size() - 1);
    }


    public void putDataAll(ArrayList<String> imageGroupKeys, Map<String, ArrayList<String>> retrieveCollection) {
        imageGroupKeysAll = imageGroupKeys;
        retrieveCollectionAll = retrieveCollection;
    }

    public void getPreferencesAll(SharedPreferences prefs) {
        String temp1 = prefs.getString("collectiondata", "");
        String[] results = temp1.split(Pattern.quote("\n"));
        //System.out.println("Deleted" + results[0]);
        String imageGroupString = prefs.getString("imageGroupKeys", "");
        List<String> imageGroupList = Arrays.asList(imageGroupString.split(","));
        imageGroupKeysAll = new ArrayList<>();
        retrieveCollectionAll = new TreeMap<String, ArrayList<String>>();
        imageGroupKeysAll.clear();
        imageGroupKeysAll.addAll(imageGroupList);
        for (int i = 0; i < results.length; ++i) {
            if (i % 2 != 0) {
                String tempo = results[i].toString().substring(1, results[i].length() - 2);
                String[] segregate;
                ArrayList<String> tArr = new ArrayList<String>();
                segregate = tempo.split(Pattern.quote("*, "));
                for (int b = 0; b < segregate.length; ++b) {
                    tArr.add(segregate[b]);
                }
                retrieveCollectionAll.put(results[i - 1], tArr);
                //System.out.println("tArr" + results[i - 1]);
            }
        }
        //TODO problem avoided by simply removing the last one since its somehow bugging out
        //imageGroupKeys.remove(imageGroupKeys.size() - 1);
    }

    public ArrayList<String> getImageGroupKeysStaticAll() {
        return imageGroupKeysAll;
    }

    public Map<String, ArrayList<String>> getRetrieveCollectionStaticAll() {
        return retrieveCollectionAll;
    }
}
