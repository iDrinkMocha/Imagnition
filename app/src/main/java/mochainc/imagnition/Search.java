package mochainc.imagnition;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.dragselectrecyclerview.DragSelectRecyclerViewAdapter;
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.google.android.gms.drive.metadata.SearchableCollectionMetadataField;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Search extends AppCompatActivity implements GalleryListAdapter.ClickListener, DragSelectRecyclerViewAdapter.SelectionListener {

    private RecyclerView tagsSearchList;
    private TagScrollListAdapter mTagScrollListAdapter;
    private IDtoPath iDtoPath = new IDtoPath();
    private Map<Integer, ArrayList<String>> e = new TreeMap<Integer, ArrayList<String>>();
    private ArrayList<String> tagsOfPhoto;
    private IDConversion idConversion;
    private static Toolbar toolbar;
    private Window window;
    private Map<String, Integer> stringIntegerMap = new TreeMap<>();
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<ParentListItem> list;
    private SearchExpandableListAdapter searchExpandableListAdapter;
    private GalleryListAdapter galleryListAdapter;
    private EditText SearchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Slidr.attach(this);
        toolbar = (Toolbar) findViewById(R.id.search_app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.light));
        }

        InferCollectionEvent inferCollectionEvent = new InferCollectionEvent();
        inferCollectionEvent.setPreferences(this);

        e = iDtoPath.getIDtoPath();
        Set<Integer> f = e.keySet();
        ArrayList<String> peoplepaths = e.get(1861);
        ArrayList<Integer> allIDList = new ArrayList<>();
        allIDList.addAll(f);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        idConversion = new IDConversion(this);
        tagsOfPhoto = idConversion.readSpecificTags(allIDList);
        //tagsOfPhoto = idConversion.readGeneralTags(allIDList);

        for (int i = 0; i < tagsOfPhoto.size(); i++) {
            stringIntegerMap.put(tagsOfPhoto.get(i), allIDList.get(i));
        }
        Set<String> g = stringIntegerMap.keySet();
        ArrayList<String> tagsOfPhotoSorted = new ArrayList<>();
        tagsOfPhotoSorted.addAll(g);

        ArrayList<Integer> allIDListSorted = new ArrayList<>();
        for (int k = 0; k < stringIntegerMap.size(); k++) {
            String key = tagsOfPhotoSorted.get(k);
            allIDListSorted.add(k, stringIntegerMap.get(key));
        }

        CollectionsData collectionsData = new CollectionsData();

        if (collectionsData.getImageGroupKeysStatic() == null) {
            collectionsData.getPreferences(preferences);
        }
        if (collectionsData.getImageGroupKeysStaticAll() == null) {
            collectionsData.getPreferencesAll(preferences);
        }

        AddressToPath addressToPath = new AddressToPath();

        if(addressToPath.getAddressToPath().size()==0){
            System.out.println("Prefs");
            addressToPath.getPreferences(preferences);
        }


        String[] listOfTitle = {"Tags", "Collections", "Places", "People"};
        ArrayList<ParentListItem> parentObjects = new ArrayList<>();
        for (String aListOfTitle : listOfTitle) {
            SearchItemInfo item = new SearchItemInfo();

            ArrayList<SearchItemChildInfo> searchItemChildInfos = new ArrayList<>();
            if (aListOfTitle.contains("Tags")) {
                for (int l = 0; l < tagsOfPhotoSorted.size(); l++) {

                    SearchItemChildInfo childInfo = new SearchItemChildInfo(tagsOfPhotoSorted.get(l), allIDListSorted.get(l));
                    searchItemChildInfos.add(l, childInfo);
                }
            } else if(aListOfTitle.contains("Collections")){
                for (int l = 0; l < collectionsData.getImageGroupKeysStatic().size(); l++) {
                    SearchItemChildInfo childInfo = new SearchItemChildInfo(collectionsData.getImageGroupKeysStatic().get(l), collectionsData.getRetrieveCollectionStatic().get(collectionsData.getImageGroupKeysStatic().get(l)), inferCollectionEvent.getCollectionsTitleArray().get(l));
                    searchItemChildInfos.add(l, childInfo);
                }
            }
            else if(aListOfTitle.contains("Places")){
                for(int l = 0; l < addressToPath.getAddressToPath().size(); l++){
                    SearchItemChildInfo childInfo = new SearchItemChildInfo(addressToPath.getAddressKey().get(l), addressToPath.getAddressToPath().get(addressToPath.getAddressKey().get(l)));
                    searchItemChildInfos.add(l, childInfo);
                }
            }
            else if(aListOfTitle.contains("People")){
                for(int l = 0; l < peoplepaths.size(); l++){
                    SearchItemChildInfo childInfo = new SearchItemChildInfo(peoplepaths.get(l));
                    searchItemChildInfos.add(l, childInfo);
                }
            }
            item.setTitle(aListOfTitle);
            item.setChildItemList(searchItemChildInfos);
            parentObjects.add(item);
        }

        list = parentObjects;
        SearchBox = (EditText) toolbar.findViewById(R.id.SearchBox);
        tagsSearchList = (RecyclerView) findViewById(R.id.tagsSearch);
        final GridLayoutManager mStaggeredLayoutManager = new GridLayoutManager(this, 3);
        searchExpandableListAdapter = new SearchExpandableListAdapter(this, list, tagsOfPhotoSorted, allIDListSorted, e, mStaggeredLayoutManager, SearchBox, peoplepaths);
        tagsSearchList.setLayoutManager(mStaggeredLayoutManager);
        tagsSearchList.setAdapter(searchExpandableListAdapter);

        GalleryImageData mGalleryImageData = new GalleryImageData();
        AllImagesPathArray allImagesPathArray = new AllImagesPathArray();
        mGalleryImageData.setImagePathArray(allImagesPathArray.getImagePathArraystatic());
        final GridLayoutManager mStaggeredLayoutManager2 = new GridLayoutManager(this, 3);

        galleryListAdapter = new GalleryListAdapter(this);
        galleryListAdapter.GalleryListAdapterPassVariables(this, mGalleryImageData, false, "", 2, 0);


        final TextView resultsheader = (TextView) findViewById(R.id.showingresultsheader);
        SearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String a = SearchBox.getText().toString().toLowerCase(Locale.getDefault());
                if (searchExpandableListAdapter.variableInt == -1) {
                    if (a.length() > 0) {
                        resultsheader.setVisibility(View.VISIBLE);
                        resultsheader.setText("Showing all related photos of \"" + a +'\"');
                        tagsSearchList.setAdapter(galleryListAdapter);
                        tagsSearchList.setLayoutManager(mStaggeredLayoutManager2);
                        galleryListAdapter.filter(a);
                    } else {
                        resultsheader.setVisibility(View.GONE);
                        tagsSearchList.setAdapter(searchExpandableListAdapter);
                        tagsSearchList.setLayoutManager(mStaggeredLayoutManager);
                    }
                } else {
                    searchExpandableListAdapter.filter(a);
                }
            }
        });

        SearchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(SearchBox!=null){
            SearchBox.setText("");
        }
        super.onBackPressed();
    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        searchExpandableListAdapter.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        searchExpandableListAdapter.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int index) {

    }

    @Override
    public void onLongClick(int index) {

    }

    @Override
    public void onDragSelectionChanged(int count) {

    }
}
