package mochainc.imagnition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZSJTraits on 3/7/16.
 */
public class SearchItemChildInfo {
    private String tagsOfPhotoSorted;
    private Map<String, ArrayList<Integer>> GeneralTagsToSpecificTagsMap;
    private Map<String, Integer> stringIntegerMap = new HashMap<>();
    private Map<Integer, String> integerStringMap = new HashMap<>();
    private int allIDSorted;
    private String date;
    private ArrayList<String> pathsOfDate;
    private String title;
    private String address;
    private ArrayList<String> pathsOfAddress;
    private String filepathPeople;


    public SearchItemChildInfo(String tagsOfPhotoSorted, int allIDListSorted){
        this.tagsOfPhotoSorted = tagsOfPhotoSorted;
        allIDSorted = allIDListSorted;
    }

    public String getTagsOfPhotoSorted(){
        return tagsOfPhotoSorted;
    }
    public int getAllIDSorted(){
        return allIDSorted;
    }

    public SearchItemChildInfo(String date, ArrayList<String> pathsOfDate, String title){
        this.date = date;
        this.pathsOfDate = pathsOfDate;
        this.title = title;
    }

    public String getDate(){
        return date;
    }
    public ArrayList<String> getPathsOfDate(){
        return pathsOfDate;
    }
    public String getCollectionsTitle(){
        return title;
    }


    public SearchItemChildInfo(String address, ArrayList<String> pathsOfAddress){
        this.address = address;
        this.pathsOfAddress = pathsOfAddress;
    }

    public String getAddress(){
        return address;
    }
    public ArrayList<String> getPathsOfAddress(){
        return pathsOfAddress;
    }


    public SearchItemChildInfo(String filepathPeople){
        this.filepathPeople = filepathPeople;
    }

    public String getFilepathPeople(){
        return filepathPeople;
    }
}
