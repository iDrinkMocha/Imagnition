package mochainc.imagnition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.afollestad.dragselectrecyclerview.DragSelectRecyclerViewAdapter;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;

import java.util.ArrayList;

public class PhotoTag extends AppCompatActivity  implements
        GalleryListAdapter.ClickListener, DragSelectRecyclerViewAdapter.SelectionListener{

    private static Toolbar toolbar;
    private ArrayList<String> arraylistOfPhotos = new ArrayList<>();
    private GalleryListAdapter mGalleryAdapter;
    private String headerTitle;
    private RecyclerView recyclerView;
    private SharedPreferences prefs;
    private int span = 1;
    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_tag);
        int secondary = Color.parseColor("#00000000");
        int primary = getResources().getColor(R.color.light);
        Slidr.attach(this, primary, secondary);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");


        Bundle photoPositionBundle = getIntent().getExtras();
        arraylistOfPhotos = photoPositionBundle.getStringArrayList("ArraylistOfPhotos");
        headerTitle = photoPositionBundle.getString("HeaderTitle");
        int i = photoPositionBundle.getInt("RandomNumer");


        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        relativeLayout = (RelativeLayout) findViewById(R.id.photoTagContainerSheet);


        GalleryImageData mGalleryImageData = new GalleryImageData();
        mGalleryImageData.setImagePathArray(arraylistOfPhotos);


        recyclerView = (RecyclerView) findViewById(R.id.photoTagReyclerView);


        if(arraylistOfPhotos.size()>=24) {
            span = 4;
        }
        else if(arraylistOfPhotos.size()>=16){
            span = 3;
        }
        else if(arraylistOfPhotos.size()>=8){
            span = 2;
        }
        else{
            span = 1;
        }
        GridLayoutManager mStaggeredLayoutManager = new GridLayoutManager(this, span);
        mStaggeredLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return span;
                } else {
                    return 1;
                }
            }
        });
        recyclerView.setLayoutManager(mStaggeredLayoutManager);
        mGalleryAdapter = new GalleryListAdapter(this);
        mGalleryAdapter.GalleryListAdapterPassVariables(this, mGalleryImageData, true, headerTitle, 4, i);
        recyclerView.setAdapter(mGalleryAdapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels, 2, true));
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
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
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
    protected void onPause() {
        super.onPause();
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


