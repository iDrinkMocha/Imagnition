package mochainc.imagnition;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * Created by ZSJTraits on 16/7/16.
 */
public class AddressToPath {
    private static Map<String, ArrayList<String>> mp = new TreeMap<String, ArrayList<String>>();
    private static Map<String, ArrayList<String>> addressToPath = new TreeMap<>();
    public void setAddressToPath(Map<String, ArrayList<String>> localm){
        mp = localm;
    }
    public Map<String, ArrayList<String>> getAddressToPath(){
        return mp;
    }

    public  ArrayList<String> getAddressKey(){
        ArrayList<String> addressKey = new ArrayList<>();
        addressKey.addAll(mp.keySet());
        return addressKey;
    }
    public void getPreferences(SharedPreferences prefs) {
        String locationMapString = prefs.getString("LocationAddress", "");
        List<String> locationMapList = Arrays.asList(locationMapString.split(Pattern.quote(";")));
        for(String s:locationMapList){
            String[] stringArray = s.split(Pattern.quote("?"));
            if(stringArray.length==3) {
                String filePath = stringArray[0];
                String address = stringArray[2];

                ArrayList<String> tas = new ArrayList<>();
                if (addressToPath.get(address) != null) { //checks if got anything already
                    ArrayList<String> tas1 = new ArrayList<>();
                    tas1 = addressToPath.get(address);
                    tas.addAll(tas1);
                    tas.add(filePath);
                    addressToPath.put(address, tas);
                } else {
                    tas.add(filePath);
                    addressToPath.put(address, tas);
                }
            }
        }

        mp = addressToPath;
        System.out.println("prefsGetAddressToPath");
    }
}
