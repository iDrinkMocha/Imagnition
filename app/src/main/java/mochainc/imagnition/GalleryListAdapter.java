package mochainc.imagnition;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.dragselectrecyclerview.DragSelectRecyclerViewAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.data.StreamAssetPathFetcher;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;


public class GalleryListAdapter extends DragSelectRecyclerViewAdapter<GalleryListAdapter.ViewHolder> {

    Context mContext;
    public GalleryImageData galleryImageData;
    AdapterView.OnItemClickListener mItemClickListener;
    private ArrayList<String> imagePathArray = new ArrayList<>();
    private boolean haveHeader;
    private String headerTitle;
    private int span;
    private int i;
    private ArrayList<String> imagePathArrayOriginal = new ArrayList<>();
    private IDtoPath iDtoPath = new IDtoPath();
    private IDConversion idConversion;
    private Map<Integer, ArrayList<String>> mapOfIDToPath;
    private ArrayList<Integer> IDArray = new ArrayList<>();
    private ArrayList<String> TagArray = new ArrayList<>();
    private IDGeneral idGeneral = new IDGeneral();
    private ArrayList<String> generalString;
    private Map<String, ArrayList<Integer>> GeneralTagsToSpecificTagsMap;
    private CollectionsData collectionsData = new CollectionsData();
    private ArrayList<String> dates;
    private Map<String, ArrayList<String>> photosOnDate;
    private AddressToPath addressToPath = new AddressToPath();
    private ArrayList<String> addressesArray;
    private Map<String, ArrayList<String>> addressToPathMap;
    private ArrayList<String> addressesArrayOriginal;
    private String dateFormatted;


    public interface ClickListener {
        void onClick(int index);

        void onLongClick(int index);
    }

    private final ClickListener mCallback;

    protected GalleryListAdapter(ClickListener callback) {
        super();
        mCallback = callback;
    }

    public void GalleryListAdapterPassVariables(Context context, GalleryImageData mGalleryImageData, boolean haveHeader, String headerTitle, int span, int i) {
        this.mContext = context;
        galleryImageData = mGalleryImageData;
        this.haveHeader = haveHeader;
        this.headerTitle = headerTitle;
        this.span = span;

        imagePathArray = galleryImageData.getImagesPath();
        imagePathArrayOriginal.addAll(imagePathArray);

        idConversion = new IDConversion(mContext);
        mapOfIDToPath = iDtoPath.getIDtoPath();
        Set<Integer> IDSet = mapOfIDToPath.keySet();
        IDArray.addAll(IDSet);
        TagArray = idConversion.readSpecificTags(IDArray);

        generalString = idGeneral.getGeneralStringArray();
        GeneralTagsToSpecificTagsMap = idGeneral.GetGeneralTagsToSpecificTagsMap(IDArray);

        dates = collectionsData.getImageGroupKeysStaticAll();
        photosOnDate = collectionsData.getRetrieveCollectionStaticAll();


        addressesArray = addressToPath.getAddressKey();
        addressToPathMap = addressToPath.getAddressToPath();
        this.i = i;
    }
    public void GalleryListAdapterPassVariables(Context context, GalleryImageData mGalleryImageData, boolean haveHeader, String headerTitle, int span, int i, String dateFormatted) {
        this.mContext = context;
        galleryImageData = mGalleryImageData;
        this.haveHeader = haveHeader;
        this.headerTitle = headerTitle;
        this.span = span;

        imagePathArray = galleryImageData.getImagesPath();
        imagePathArrayOriginal.addAll(imagePathArray);

        idConversion = new IDConversion(mContext);
        mapOfIDToPath = iDtoPath.getIDtoPath();
        Set<Integer> IDSet = mapOfIDToPath.keySet();
        IDArray.addAll(IDSet);
        TagArray = idConversion.readSpecificTags(IDArray);

        generalString = idGeneral.getGeneralStringArray();
        GeneralTagsToSpecificTagsMap = idGeneral.GetGeneralTagsToSpecificTagsMap(IDArray);

        dates = collectionsData.getImageGroupKeysStaticAll();
        photosOnDate = collectionsData.getRetrieveCollectionStaticAll();


        addressesArray = addressToPath.getAddressKey();
        addressToPathMap = addressToPath.getAddressToPath();
        this.i = i;
        this.dateFormatted = dateFormatted;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (haveHeader) {
            if (span == 4 && position == 0) {
                holder.headerText.setVisibility(View.VISIBLE);
                holder.headerText.setText(headerTitle);
                holder.subHeader.setText("");
                holder.subHeader.setVisibility(View.GONE);
                String mImagePath = imagePathArray.get(i);
                Glide.with(mContext).load(mImagePath).into(holder.photo);
                holder.photo.animate().scaleY(1.3f).scaleX(1.3f).setDuration(5000).start();
                holder.LongPressHolder.setBackgroundColor(Color.parseColor("#000000"));
                holder.LongPressHolder.setAlpha(0.5f);
            } else if (position == 0) {
                holder.headerText.setVisibility(View.VISIBLE);
                holder.headerText.setText(headerTitle);
                holder.subHeader.setVisibility(View.VISIBLE);
                holder.subHeader.setText(dateFormatted);
                String mImagePath = imagePathArray.get(0);
                Glide.with(mContext).load(mImagePath).into(holder.photo);
                holder.photo.animate().scaleY(1.3f).scaleX(1.3f).setDuration(5000).start();
                holder.LongPressHolder.setBackgroundColor(Color.parseColor("#000000"));
                holder.LongPressHolder.setAlpha(0.5f);
            } else {
                String mImagePath = imagePathArray.get(position - 1);
                Glide.with(mContext).load(mImagePath).into(holder.photo);
                holder.headerText.setText("");
                holder.subHeader.setText("");
                holder.headerText.setVisibility(View.GONE);
                holder.subHeader.setVisibility(View.GONE);
                holder.LongPressHolder.setBackgroundColor(Color.parseColor("#00000000"));
                holder.LongPressHolder.setAlpha(1);
            }
        } else {
            holder.headerText.setVisibility(View.GONE);
            String mImagePath = imagePathArray.get(position);
            Glide.with(mContext).load(mImagePath).into(holder.photo);
        }

        if (isIndexSelected(position) && span == 3) {
            // Item is selected, change it somehow
            holder.LongPressHolder.setBackgroundColor(Color.parseColor("#80FFFFFF"));
            holder.smoothCheckBox.setVisibility(View.VISIBLE);
            holder.smoothCheckBox.setChecked(true, true);
            ;
        } else if (!isIndexSelected(position) && span == 3) {
            // Item is not selected, reset it to a non-selected state
            holder.LongPressHolder.setBackgroundColor(Color.parseColor("#00000000"));
            holder.smoothCheckBox.setVisibility(View.GONE);
            holder.smoothCheckBox.setChecked(false);

        }
    }

