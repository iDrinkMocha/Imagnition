package mochainc.imagnition;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * Created by ZSJTraits on 21/6/16.
 */
public class TagScrollListAdapter extends RecyclerView.Adapter<TagScrollListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> tagsOfPhoto;
    private Map<Integer, ArrayList<String>> g;
    private ArrayList<Integer> idTags;
    private Random r = new Random();
    private int i;
    private boolean fromImageFullScreen;
    private ArrayList<Integer> arrayOfRandomNumbers = new ArrayList<>();
    private ArrayList<String> orignialTagsOfPhoto = new ArrayList<>();
    private ArrayList<Integer> orignialIDTags = new ArrayList<>();
    private Map<String, Integer> stringIntegerMap = new HashMap<>();
    private Map<Integer, String> integerStringMap = new HashMap<>();
    private IDGeneral idGeneral = new IDGeneral();
    private ArrayList<String> generalString;
    private Map<String, ArrayList<Integer>> GeneralTagsToSpecificTagsMap;
    private TextView headerTextView;

    public TagScrollListAdapter(Context context, ArrayList<String> tags, RecyclerView tagScrollList, Map<Integer, ArrayList<String>> e, ArrayList<Integer> idTags, boolean fromImageFullScreen, TextView textView) {
        headerTextView = textView;
        constructor(context, tags,tagScrollList,e, idTags,fromImageFullScreen);
    }

    public TagScrollListAdapter(Context context, ArrayList<String> tags, RecyclerView tagScrollList, Map<Integer, ArrayList<String>> e, ArrayList<Integer> idTags, boolean fromImageFullScreen) {
        constructor(context, tags,tagScrollList,e, idTags,fromImageFullScreen);
    }

    private void constructor(Context context, ArrayList<String> tags, RecyclerView tagScrollList, Map<Integer, ArrayList<String>> e, ArrayList<Integer> idTags, boolean fromImageFullScreen){
        mContext = context;
        tagsOfPhoto = tags;
        orignialTagsOfPhoto.addAll(tagsOfPhoto);
        this.g = e;
        this.idTags = idTags;
        orignialIDTags.addAll(this.idTags);
        this.fromImageFullScreen = fromImageFullScreen;

        for (int k = 0; k < tagsOfPhoto.size(); k++) {
            stringIntegerMap.put(tagsOfPhoto.get(k), idTags.get(k));
            integerStringMap.put(idTags.get(k), tagsOfPhoto.get(k));
        }

        generalString = idGeneral.getGeneralStringArray();
        GeneralTagsToSpecificTagsMap = idGeneral.GetGeneralTagsToSpecificTagsMap(idTags);

        if (tagsOfPhoto == null | tagsOfPhoto.contains("NULL")) {
            tagScrollList.setVisibility(View.GONE);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_scroll_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tagName.setText(tagsOfPhoto.get(position));
        if (idTags != null) {
            ArrayList<String> tagList = g.get(idTags.get(position));
            if (tagList != null) {
                int tagListSize = tagList.size();
                i = r.nextInt(tagListSize);
                if (position >= arrayOfRandomNumbers.size()) {
                    arrayOfRandomNumbers.add(position, i);
                } else {
                    arrayOfRandomNumbers.set(position, i);
                }
                String imagePreviewPath = tagList.get(i);
                Glide.with(mContext).load(imagePreviewPath).into(holder.tagItemBackground);
            }
        }
    }

    @Override
    public int getItemCount() {
        return tagsOfPhoto.size();
    }


    public void updateData(ArrayList<String> newTags, ArrayList<Integer> newidTags) {
        tagsOfPhoto.clear();
        tagsOfPhoto.addAll(newTags);
        idTags.clear();
        idTags.addAll(newidTags);
        notifyDataSetChanged();
    }

    private ArrayList<String> specificTagsOfEachGeneralTag = new ArrayList<>();

    private void getGeneralTagsToSpecificTagsMap(String generalTag) {
        idArray = new ArrayList<>();
        specificTagsOfEachGeneralTag = new ArrayList<>();

        idArray = GeneralTagsToSpecificTagsMap.get(generalTag);
        for (int id : idArray) {
            specificTagsOfEachGeneralTag.add(integerStringMap.get(id));
        }
        tagsOfPhoto.addAll(specificTagsOfEachGeneralTag);
        idTags.addAll(idArray);
    }

    private ArrayList<Integer> idArray = new ArrayList<>();
    private ArrayList<String> tag = new ArrayList<>();

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        tagsOfPhoto.clear();
        idTags.clear();
        if (charText.length() == 0) {
            tagsOfPhoto.addAll(orignialTagsOfPhoto);
            idTags.addAll(orignialIDTags);
        } else {

            for (String item : orignialTagsOfPhoto) {
                if(item.toLowerCase(Locale.getDefault()).contains(" ")){
                    String[] split = item.split(" ");
                    int noOfWords = split.length;
                    for(int k=0; k<noOfWords; k++){
                        if(split[k].toLowerCase(Locale.getDefault()).startsWith(charText)){
                            tagsOfPhoto.add(item);
                            idTags.add(stringIntegerMap.get(item));
                        }
                    }
                }
                else if (item.toLowerCase(Locale.getDefault()).startsWith(charText)) {
                    tagsOfPhoto.add(item);
                    idTags.add(stringIntegerMap.get(item));
                }
            }


            if ("animals".startsWith(charText) || "fauna".startsWith(charText)|| "creatures".startsWith(charText)|| "wildlife".startsWith(charText) || "zoo".startsWith(charText) || "pets".startsWith(charText) ) {
                getGeneralTagsToSpecificTagsMap("Animals");
            }
            if ("clothes".startsWith(charText)  || "accessory".startsWith(charText)|| "accessories".startsWith(charText) || "wear".startsWith(charText) || "outfit".startsWith(charText) || "costume".startsWith(charText)  || "apparels".startsWith(charText)  || "fashion".startsWith(charText) ) {
                getGeneralTagsToSpecificTagsMap("Clothes");
            }
            if ("electronics".startsWith(charText)|| "gadgets".startsWith(charText) || "technologies".startsWith(charText)|| "technology".startsWith(charText) || "appliances".startsWith(charText) || "tools".startsWith(charText)|| "devices".startsWith(charText)) {
                getGeneralTagsToSpecificTagsMap("Electronics");
            }
            if ("equipments".startsWith(charText)|| "gadgets".startsWith(charText)|| "tools".startsWith(charText) || "utility".startsWith(charText)  || "apparatus".startsWith(charText) ) {
                getGeneralTagsToSpecificTagsMap("Equipment");
            }
            if ("foods".startsWith(charText) || "dinning".startsWith(charText) || "dinner".startsWith(charText) || "lunch".startsWith(charText) || "breakfast".startsWith(charText) || "meal".startsWith(charText)  || "snacks".startsWith(charText) || "fruits".startsWith(charText) || "cutlery".startsWith(charText) || "utensils".startsWith(charText) || "plates".startsWith(charText) ) {
                getGeneralTagsToSpecificTagsMap("Food");
            }
            if ("furnitures".startsWith(charText)|| "household".startsWith(charText) || "room fittings".startsWith(charText) || "house fittings".startsWith(charText) || "appliances".startsWith(charText) ||  "chairs".startsWith(charText) || "furnishings".startsWith(charText) || "tables".startsWith(charText) ) {
                getGeneralTagsToSpecificTagsMap("Furniture");
            }
            if ("instruments".startsWith(charText) || "tools".startsWith(charText) || "musical".startsWith(charText)) {
                getGeneralTagsToSpecificTagsMap("Instruments");
            }
            if ("miscellaneous".startsWith(charText) || "stuff".startsWith(charText) || "others".startsWith(charText)) {
                getGeneralTagsToSpecificTagsMap("Misc");
            }
            if ("natures".startsWith(charText)|| "earth".startsWith(charText)|| "mother nature".startsWith(charText) || "flora and fauna".startsWith(charText) || "wildlife".startsWith(charText) || "plants".startsWith(charText) || "flowers".startsWith(charText) || "sceneneries".startsWith(charText)|| "scenery".startsWith(charText) || "landscapes".startsWith(charText)) {
                getGeneralTagsToSpecificTagsMap("Nature");
            }
            if ("vehicles".startsWith(charText) || "automobiles".startsWith(charText) || "cars".startsWith(charText) || "means of transportation".startsWith(charText) || "motors".startsWith(charText)) {
                getGeneralTagsToSpecificTagsMap("Vehicles");
            }
            if ("places".startsWith(charText) || "locations".startsWith(charText) || "shops".startsWith(charText) || "sites".startsWith(charText)) {
                getGeneralTagsToSpecificTagsMap("Places");
            }
        }
        notifyDataSetChanged();

        if(idTags.size()==0 && headerTextView!=null){
            headerTextView.setText("No results for \""+ charText +"\"" + "\n\nTry searching with a more general keyword");
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout rippleHolder;
        public TextView tagName;
        public ImageView tagItemBackground;

        public ViewHolder(View itemView) {
            super(itemView);

            tagName = (TextView) itemView.findViewById(R.id.tagName);
            tagItemBackground = (ImageView) itemView.findViewById(R.id.tag_item_background);

            rippleHolder = (LinearLayout) itemView.findViewById(R.id.tag_list_rippleholder);
            rippleHolder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getPosition();
            ArrayList<String> f = g.get(idTags.get(position));
            String tagName = tagsOfPhoto.get(position);
            Intent e = new Intent(mContext, PhotoTag.class);
            e.putExtra("ArraylistOfPhotos", f);
            e.putExtra("HeaderTitle", tagName);
            e.putExtra("RandomNumer", arrayOfRandomNumbers.get(position));

            if (fromImageFullScreen) {
                ((ImageFullScreen) mContext).finish();
            }
            mContext.startActivity(e);
        }

    }
}
