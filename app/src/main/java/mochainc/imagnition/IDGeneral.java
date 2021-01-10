package mochainc.imagnition;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Chun Wei on 25/6/2016.
 */
// Note to self: Chun is god, coffee is key to life -thanks dom
public class IDGeneral {
    private String generaltag;
    public String getGeneraltag(Integer i){
        if(i >= 1 && i <= 226) generaltag = "Animals";
        else if(i == 227) generaltag = "Instruments"; //musical instruments, decide what to say for this
        else if(i == 228) generaltag = "Electronics";
        else if(i == 229) generaltag = "Food";
        else if(i <= 295) generaltag = "Vehicles";
        else if(i <= 317) generaltag = "Furniture";
        else if(i <= 332) generaltag = "Food";
        else if(i <= 356) generaltag = "Instruments";
        else if(i <= 368) generaltag = "Nature";
        else if(i <= 382) generaltag = "Equipment";
        else if(i <= 501) generaltag = "Animals";
        else if(i <= 549) generaltag = "Misc.";
        else if(i <= 553) generaltag = "Electronics";
        else if(i <= 555) generaltag = "Vehicles";
        else if(i <= 561) generaltag = "Electronics";
        else if(i <= 599) generaltag = "Misc.";
        else if(i <= 658) generaltag = "Animals";
        else if(i <= 676) generaltag = "Misc.";
        else if(i <= 733) generaltag = "Places";
        else if(i <= 746) generaltag = "Food";
        else if(i <= 1081) generaltag = "Misc.";
        else if(i <= 1118) generaltag = "Nature";
        else if(i <= 1387) generaltag = "Animals";
        else if(i <= 1407) generaltag = "Instruments";
        else if(i <= 1420) generaltag = "Electronics";
        else if(i <= 1449) generaltag = "Equipment";
        else if(i == 1450) generaltag = "Electronics";
        else if(i <= 1454) generaltag = "Equipment";
        else if(i <= 1460) generaltag = "Electronics";
        else if(i <= 1468) generaltag = "Equipment";
        else if(i <= 1485) generaltag = "Misc.";
        else if(i <= 1616) generaltag = "Equipment";
        else if(i <= 1620) generaltag = "Misc.";
        else if(i <= 1647) generaltag = "Vehicles";
        else if(i <= 1670) generaltag = "Furniture";
        else if(i == 1671) generaltag = "Misc.";
        else if(i == 1672) generaltag = "Electronics";
        else if(i <= 1674) generaltag = "Misc.";
        else if(i <= 1677) generaltag = "Electronics";
        else if(i <= 1709) generaltag = "Places";
        else if(i <= 1737) generaltag = "Misc.";
        else if(i <= 1797) generaltag = "Clothes";
        else if(i <= 1860) generaltag = "Misc.";

        return generaltag;
    }

    public Map<String, ArrayList<Integer>> GetGeneralTagsToSpecificTagsMap(ArrayList<Integer> allIDList){
        Map<String, ArrayList<Integer>> GeneralTagsToSpecificTagsMap = new TreeMap<>();
        Map<Integer, String> g = new TreeMap<>();
        ArrayList<Integer> animalsArray = new ArrayList<>();
        ArrayList<Integer> clothesArray = new ArrayList<>();
        ArrayList<Integer> electronicsArray = new ArrayList<>();
        ArrayList<Integer> equipmentArray = new ArrayList<>();
        ArrayList<Integer> foodArray = new ArrayList<>();
        ArrayList<Integer> furnitureArray = new ArrayList<>();
        ArrayList<Integer> intsrumentsArray = new ArrayList<>();
        ArrayList<Integer> miscArray = new ArrayList<>();
        ArrayList<Integer> natureArray = new ArrayList<>();
        ArrayList<Integer> vehiclesArray = new ArrayList<>();
        ArrayList<Integer> placesArray = new ArrayList<>();
        for(int i:allIDList) {
            g.put(i ,getGeneraltag(i));
        }

        for(int e = 1; e<=g.size();e++){
            if(g.get(e)!=null) {
                if (g.get(e).contains("Animals")) {
                    animalsArray.add(e);
                }
                if (g.get(e).contains("Clothes")) {
                    clothesArray.add(e);
                }
                if (g.get(e).contains("Electronics")) {
                    electronicsArray.add(e);
                }
                if (g.get(e).contains("Equipment")) {
                    equipmentArray.add(e);
                }
                if (g.get(e).contains("Food")) {
                    foodArray.add(e);
                }
                if (g.get(e).contains("Furniture")) {
                    furnitureArray.add(e);
                }
                if (g.get(e).contains("Instruments")) {
                    intsrumentsArray.add(e);
                }
                if (g.get(e).contains("Misc")) {
                    miscArray.add(e);
                }
                if (g.get(e).contains("Nature")) {
                    natureArray.add(e);
                }
                if (g.get(e).contains("Vehicles")) {
                    vehiclesArray.add(e);
                }
                if(g.get(e).contains("Places")){
                    placesArray.add(e);
                }
            }
        }

        GeneralTagsToSpecificTagsMap.put("Animals", animalsArray);
        GeneralTagsToSpecificTagsMap.put("Clothes", clothesArray);
        GeneralTagsToSpecificTagsMap.put("Electronics", electronicsArray);
        GeneralTagsToSpecificTagsMap.put("Equipment", equipmentArray);
        GeneralTagsToSpecificTagsMap.put("Food", foodArray);
        GeneralTagsToSpecificTagsMap.put("Furniture", furnitureArray);
        GeneralTagsToSpecificTagsMap.put("Instruments", intsrumentsArray);
        GeneralTagsToSpecificTagsMap.put("Misc", miscArray);
        GeneralTagsToSpecificTagsMap.put("Nature", natureArray);
        GeneralTagsToSpecificTagsMap.put("Vehicles", vehiclesArray);
        GeneralTagsToSpecificTagsMap.put("Places", placesArray);

        return GeneralTagsToSpecificTagsMap;
    }
    public ArrayList<String> getGeneralStringArray(){
        ArrayList<String> generalStringArray = new ArrayList<>();
        generalStringArray.add("animals");
        generalStringArray.add("clothes");
        generalStringArray.add("electronics");
        generalStringArray.add("equipment");
        generalStringArray.add("food");
        generalStringArray.add("furniture");
        generalStringArray.add("instruments");
        generalStringArray.add("misc");
        generalStringArray.add("nature");
        generalStringArray.add("vehicles");
        generalStringArray.add("places");
        return generalStringArray;
    }
}