    @Override
    protected boolean isIndexSelectable(int index) {
        // This method is OPTIONAL, returning false will prevent the item at the specified index from being selected.
        // Both initial selection, and drag selection.
        return true;
    }


    @Override
    public int getItemCount() {
        if (haveHeader) {
            int numberOfItems = galleryImageData.getImagesPath().size() + 1;
            return numberOfItems;
        } else {
            int numberOfItems = galleryImageData.getImagesPath().size();
            return numberOfItems;
        }
    }

    public void updateData(ArrayList<String> imageFilePathArray) {
        imagePathArray.clear();
        imagePathArray.addAll(imageFilePathArray);
        notifyDataSetChanged();
    }

    private ArrayList<String> removeDuplicates(ArrayList<String> list) {

        // Store unique items in result.
        ArrayList<String> result = new ArrayList<>();

        // Record encountered Strings in HashSet.
        HashSet<String> set = new HashSet<>();

        // Loop over argument list.
        for (String item : list) {

            // If String is not in set, add it to the list and the set.
            if (!set.contains(item)) {
                result.add(item);
                set.add(item);
            }
        }
        return result;
    }

    private ArrayList<Integer> idArraytemp = new ArrayList<>();
    private ArrayList<String> specificTagsOfEachGeneralTag = new ArrayList<>();
    private ArrayList<String> imagePathArray2 = new ArrayList<>();

