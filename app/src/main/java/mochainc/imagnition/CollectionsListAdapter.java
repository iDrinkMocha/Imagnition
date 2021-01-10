package mochainc.imagnition;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class CollectionsListAdapter extends RecyclerView.Adapter<CollectionsListAdapter.ViewHolder> {

    Context mContext;
    //public PsuedoCollections collectionsImageData;
    private ArrayList<String> DateGroupArray;
    private ArrayList<String> collectionsDayArray;
    private ArrayList<String> collectionsDayArrayInitialise;
    private Map<String, ArrayList<String>> mappedPath;
    private String dates;
    private String dateInitialise;
    private SharedPreferences prefs;
    private String dateFormatted;
    private String Day;
    private Date parsedDate;
    private Date parsedDate2;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
    private SimpleDateFormat sdfParse = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private SimpleDateFormat sdfDayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH);
    private IDConversion idConversion;
    private ArrayList<Integer> arrayOfIDPerPhoto;
    private ArrayList<Integer> arrayOfIDOfAllPhotosPerCollection;
    private ArrayList<ArrayList<Integer>> arrayOfIDOfAllPhotosInDays = new ArrayList<>();
    private InferCollectionEvent inferCollectionEvent = new InferCollectionEvent();
    private ArrayList<String> dayString = new ArrayList<>();
    private ArrayList<String> collectionsTitleArray = new ArrayList<>();


    public CollectionsListAdapter(Context context, Map<String, ArrayList<String>> mappedPath, ArrayList<String> DateGroupArray) {
        this.mContext = context;
        //collectionsImageData = mCollectionsImageData;
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        //mappedPath = collectionsImageData.getMap();
        this.mappedPath = mappedPath;
        this.DateGroupArray = DateGroupArray;
        this.idConversion = new IDConversion(mContext);

        /*for(int i=0; i<DateGroupArray.size(); i++) {
            dateInitialise = DateGroupArray.get(i);
            collectionsDayArrayInitialise = mappedPath.get(dateInitialise);
            arrayOfIDOfAllPhotosPerCollection = new ArrayList<>();

            try {
                parsedDate2 = sdfParse.parse(dateInitialise);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Day = sdfDayOfWeek.format(parsedDate2);
            for(String filePath: collectionsDayArrayInitialise){
                arrayOfIDPerPhoto = new ArrayList<>();
                arrayOfIDPerPhoto = idConversion.getID(filePath);
                arrayOfIDOfAllPhotosPerCollection.addAll(arrayOfIDPerPhoto);
            }
            arrayOfIDOfAllPhotosPerCollection = removeDuplicates(arrayOfIDOfAllPhotosPerCollection);
            arrayOfIDOfAllPhotosInDays.add(arrayOfIDOfAllPhotosPerCollection);
            dayString.add(Day);
        }

        collectionsTitleArray = inferCollectionEvent.inferEvent(arrayOfIDOfAllPhotosInDays, dayString);*/
        this.collectionsTitleArray = inferCollectionEvent.getCollectionsTitleArray();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collections_cardview, parent, false);
        return new ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //DateGroupArray = collectionsImageData.getImageGroupKeys();
        dates = DateGroupArray.get(position);
        collectionsDayArray = mappedPath.get(dates);
        try {
            parsedDate = sdfParse.parse(dates);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dateFormatted = sdf.format(parsedDate);

        String mImagePath1;
        String mImagePath2;
        String mImagePath3;

        if (collectionsDayArray != null) {
            //mImagePath1 = collectionsDayArray.get(0);
            //Glide.with(mContext).load(mImagePath1).into(holder.photo1);


            if (collectionsDayArray.size() >= 3) {
                mImagePath1 = collectionsDayArray.get(0);
                mImagePath2 = collectionsDayArray.get(1);
                mImagePath3 = collectionsDayArray.get(2);

                Glide.with(mContext).load(mImagePath1).into(holder.photo1);
                Glide.with(mContext).load(mImagePath2).into(holder.photo2);
                Glide.with(mContext).load(mImagePath3).into(holder.photo3);
            } else if (collectionsDayArray.size() == 2) {
                mImagePath1 = collectionsDayArray.get(0);
                mImagePath2 = collectionsDayArray.get(1);

                Glide.with(mContext).load(mImagePath1).into(holder.photo1);
                Glide.with(mContext).load(mImagePath2).into(holder.photo2);
            } else if (collectionsDayArray.size() == 1) {
                mImagePath1 = collectionsDayArray.get(0);
                Glide.with(mContext).load(mImagePath1).into(holder.photo1);
            }
        }

        String collectionsTitle = collectionsTitleArray.get(position);
        holder.collectionsDate.setText(dateFormatted);
        holder.collectionsTitle.setText(collectionsTitle);

    }

    @Override
    public int getItemCount() {
        //int numberOfItems = collectionsImageData.getImageGroupKeys().size();
        int numberOfItems = DateGroupArray.size();
        System.out.println(numberOfItems);
        return numberOfItems;
    }

    /*public void updateData() {
        mappedPath.clear();
        mappedPath = collectionsImageData.getMap();
        notifyDataSetChanged();
    }*/


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout rippleHolder;
        public ImageView photo1;
        public ImageView photo2;
        public ImageView photo3;
        public TextView collectionsTitle;
        public TextView collectionsDate;

        public ViewHolder(View itemView) {
            super(itemView);
            rippleHolder = (LinearLayout) itemView.findViewById(R.id.collections_rippleHolder);
            rippleHolder.setOnClickListener(this);

            photo1 = (ImageView) itemView.findViewById(R.id.collections_image1);
            photo2 = (ImageView) itemView.findViewById(R.id.collections_image2);
            photo3 = (ImageView) itemView.findViewById(R.id.collections_image3);
            collectionsTitle = (TextView) itemView.findViewById(R.id.collections_title);
            collectionsDate = (TextView) itemView.findViewById(R.id.collections_date);

        }

        @Override
        public void onClick(View v) {
            dates = DateGroupArray.get(getPosition());
            collectionsDayArray = mappedPath.get(dates);
            try {
                parsedDate = sdfParse.parse(dates);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dateFormatted = sdf.format(parsedDate);
            String collectionsTitle = collectionsTitleArray.get(getPosition());
            ArrayList<String> tAL = mappedPath.get(dates);
            Intent e = new Intent(mContext, Collections.class);
            e.putExtra("CollectionsArrayPos", getPosition());
            e.putExtra("CollectionDateKey", tAL);
            e.putExtra("HeaderTitle", collectionsTitle);
            e.putExtra("Date", dateFormatted);
            mContext.startActivity(e);
        }

    }


}