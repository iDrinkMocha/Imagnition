package mochainc.imagnition;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by ZSJTraits on 5/7/16.
 */
public class InferCollectionEvent {
    public InferCollectionEvent() {

    }

    private boolean birthday = false;
    private boolean trip = false;
    private boolean meal = false;
    private String collectionsTitleBasic;
    private static ArrayList<String> collectionsTitleArray = new ArrayList<>();
    private SharedPreferences preferences;

    public void setPreferences(Context mContext){
        preferences =  PreferenceManager.getDefaultSharedPreferences(mContext);
    }


    public ArrayList<String> inferEvent(ArrayList<ArrayList<Integer>> arrayOfAllIDOfallcollections, ArrayList<String> dayString, Map<String, ArrayList<String>> mappedPath, ArrayList<String> dateGroupArray) {
        //reset all
        int counter = 0;
        String photoLocation = preferences.getString("LocationAddress", "");
        List<String> photoLocationList = Arrays.asList(photoLocation.split(Pattern.quote(";")));

        for (ArrayList<Integer> arrayOfIDOfAllPhotosPerCollection : arrayOfAllIDOfallcollections) {
            birthday = false;
            trip = false;
            meal = false;
            String Day = dayString.get(counter);
            String date = dateGroupArray.get(counter);
            ArrayList<String> filePaths = mappedPath.get(date);
            ArrayList<String> addressArray = new ArrayList<>();
            String address = "";
            for(String filepath: filePaths){
                for (String individualLocationAddressFilePath : photoLocationList) {
                    if (individualLocationAddressFilePath.contains(filepath)) {
                        String[] locationArray = individualLocationAddressFilePath.split(Pattern.quote("?"));
                        if (locationArray.length == 3) {
                            address = locationArray[2];
                            System.out.println("address" + address);
                            break;
                        }
                    }
                }
            }

            //If contains 2 types of ID
            //birthday take higher priority
            if (arrayOfIDOfAllPhotosPerCollection.contains(591) && arrayOfIDOfAllPhotosPerCollection.contains(594) && arrayOfIDOfAllPhotosPerCollection.contains(546)) {
                //If Candle & Torch
                birthday = true;
            }
            //If ID falls in a range
            else {
                for (int ID : arrayOfIDOfAllPhotosPerCollection) {
                    //meal take greater priority than trip
                    if (ID == 229) {
                        meal = true;
                    } else if (ID > 317 && ID <= 332) {
                        meal = true;
                    } else if (ID > 733 && ID <= 746) {
                        meal = true;
                    } else if (ID > 229 && ID <= 295) {
                        trip = true;
                    } else if (ID > 1620 && ID <= 1647) {
                        trip = true;
                    } else if (ID > 676 && ID <= 733) {
                        trip = true;
                    } else if (ID > 1677 && ID <= 1709) {
                        trip = true;
                    }
                }
            }

            if (birthday) {
                collectionsTitleBasic = "Birthday celebration\non " + Day;//TODO update this with people aka whose bday is it
            } else if (meal) {
                if(!address.equals("")) {
                    collectionsTitleBasic = "Meal at " + address; //TODO update this with people (aka meal with who? eg john and 7 others
                }
                else{
                    collectionsTitleBasic = "Meal on " + Day; //TODO update this with people (aka meal with who? eg john and 7 others
                }
            } else if (trip) {
                if(!address.equals("")) {
                    collectionsTitleBasic = "Trip to " + address;
                }
                else{
                    collectionsTitleBasic = "Trip out on " + Day;
                }
            } else {
                collectionsTitleBasic = Day + " in Singapore";//TODO update this with location
            }

            collectionsTitleArray.add(collectionsTitleBasic);
            counter++;
        }

        if (preferences != null) {
            String output = "";
            for (String s : collectionsTitleArray) {
                output += s + ",";
            }
            preferences.edit().putString("CollectionTitle", output).apply();
        }

        return collectionsTitleArray;
    }

    public ArrayList<String> getCollectionsTitleArray() {
        if(collectionsTitleArray.size()==0){
            String output = preferences.getString("CollectionTitle", "");
            collectionsTitleArray.addAll(Arrays.asList(output.split(Pattern.quote(","))));
            return collectionsTitleArray;
        }
        else {
            return collectionsTitleArray;
        }

    }
}
