package mochainc.imagnition;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.data.StreamAssetPathFetcher;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.net.URI;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageFullScreen extends AppCompatActivity {

    private static HackyViewPager mPager;
    private PagerAdapter mPagerAdapter2;
    private static int numberOfItems;
    private int photoPosition;
    private static SlidingUpPanelLayout panel;
    private static Toolbar toolbar;
    private static View gradient;
    private static View gradientBottom;
    private static ArrayList<String> arrayOfPhotos;
    private static View decorView;
    private static final int HIDESTATUSBAR = View.SYSTEM_UI_FLAG_FULLSCREEN;
    private static final int SHOWSTATUSBAR = View.SYSTEM_UI_FLAG_VISIBLE;
    private static LinearLayout bottombar;
    private SharedPreferences prefs;
    private String fileDetailsString;
    private String FileName;
    private List<String> listOfImageData;
    private RelativeLayout relativeLayoutName;
    private RelativeLayout relativeLayoutDate;
    private RelativeLayout relativeLayoutLocation;
    private RelativeLayout relativeLayoutCamera;
    private LinearLayout infoButton;
    private LinearLayout linearLayout;
    private TagScrollListAdapter mTagScrollListAdapter;
    private IDConversion idConversion;
    private ArrayList<String> tagsOfPhoto = new ArrayList<>();
    private RecyclerView tagScrollList;
    private ArrayList<Integer> idTags;
    private ArrayList<ArrayList> arrayList = new ArrayList<>();
    private IDtoPath iDtoPath = new IDtoPath();
    private Map<Integer, ArrayList<String>> e = new TreeMap<Integer, ArrayList<String>>();
    private boolean intentFromPhotoTag = false;
    private List<String> photoLocationList;
    private ImageView map;
    private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_screen);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Bundle photoPositionBundle = getIntent().getExtras();
        photoPosition = photoPositionBundle.getInt("PhotoPosition");
        arrayOfPhotos = photoPositionBundle.getStringArrayList("PhotoArrayList");
        intentFromPhotoTag = photoPositionBundle.getBoolean("IntentFromPhotoTag");


        numberOfItems = arrayOfPhotos.size();

        mPager = (HackyViewPager) findViewById(R.id.pager2);
        mPagerAdapter2 = new MyPagerAdapter2();
        mPager.setAdapter(mPagerAdapter2);
        mPager.setCurrentItem(photoPosition);
        mPager.setPageMargin(50);

        panel = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        relativeLayoutName = (RelativeLayout) panel.findViewById(R.id.photoInfo);
        relativeLayoutDate = (RelativeLayout) panel.findViewById(R.id.DateInfo);
        relativeLayoutLocation = (RelativeLayout) panel.findViewById(R.id.locationInfo);
        relativeLayoutCamera = (RelativeLayout) panel.findViewById(R.id.CameraInfo);
        infoButton = (LinearLayout) findViewById(R.id.infoButtonLinear);
        linearLayout = (LinearLayout) findViewById(R.id.FullScreenImageBottomBar);
        map = (ImageView) panel.findViewById(R.id.map);
        ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
        panel.setScrollableView(scrollView);

        String currentPhoto = arrayOfPhotos.get(photoPosition);


        String imageData = prefs.getString("imgdata", ""); // imagePath + ':' + date + '*' + Latitude + '*' + Longitude + '|';
        listOfImageData = Arrays.asList(imageData.split(Pattern.quote("|")));
        String photoLocation = prefs.getString("LocationAddress", "");
        photoLocationList = Arrays.asList(photoLocation.split(Pattern.quote(";")));
        getInfoDetails(currentPhoto);


        gradient = findViewById(R.id.gradient);
        gradientBottom = findViewById(R.id.gradientBottom);
        bottombar = (LinearLayout) findViewById(R.id.FullScreenImageBottomBar);
        decorView = getWindow().getDecorView();

        idConversion = new IDConversion(this);

        idTags = idConversion.getID(currentPhoto);
        //tagsOfPhoto = idConversion.getAllTags(currentPhoto2);
        //TODO Use this to retrieve photopaths for each ID


        if (idTags.get(0) != -1) {
            tagsOfPhoto = idConversion.readSpecificTags(idTags);
        } else {
            tagsOfPhoto.add(0, "NULL");
        }

        e = iDtoPath.getIDtoPath();
        tagScrollList = (RecyclerView) findViewById(R.id.tagScroll);
        StaggeredGridLayoutManager tagsStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        tagScrollList.setLayoutManager(tagsStaggeredLayoutManager);
        mTagScrollListAdapter = new TagScrollListAdapter(this, tagsOfPhoto, tagScrollList, e, idTags, true);
        tagScrollList.setAdapter(mTagScrollListAdapter);


        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String currentPhoto2 = arrayOfPhotos.get(position);
                getInfoDetails(currentPhoto2);

                //idConversion = new IDConversion(getApplicationContext());
                idTags = idConversion.getID(currentPhoto2);
                //tagsOfPhoto = idConversion.getAllTags(currentPhoto2);
                if (idTags.get(0) != -1) {
                    tagsOfPhoto = idConversion.readSpecificTags(idTags);

                    tagScrollList.setVisibility(View.VISIBLE);
                    mTagScrollListAdapter.updateData(tagsOfPhoto, idTags);
                } else {
                    tagsOfPhoto = new ArrayList<>();
                    tagsOfPhoto.add(0, "NULL");
                    tagScrollList.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        window = getWindow();
        SlidingUpPanelLayout.PanelSlideListener panelSlideListener = new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.DRAGGING) {
                    window.setStatusBarColor(Color.parseColor("#80000000"));
                } else if (newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    window.setStatusBarColor(Color.parseColor("#80000000"));
                }
                else{
                    window.setStatusBarColor(Color.parseColor("#00000000"));
                }
            }
        };
        panel.addPanelSlideListener(panelSlideListener);
    }

    private void getInfoDetails(String currentPhoto) {
        relativeLayoutName.setVisibility(View.VISIBLE);
        relativeLayoutDate.setVisibility(View.VISIBLE);
        relativeLayoutCamera.setVisibility(View.VISIBLE);
        relativeLayoutLocation.setVisibility(View.VISIBLE);
        map.setVisibility(View.VISIBLE);


        String individualImageData = null;
        for (int i = 0; i < listOfImageData.size(); i++) {
            if (listOfImageData.get(i).contains(currentPhoto)) {
                individualImageData = listOfImageData.get(i);
                break;
            }
        }
        String individualLocationAddressFilePath = null;
        for (int k = 0; k < photoLocationList.size(); k++) {
            if (photoLocationList.get(k).contains(currentPhoto)) {
                individualLocationAddressFilePath = photoLocationList.get(k);
                break;
            }
        }


        if (individualImageData != null) {
            infoButton.setVisibility(View.VISIBLE);
            linearLayout.setWeightSum(4);
            List<String> e = Arrays.asList(individualImageData.split(Pattern.quote("?")));
            String imagePath = e.get(0);
            File file = new File(imagePath);
            FileName = file.getName();
            String imageDir = file.getParent();
            DecimalFormat df = new DecimalFormat("###.##");
            long FileSizeInBytes = file.length();
            // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
            float fileSizeInKB = FileSizeInBytes / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
            float fileSizeInMB = fileSizeInKB / 1024;
            String FileSizeString;
            if (fileSizeInMB >= 1) {
                FileSizeString = String.valueOf(df.format(fileSizeInMB)) + " MB";
            } else if (fileSizeInKB >= 1) {
                FileSizeString = String.valueOf(df.format(fileSizeInKB)) + " KB";
            } else {
                FileSizeString = String.valueOf(FileSizeInBytes) + " bytes";
            }


            String dateLatLong = e.get(1);
            String[] f = dateLatLong.split(Pattern.quote("*"));
            String photoDate = f[0];
            String photoTime = f[1];
            String photoLat = f[2];
            String photoLong = f[3];
            String cameraName = f[4];
            String aperture = f[5];
            String shutterspeed = f[6];
            String zoom = f[7];
            String ISO = f[8];

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;

            fileDetailsString = imageDir + "\n" + FileSizeString + "    " + imageWidth + " x " + imageHeight;
            String latLong = photoLat + ", " + photoLong;


            TextView FileNameTextView = (TextView) panel.findViewById(R.id.PropertiesHeader);
            TextView FileDetails = (TextView) panel.findViewById(R.id.PropertiesText);
            FileNameTextView.setText(FileName);
            FileDetails.setText(fileDetailsString);

            TextView Date = (TextView) panel.findViewById(R.id.DateHeader);
            TextView Time = (TextView) panel.findViewById(R.id.DateText);
            Date.setText(photoDate);
            Time.setText(photoTime);


            if (!cameraName.contains("null")) {
                TextView Camera = (TextView) panel.findViewById(R.id.CameraHeader);
                TextView CameraInfo = (TextView) panel.findViewById(R.id.CameraText);
                Camera.setText(cameraName);
                String CameraPhotoInfo = aperture + "    " + shutterspeed + "    " + zoom + "    " + ISO;
                CameraInfo.setText(CameraPhotoInfo);
            } else {
                relativeLayoutCamera.setVisibility(View.GONE);
            }


            if (!photoLat.contains("null") && !photoLong.contains("null")) {
                TextView LocationAddress = (TextView) panel.findViewById(R.id.LocationHeader);
                TextView LatLong = (TextView) panel.findViewById(R.id.LocationText);

                if (individualLocationAddressFilePath != null) {
                    String[] locationArray = individualLocationAddressFilePath.split(Pattern.quote("?"));
                    if (locationArray.length == 3) {
                        LocationAddress.setText(locationArray[1]);
                    } else {
                        LocationAddress.setText("Location address");
                    }
                } else {
                    LocationAddress.setText("Location address");
                }
                LatLong.setText(latLong);
                String url = "http://maps.google.com/maps/api/staticmap?center=" + photoLat + "," + photoLong + "&zoom=17&size=640x640&scale=2&sensor=false&markers=color:red%7C" + photoLat + "," + photoLong;
                Glide.with(this).load(url).crossFade().into(map);
            } else {
                relativeLayoutLocation.setVisibility(View.GONE);
                map.setVisibility(View.GONE);
            }
        } else {
            relativeLayoutName.setVisibility(View.GONE);
            relativeLayoutDate.setVisibility(View.GONE);
            relativeLayoutCamera.setVisibility(View.GONE);
            relativeLayoutLocation.setVisibility(View.GONE);
            infoButton.setVisibility(View.GONE);
            linearLayout.setWeightSum(3);
        }

    }


    @Override
    public void onBackPressed() {
        if (panel.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
            finish();
        } else {
            panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
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
    protected void onPause() {
        super.onPause();
        int currentTab = mPager.getCurrentItem();

        if (!intentFromPhotoTag) {
            prefs.edit().putInt("currentPhotoTab", currentTab).apply();
        }
    }

    public void info(View view) {
        panel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
    }

    public void shareImage(View view) {
        int position = mPager.getCurrentItem();
        String filePath = arrayOfPhotos.get(position);
        String text = "Look at my awesome picture";
        File media = new File(filePath);
        Uri uri = Uri.fromFile(media);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share to"));
    }

    public void deletePhoto(View view) {
        final int position = mPager.getCurrentItem();
        final String filePath = arrayOfPhotos.get(position);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Are you sure you want to delete this photo?");
        dialog.setMessage("This photo will be deleted.");
        dialog.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                arrayOfPhotos.remove(position);
                MyPagerAdapter2.notifyChangeInPosition(1);
                mPagerAdapter2.notifyDataSetChanged();

                File photoToBeDeleted = new File(filePath);
                boolean photoDeleted = photoToBeDeleted.delete();
                if (photoDeleted) {
                    callBroadCast(filePath);
                }
                Log.v("log_tag", "deleted: " + photoDeleted);
            }
        });
        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
            }
        });
        AlertDialog alert = dialog.create();
        alert.show();
        alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
        alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent));
    }

    public void callBroadCast(String filePath) {
        String[] retCol = {MediaStore.Audio.Media._ID};
        Cursor cur = this.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                retCol,
                MediaStore.MediaColumns.DATA + "='" + filePath + "'", null, null);
        if (cur.getCount() == 0) {
            return;
        }
        cur.moveToFirst();
        int id = cur.getInt(cur.getColumnIndex(MediaStore.MediaColumns._ID));
        cur.close();

        Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                id);
        this.getContentResolver().delete(uri, null, null);
    }

    public void editPhoto(View view) {
    }


    private static class MyPagerAdapter2 extends PagerAdapter {
        private static long baseId = 0;
        boolean click = false;

        @Override
        public int getCount() {
            return numberOfItems;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //this is called when notifyDataSetChanged() is called
        @Override
        public int getItemPosition(Object object) {
            // refresh all fragments when data set changed
            return PagerAdapter.POSITION_NONE;
        }


        @Override
        public View instantiateItem(ViewGroup container, int position) {
            final PhotoView photoView = new PhotoView(container.getContext());
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
            String mImagePath = arrayOfPhotos.get(position);
            Glide.with(container.getContext()).load(mImagePath).into(photoView);

            photoView.setMinimumScale(1);
            photoView.setMaximumScale(8);
            photoView.setScale(1);

            photoView.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    hideShowStatusBar();
                    return true;
                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    float x = e.getX();
                    float y = e.getY();
                    if (photoView.getScale() > 1) {
                        photoView.setScale(1, x, y, true);
                        click = true;
                    } else {
                        photoView.setScale(4, x, y, true);
                        click = false;
                    }
                    hideShowStatusBar();
                    return true;
                }

                @Override
                public boolean onDoubleTapEvent(MotionEvent e) {
                    return false;
                }
            });


            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        public static void notifyChangeInPosition(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            baseId += numberOfItems + n;
        }

        public void hideShowStatusBar() {
            if (click) {
                //show
                click = false;
                toolbar.animate().translationY(0).setDuration(300).start();
                gradient.animate().translationY(0).setDuration(300).start();
                gradientBottom.animate().translationY(0).setDuration(300).start();
                bottombar.animate().translationY(0).setDuration(300).start();
                decorView.setSystemUiVisibility(SHOWSTATUSBAR);
            } else if (!click) {
                //hide
                click = true;
                toolbar.animate().translationY(-300).setDuration(300).start();
                gradient.animate().translationY(-300).setDuration(300).start();
                gradientBottom.animate().translationY(300).setDuration(300).start();
                bottombar.animate().translationY(300).setDuration(300).start();
                decorView.setSystemUiVisibility(HIDESTATUSBAR);
            }

            if (panel.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
                panel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        }

    }
}