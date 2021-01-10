package mochainc.imagnition;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.media.ExifInterface;
import android.preference.PreferenceManager;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;
/**
 * Created by Chun Wei on 16/6/2016.
 */
public class IDConversion {
    private Context idcontext;
    private Map<Integer, String> idmap;
    private  ArrayList<String> tags;
    private  ArrayList<Integer> IDtags;
    private ArrayList<Integer> idarray;
    private ArrayList<String> alltags;
    private SharedPreferences spref = null;
    private int counter = 1;
    private IDGeneral idGeneral = new IDGeneral();
    public IDConversion(Context context){
        idcontext = context;
        idmap = new TreeMap<>();
        spref = PreferenceManager.getDefaultSharedPreferences(idcontext);
        //TODO change file name, make sure this try catch is only run once
        AssetManager assManager = idcontext.getAssets();
        InputStream is = null;
        try {
            is = assManager.open("simpletags.txt");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = new String();
            while((line = br.readLine()) != null){
                idmap.put(counter,line);
                counter++;
            }
        }catch(FileNotFoundException fnfe){
            fnfe.printStackTrace();
        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }
    public ArrayList<String> readSpecificTags(ArrayList<Integer> a){
        tags = new ArrayList<>();
        for(int i = 0; i < a.size(); ++i){
            tags.add(i, idmap.get(a.get(i)));
        }
        return tags;
    }
    public ArrayList<Integer> getID(String path){
        idarray = new ArrayList<>();
        ExifInterface exif = null;
        try{
            exif = new ExifInterface(path);
        } catch (IOException e){
            e.printStackTrace();
        }
        if(exif != null){
            String temp = exif.getAttribute("UserComment");
            if(temp!=null) {
                String[] tarr = temp.split(Pattern.quote("|"));
                ArrayList<String> atarr = new ArrayList<>(Arrays.asList(tarr));
                if (atarr.contains("IMAGNITIONS")) {
                    int initial = atarr.indexOf("IMAGNITIONS"); //Detect where is our label(index)
                    for (int x = 1; x <= 5; ++x) { //from that index, get the top 5 tags,each are 1 line
                        String temp1 = tarr[initial + x];
                        String[] split = temp1.split(Pattern.quote(":")); //split so that I get the ID
                        idarray.add((x - 1), Integer.parseInt(split[0].trim())); //ID is at the first, so array[0]
                    }
                } else {
                    idarray.add(0, -1); //failed case always use -1;
                }
                if (atarr.contains("IMGFACE") && tarr[atarr.indexOf("IMGFACE") + 1].equals("1861:people")) {
                    //9999 for person detected, 9998 for nobody detected
                    int initial = atarr.indexOf("IMGFACE");
                    String temp1 = tarr[initial + 1];
                    String[] split = temp1.split(Pattern.quote(":")); //split so that I get the ID
                    ArrayList<String> tas = new ArrayList<>();
                    idarray.add(5, Integer.parseInt(split[0].trim()));
                }
            }
            else{
                idarray.add(0, -1);
            }
        }else{
            idarray.add(0,-1);
        }
        return idarray;
    }
    public ArrayList<String> getAllTags(String path){
        alltags = new ArrayList<>();
        ExifInterface exif = null;
        try{
            exif = new ExifInterface(path);
        } catch (IOException e){
            e.printStackTrace();
        }
        if(exif != null){
            String temp = exif.getAttribute("UserComment");
            String[] tarr = temp.split(Pattern.quote("|"));
            ArrayList<String> atarr = new ArrayList<>(Arrays.asList(tarr));
            if(atarr.contains("IMAGNITIONS")){
                int initial = atarr.indexOf("IMAGNITIONS"); //Detect where is our label(index)
                int count = 0; //counting var for the tags storage
                for(int x = 1; x <= 5; ++x){ //from that index, get the top 5 tags,each are 1 line
                    String temp1 = tarr[initial + x];
                    String[] split = temp1.split(Pattern.quote(":")); //split so that I get the tags w/ commas
                    String[] procs = split[1].split(Pattern.quote(", "));//tags processed
                    for(int y = 0; y < procs.length; ++y){
                        alltags.add(count, procs[y]); //ensures that tags can be stored to global var
                        ++count;
                    }
                }
            }else{
                alltags.add(0,"NULL"); //failed case always use NULL caps;
            }
        }else{
            alltags.add(0,"NULL");
        }
        return alltags;
    }

    public ArrayList<String> readGeneralTags(ArrayList<Integer> allIDList){
        ArrayList<String> generalTagArrayString = new ArrayList<>();

        for(int i = 0; i<allIDList.size(); i++){
            String generalTag = idGeneral.getGeneraltag(allIDList.get(i));
            generalTagArrayString.add(i, generalTag);
        }
        return generalTagArrayString;
    }
}