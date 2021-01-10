package mochainc.imagnition;


import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZSJTraits on 2/7/16.
 */
public class SearchItemInfo implements ParentListItem {
    /* Create an instance variable for your list of children */
    private List<SearchItemChildInfo> mChildrenList;
    private String title;

    public SearchItemInfo(){
    }




    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }

    public void setChildItemList(List<SearchItemChildInfo> mChildrenList){
        this.mChildrenList = mChildrenList;
    }

    @Override
    public List<SearchItemChildInfo> getChildItemList() {
        return mChildrenList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
