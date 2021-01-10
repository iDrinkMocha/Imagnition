package mochainc.imagnition;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by ZSJTraits on 2/7/16.
 */
public class SearchExpandableListAdapter extends ExpandableRecyclerAdapter<SearchExpandableListAdapter.SearchItemParentViewHolder, SearchExpandableListAdapter.SearchItemChildViewHolder> {
    private LayoutInflater mInflater;
    private Map<String, Integer> stringIntegerMap = new HashMap<>();
    private Map<Integer, String> integerStringMap = new HashMap<>();
    private ArrayList<Integer> parentPositions = new ArrayList<>();
    private Context mContext;
    private Map<Integer, ArrayList<String>> g;
    private ArrayList<String> tagsOfPhotoSorted;
    private ArrayList<Integer> allIDListSorted;
    private List<ParentListItem> list;
    private IDGeneral idGeneral = new IDGeneral();
    private ArrayList<String> generalString;
    private ArrayList<String> orignialTagsOfPhoto = new ArrayList<>();
    private ArrayList<Integer> orignialIDTags = new ArrayList<>();
    public int variableInt = -1;
    private Map<String, ArrayList<String>> filepathsOfDate;
    private ArrayList<String> datesOriginal;
    private ArrayList<String> dates;
    public EditText searchBox;
    private ArrayList<String> collectionstitle;
    private ArrayList<String> collectionstitleOriginal;
    private ArrayList<String> addressesArray;
    private Map<String, ArrayList<String>> addressToPathMap;
    private ArrayList<String> addressesArrayOriginal;
    private ArrayList<String> peoplepaths;

