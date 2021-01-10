package mochainc.imagnition;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Chun Wei on 11/6/2016.
 */
public class PsuedoCollections extends AsyncTask<ArrayList<String>, Void, ArrayList<String>> {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    SimpleDateFormat sdfTime = new SimpleDateFormat("EEEE hh:mm a", Locale.ENGLISH);
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.ENGLISH);
    private String dateString = null;
    private String date = null;
    private String attrLONGITUDE_REF, attrLATITUDE_REF;
    private String attrLONGITUDE, attrLATITUDE;
    private String Longitude, Latitude;
    private String time;
    private String address;
    private String aperture;
    private String iso;
    private String shutterspeed;
    private String zoom;
    private String cameraName;
    private String tagging;
    private String strloc = "";
    private String outputdata = "";
    private String outputcollect = "";
    private String imgdataTBW; //image data to be written
    private Map<String, ArrayList<String>> collectionmap;
    private Map<String, ArrayList<String>> retrieveCollection;
    private Map<Integer, ArrayList<String>> idtomap;
    private boolean valid = false;
    private Context context;
    private ArrayList<String> imageGroupKeys = new ArrayList<>();
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private ViewGroup rootView;
    private RecyclerView collectionsRecyclerView;
    private StaggeredGridLayoutManager collectionsStaggeredLayoutManager;
    private CollectionsListAdapter mCollectionsAdapter;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private static Window window;
    private ProgressBar collectionsLoadingCircle;
    private IDtoPath itp;
    private CollectionsData collectionsData = new CollectionsData();
    private ArrayList<String> collectionsDayArrayInitialise;
    private String Day;
    private Date parsedDate2;
    private SimpleDateFormat sdfDayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH);
    private IDConversion idConversion;
    private ArrayList<Integer> arrayOfIDPerPhoto;
    private ArrayList<Integer> arrayOfIDOfAllPhotosPerCollection;
    private ArrayList<ArrayList<Integer>> arrayOfIDOfAllPhotosInDays = new ArrayList<>();
    private InferCollectionEvent inferCollectionEvent;
    private ArrayList<String> dayString = new ArrayList<>();
    private ArrayList<String> collectionsTitleArray = new ArrayList<>();
    private String dateInitialise;
    private SimpleDateFormat sdfParse = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private Map<String, ArrayList<String>> mappedPath;

    public PsuedoCollections(Context context, ViewGroup rootView, Toolbar toolbar, FloatingActionButton fab, Window window) {
        this.context = context;
        this.rootView = rootView;
        this.toolbar = toolbar;
        this.fab = fab;
        this.window = window;
        collectionmap = new TreeMap<String, ArrayList<String>>(); //sorts in ascending order ACSII value
        idtomap = new TreeMap<Integer, ArrayList<String>>();
        retrieveCollection = new TreeMap<String, ArrayList<String>>();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
        itp = new IDtoPath();

        idConversion = new IDConversion(context);
        inferCollectionEvent = new InferCollectionEvent();
    }

    @Override
    protected synchronized ArrayList<String> doInBackground(ArrayList<String>... params) {
        ArrayList<String> ipa = params[0];
        setImagePathArray(ipa);

        mappedPath = getMap();
        ArrayList<String> DateGroupArray = getImageGroupKeys();

        for (int i = 0; i < DateGroupArray.size(); i++) {
            dateInitialise = DateGroupArray.get(i);
            collectionsDayArrayInitialise = mappedPath.get(dateInitialise);
            arrayOfIDOfAllPhotosPerCollection = new ArrayList<>();

            try {
                parsedDate2 = sdfParse.parse(dateInitialise);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Day = sdfDayOfWeek.format(parsedDate2);
            for (String filePath : collectionsDayArrayInitialise) {
                arrayOfIDPerPhoto = new ArrayList<>();
                arrayOfIDPerPhoto = idConversion.getID(filePath);
                arrayOfIDOfAllPhotosPerCollection.addAll(arrayOfIDPerPhoto);
            }
            arrayOfIDOfAllPhotosPerCollection = removeDuplicates(arrayOfIDOfAllPhotosPerCollection);
            arrayOfIDOfAllPhotosInDays.add(arrayOfIDOfAllPhotosPerCollection);
            dayString.add(Day);
        }

        inferCollectionEvent.setPreferences(context);
        collectionsTitleArray = inferCollectionEvent.inferEvent(arrayOfIDOfAllPhotosInDays, dayString, mappedPath, DateGroupArray);


        return ipa;
    }

    @Override
    protected synchronized void onPreExecute() {
        super.onPreExecute();
        collectionsLoadingCircle = (ProgressBar) rootView.findViewById(R.id.collectionsLoadingCircle);
        collectionsLoadingCircle.setVisibility(View.VISIBLE);
        collectionsRecyclerView = (RecyclerView) rootView.findViewById(R.id.collectionsList);
        collectionsRecyclerView.setVisibility(View.GONE);
    }

    @Override
    protected synchronized void onPostExecute(ArrayList<String> unusedNullDao) {
        collectionsLoadingCircle.setVisibility(View.GONE);
        collectionsRecyclerView.setVisibility(View.VISIBLE);
        collectionsStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        collectionsRecyclerView.setLayoutManager(collectionsStaggeredLayoutManager);
        mCollectionsAdapter = new CollectionsListAdapter(context, getMap(), getImageGroupKeys());

        collectionsRecyclerView.setAdapter(mCollectionsAdapter);
        collectionsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) { // on scroll stop
                    toolbar.animate().translationY(0).setDuration(300).start();
                    fab.show();
                    window.setStatusBarColor(context.getResources().getColor(R.color.light));
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    toolbar.animate().translationY(-300).setDuration(300).start();
                    fab.hide();
                    window.setStatusBarColor(Color.parseColor("#80000000"));
                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                }
            }
        });

        collectionsData.putData(imageGroupKeys, retrieveCollection);
        collectionsData.putDataAll(imageGroupKeysAll, retrieveCollectionAll);

        Intent intent = new Intent(context, GeocodeAddressIntentService.class);
        intent.putExtra("LocationsArrays", (Serializable) filePathToLocationMap);
        context.startService(intent);
    }

    private ArrayList<Integer> removeDuplicates(ArrayList<Integer> list) {

        // Store unique items in result.
        ArrayList<Integer> result = new ArrayList<>();

        // Record encountered Strings in HashSet.
        HashSet<Integer> set = new HashSet<>();

        // Loop over argument list.
        for (int item : list) {

            // If String is not in set, add it to the list and the set.
            if (!set.contains(item)) {
                result.add(item);
                set.add(item);
            }
        }
        return result;
    }

    //TODO Basically the main method ---LOOK LOOK LOOK AT ME LOOK-----
    private Map<String, Location> filePathToLocationMap = new HashMap<>();
    public void setImagePathArray(ArrayList<String> ipa) {


        for (int i = 0; i < ipa.size(); i++) {
            if (this.isCancelled()) break;
            String imagePath = ipa.get(i);
            //Calls upon exif processing
            imageEXIF(imagePath);
            imgdataTBW = imagePath + '?' + date + '*' + time + '*' + Latitude + '*' + Longitude + '*' + cameraName + '*' + aperture + '*' + shutterspeed + '*' + zoom + '*' + iso + '|';
            outputdata += imgdataTBW;

            if(Lat!=null && Long!=null) {
                Location location = new Location("");
                location.setLatitude(Lat);
                location.setLongitude(Long);
                filePathToLocationMap.put(imagePath, location);
            }

            /*TODO proposed algorithmic solution:
            When choosing what collections to display, then do the 10 check
            More dynamic, able to detect when future photos add in and passes the requirement
             */
            ArrayList<String> temp = new ArrayList<>();
            if (!imageGroupKeys.contains(date)) {
                imageGroupKeys.add(date);
                temp.add(imagePath + "*");
                collectionmap.put(date, temp);
            } else {
                //Get all filepath of the date
                temp.addAll(collectionmap.get(date));
                //Add in new filepath
                temp.add(imagePath + "*");
                //Appends to collectionmap
                collectionmap.put(date, temp);
            }
        }
        // FOR LOOP ENDS HERE
        //TODO data to be segregated for both
        //TODO prohibited * ? / \ | [ ] " :
        for (Map.Entry<String, ArrayList<String>> entry : collectionmap.entrySet()) {
            String key = entry.getKey();
            ArrayList<String> values = entry.getValue();
            //Be wary to assign the original value of outputcollect to "" if not null will come out
            outputcollect += key + "\n" + values.toString() + "\n";
        }
        editor.putString("imgdata", outputdata).apply(); // imagePath + ':' + date + '*' + Latitude + '*' + Longitude + '|';
        editor.putString("collectiondata", outputcollect).apply(); // date=[path1, path2, path3];date2=[....]
        java.util.Collections.sort(imageGroupKeys, new NumericalStringComparator());
        java.util.Collections.reverse(imageGroupKeys);
        String listString = "";
        for (String s : imageGroupKeys) {
            listString += s + ",";
        }
        editor.putString("imageGroupKeys", listString).apply();


    }

    private int gcd(int num, int den) {
        if (den == 0 || num == 0) {
            return num + den;
        }
        return gcd(den, num % den);
    }

    private String simplifyFraction(int num, int den) {
        /*BigInteger n = BigInteger.valueOf(num);
        BigInteger d = BigInteger.valueOf(den);
        BigInteger gcd = n.gcd(d);*/
        int greatestCommondDeno = gcd(num, den);
        num = num / greatestCommondDeno;
        den = den / greatestCommondDeno;
        return String.valueOf(num + "/" + den);
    }

    private void imageEXIF(String image) {
        ExifInterface intf = null;
        try {
            intf = new ExifInterface(image);
        } catch (IOException g) {
            g.printStackTrace();
        }
        //Checks if photo has data attached, if not set it to null, if so, fetch all types
        if (intf != null) {
            dateString = intf.getAttribute(ExifInterface.TAG_DATETIME);
            attrLATITUDE = intf.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            attrLATITUDE_REF = intf.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            attrLONGITUDE = intf.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            attrLONGITUDE_REF = intf.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            cameraName = intf.getAttribute(ExifInterface.TAG_MAKE) + " " + intf.getAttribute(ExifInterface.TAG_MODEL);
            iso = "ISO" + intf.getAttribute(ExifInterface.TAG_ISO);
            tagging = intf.getAttribute("UserComment");

            if (tagging != null) {
                String[] tarr = tagging.split(Pattern.quote("|"));
                ArrayList<String> atarr = new ArrayList<>(Arrays.asList(tarr));
                if (atarr.contains("IMAGNITIONS")) {
                    int initial = atarr.indexOf("IMAGNITIONS"); //Detect where is our label(index)
                    for (int x = 1; x <= 5; ++x) { //from that index, get the top 5 tags,each are 1 line
                        String temp1 = tarr[initial + x];
                        String[] split = temp1.split(Pattern.quote(":")); //split so that I get the ID
                        ArrayList<String> tas = new ArrayList<>();
                        if (idtomap.get(Integer.parseInt(split[0].trim())) != null) { //checks if got anything already
                            ArrayList<String> tas1 = new ArrayList<>();
                            tas1 = idtomap.get(Integer.parseInt(split[0].trim()));
                            tas.addAll(tas1);
                            tas.add(image);
                            idtomap.put(Integer.parseInt(split[0].trim()), tas);
                        } else {
                            tas.add(image);
                            idtomap.put(Integer.parseInt(split[0].trim()), tas);
                        }
                    }
                    itp.setIDtoPath(idtomap);
                }

                if (atarr.contains("IMGFACE") && tarr[atarr.indexOf("IMGFACE") + 1].equals("1861:people")) {
                    //9999 for person detected, 9998 for nobody detected
                    int initial = atarr.indexOf("IMGFACE");
                    String temp1 = tarr[initial + 1];
                    String[] split = temp1.split(Pattern.quote(":")); //split so that I get the ID
                    ArrayList<String> tas = new ArrayList<>();
                    if (idtomap.get(Integer.parseInt(split[0].trim())) != null) { //checks if got anything already
                        ArrayList<String> tas1 = new ArrayList<>();
                        tas1 = idtomap.get(Integer.parseInt(split[0].trim()));
                        tas.addAll(tas1);
                        tas.add(image);
                        idtomap.put(Integer.parseInt(split[0].trim()), tas);
                    } else {
                        tas.add(image);
                        idtomap.put(Integer.parseInt(split[0].trim()), tas);
                    }
                    itp.setIDtoPath(idtomap);
                }
                //System.out.println("TEST" + tagging);
            }

            String shutterspeedUnprocessed = intf.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
            if (shutterspeedUnprocessed != null && shutterspeedUnprocessed.contains(".")) {
                String[] shutterspeedUnprocessedArray = shutterspeedUnprocessed.split(Pattern.quote("."));
                int noOfDP = shutterspeedUnprocessedArray[1].length();
                int power = (int) Math.pow(10, noOfDP);
                float shutterspeedWithDP = Float.valueOf(shutterspeedUnprocessed);
                float shutterspeedwhole = shutterspeedWithDP * (power);
                int num = (int) shutterspeedwhole;
                int den = power;
                shutterspeed = simplifyFraction(num, den);
            } else if (shutterspeedUnprocessed != null) {
                shutterspeed = shutterspeedUnprocessed;
            } else {
                shutterspeed = null;
            }
            String zoomUnProcessed = intf.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
            if (zoomUnProcessed != null && zoomUnProcessed.contains("/")) {
                String[] zoomUnprocessedArray = zoomUnProcessed.split(Pattern.quote("/"));
                float numerator = Float.valueOf(zoomUnprocessedArray[0]);
                float denominator = Float.valueOf(zoomUnprocessedArray[1]);
                float unroundedFloat = numerator / denominator;
                DecimalFormat df = new DecimalFormat("###.##");
                zoom = String.valueOf(df.format(unroundedFloat)) + " mm";
            } else if (zoomUnProcessed != null) {
                zoom = zoomUnProcessed + " mm";
            } else {
                zoom = null;
            }
            aperture = "f/" + intf.getAttribute(ExifInterface.TAG_APERTURE);
            //TODO Add location is added here instead
            addLocation();
            if (dateString != null) {
                Date d = null;
                try {
                    d = sdf2.parse(dateString);
                    date = sdf.format(d);
                    time = sdfTime.format(d);
                } catch (ParseException g) {
                    g.printStackTrace();
                }
            } else {
                File imageFile = new File(image);
                date = sdf.format(imageFile.lastModified());
                time = sdfTime.format(imageFile.lastModified());
            }
        } else {
            File imageFile = new File(image);
            date = sdf.format(imageFile.lastModified());
            time = sdfTime.format(imageFile.lastModified());
            attrLATITUDE = null;
            attrLATITUDE_REF = null;
            attrLONGITUDE = null;
            attrLONGITUDE_REF = null;
            Latitude = null;
            Longitude = null;
            cameraName = null;
            iso = null;
            shutterspeed = null;
            zoom = null;
            aperture = null;
            Lat = null;
            Long = null;
        }
    }

    private Double Lat, Long;

    private void addLocation() {
        if (attrLATITUDE != null && attrLATITUDE_REF != null && attrLONGITUDE != null && attrLONGITUDE_REF != null) {
            valid = true;
            DecimalFormat df = new DecimalFormat("##.###");
            if (attrLATITUDE_REF.equals("N")) {
                Lat = convertToDegree(attrLATITUDE);
                Latitude = df.format(Lat);
            } else {
                Lat = (0 - convertToDegree(attrLATITUDE));
                Latitude = df.format(Lat);
            }
            if (attrLONGITUDE_REF.equals("E")) {
                Long = convertToDegree(attrLONGITUDE);
                Longitude = df.format(Long);
            } else {
                Long = (0 - convertToDegree(attrLONGITUDE));
                Longitude = df.format(Long);
            }
            Location location = new Location("Location");
            location.setLongitude(Double.valueOf(Longitude));
            location.setLatitude(Double.valueOf(Latitude));
            //TODO parse to string to write to file
            //strloc =
        } else {
            //TODO parse to string to write to file
            //locationArray.add(null);
            strloc = "null";
            Lat = null;
            Long = null;
            Latitude = null;
            Longitude = null;
        }
    }

    private Double convertToDegree(String stringDMS) {
        Double result = null;
        String[] DMS = stringDMS.split(",", 3);
        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0 / D1;
        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0 / M1;
        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0 / S1;
        result = new Double(FloatD + (FloatM / 60) + (FloatS / 3600));
        return result;
    }

    /*public Map<String, ArrayList<ArrayList>> getCollectionsArray() {
        return mappedArray;
    }*/
    private ArrayList<String> imageGroupKeysAll = new ArrayList<>();
    private Map<String, ArrayList<String>> retrieveCollectionAll = new TreeMap<>();

    public ArrayList<String> getImageGroupKeys() {
        return imageGroupKeys;
    }

    public void getPreferences() {
        String temp1 = prefs.getString("collectiondata", "");
        String[] results = temp1.split(Pattern.quote("\n"));
        //System.out.println("Deleted" + results[0]);
        String imageGroupString = prefs.getString("imageGroupKeys", "");
        List<String> imageGroupList = Arrays.asList(imageGroupString.split(","));
        imageGroupKeys.clear();
        imageGroupKeys.addAll(imageGroupList);
        imageGroupKeysAll.addAll(imageGroupList);
        for (int i = 0; i < results.length; ++i) {
            if (i % 2 != 0) {
                String tempo = results[i].toString().substring(1, results[i].length() - 2);
                String[] segregate;
                ArrayList<String> tArr = new ArrayList<String>();
                segregate = tempo.split(Pattern.quote("*, "));
                for (int b = 0; b < segregate.length; ++b) {
                    tArr.add(segregate[b]);
                }
                retrieveCollectionAll.put(results[i - 1], tArr);
                if (tArr.size() >= 10) {
                    retrieveCollection.put(results[i - 1], tArr);
                    //System.out.println("tArr" + results[i - 1]);
                } else {
                    imageGroupKeys.remove(results[i - 1]);
                }
            }
        }
        //TODO problem avoided by simply removing the last one since its somehow bugging out
        //imageGroupKeys.remove(imageGroupKeys.size() - 1);
    }

    public Map<String, ArrayList<String>> getMap() {
        getPreferences();
        return retrieveCollection;
    }
}