    private void getGeneralTagsToSpecificTagsMap(String generalTag) {
        idArraytemp = new ArrayList<>();
        specificTagsOfEachGeneralTag = new ArrayList<>();

        idArraytemp = GeneralTagsToSpecificTagsMap.get(generalTag);
        for (int id : idArraytemp) {
            imagePathArray2.addAll(mapOfIDToPath.get(id));
        }


    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        imagePathArray.clear();
        imagePathArray2.clear();
        if (charText.length() == 0) {
            imagePathArray.addAll(imagePathArrayOriginal);
        } else {
            int counter = 0;
            for (String tag : TagArray) {
                if (charText.toLowerCase(Locale.getDefault()).contains(" ")) {
                    String[] split2 = charText.split(" ");
                    int noOfWords2 = split2.length;
                    for (int k2 = 0; k2 < noOfWords2; k2++) {
                        if (tag.toLowerCase(Locale.getDefault()).contains(" ")) {
                            String[] split = tag.split(" ");
                            int noOfWords = split.length;
                            for (int k = 0; k < noOfWords; k++) {
                                if (split[k].toLowerCase(Locale.getDefault()).startsWith(split2[k2])) {
                                    int ID = IDArray.get(counter);
                                    imagePathArray2.addAll(mapOfIDToPath.get(ID));
                                }
                            }
                        } else if (tag.toLowerCase(Locale.getDefault()).startsWith(split2[k2])) {
                            int ID = IDArray.get(counter);
                            imagePathArray2.addAll(mapOfIDToPath.get(ID));
                        }
                    }
                }
                else{
                    if (tag.toLowerCase(Locale.getDefault()).contains(" ")) {
                        String[] split = tag.split(" ");
                        int noOfWords = split.length;
                        for (int k = 0; k < noOfWords; k++) {
                            if (split[k].toLowerCase(Locale.getDefault()).startsWith(charText)) {
                                int ID = IDArray.get(counter);
                                imagePathArray2.addAll(mapOfIDToPath.get(ID));
                            }
                        }
                    } else if (tag.toLowerCase(Locale.getDefault()).startsWith(charText)) {
                        int ID = IDArray.get(counter);
                        imagePathArray2.addAll(mapOfIDToPath.get(ID));
                    }
                }
                counter++;
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

            for (String address : addressesArray) {
                if (charText.toLowerCase(Locale.getDefault()).contains(" ")) {
                    String[] split2 = charText.split(" ");
                    int noOfWords2 = split2.length;
                    for (int k2 = 0; k2 < noOfWords2; k2++) {
                        if (address.toLowerCase(Locale.getDefault()).contains(" ")) {
                            String[] split = address.split(" ");
                            int noOfWords = split.length;

                            for (int k = 0; k < noOfWords; k++) {
                                if (split[k].toLowerCase(Locale.getDefault()).startsWith(split2[k2])) {
                                    imagePathArray2.addAll(addressToPathMap.get(address));
                                }
                            }
                        } else if (address.toLowerCase(Locale.getDefault()).startsWith(split2[k2])) {
                            imagePathArray2.addAll(addressToPathMap.get(address));
                        }
                    }
                }
                else{
                    if (address.toLowerCase(Locale.getDefault()).contains(" ")) {
                        String[] split = address.split(" ");
                        int noOfWords = split.length;

                        for (int k = 0; k < noOfWords; k++) {
                            if (split[k].toLowerCase(Locale.getDefault()).startsWith(charText)) {
                                imagePathArray2.addAll(addressToPathMap.get(address));
                            }
                        }
                    } else if (address.toLowerCase(Locale.getDefault()).startsWith(charText)) {
                        imagePathArray2.addAll(addressToPathMap.get(address));
                    }
                }
            }

            for (String date : dates) {
                if (charText.toLowerCase(Locale.getDefault()).contains(" ")) {
                    String[] split = charText.split(" ");
                    int noOfWords = split.length;
                    for (int k = 0; k < noOfWords; k++) {
                        if (date.toLowerCase(Locale.getDefault()).contains(split[k])) {
                            imagePathArray2.addAll(photosOnDate.get(date));
                        } else {

                            if ((k + 1) < split.length) {
                                if ("new".contains(split[k]) && "year".contains(split[k + 1])) {
                                    if (date.split(Pattern.quote("-"))[1].contains("01")) {
                                        if (date.split(Pattern.quote("-"))[2].contains("01") || date.split(Pattern.quote("-"))[2].contains("02")) {
                                            imagePathArray2.addAll(photosOnDate.get(date));
                                        }
                                    } else if (date.split(Pattern.quote("-"))[1].contains("12")) {
                                        if (date.split(Pattern.quote("-"))[2].contains("31")) {
                                            imagePathArray2.addAll(photosOnDate.get(date));
                                        }
                                    }
                                }
                            }
                            if ("christmas".contains(split[k])) {
                                if (date.split(Pattern.quote("-"))[1].contains("12")) {
                                    if (date.split(Pattern.quote("-"))[2].contains("25") || date.split(Pattern.quote("-"))[2].contains("24")) {
                                        imagePathArray2.addAll(photosOnDate.get(date));
                                    }
                                }
                            }

                            Calendar c = Calendar.getInstance();
                            int year = c.get(Calendar.YEAR);
                            int month = c.get(Calendar.MONTH) + 1;
                            int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
                            int FirstDayOfWeek = c.getFirstDayOfWeek();


                            if ("today".contains(split[k])) {
                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(month)) && date.split(Pattern.quote("-"))[2].contains(String.valueOf(dayOfMonth))) {
                                    imagePathArray2.addAll(photosOnDate.get(date));
                                }
                            } else if ("yesterday".contains(split[k])) {
                                Calendar c2 = Calendar.getInstance();
                                c2.add(Calendar.DATE, -1);
                                Date d = c2.getTime();
                                DateFormat sdfyear = new SimpleDateFormat("yyyy");
                                DateFormat sdfmonth = new SimpleDateFormat("MM");
                                DateFormat sdfdate = new SimpleDateFormat("dd");
                                String month2 = sdfmonth.format(d);
                                String year2 = sdfyear.format(d);
                                String day2 = sdfdate.format(d);
                                if (date.split(Pattern.quote("-"))[0].contains(year2) && date.split(Pattern.quote("-"))[1].contains(month2) && date.split(Pattern.quote("-"))[2].contains(day2)) {
                                    imagePathArray2.addAll(photosOnDate.get(date));
                                }
                            } else if ("this".contains(split[k])) {
                                if ((k + 1) < split.length) {
                                    if ("week".contains(split[k + 1])) {
                                        for (int g = FirstDayOfWeek; g < dayOfMonth; g++) {
                                            if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(month)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(g))) {
                                                imagePathArray2.addAll(photosOnDate.get(date));
                                            }
                                        }
                                    } else if ("month".contains(split[k + 1])) {
                                        if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(month))) {
                                            imagePathArray2.addAll(photosOnDate.get(date));
                                        }
                                    } else if ("year".contains(split[k + 1])) {
                                        if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year))) {
                                            imagePathArray2.addAll(photosOnDate.get(date));
                                        }
                                    }
                                }
                            } else if ("ago".contains(split[k])) {
                                if (k - 2 >= 0) {
                                    if ("days".contains(split[k - 1])) {
                                        String quantifier = split[k - 2];
                                        int quantifierInt = 0;
                                        if ("one".contains(quantifier)) {
                                            quantifierInt = 1;
                                        } else if ("two".contains(quantifier)) {
                                            quantifierInt = 2;
                                        } else if ("three".contains(quantifier)) {
                                            quantifierInt = 3;
                                        } else if ("four".contains(quantifier)) {
                                            quantifierInt = 4;
                                        } else if ("five".contains(quantifier)) {
                                            quantifierInt = 5;
                                        } else if ("six".contains(quantifier)) {
                                            quantifierInt = 6;
                                        } else if ("seven".contains(quantifier)) {
                                            quantifierInt = 7;
                                        } else if ("eight".contains(quantifier)) {
                                            quantifierInt = 8;
                                        } else if ("nine".contains(quantifier)) {
                                            quantifierInt = 9;
                                        } else if ("ten".contains(quantifier)) {
                                            quantifierInt = 10;
                                        } else if ("eleven".contains(quantifier)) {
                                            quantifierInt = 11;
                                        } else if ("twelve".contains(quantifier)) {
                                            quantifierInt = 12;
                                        } else if ("thirteen".contains(quantifier)) {
                                            quantifierInt = 13;
                                        } else if ("fourteen".contains(quantifier)) {
                                            quantifierInt = 14;
                                        } else if ("fifteen".contains(quantifier)) {
                                            quantifierInt = 15;
                                        } else if ("sixteen".contains(quantifier)) {
                                            quantifierInt = 16;
                                        } else if ("seventeen".contains(quantifier)) {
                                            quantifierInt = 17;
                                        } else if ("eighteen".contains(quantifier)) {
                                            quantifierInt = 18;
                                        } else if ("nineteen".contains(quantifier)) {
                                            quantifierInt = 19;
                                        } else if ("twenty".contains(quantifier)) {
                                            quantifierInt = 20;
                                        }
                                        Calendar c2 = Calendar.getInstance();
                                        c2.add(Calendar.DATE, -quantifierInt);
                                        int month2 = c2.get(Calendar.MONTH) + 1;
                                        int year2 = c2.get(Calendar.YEAR);
                                        int day2 = c2.get(Calendar.DAY_OF_MONTH);
                                        if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year2)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(month2)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(day2))) {
                                            imagePathArray2.addAll(photosOnDate.get(date));
                                        }
                                    } else if ("weeks".contains(split[k - 1])) {
                                        String quantifier = split[k - 2];
                                        int quantifierInt = 0;
                                        if ("one".contains(quantifier)) {
                                            quantifierInt = 1;
                                        } else if ("two".contains(quantifier)) {
                                            quantifierInt = 2;
                                        } else if ("three".contains(quantifier)) {
                                            quantifierInt = 3;
                                        } else if ("four".contains(quantifier)) {
                                            quantifierInt = 4;
                                        } else if ("five".contains(quantifier)) {
                                            quantifierInt = 5;
                                        } else if ("six".contains(quantifier)) {
                                            quantifierInt = 6;
                                        } else if ("seven".contains(quantifier)) {
                                            quantifierInt = 7;
                                        } else if ("eight".contains(quantifier)) {
                                            quantifierInt = 8;
                                        } else if ("nine".contains(quantifier)) {
                                            quantifierInt = 9;
                                        } else if ("ten".contains(quantifier)) {
                                            quantifierInt = 10;
                                        } else if ("eleven".contains(quantifier)) {
                                            quantifierInt = 11;
                                        } else if ("twelve".contains(quantifier)) {
                                            quantifierInt = 12;
                                        } else if ("thirteen".contains(quantifier)) {
                                            quantifierInt = 13;
                                        } else if ("fourteen".contains(quantifier)) {
                                            quantifierInt = 14;
                                        } else if ("fifteen".contains(quantifier)) {
                                            quantifierInt = 15;
                                        } else if ("sixteen".contains(quantifier)) {
                                            quantifierInt = 16;
                                        } else if ("seventeen".contains(quantifier)) {
                                            quantifierInt = 17;
                                        } else if ("eighteen".contains(quantifier)) {
                                            quantifierInt = 18;
                                        } else if ("nineteen".contains(quantifier)) {
                                            quantifierInt = 19;
                                        } else if ("twenty".contains(quantifier)) {
                                            quantifierInt = 20;
                                        }
                                        Calendar c2 = Calendar.getInstance();
                                        int firstDayOfCurrentWeek = c2.getFirstDayOfWeek();
                                        int year2 = c2.get(Calendar.YEAR);
                                        int month2 = c2.get(Calendar.MONTH) + 1;
                                        c2.set(year2, month2, firstDayOfCurrentWeek);
                                        c2.add(Calendar.DATE, -(quantifierInt * 7));
                                        int month3 = c2.get(Calendar.MONTH) + 1;
                                        int year3 = c2.get(Calendar.YEAR);
                                        int day3 = c2.get(Calendar.DAY_OF_MONTH);

                                        for (int g = day3; g < (day3 + 7); g++) {
                                            if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year3)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(month3)) && date.split(Pattern.quote("-"))[2].contains(String.valueOf(g))) {
                                                imagePathArray2.addAll(photosOnDate.get(date));
                                            }
                                        }
                                    } else if ("months".contains(split[k - 1])) {
                                        if ("one".contains(split[k - 2])) {
                                            if ((month - 1) > 0) {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(month - 1))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            } else {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(12 + (month - 1)))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            }
                                        } else if ("two".contains(split[k - 2])) {
                                            if ((month - 2) > 0) {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(month - 2))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            } else {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(12 + (month - 2)))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            }
                                        } else if ("three".contains(split[k - 2])) {
                                            if ((month - 3) > 0) {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(month - 3))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            } else {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(12 + (month - 3)))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            }
                                        } else if ("four".contains(split[k - 2])) {
                                            if ((month - 4) > 0) {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(month - 4))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            } else {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(12 + (month - 4)))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            }
                                        } else if ("five".contains(split[k - 2])) {
                                            if ((month - 5) > 0) {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(month - 5))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            } else {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(12 + (month - 5)))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            }
                                        } else if ("six".contains(split[k - 2])) {
                                            if ((month - 6) > 0) {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(month - 6))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            } else {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(12 + (month - 6)))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            }
                                        } else if ("seven".contains(split[k - 2])) {
                                            if ((month - 7) > 0) {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(month - 7))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            } else {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(12 + (month - 7)))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            }
                                        } else if ("eight".contains(split[k - 2])) {
                                            if ((month - 8) > 0) {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(month - 8))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            } else {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(12 + (month - 8)))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            }
                                        } else if ("nine".contains(split[k - 2])) {
                                            if ((month - 9) > 0) {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(month - 9))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            } else {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(12 + (month - 9)))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            }
                                        } else if ("ten".contains(split[k - 2])) {
                                            if ((month - 10) > 0) {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(month - 10))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            } else {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(12 + (month - 10)))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            }
                                        } else if ("eleven".contains(split[k - 2])) {
                                            if ((month - 11) > 0) {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(month - 11))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            } else {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(12 + (month - 11)))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            }
                                        } else if ("tweleve".contains(split[k - 2])) {
                                            if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(month))) {
                                                imagePathArray2.addAll(photosOnDate.get(date));
                                            }
                                        }
                                    } else if ("years".contains(split[k - 1])) {
                                        if ("one".contains(split[k - 2])) {
                                            if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1))) {
                                                imagePathArray2.addAll(photosOnDate.get(date));
                                            }
                                        } else if ("two".contains(split[k - 2])) {
                                            if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 2))) {
                                                imagePathArray2.addAll(photosOnDate.get(date));
                                            }
                                        } else if ("three".contains(split[k - 2])) {
                                            if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 3))) {
                                                imagePathArray2.addAll(photosOnDate.get(date));
                                            }
                                        } else if ("four".contains(split[k - 2])) {
                                            if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 4))) {
                                                imagePathArray2.addAll(photosOnDate.get(date));
                                            }
                                        } else if ("five".contains(split[k - 2])) {
                                            if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 5))) {
                                                imagePathArray2.addAll(photosOnDate.get(date));
                                            }
                                        } else if ("six".contains(split[k - 2])) {
                                            if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 6))) {
                                                imagePathArray2.addAll(photosOnDate.get(date));
                                            }
                                        } else if ("seven".contains(split[k - 2])) {
                                            if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 7))) {
                                                imagePathArray2.addAll(photosOnDate.get(date));
                                            }
                                        } else if ("eight".contains(split[k - 2])) {
                                            if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 8))) {
                                                imagePathArray2.addAll(photosOnDate.get(date));
                                            }
                                        } else if ("nine".contains(split[k - 2])) {
                                            if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 9))) {
                                                imagePathArray2.addAll(photosOnDate.get(date));
                                            }
                                        } else if ("ten".contains(split[k - 2])) {
                                            if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 10))) {
                                                imagePathArray2.addAll(photosOnDate.get(date));
                                            }
                                        }
                                    }
                                }
                            } else if ("last".contains(split[k]) || "previous".contains(split[k])) {
                                if ((k + 1) < split.length) {
                                    int lastMonth;
                                    if (month != 1) {
                                        lastMonth = month - 1;
                                    } else {
                                        lastMonth = 12;
                                    }
                                    int day = c.get(Calendar.DAY_OF_MONTH);

                                    if ("monday".contains(split[k + 1])) {
                                        //todo last __day___
                                    } else if ("week".contains(split[k + 1])) {
                                        Calendar c2 = Calendar.getInstance();
                                        int firstDayOfCurrentWeek = c2.getFirstDayOfWeek();
                                        int year2 = c2.get(Calendar.YEAR);
                                        int month2 = c2.get(Calendar.MONTH) + 1;
                                        c2.set(year2, month2, firstDayOfCurrentWeek);
                                        c2.add(Calendar.DATE, -7);
                                        int month3 = c2.get(Calendar.MONTH) + 1;
                                        int year3 = c2.get(Calendar.YEAR);
                                        int day3 = c2.get(Calendar.DAY_OF_MONTH);

                                        for (int g = day3; g < (day3 + 7); g++) {
                                            if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year3)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(month3)) && date.split(Pattern.quote("-"))[2].contains(String.valueOf(g))) {
                                                imagePathArray2.addAll(photosOnDate.get(date));
                                            }
                                        }

                                    } else if ("month".contains(split[k + 1])) {
                                        if (month != 1 && date.split(Pattern.quote("-"))[0].contains(String.valueOf(year)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(lastMonth))) {
                                            imagePathArray2.addAll(photosOnDate.get(date));
                                        } else if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(lastMonth))) {
                                            imagePathArray2.addAll(photosOnDate.get(date));
                                        }
                                    } else if ("year".contains(split[k + 1])) {
                                        if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1))) {
                                            imagePathArray2.addAll(photosOnDate.get(date));
                                        }
                                    }


                                    if ("january".contains(split[k + 1])) {
                                        if (date.split(Pattern.quote("-"))[1].contains("01")) {
                                            if (month > 1) {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            } else {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            }
                                        }
                                    } else if ("february".contains(split[k + 1])) {
                                        if (date.split(Pattern.quote("-"))[1].contains("02")) {
                                            if (month > 2) {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            } else {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            }
                                        }
                                    } else if ("march".contains(split[k + 1])) {
                                        if (date.split(Pattern.quote("-"))[1].contains("03")) {
                                            if (month > 3) {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            } else {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            }
                                        }
                                    } else if ("april".contains(split[k + 1])) {
                                        if (date.split(Pattern.quote("-"))[1].contains("04")) {
                                            if (month > 4) {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            } else {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            }
                                        }
                                    } else if ("may".contains(split[k + 1])) {
                                        if (date.split(Pattern.quote("-"))[1].contains("05")) {
                                            if (month > 5) {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            } else {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            }
                                        }
                                    } else if ("june".contains(split[k + 1])) {
                                        if (date.split(Pattern.quote("-"))[1].contains("06")) {
                                            if (month > 6) {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            } else {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            }
                                        }
                                    } else if ("july".contains(split[k + 1])) {
                                        if (date.split(Pattern.quote("-"))[1].contains("07")) {
                                            if (month > 7) {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            } else {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            }
                                        }
                                    } else if ("august".contains(split[k + 1])) {
                                        if (date.split(Pattern.quote("-"))[1].contains("08")) {
                                            if (month > 8) {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            } else {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            }
                                        }
                                    } else if ("september".contains(split[k + 1])) {
                                        if (date.split(Pattern.quote("-"))[1].contains("09")) {
                                            if (month > 9) {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            } else {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            }
                                        }
                                    } else if ("october".contains(split[k + 1])) {
                                        if (date.split(Pattern.quote("-"))[1].contains("10")) {
                                            if (month > 10) {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            } else {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            }
                                        }
                                    } else if ("november".contains(split[k + 1])) {
                                        if (date.split(Pattern.quote("-"))[1].contains("11")) {
                                            if (month > 11) {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            } else {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            }
                                        }
                                    } else if ("december".contains(split[k + 1])) {
                                        if (date.split(Pattern.quote("-"))[1].contains("12")) {
                                            if (month > 12) {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            } else {
                                                if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year - 1))) {
                                                    imagePathArray2.addAll(photosOnDate.get(date));
                                                }
                                            }
                                        }
                                    }
                                }


                            } else if ("january".contains(split[k])) {
                                if (date.split(Pattern.quote("-"))[1].contains("01")) {
                                    imagePathArray2.addAll(photosOnDate.get(date));
                                }
                            } else if ("february".contains(split[k])) {
                                if (date.split(Pattern.quote("-"))[1].contains("02")) {
                                    imagePathArray2.addAll(photosOnDate.get(date));
                                }
                            } else if ("march".contains(split[k])) {
                                if (date.split(Pattern.quote("-"))[1].contains("03")) {
                                    imagePathArray2.addAll(photosOnDate.get(date));
                                }
                            } else if ("april".contains(split[k])) {
                                if (date.split(Pattern.quote("-"))[1].contains("04")) {
                                    imagePathArray2.addAll(photosOnDate.get(date));
                                }
                            } else if ("may".contains(split[k])) {
                                if (date.split(Pattern.quote("-"))[1].contains("05")) {
                                    imagePathArray2.addAll(photosOnDate.get(date));
                                }
                            } else if ("june".contains(split[k])) {
                                if (date.split(Pattern.quote("-"))[1].contains("06")) {
                                    imagePathArray2.addAll(photosOnDate.get(date));
                                }
                            } else if ("july".contains(split[k])) {
                                if (date.split(Pattern.quote("-"))[1].contains("07")) {
                                    imagePathArray2.addAll(photosOnDate.get(date));
                                }
                            } else if ("august".contains(split[k])) {
                                if (date.split(Pattern.quote("-"))[1].contains("08")) {
                                    imagePathArray2.addAll(photosOnDate.get(date));
                                }
                            } else if ("september".contains(split[k])) {
                                if (date.split(Pattern.quote("-"))[1].contains("09")) {
                                    imagePathArray2.addAll(photosOnDate.get(date));
                                }
                            } else if ("october".contains(split[k])) {
                                if (date.split(Pattern.quote("-"))[1].contains("10")) {
                                    imagePathArray2.addAll(photosOnDate.get(date));
                                }
                            } else if ("november".contains(split[k])) {
                                if (date.split(Pattern.quote("-"))[1].contains("11")) {
                                    imagePathArray2.addAll(photosOnDate.get(date));
                                }
                            } else if ("december".contains(split[k])) {
                                if (date.split(Pattern.quote("-"))[1].contains("12")) {
                                    imagePathArray2.addAll(photosOnDate.get(date));
                                }
                            }
                        }
                    }
                } else {
                    if (date.toLowerCase(Locale.getDefault()).contains(charText)) {
                        imagePathArray2.addAll(photosOnDate.get(date));
                    } else {
                        Calendar c = Calendar.getInstance();
                        int year = c.get(Calendar.YEAR);
                        int month = c.get(Calendar.MONTH) + 1;
                        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

                        if ("today".contains(charText)) {
                            if (date.split(Pattern.quote("-"))[0].contains(String.valueOf(year)) && date.split(Pattern.quote("-"))[1].contains(String.valueOf(month)) && date.split(Pattern.quote("-"))[2].contains(String.valueOf(dayOfMonth))) {
                                imagePathArray2.addAll(photosOnDate.get(date));
                            }
                        } else if ("yesterday".contains(charText)) {
                            Calendar c2 = Calendar.getInstance();
                            c2.add(Calendar.DATE, -1);
                            Date d = c2.getTime();
                            DateFormat sdfyear = new SimpleDateFormat("yyyy");
                            DateFormat sdfmonth = new SimpleDateFormat("MM");
                            DateFormat sdfdate = new SimpleDateFormat("dd");
                            String month2 = sdfmonth.format(d);
                            String year2 = sdfyear.format(d);
                            String day2 = sdfdate.format(d);
                            if (date.split(Pattern.quote("-"))[0].contains(year2) && date.split(Pattern.quote("-"))[1].contains(month2) && date.split(Pattern.quote("-"))[2].contains(day2)) {
                                imagePathArray2.addAll(photosOnDate.get(date));
                            }
                        } else if ("january".contains(charText)) {
                            if (date.split(Pattern.quote("-"))[1].contains("01")) {
                                imagePathArray2.addAll(photosOnDate.get(date));
                            }
                        } else if ("february".contains(charText)) {
                            if (date.split(Pattern.quote("-"))[1].contains("02")) {
                                imagePathArray2.addAll(photosOnDate.get(date));
                            }
                        } else if ("march".contains(charText)) {
                            if (date.split(Pattern.quote("-"))[1].contains("03")) {
                                imagePathArray2.addAll(photosOnDate.get(date));
                            }
                        } else if ("april".contains(charText)) {
                            if (date.split(Pattern.quote("-"))[1].contains("04")) {
                                imagePathArray2.addAll(photosOnDate.get(date));
                            }
                        } else if ("may".contains(charText)) {
                            if (date.split(Pattern.quote("-"))[1].contains("05")) {
                                imagePathArray2.addAll(photosOnDate.get(date));
                            }
                        } else if ("june".contains(charText)) {
                            if (date.split(Pattern.quote("-"))[1].contains("06")) {
                                imagePathArray2.addAll(photosOnDate.get(date));
                            }
                        } else if ("july".contains(charText)) {
                            if (date.split(Pattern.quote("-"))[1].contains("07")) {
                                imagePathArray2.addAll(photosOnDate.get(date));
                            }
                        } else if ("august".contains(charText)) {
                            if (date.split(Pattern.quote("-"))[1].contains("08")) {
                                imagePathArray2.addAll(photosOnDate.get(date));
                            }
                        } else if ("september".contains(charText)) {
                            if (date.split(Pattern.quote("-"))[1].contains("09")) {
                                imagePathArray2.addAll(photosOnDate.get(date));
                            }
                        } else if ("october".contains(charText)) {
                            if (date.split(Pattern.quote("-"))[1].contains("10")) {
                                imagePathArray2.addAll(photosOnDate.get(date));
                            }
                        } else if ("november".contains(charText)) {
                            if (date.split(Pattern.quote("-"))[1].contains("11")) {
                                imagePathArray2.addAll(photosOnDate.get(date));
                            }
                        } else if ("december".contains(charText)) {
                            if (date.split(Pattern.quote("-"))[1].contains("12")) {
                                imagePathArray2.addAll(photosOnDate.get(date));
                            }
                        }

                        if ("christmas".contains(charText)) {
                            if (date.split(Pattern.quote("-"))[1].contains("12")) {
                                if (date.split(Pattern.quote("-"))[2].contains("25") || date.split(Pattern.quote("-"))[2].contains("24")) {
                                    imagePathArray2.addAll(photosOnDate.get(date));
                                }
                            }
                        }
                    }
                }
            }

            imagePathArray.addAll(removeDuplicates(imagePathArray2));
            notifyDataSetChanged();
        }


    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public LinearLayout rippleHolder;
        public ImageView photo;
        public TextView headerText;
        public LinearLayout LongPressHolder;
        public RelativeLayout container;
        public SmoothCheckBox smoothCheckBox;
        public TextView subHeader;

        public ViewHolder(View itemView) {
            super(itemView);
            rippleHolder = (LinearLayout) itemView.findViewById(R.id.rippleHolder);
            LongPressHolder = (LinearLayout) itemView.findViewById(R.id.LongPressHolder);
            container = (RelativeLayout) itemView.findViewById(R.id.container);
            photo = (ImageView) itemView.findViewById(R.id.placeImage);
            headerText = (TextView) itemView.findViewById(R.id.headerText);
            smoothCheckBox = (SmoothCheckBox) itemView.findViewById(R.id.scb);
            subHeader = (TextView) itemView.findViewById(R.id.subHeader);
            rippleHolder.setOnClickListener(this);
            rippleHolder.setOnLongClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if (span != 3) {
                Intent e = new Intent(mContext, ImageFullScreen.class);
                int position = getPosition();

                if (haveHeader) {
                    if (position == 0 && span == 4) {
                        e.putExtra("PhotoPosition", i);
                    } else if (position == 0) {
                        e.putExtra("PhotoPosition", 0);
                    } else {
                        e.putExtra("PhotoPosition", position - 1);
                    }
                } else {
                    e.putExtra("PhotoPosition", position);
                }
                e.putExtra("PhotoArrayList", imagePathArray);


                if (span == 4) {
                    e.putExtra("IntentFromPhotoTag", true);
                    ((PhotoTag) mContext).finish();
                } else {
                    e.putExtra("IntentFromPhotoTag", false);
                }
                mContext.startActivity(e);
            }
            if (mCallback != null) mCallback.onClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            // Forwards to the adapter's constructor callback
            if (mCallback != null) mCallback.onLongClick(getAdapterPosition());
            return true;
        }

    }


}
