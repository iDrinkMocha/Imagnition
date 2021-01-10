package mochainc.imagnition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.afollestad.dragselectrecyclerview.DragSelectRecyclerViewAdapter;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.widget.SliderPanel;

import java.util.ArrayList;

public class Collections extends AppCompatActivity implements
        GalleryListAdapter.ClickListener, DragSelectRecyclerViewAdapter.SelectionListener {

    private int collectionsPos;
    private static Toolbar toolbar;
    private ArrayList<String> collectionDateKey = new ArrayList<>();
    private GalleryListAdapter mGalleryAdapter;
    private String headerTitle;
    private RecyclerView collectionsRecylerview;
    private SharedPreferences prefs;
    private String date;
    private RelativeLayout relativeLayout;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);
        int secondary = Color.parseColor("#00000000");
        int primary = getResources().getColor(R.color.light);
        Slidr.attach(this, primary, secondary);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        Bundle photoPositionBundle = getIntent().getExtras();
        collectionsPos = photoPositionBundle.getInt("CollectionsArrayPos");
        collectionDateKey = photoPositionBundle.getStringArrayList("CollectionDateKey");
        date = photoPositionBundle.getString("Date");
        headerTitle = photoPositionBundle.getString("HeaderTitle");

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        relativeLayout = (RelativeLayout) findViewById(R.id.collectionsContainerSheet);

        GalleryImageData mGalleryImageData = new GalleryImageData();
        mGalleryImageData.setImagePathArray(collectionDateKey);


        collectionsRecylerview = (RecyclerView) findViewById(R.id.collectionsActivityList);
        GridLayoutManager mStaggeredLayoutManager = new GridLayoutManager(this, 3);
        mStaggeredLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 3;
                } else if (position % 3 == 0) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        collectionsRecylerview.setLayoutManager(mStaggeredLayoutManager);
        mGalleryAdapter = new GalleryListAdapter(this);
        mGalleryAdapter.GalleryListAdapterPassVariables(this, mGalleryImageData, true, headerTitle, 2, 0, date);
        collectionsRecylerview.setAdapter(mGalleryAdapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        collectionsRecylerview.addItemDecoration(new SpacesItemDecoration(spacingInPixels, 2, true));

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (collectionsRecylerview != null) {
            int currentTab = prefs.getInt("currentPhotoTab", 0);
            if ((currentTab - 4) >= 0) {
                collectionsRecylerview.scrollToPosition(currentTab - 4);
            } else {
                collectionsRecylerview.scrollToPosition(currentTab - 4);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        prefs.edit().putInt("currentPhotoTab", 0).apply();
    }

    @Override
    public void onBackPressed() {
        relativeLayout.animate().translationX(1500).setDuration(250).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                finish();
            }
        }).setInterpolator(new AccelerateDecelerateInterpolator()).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                relativeLayout.animate().translationX(1500).setDuration(250).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        finish();
                    }
                }).setInterpolator(new AccelerateDecelerateInterpolator()).start();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int index) {
        // Single click will select or deselect an item
        if (mGalleryAdapter != null) {
            //mGalleryAdapter.toggleSelected(index);
        }
    }

    @Override
    public void onLongClick(int index) {
        if (collectionsRecylerview != null) {
            //collectionsRecylerview.setDragSelectActive(true, index);
        }
    }

    @Override
    public void onDragSelectionChanged(int count) {

    }
}