    public SearchExpandableListAdapter(Context context, List<ParentListItem> parentItemList, ArrayList<String> tagsOfPhotoSorted, ArrayList<Integer> allIDListSorted, Map<Integer, ArrayList<String>> g, final GridLayoutManager mStaggeredLayoutManager, final EditText searchBox, ArrayList<String> peoplepaths) {
        super(parentItemList);
        mContext = context;
        mInflater = LayoutInflater.from(context);

        list = parentItemList;

        this.tagsOfPhotoSorted = tagsOfPhotoSorted;
        this.allIDListSorted = allIDListSorted;
        this.g = g;
        this.searchBox = searchBox;
        this.peoplepaths = peoplepaths;


        orignialTagsOfPhoto.addAll(this.tagsOfPhotoSorted);
        orignialIDTags.addAll(this.allIDListSorted);
        for (int k = 0; k < this.tagsOfPhotoSorted.size(); k++) {
            stringIntegerMap.put(this.tagsOfPhotoSorted.get(k), this.allIDListSorted.get(k));
            integerStringMap.put(this.allIDListSorted.get(k), this.tagsOfPhotoSorted.get(k));
        }

        generalString = idGeneral.getGeneralStringArray();
        GeneralTagsToSpecificTagsMap = idGeneral.GetGeneralTagsToSpecificTagsMap(this.allIDListSorted);


        setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
            @Override
            public void onListItemExpanded(int position) {
                mStaggeredLayoutManager.setSpanSizeLookup(positon0Expanded);
                if (position == 0) {
                    variableInt = 1;
                    notifyParentItemRemoved(3);
                    notifyParentItemRemoved(2);
                    notifyParentItemRemoved(1);
                    searchBox.setHint("Search tags");
                } else if (position == 1) {
                    variableInt = 2;
                    notifyParentItemRemoved(3);
                    notifyParentItemRemoved(2);
                    notifyParentItemRemoved(0);
                    searchBox.setHint("Search collections");
                } else if (position == 2) {
                    variableInt = 3;
                    notifyParentItemRemoved(3);
                    notifyParentItemRemoved(1);
                    notifyParentItemRemoved(0);
                    searchBox.setHint("Search places");
                } else if (position == 3) {
                    variableInt = 4;
                    notifyParentItemRemoved(2);
                    notifyParentItemRemoved(1);
                    notifyParentItemRemoved(0);
                    searchBox.setHint("Search people");
                }
            }

            @Override
            public void onListItemCollapsed(int position) {
                //allCollapsed
                mStaggeredLayoutManager.setSpanSizeLookup(positon3Expanded);
                variableInt = -1;
                searchBox.setHint("Search photos");
            }
        });

        mStaggeredLayoutManager.setSpanSizeLookup(positon3Expanded);


        CollectionsData collectionsData = new CollectionsData();
        InferCollectionEvent inferCollectionEvent = new InferCollectionEvent();
        dates = collectionsData.getImageGroupKeysStatic();
        datesOriginal = new ArrayList<>();
        datesOriginal.addAll(dates);

        collectionstitle = inferCollectionEvent.getCollectionsTitleArray();
        collectionstitleOriginal = new ArrayList<>();
        collectionstitleOriginal.addAll(collectionstitle);
        filepathsOfDate = collectionsData.getRetrieveCollectionStatic();

        AddressToPath addressToPath = new AddressToPath();
        addressesArray = addressToPath.getAddressKey();
        addressToPathMap = addressToPath.getAddressToPath();
        addressesArrayOriginal = new ArrayList<>();
        addressesArrayOriginal.addAll(addressesArray);
    }


    private GridLayoutManager.SpanSizeLookup positon0Expanded = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            ParentListItem item1 = list.get(0);
            int sizeOfChildList1 = item1.getChildItemList().size();

            int positionOfFirstParent = 0;
            int positionOfSecondParent = sizeOfChildList1 + 1;
            int positionOfThirdParent = positionOfSecondParent + 1;
            int positionOfFourthParent = positionOfThirdParent + 1;


            if (position == positionOfFirstParent || position == positionOfSecondParent || position == positionOfThirdParent || position == positionOfFourthParent) {
                return 3;
            } else {
                return 1;
            }
        }
    };

    private GridLayoutManager.SpanSizeLookup positon3Expanded = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {

            int positionOfFirstParent = 0;
            int positionOfSecondParent = 1;
            int positionOfThirdParent = 2;
            int positionOfFourthParent = 3;

            if (position == positionOfFirstParent || position == positionOfSecondParent || position == positionOfThirdParent || position == positionOfFourthParent) {
                return 3;
            } else {
                return 1;
            }
        }
    };


    @Override
    public SearchItemParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.search_item_parent, viewGroup, false);
        return new SearchItemParentViewHolder(view);
    }

    @Override
    public SearchItemChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.tag_scroll_list_item, viewGroup, false);
        return new SearchItemChildViewHolder(view);
    }


    @Override
    public void onBindParentViewHolder(SearchItemParentViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
        SearchItemInfo searchItemInfo = (SearchItemInfo) parentListItem;
        parentViewHolder.parentListItem.setText(searchItemInfo.getTitle());
    }

    @Override
    public void onBindChildViewHolder(SearchItemChildViewHolder searchItemChildViewHolder, int position, Object o) {
        String tagName;
        int ID;
        SearchItemChildInfo searchItemChildInfo = (SearchItemChildInfo) o;
        String imagePreviewPath;
        if (variableInt == 1) {
            SearchItemChildInfo search = (SearchItemChildInfo) list.get(0).getChildItemList().get(position - variableInt);
            tagName = search.getTagsOfPhotoSorted();
            ID = search.getAllIDSorted();


            ArrayList<String> filepaths = g.get(ID);
            imagePreviewPath = filepaths.get(0);

        } else if (variableInt == 2) {
            //tagName = searchItemChildInfo.getDate();
            tagName = searchItemChildInfo.getCollectionsTitle();
            ArrayList<String> arrayOfPaths = searchItemChildInfo.getPathsOfDate();
            imagePreviewPath = arrayOfPaths.get(0);
        } else if (variableInt == 3) {
            tagName = searchItemChildInfo.getAddress();
            ArrayList<String> arrayOfPaths = searchItemChildInfo.getPathsOfAddress();
            imagePreviewPath = arrayOfPaths.get(0);
        } else {
            tagName = "";
            imagePreviewPath = searchItemChildInfo.getFilepathPeople();
        }

        searchItemChildViewHolder.childListText.setText(tagName);
        Glide.with(mContext).load(imagePreviewPath).into(searchItemChildViewHolder.thumbnail);

    }

    private ArrayList<Integer> idArray = new ArrayList<>();
    private ArrayList<String> specificTagsOfEachGeneralTag = new ArrayList<>();
    private Map<String, ArrayList<Integer>> GeneralTagsToSpecificTagsMap;

    private void getGeneralTagsToSpecificTagsMap(String generalTag) {
        idArray = new ArrayList<>();
        specificTagsOfEachGeneralTag = new ArrayList<>();

        idArray = GeneralTagsToSpecificTagsMap.get(generalTag);
        for (int id : idArray) {
            specificTagsOfEachGeneralTag.add(integerStringMap.get(id));
        }
        tagsOfPhotoSorted.addAll(specificTagsOfEachGeneralTag);
        allIDListSorted.addAll(idArray);
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        collapseAllParents();
        if (variableInt == 1) {
            SearchItemInfo search = (SearchItemInfo) list.get(0);
            search.setChildItemList(null);

            tagsOfPhotoSorted.clear();
            allIDListSorted.clear();
            if (charText.length() == 0) {
                tagsOfPhotoSorted.addAll(orignialTagsOfPhoto);
                allIDListSorted.addAll(orignialIDTags);
            } else {

                for (String item : orignialTagsOfPhoto) {
                    if (item.toLowerCase(Locale.getDefault()).contains(" ")) {
                        String[] split = item.split(" ");
                        int noOfWords = split.length;
                        for (int k = 0; k < noOfWords; k++) {
                            if (split[k].toLowerCase(Locale.getDefault()).startsWith(charText)) {
                                tagsOfPhotoSorted.add(item);
                                allIDListSorted.add(stringIntegerMap.get(item));
                            }
                        }
                    } else if (item.toLowerCase(Locale.getDefault()).startsWith(charText)) {
                        tagsOfPhotoSorted.add(item);
                        allIDListSorted.add(stringIntegerMap.get(item));
                    }
                }


                if ("animals".startsWith(charText) || "fauna".startsWith(charText) || "creatures".startsWith(charText) || "wildlife".startsWith(charText) || "zoo".startsWith(charText) || "pets".startsWith(charText)) {
                    getGeneralTagsToSpecificTagsMap("Animals");
                }
                if ("clothes".startsWith(charText) || "accessory".startsWith(charText) || "accessories".startsWith(charText) || "wear".startsWith(charText) || "outfit".startsWith(charText) || "costume".startsWith(charText) || "apparels".startsWith(charText) || "fashion".startsWith(charText)) {
                    getGeneralTagsToSpecificTagsMap("Clothes");
                }
                if ("electronics".startsWith(charText) || "gadgets".startsWith(charText) || "technologies".startsWith(charText) || "technology".startsWith(charText) || "appliances".startsWith(charText) || "tools".startsWith(charText) || "devices".startsWith(charText)) {
                    getGeneralTagsToSpecificTagsMap("Electronics");
                }
                if ("equipments".startsWith(charText) || "gadgets".startsWith(charText) || "tools".startsWith(charText) || "utility".startsWith(charText) || "apparatus".startsWith(charText)) {
                    getGeneralTagsToSpecificTagsMap("Equipment");
                }
                if ("foods".startsWith(charText) || "dinning".startsWith(charText) || "dinner".startsWith(charText) || "lunch".startsWith(charText) || "breakfast".startsWith(charText) || "meal".startsWith(charText) || "snacks".startsWith(charText) || "fruits".startsWith(charText) || "cutlery".startsWith(charText) || "utensils".startsWith(charText) || "plates".startsWith(charText)) {
                    getGeneralTagsToSpecificTagsMap("Food");
                }
                if ("furnitures".startsWith(charText) || "household".startsWith(charText) || "room fittings".startsWith(charText) || "house fittings".startsWith(charText) || "appliances".startsWith(charText) || "chairs".startsWith(charText) || "furnishings".startsWith(charText) || "tables".startsWith(charText)) {
                    getGeneralTagsToSpecificTagsMap("Furniture");
                }
                if ("instruments".startsWith(charText) || "tools".startsWith(charText) || "musical".startsWith(charText)) {
                    getGeneralTagsToSpecificTagsMap("Instruments");
                }
                if ("miscellaneous".startsWith(charText) || "stuff".startsWith(charText) || "others".startsWith(charText)) {
                    getGeneralTagsToSpecificTagsMap("Misc");
                }
                if ("natures".startsWith(charText) || "earth".startsWith(charText) || "mother nature".startsWith(charText) || "flora and fauna".startsWith(charText) || "wildlife".startsWith(charText) || "plants".startsWith(charText) || "flowers".startsWith(charText) || "sceneneries".startsWith(charText) || "scenery".startsWith(charText) || "landscapes".startsWith(charText)) {
                    getGeneralTagsToSpecificTagsMap("Nature");
                }
                if ("vehicles".startsWith(charText) || "automobiles".startsWith(charText) || "cars".startsWith(charText) || "means of transportation".startsWith(charText) || "motors".startsWith(charText)) {
                    getGeneralTagsToSpecificTagsMap("Vehicles");
                }
                if ("places".startsWith(charText) || "locations".startsWith(charText) || "shops".startsWith(charText) || "sites".startsWith(charText)) {
                    getGeneralTagsToSpecificTagsMap("Places");
                }
            }

            ArrayList<SearchItemChildInfo> searchItemChildInfos = new ArrayList<>();
            for (int l = 0; l < tagsOfPhotoSorted.size(); l++) {
                SearchItemChildInfo childInfo = new SearchItemChildInfo(tagsOfPhotoSorted.get(l), allIDListSorted.get(l));
                searchItemChildInfos.add(l, childInfo);
            }
            search.setChildItemList(searchItemChildInfos);
            expandParent(0);

            notifyDataSetChanged();
        }

        if (variableInt == 2) {
            SearchItemInfo search = (SearchItemInfo) list.get(1);
            search.setChildItemList(null);

            dates.clear();
            if (charText.length() == 0) {
                dates.addAll(datesOriginal);
                collectionstitle.addAll(collectionstitleOriginal);
            } else {
                for (String item : datesOriginal) {
                    if (charText.contains(" ")) {
                        String[] split = charText.split(" ");
                        int noOfWords = split.length;
                        for (int k = 0; k < noOfWords; k++) {
                            if (item.toLowerCase(Locale.getDefault()).contains(split[k])) {
                                dates.add(item);
                            }
                        }
                    } else {
                        if (item.toLowerCase(Locale.getDefault()).contains(charText)) {
                            dates.add(item);
                        }
                    }
                }
            }

            ArrayList<SearchItemChildInfo> searchItemChildInfos = new ArrayList<>();
            for (int l = 0; l < dates.size(); l++) {
                SearchItemChildInfo childInfo = new SearchItemChildInfo(dates.get(l), filepathsOfDate.get(dates.get(l)), collectionstitle.get(l));
                searchItemChildInfos.add(l, childInfo);
            }
            search.setChildItemList(searchItemChildInfos);
            expandParent(0);

            notifyDataSetChanged();
        }

        if (variableInt == 3) {
            SearchItemInfo search = (SearchItemInfo) list.get(2);
            search.setChildItemList(null);

            addressesArray.clear();
            if (charText.length() == 0) {
                addressesArray.addAll(addressesArrayOriginal);
            } else {
                for (String item : addressesArrayOriginal) {
                    if (charText.contains(" ")) {
                        String[] split = charText.split(" ");
                        int noOfWords = split.length;
                        for (int k = 0; k < noOfWords; k++) {
                            if (item.toLowerCase(Locale.getDefault()).contains(split[k])) {
                                addressesArray.add(item);
                            }
                        }
                    } else {
                        if (item.toLowerCase(Locale.getDefault()).contains(charText)) {
                            addressesArray.add(item);
                        }
                    }
                }
            }

            ArrayList<SearchItemChildInfo> searchItemChildInfos = new ArrayList<>();
            for (int l = 0; l < addressesArray.size(); l++) {
                SearchItemChildInfo childInfo = new SearchItemChildInfo(addressesArray.get(l), addressToPathMap.get(addressesArray.get(l)));
                searchItemChildInfos.add(l, childInfo);
            }
            search.setChildItemList(searchItemChildInfos);
            expandParent(0);

            notifyDataSetChanged();
        }

        if (variableInt == 4){

        }

    }

    public void resetFilter() {
        SearchItemInfo search0 = (SearchItemInfo) list.get(0);
        search0.setChildItemList(null);
        SearchItemInfo search1 = (SearchItemInfo) list.get(1);
        search1.setChildItemList(null);

        dates.clear();
        dates.addAll(datesOriginal);

        tagsOfPhotoSorted.clear();
        allIDListSorted.clear();
        tagsOfPhotoSorted.addAll(orignialTagsOfPhoto);
        allIDListSorted.addAll(orignialIDTags);

        ArrayList<SearchItemChildInfo> searchItemChildInfos = new ArrayList<>();
        for (int l = 0; l < tagsOfPhotoSorted.size(); l++) {
            SearchItemChildInfo childInfo = new SearchItemChildInfo(tagsOfPhotoSorted.get(l), allIDListSorted.get(l));
            searchItemChildInfos.add(l, childInfo);
        }
        search0.setChildItemList(searchItemChildInfos);

        ArrayList<SearchItemChildInfo> searchItemChildInfos1 = new ArrayList<>();
        for (int l = 0; l < dates.size(); l++) {
            SearchItemChildInfo childInfo = new SearchItemChildInfo(dates.get(l), filepathsOfDate.get(dates.get(l)), collectionstitle.get(l));
            searchItemChildInfos1.add(l, childInfo);
        }
        search1.setChildItemList(searchItemChildInfos1);
    }


    public class SearchItemParentViewHolder extends ParentViewHolder {
        public TextView parentListItem;
        public TextView expandButton;
        public RelativeLayout rippleHolder;

        public SearchItemParentViewHolder(View itemView) {
            super(itemView);
            parentListItem = (TextView) itemView.findViewById(R.id.search_item_title_text);
            expandButton = (TextView) itemView.findViewById(R.id.expand_button);
            rippleHolder = (RelativeLayout) itemView.findViewById(R.id.rippleHolderParent);
            rippleHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isExpanded()) {
                        collapseView();
                        expandButton.setText("MORE");
                        notifyParentItemInserted(0);
                        notifyParentItemInserted(1);
                        notifyParentItemInserted(2);
                        notifyParentItemInserted(3);
                        notifyDataSetChanged();
                        notifyParentItemRemoved(3);
                        searchBox.setText("");
                        resetFilter();
                    } else {
                        //collapseAllParents();
                        expandView();
                        expandButton.setText("LESS");
                    }
                }
            });


        }

        @Override
        public boolean shouldItemViewClickToggleExpansion() {
            return false;
        }
    }

    public class SearchItemChildViewHolder extends ChildViewHolder {
        public TextView childListText;
        public ImageView thumbnail;
        public LinearLayout rippleHolder;


        public SearchItemChildViewHolder(View itemView) {
            super(itemView);
            childListText = (TextView) itemView.findViewById(R.id.tagName);
            thumbnail = (ImageView) itemView.findViewById(R.id.tag_item_background);

            rippleHolder = (LinearLayout) itemView.findViewById(R.id.tag_list_rippleholder);
            rippleHolder.setOnClickListener(onClickListener);
        }

        private Date parsedDate;
        private String dateFormatted;
        private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        private SimpleDateFormat sdfParse = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getPosition() - variableInt;
                if (variableInt == 1) {
                    SearchItemChildInfo search = (SearchItemChildInfo) list.get(0).getChildItemList().get(position);
                    ArrayList<String> f = g.get(search.getAllIDSorted());
                    String tagName = search.getTagsOfPhotoSorted();
                    Intent e = new Intent(mContext, PhotoTag.class);
                    e.putExtra("ArraylistOfPhotos", f);
                    e.putExtra("HeaderTitle", tagName);
                    e.putExtra("RandomNumer", 0);//arrayOfRandomNumbers.get(position));
                    mContext.startActivity(e);
                } else if (variableInt == 2) {
                    SearchItemChildInfo search = (SearchItemChildInfo) list.get(1).getChildItemList().get(position + 1);
                    String dates = search.getDate();
                    try {
                        parsedDate = sdfParse.parse(dates);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dateFormatted = sdf.format(parsedDate);
                    String title = search.getCollectionsTitle();
                    ArrayList<String> tAL = search.getPathsOfDate();
                    Intent e = new Intent(mContext, Collections.class);
                    e.putExtra("CollectionsArrayPos", getPosition());
                    e.putExtra("CollectionDateKey", tAL);
                    e.putExtra("HeaderTitle", title);
                    e.putExtra("Date", dateFormatted);
                    mContext.startActivity(e);
                } else if (variableInt == 3) {
                    SearchItemChildInfo search = (SearchItemChildInfo) list.get(2).getChildItemList().get(position + 2);
                    String address = search.getAddress();
                    ArrayList<String> tAL = search.getPathsOfAddress();
                    Intent e = new Intent(mContext, PhotoTag.class);
                    e.putExtra("ArraylistOfPhotos", tAL);
                    e.putExtra("HeaderTitle", address);
                    e.putExtra("RandomNumer", 0);
                    mContext.startActivity(e);
                } else {
                    Intent e = new Intent(mContext, ImageFullScreen.class);
                    e.putExtra("PhotoPosition", position + 3);
                    e.putExtra("PhotoArrayList", peoplepaths);
                    e.putExtra("IntentFromPhotoTag", false);
                    mContext.startActivity(e);
                }
            }
        };
    }
}
