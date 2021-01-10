package mochainc.imagnition;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
/**
 * Created by Chun Wei on 22/6/2016.
 */
public class IDtoPath {
    private static Map<Integer, ArrayList<String>> mp = new TreeMap<Integer, ArrayList<String>>();
    public IDtoPath(){
        System.out.println("IDtoPath initialised!");
    }
    public Map<Integer, ArrayList<String>> getIDtoPath(){
        return mp;
    }
    public void setIDtoPath(Map<Integer, ArrayList<String>> localm){
        mp = localm;
    }
}