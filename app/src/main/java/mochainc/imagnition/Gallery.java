package mochainc.imagnition;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.location.Address;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.dragselectrecyclerview.DragSelectRecyclerView;
import com.afollestad.dragselectrecyclerview.DragSelectRecyclerViewAdapter;
import com.afollestad.materialcab.MaterialCab;
import com.bumptech.glide.Glide;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class Gallery extends AppCompatActivity implements SensorEventListener, GalleryListAdapter.ClickListener, DragSelectRecyclerViewAdapter.SelectionListener
        /*implements NavigationView.OnNavigationItemSelectedListener*/ {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private BottomBar mBottomBar;
    private static NonSwipeableViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private static FloatingActionButton fab;

    private static final int HIDESTATUSBAR = View.SYSTEM_UI_FLAG_FULLSCREEN;
    private static final int SHOWSTATUSBAR = View.SYSTEM_UI_FLAG_VISIBLE;
    private View decorView2;


    private static final int REARFACINGCAMERA = 0;
    private static final int FRONTFACINGCAMERA = 1;
    private int mCameraType;
    private static Toolbar toolbar;


    private static GalleryImageData mGalleryImageData;
    private static PsuedoCollections mCollectionsImageData;

    private HandlerThread mBackgroundThread2;
    private Handler mBackgroundHandler2;
    private static View decorView;

    private SensorManager mSensorManager;
    private Sensor mSensorOrientation;
    private SharedPreferences prefs;
    public static ArrayList<String> imagePathArray;
    private static Window window;
    private static PsuedoCollections psuedoCollections;
    private boolean firstTimeReload = true;
    private static MaterialCab cab;
    private static MaterialCab.Callback mCabCallBack;
    private ComsManager comsManager = new ComsManager();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        mPager = (NonSwipeableViewPager) findViewById(R.id.pager);
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);


        final Typeface robotoSlabLight = Typeface.createFromAsset(getAssets(), "fonts/RobotoSlab-Light.ttf");
        TextView actionbartitle = (TextView) findViewById(R.id.actionbartitle);
        actionbartitle.setTypeface(robotoSlabLight);

        decorView = getWindow().getDecorView();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });
        fab.hide();



        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/


        //TO BE CHANGED
        mBottomBar = BottomBar.attachShy((CoordinatorLayout) findViewById(R.id.app_bar_gallery),
                findViewById(R.id.drawer_layout), savedInstanceState);
        //mBottomBar = BottomBar.attach(findViewById(R.id.app_bar_gallery), savedInstanceState);
        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(int menuItemId) {
                if (menuItemId == R.id.bb_menu_camera) {
                    mPager.setCurrentItem(0);

                } else if (menuItemId == R.id.bb_menu_albums) {
                    mPager.setCurrentItem(1);
                } else if (menuItemId == R.id.bb_menu_collections) {
                    mPager.setCurrentItem(2);
                }
            }

            @Override
            public void onMenuTabReSelected(int menuItemId) {
                if (menuItemId == R.id.bb_menu_albums) {
                    if (rootView != null && mPager.getCurrentItem() == 1 && mRecyclerView != null) {
                        mRecyclerView.smoothScrollToPosition(0);
                    }
                } else if (menuItemId == R.id.bb_menu_collections) {
                    if (rootView != null && mPager.getCurrentItem() == 2 && collectionsRecyclerView != null) {
                        collectionsRecyclerView.smoothScrollToPosition(0);
                    }
                }
            }
        });


        // Setting colors for different tabs when there's more than three of them.
        // You can set colors for tabs in three different ways as shown below.
        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        mBottomBar.mapColorForTab(1, ContextCompat.getColor(this, R.color.colorAccent));
        mBottomBar.mapColorForTab(2, ContextCompat.getColor(this, R.color.colorAccent));

        //preventscreentimeout
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.light));
        }

        decorView2 = getWindow().getDecorView();

        //set current tab at the second tab (gallery)
        mPager.setCurrentItem(1);
        mBottomBar.selectTabAtPosition(1, true);


        if (mPager.getCurrentItem() == 0) {
            mBottomBar.setAlpha(0);
            mBottomBar.setTranslationY(300);
            toolbar.setTranslationY(-300);
            fab.hide();
            // Hide the status bar.
            decorView.setSystemUiVisibility(HIDESTATUSBAR);
        }


        ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int page = mPager.getCurrentItem();
                if (page == 0) {
                    fab.hide();
                    mBottomBar.selectTabAtPosition(0, true);
                    mBottomBar.animate().translationY(300).setDuration(300).start();
                    toolbar.animate().translationY(-300).setDuration(300).start();
                    // Hide the status bar.
                    decorView.setSystemUiVisibility(HIDESTATUSBAR);

                    if (rootView != null && psuedoCollections.getStatus() != AsyncTask.Status.FINISHED) {
                        psuedoCollections.cancel(true);
                        firstTimeLoad = true;
                    }


                    if (mBackgroundThread == null) {
                        openBackgroundThread();
                        if (mTextureView.isAvailable()) {
                            setupCamera(mTextureView.getWidth(), mTextureView.getHeight());
                            openCamera();
                        } else {
                            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
                            settedsurface = true;
                        }
                    }

                } else if (page == 1) {
                    mBottomBar.setAlpha(1);
                    fab.hide();
                    mBottomBar.selectTabAtPosition(1, true);
                    mBottomBar.animate().translationY(0).setDuration(300).start();
                    toolbar.animate().translationY(0).setDuration(300).start();

                    decorView.setSystemUiVisibility(SHOWSTATUSBAR);


                    //mGalleryAdapter.updateData(getImagesPath(Gallery.this));

                } else if (page == 2) {
                    mBottomBar.setAlpha(1);
                    fab.show();
                    mBottomBar.selectTabAtPosition(2, true);
                    mBottomBar.animate().translationY(0).setDuration(300).start();
                    toolbar.animate().translationY(0).setDuration(300).start();

                    decorView.setSystemUiVisibility(SHOWSTATUSBAR);

                    if (psuedoCollections != null && psuedoCollections.isCancelled() && psuedoCollections.getStatus() == AsyncTask.Status.FINISHED && firstTimeReload) {
                        firstTimeLoad = false;
                        firstTimeReload = false;
                        rootView = (ViewGroup) findViewById(R.id.collections_rootview);
                        psuedoCollections = new PsuedoCollections(Gallery.this, rootView, toolbar, fab, window);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            psuedoCollections.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imagePathArray);
                        } else {
                            psuedoCollections.execute(imagePathArray);
                        }
                    } else if (firstTimeReload) {
                        firstTimeLoad = false;
                        firstTimeReload = false;
                        rootView = (ViewGroup) findViewById(R.id.collections_rootview);
                        psuedoCollections = new PsuedoCollections(Gallery.this, rootView, toolbar, fab, window);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            psuedoCollections.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imagePathArray);
                        } else {
                            psuedoCollections.execute(imagePathArray);
                        }
                    }

                    if (mBackgroundThread != null) {
                        closeCamera();
                        closeBackgroundThread();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        mPager.addOnPageChangeListener(changeListener);


        mCameraType = REARFACINGCAMERA;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //get image file paths
            imagePathArray = getImagesPath(this);
            mGalleryImageData = new GalleryImageData();
            mGalleryImageData.setImagePathArray(imagePathArray);

            AllImagesPathArray allImagesPathArray = new AllImagesPathArray();
            allImagesPathArray.setImagePathArraystatic(imagePathArray);

            //send image to server
            ArrayList<String> aspath = allImagesPathArray.getImagePathArraystatic();
            comsManager.startComsManager(getApplicationContext(), aspath);
            //new ClientComs(getApplicationContext()).execute(aspath);


        } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            return;
        }


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        cab = new MaterialCab(this, R.id.cab_stub).setMenu(R.menu.cab_menu);

        mCabCallBack = new MaterialCab.Callback() {
            @Override
            public boolean onCabCreated(MaterialCab cab, Menu menu) {
                // The CAB was started, return true to allow creation to continue.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(getResources().getColor(R.color.darkColor));
                }
                return true;
            }

            @Override
            public boolean onCabItemClicked(MenuItem item) {
                // An item in the toolbar or overflow menu was tapped.
                /*if(item==R.id.share){

                }*/
                return true;
            }

            @Override
            public boolean onCabFinished(MaterialCab cab) {
                // The CAB was finished, return true to allow destruction to continue.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(getResources().getColor(R.color.light));
                }
                mGalleryAdapter.clearSelected();
                return true;
            }
        };

    }


    public static final int UPSIDE_DOWN = 2;
    public static final int LANDSCAPE_RIGHT = 3;
    public static final int PORTRAIT = 0;
    public static final int LANDSCAPE_LEFT = 1;
    public int mOrientationDeg; //last rotation in degrees
    public int mOrientationRounded; //last orientation int
    public int tempOrientRounded;
    private static final int _DATA_X = 0;
    private static final int _DATA_Y = 1;
    private static final int _DATA_Z = 2;
    private int ORIENTATION_UNKNOWN = -1;

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        int orientation = ORIENTATION_UNKNOWN;
        float X = -values[_DATA_X];
        float Y = -values[_DATA_Y];
        float Z = -values[_DATA_Z];
        float magnitude = X * X + Y * Y;
        // Don't trust the angle if the magnitude is small compared to the y value
        if (magnitude * 4 >= Z * Z) {
            float OneEightyOverPi = 57.29577957855f;
            float angle = (float) Math.atan2(-Y, X) * OneEightyOverPi;
            orientation = 90 - (int) Math.round(angle);
            // normalize to 0 - 359 range
            while (orientation >= 360) {
                orientation -= 360;
            }
            while (orientation < 0) {
                orientation += 360;
            }
        }


        if (orientation != mOrientationDeg) {
            mOrientationDeg = orientation;
            //figure out actual orientation
            if (orientation == -1) {//basically flat

            } else if (orientation <= 45 || orientation > 315) {
                tempOrientRounded = PORTRAIT;

            } else if (orientation > 45 && orientation <= 135) {
                tempOrientRounded = LANDSCAPE_RIGHT;
            } else if (orientation > 135 && orientation <= 225) {
                tempOrientRounded = UPSIDE_DOWN;
            } else if (orientation > 225 && orientation <= 315) {
                tempOrientRounded = LANDSCAPE_LEFT;
            }

        }

        if (mOrientationRounded != tempOrientRounded) {
            //if original != new
            //Orientation changed, update orientation value
            mOrientationRounded = tempOrientRounded;

            if (rootView != null && mPager.getCurrentItem() == 0 && swapcamera != null) {
                if (mOrientationRounded == PORTRAIT) {
                    swapcamera.animate().rotation(0);
                    beaming.animate().rotation(0);
                    flash.animate().rotation(0);
                } else if (mOrientationRounded == LANDSCAPE_RIGHT) {
                    swapcamera.animate().rotation(-90);
                    beaming.animate().rotation(-90);
                    flash.animate().rotation(-90);
                } else if (mOrientationRounded == LANDSCAPE_LEFT) {
                    swapcamera.animate().rotation(90);
                    beaming.animate().rotation(90);
                    flash.animate().rotation(90);
                } else if (mOrientationRounded == UPSIDE_DOWN) {
                    swapcamera.animate().rotation(180);
                    beaming.animate().rotation(180);
                    flash.animate().rotation(180);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            mSensorManager.registerListener(this, mSensorOrientation, SensorManager.SENSOR_DELAY_FASTEST);
        }
        prefs.edit().putBoolean("isAppRunningInForeground", true).apply();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (rootView != null && mPager.getCurrentItem() == 1) {
                mGalleryAdapter.updateData(getImagesPath(Gallery.this));
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mSensorManager != null && mSensorOrientation != null) {
            mSensorManager.unregisterListener(this, mSensorOrientation);
        }
        prefs.edit().putBoolean("isAppRunningInForeground", false).apply();
    }


    public static ArrayList<String> getImagesPath(Activity activity) {
        Uri uri;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        String PathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        String epochtime = "1970-01-01";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        while (cursor.moveToNext()) {
            PathOfImage = cursor.getString(column_index_data);

            File file = new File(PathOfImage);
            String date = sdf.format(file.lastModified());
            String filenameArray[] = PathOfImage.split("\\.");
            String extension = filenameArray[filenameArray.length - 1];

            if (!date.contains(epochtime)) {
                if (extension.contains("jpg") || extension.contains("JPG") || extension.contains("jpeg") || extension.contains("JPEG")) {
                    listOfAllImages.add(PathOfImage);
                }
            }
        }
        Collections.reverse(listOfAllImages);
        return listOfAllImages;
    }

    @Override
    public void onBackPressed() {
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/

        if (mPager.getCurrentItem() == 1 && mGalleryAdapter != null && mGalleryAdapter.getSelectedCount() > 0) {
            mGalleryAdapter.clearSelected();
        } else {
            super.onBackPressed();
        }
    }

    public void search(View view) {
        Intent e = new Intent(this, Search.class);
        this.startActivity(e);
    }

    public void previewLatestImage(View view) {
        mPager.setCurrentItem(1);
        if (rootView != null && mPager.getCurrentItem() == 1 && mRecyclerView != null) {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    @Override
    public void onClick(int index) {
        // Single click will select or deselect an item
        if (mGalleryAdapter != null && cab != null) {
            if (cab.isActive()) {
                mGalleryAdapter.toggleSelected(index);
            }
            else{
                Intent e = new Intent(this, ImageFullScreen.class);
                e.putExtra("PhotoPosition", index);
                e.putExtra("PhotoArrayList", imagePathArray);
                startActivity(e);
            }
        }
    }

    @Override
    public void onLongClick(int index) {
        // Long click initializes drag selection, and selects the initial item
        if (mRecyclerView != null) {
            mRecyclerView.setDragSelectActive(true, index);
        }
    }

    @Override
    public void onDragSelectionChanged(int count) {
        if (count > 0) {
            cab.start(mCabCallBack);
            cab.setTitle(String.valueOf(count));
            //cab.setTitleRes(R.string.cab_title_x, count);
        } else if (cab != null && cab.isActive()) {
            cab.finish();
        }
    }



    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            intro myFragment = intro.getInstance(position);
            return myFragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    private Size mPreviewSize;
    private String mCameraId;
    private static TextureView mTextureView;
    private TextureView.SurfaceTextureListener mSurfaceTextureListener =
            new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                    //Toast.makeText(getApplicationContext(), "surfacetextureavailable", Toast.LENGTH_LONG).show();
                    setupCamera(width, height);
                    openCamera();

                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                    return false;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surface) {

                }
            };


    private boolean mFlashSupported;

    private CameraDevice mCameraDevice;
    private boolean cameraopen;
    private CameraDevice.StateCallback mCameraDeviceStateCallback =
            new CameraDevice.StateCallback() {
                @Override
                public void onOpened(CameraDevice camera) {
                    mCameraDevice = camera;
                    //Toast.makeText(getApplicationContext(), "Camera Opened", Toast.LENGTH_LONG).show();
                    cameraopen = true;
                    createCameraPreviewSession();
                }

                @Override
                public void onDisconnected(CameraDevice camera) {
                    camera.close();
                    mCameraDevice = null;
                    cameraopen = false;
                    Toast.makeText(getApplicationContext(), "Camera disconnected", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(CameraDevice camera, int error) {
                    camera.close();
                    mCameraDevice = null;
                    cameraopen = false;
                    Toast.makeText(getApplicationContext(), "Camera error", Toast.LENGTH_LONG).show();
                }
            };

    private static final int STATE_PREVIEW = 0;
    private static final int STATE_WAIT_LOCK = 1;
    private static final int STATE_WAITING_PRECAPTURE = 2;
    private static final int STATE_WAITING_NON_PRECAPTURE = 3;
    private static final int STATE_PICTURE_CAPTURED = 4;
    private int mState;

    private CaptureRequest mPreviewCaptureRequest;
    private CaptureRequest.Builder mPreviewCaptureRequestBuilder;
    private CameraCaptureSession mCameraCaptureSession;
    private CameraCaptureSession.CaptureCallback mSessionCaptureCallback
            = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber) {
            super.onCaptureStarted(session, request, timestamp, frameNumber);
        }

        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);

            if (toggleCamera == 0) {
                process(result);
            }
        }

        @Override
        public void onCaptureFailed(CameraCaptureSession session, CaptureRequest request, CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);
            Toast.makeText(getApplicationContext(), "Focus lock failed ", Toast.LENGTH_LONG).show();
        }

        private void process(CaptureResult result) {
            switch (mState) {
                case STATE_PREVIEW: {
                    //do nothing when in preview
                    break;
                }
                case STATE_WAIT_LOCK: {
                    //focus lock when state is about to capture photo after shutter button pressed
                    Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    if (afState == null) {
                        captureStillImage();
                    } else if (afState == CaptureRequest.CONTROL_AF_STATE_FOCUSED_LOCKED || afState == CaptureRequest.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED) {

                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        if (aeState == null ||
                                aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                            mState = STATE_PICTURE_CAPTURED;
                            captureStillImage();
                        } else {
                            runPrecaptureSequence();
                        }
                    }
                    break;
                }
                case STATE_WAITING_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null ||
                            aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                            aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                        mState = STATE_WAITING_NON_PRECAPTURE;
                    }
                    break;
                }
                case STATE_WAITING_NON_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        mState = STATE_PICTURE_CAPTURED;
                        captureStillImage();
                    }
                    break;
                }
            }
        }
    };

    private static File mImageFile;
    private ImageReader mImageReader;
    private ArrayList<String> updatedPaths;
    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener =
            new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    //image available(taken from camera)
                    mBackgroundHandler.post(new ImageSaver(reader.acquireLatestImage()));
                }
            };


    private class ImageSaver implements Runnable {

        private final Image mImage;
        private Bitmap bitmapImage;

        private ImageSaver(Image image) {
            mImage = image;
        }

        @Override
        public void run() {
            ByteBuffer byteBuffer = mImage.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(bytes);

            FileOutputStream fileOutputStream = null;

            try {
                if (mImageFile != null) {
                    fileOutputStream = new FileOutputStream(mImageFile);
                    fileOutputStream.write(bytes);

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    File f = new File(mImageFileLocation);
                    Uri contentUri = Uri.fromFile(f);
                    mediaScanIntent.setData(contentUri);
                    sendBroadcast(mediaScanIntent);
                } else {
                    System.out.println("null image file");
                    captureStillImage();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mImage.close();
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            runOnUiThread(new Runnable() {
                public void run() {
                    if (mImageFile != null && mImageFileLocation != null) {
                        System.out.println(mImageFileLocation);
                        Glide.with(Gallery.this).load(mImageFileLocation).into(previewLatestImage);
                        ArrayList<String> newImagePathArray = new ArrayList<String>();
                        newImagePathArray.addAll(imagePathArray);
                        newImagePathArray.add(0, mImageFileLocation);
                        mGalleryAdapter.updateData(newImagePathArray);
                        comsManager.RestartClientComms(newImagePathArray);
                    }
                }
            });

        }
    }

    View blackOverlay = null;
    static ImageView previewLatestImage = null;

    public void capturePhoto(View shutterButton) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            return;
        } else {
            if (rootView != null && mPager.getCurrentItem() == 0) {
                blackOverlay = findViewById(R.id.blackOverlay);
                blackOverlay.animate()
                        .alpha(1f)
                        .setDuration(100)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                blackOverlay.animate().alpha(0f).setDuration(100).start();
                            }
                        }).start();
            }
            lockFocus();
        }
    }

    //Create image file
    private String mImageFileLocation = "";

    int i = 0;

    File createImageFile() throws IOException {
        i++;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + String.valueOf(i) + ".jpg";
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = new File(storageDirectory, imageFileName);
        mImageFileLocation = image.getAbsolutePath();

        return image;
    }

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private void runPrecaptureSequence() {
        try {
            // This is how to tell the camera to trigger.
            mPreviewCaptureRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
                    CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the precapture sequence to be set.
            mState = STATE_WAITING_PRECAPTURE;
            mCameraCaptureSession.capture(mPreviewCaptureRequestBuilder.build(), mSessionCaptureCallback,
                    mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private void captureStillImage() {
        try {
            CaptureRequest.Builder captureStillBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_ZERO_SHUTTER_LAG);
            captureStillBuilder.addTarget(mImageReader.getSurface());

            //Camera settings here
            captureStillBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);


            //captureStillBuilder.set(CaptureRequest.NOISE_REDUCTION_MODE, CaptureRequest.NOISE_REDUCTION_MODE_FAST);

            if (toggleCamera == 0) {
                //rearfacing
                captureStillBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(mOrientationRounded));
                setAutoFlash(captureStillBuilder);
            } else if (toggleCamera == 1) {
                //frontfacing
                int frontOrientation;
                if (mOrientationRounded == 0) {
                    frontOrientation = 2;
                    captureStillBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(frontOrientation));
                } else if (mOrientationRounded == 2) {
                    frontOrientation = 0;
                    captureStillBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(frontOrientation));
                } else {
                    captureStillBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(mOrientationRounded));
                }
            }

            //image save callback (check if image file is saved)

            CameraCaptureSession.CaptureCallback captureCallback =
                    new CameraCaptureSession.CaptureCallback() {
                        @Override
                        public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber) {
                            super.onCaptureStarted(session, request, timestamp, frameNumber);

                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                try {
                                    //Toast.makeText(getApplicationContext(), "capturestillimagestarted", Toast.LENGTH_LONG).show();
                                    System.out.println("createImagefile");
                                    mImageFile = createImageFile();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            } else if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions((Activity) getApplicationContext(),
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                                return;
                            }
                        }

                        @Override
                        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                            super.onCaptureCompleted(session, request, result);
                            //code for things to do after image is captured, eg updating gallery here
                            unlockFocus();
                        }
                    };

            //mCameraCaptureSession.stopRepeating(); //uncomment to pause preview to show a preview of captured photo
            mCameraCaptureSession.capture(captureStillBuilder.build(), captureCallback, null);


        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private void lockFocus() {
        try {
            //Toast.makeText(getApplicationContext(), "lockfocus", Toast.LENGTH_LONG).show();
            mState = STATE_WAIT_LOCK;
            if (toggleCamera == 0) {
                //rearfacing
                mPreviewCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_START);
                setAutoFlash(mPreviewCaptureRequestBuilder);
                mCameraCaptureSession.capture(mPreviewCaptureRequestBuilder.build(), mSessionCaptureCallback, mBackgroundHandler);
            } else if (toggleCamera == 1) {
                //frontfacing
                captureStillImage();
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void unlockFocus() {
        try {
            mState = STATE_PREVIEW;

            if (toggleCamera == 0) {
                //rearfacing
                mPreviewCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_CANCEL);
                mCameraCaptureSession.capture(mPreviewCaptureRequestBuilder.build(), mSessionCaptureCallback, mBackgroundHandler);

            } else if (toggleCamera == 1) {
                //frontfacing
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setAutoFlash(CaptureRequest.Builder requestBuilder) {
        if (mFlashSupported) {
            if (toggleFlash == AUTO_FLASH) {
                requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                        CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
            } else if (toggleFlash == FLASH_OFF) {
                requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                        CaptureRequest.CONTROL_AE_MODE_ON);
            } else if (toggleFlash == FLASH_ON) {
                requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                        CaptureRequest.CONTROL_AE_MODE_ON_ALWAYS_FLASH);
            }
        }
    }


    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;


    private void openBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera 2 background thread");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void closeBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void openCamera() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                try {
                    //Toast.makeText(getApplicationContext(), "opencamera", Toast.LENGTH_LONG).show();
                    if (ActivityCompat.checkSelfPermission(Gallery.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Gallery.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                        return;
                    } else {

                        cameraManager.openCamera(mCameraId, mCameraDeviceStateCallback, mBackgroundHandler);

                    }
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private void closeCamera() {
        //Toast.makeText(this, "Closed camera", Toast.LENGTH_LONG).show();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                if (mCameraCaptureSession != null) {
                    mCameraCaptureSession.close();
                    mCameraCaptureSession = null;
                }
                if (mCameraDevice != null) {
                    mCameraDevice.close();
                    mCameraDevice = null;
                }
                if (mImageReader != null) {
                    mImageReader.close();
                    mImageReader = null;
                }
            }
        });
    }

    private void createCameraPreviewSession() {
        try {
            SurfaceTexture surfaceTexture = mTextureView.getSurfaceTexture();
            surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            Surface previewSurface = new Surface(surfaceTexture);
            mPreviewCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewCaptureRequestBuilder.addTarget(previewSurface);
            mCameraDevice.createCaptureSession(Arrays.asList(previewSurface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(CameraCaptureSession session) {
                            if (mCameraDevice == null) {
                                return;
                            }
                            try {
                                // Auto focus should be continuous for camera preview.
                                mPreviewCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

                                setAutoFlash(mPreviewCaptureRequestBuilder);

                                mPreviewCaptureRequest = mPreviewCaptureRequestBuilder.build();
                                mCameraCaptureSession = session;
                                mCameraCaptureSession.setRepeatingRequest(
                                        mPreviewCaptureRequest,
                                        mSessionCaptureCallback,
                                        mBackgroundHandler
                                );
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession session) {
                            Toast.makeText(getApplicationContext(), "create camera session failed", Toast.LENGTH_LONG).show();
                        }
                    }, null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    static ViewGroup rootView;
    private static DragSelectRecyclerView mRecyclerView;
    private static StaggeredGridLayoutManager mStaggeredLayoutManager;
    private static GalleryListAdapter mGalleryAdapter;


    private static RecyclerView collectionsRecyclerView;
    private static StaggeredGridLayoutManager collectionsStaggeredLayoutManager;
    private static CollectionsListAdapter mCollectionsAdapter;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!

                    if (mSensorManager != null) {
                        mSensorManager.registerListener(this, mSensorOrientation, SensorManager.SENSOR_DELAY_FASTEST);
                    } else {
                        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                        mSensorOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                        mSensorManager.registerListener(this, mSensorOrientation, SensorManager.SENSOR_DELAY_FASTEST);
                    }

                    if (mPager.getCurrentItem() == 0) {
                        openCamera();
                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    imagePathArray = getImagesPath(this);
                    mGalleryImageData = new GalleryImageData();
                    mGalleryImageData.setImagePathArray(imagePathArray);


                    if (rootView != null) {
                        mRecyclerView = (DragSelectRecyclerView) findViewById(R.id.galleyList);
                        mStaggeredLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
                        mGalleryAdapter = new GalleryListAdapter(this);
                        mGalleryAdapter.setSelectionListener(this);
                        mGalleryAdapter.GalleryListAdapterPassVariables(this, mGalleryImageData, false, "", 3, 0);
                        mRecyclerView.setAdapter(mGalleryAdapter);
                        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
                        mRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels, 3, false));

                        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                if (!cab.isActive()) {
                                    if (newState == RecyclerView.SCROLL_STATE_IDLE) { // on scroll stop
                                        toolbar.animate().translationY(0).setDuration(300).start();


                                        window.setStatusBarColor(getResources().getColor(R.color.light));


                                    } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                                        toolbar.animate().translationY(-300).setDuration(300).start();
                                        window.setStatusBarColor(Color.parseColor("#80000000"));
                                    } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                                    }
                                }
                            }
                        });
                    }
                } else {
                    finish();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private static ImageButton swapcamera;
    private static ImageButton beaming;
    private static ImageButton flash;
    private static boolean firstTimeLoad = true;

    public static class intro extends Fragment implements
            GalleryListAdapter.ClickListener, DragSelectRecyclerViewAdapter.SelectionListener {
        public static intro getInstance(int position) {
            intro myFragment = new intro();
            Bundle args = new Bundle();
            args.putInt("position", position);
            myFragment.setArguments(args);
            return myFragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = null;
            Bundle bundle = getArguments();


            if (bundle != null) {
                if (bundle.getInt("position") == 0) {
                    rootView = (ViewGroup) inflater.inflate(
                            R.layout.content_camera, container, false);

                    swapcamera = (ImageButton) rootView.findViewById(R.id.swapcamera);
                    beaming = (ImageButton) rootView.findViewById(R.id.beaming);
                    flash = (ImageButton) rootView.findViewById(R.id.flash);

                    if (toggleCamera == 0) {
                        swapcamera.setBackground(getResources().getDrawable(R.drawable.ic_camera_front_white_24dp));
                    } else if (toggleCamera == 1) {
                        swapcamera.setBackground(getResources().getDrawable(R.drawable.ic_camera_rear_white_24dp));
                    }

                    if (toggleBeaming == BEAMING_ON) {
                        beaming.setBackground(getResources().getDrawable(R.drawable.ic_wifi_tethering_white_24dp));
                    } else if (toggleBeaming == BEAMING_OFF) {
                        beaming.setBackground(getResources().getDrawable(R.drawable.ic_portable_wifi_off_white_24dp));
                    }

                    if (toggleFlash == AUTO_FLASH) {
                        flash.setBackground(getResources().getDrawable(R.drawable.ic_flash_auto_white_24dp));
                    } else if (toggleFlash == FLASH_ON) {
                        flash.setBackground(getResources().getDrawable(R.drawable.ic_flash_on_white_24dp));
                    } else if (toggleFlash == FLASH_OFF) {
                        flash.setBackground(getResources().getDrawable(R.drawable.ic_flash_off_white_24dp));
                    }
                    if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        previewLatestImage = (ImageView) rootView.findViewById(R.id.previewLatestImage);
                        if(imagePathArray.size()>0) {
                            String latestImagePath = imagePathArray.get(0);
                            Glide.with(this.getActivity()).load(latestImagePath).into(previewLatestImage);
                        }
                    }

                    mTextureView = (TextureView) rootView.findViewById(R.id.textureView);

                } else if (bundle.getInt("position") == 1) {
                    rootView = (ViewGroup) inflater.inflate(
                            R.layout.content_gallery, container, false);


                    if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        mRecyclerView = (DragSelectRecyclerView) rootView.findViewById(R.id.galleyList);
                        mStaggeredLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
                        mGalleryAdapter = new GalleryListAdapter(this);
                        mGalleryAdapter.GalleryListAdapterPassVariables(this.getActivity(), mGalleryImageData, false, "", 3, 0);
                        mGalleryAdapter.setSelectionListener(this);
                        mRecyclerView.setAdapter(mGalleryAdapter);
                        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
                        mRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels, 3, false));


                        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                if (cab == null || !cab.isActive()) {
                                    if (newState == RecyclerView.SCROLL_STATE_IDLE) { // on scroll stop
                                        toolbar.animate().translationY(0).setDuration(300).start();


                                        window.setStatusBarColor(getResources().getColor(R.color.light));


                                    } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                                        toolbar.animate().translationY(-300).setDuration(300).start();
                                        window.setStatusBarColor(Color.parseColor("#80000000"));
                                    } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                                    }
                                }
                            }
                        });
                    }

                } else if (bundle.getInt("position") == 2) {
                    rootView = (ViewGroup) inflater.inflate(
                            R.layout.content_collections, container, false);


                    if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        if (firstTimeLoad) {
                            firstTimeLoad = false;
                            psuedoCollections = new PsuedoCollections(this.getActivity(), rootView, toolbar, fab, window);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                psuedoCollections.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imagePathArray);
                            } else {
                                psuedoCollections.execute(imagePathArray);
                            }
                        } else if (psuedoCollections.getStatus() == AsyncTask.Status.FINISHED) {
                            collectionsRecyclerView = (RecyclerView) rootView.findViewById(R.id.collectionsList);
                            collectionsStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
                            collectionsRecyclerView.setLayoutManager(collectionsStaggeredLayoutManager);
                            mCollectionsAdapter = new CollectionsListAdapter(getContext(), psuedoCollections.getMap(), psuedoCollections.getImageGroupKeys());
                            collectionsRecyclerView.setAdapter(mCollectionsAdapter);

                            collectionsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);
                                    if (newState == RecyclerView.SCROLL_STATE_IDLE) { // on scroll stop
                                        toolbar.animate().translationY(0).setDuration(300).start();
                                        fab.show();
                                        window.setStatusBarColor(getResources().getColor(R.color.light));
                                    } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                                        toolbar.animate().translationY(-300).setDuration(300).start();
                                        fab.hide();
                                        window.setStatusBarColor(Color.parseColor("#80000000"));
                                    } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                                    }
                                }
                            });
                        }
                    }
                }
            }

            return rootView;
        }


        @Override
        public void onClick(int index) {
            // Single click will select or deselect an item
            if (mGalleryAdapter != null && cab != null) {
                if (cab.isActive()) {
                    mGalleryAdapter.toggleSelected(index);
                }
                else{
                    Intent e = new Intent(this.getContext(), ImageFullScreen.class);
                    e.putExtra("PhotoPosition", index);
                    e.putExtra("PhotoArrayList", imagePathArray);
                    startActivity(e);
                }
            }
        }

        @Override
        public void onLongClick(int index) {
            // Long click initializes drag selection, and selects the initial item
            if (mRecyclerView != null) {
                mRecyclerView.setDragSelectActive(true, index);
            }
        }

        @Override
        public void onDragSelectionChanged(int count) {
            if (count > 0) {
                cab.start(mCabCallBack);
                cab.setTitle(String.valueOf(count));
                //cab.setTitleRes(R.string.cab_title_x, count);
            } else if (cab != null && cab.isActive()) {
                cab.finish();
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }

    private boolean settedsurface;


    @Override
    public void onResume() {
        super.onResume();
        if (mPager.getCurrentItem() == 0) {
            decorView2.setSystemUiVisibility(HIDESTATUSBAR);
        }
        if (rootView != null) {

            if (mPager.getCurrentItem() == 1 && mRecyclerView != null) {
                int currentTab = prefs.getInt("currentPhotoTab", 0);
                if ((currentTab - 4) >= 0) {
                    mRecyclerView.scrollToPosition(currentTab - 4);
                } else {
                    mRecyclerView.scrollToPosition(currentTab - 4);
                }
            }

            if (mBackgroundThread == null) {
                openBackgroundThread();
                if (mTextureView.isAvailable()) {
                    setupCamera(mTextureView.getWidth(), mTextureView.getHeight());
                    openCamera();
                } else {
                    mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
                    settedsurface = true;
                }
            } else if (mBackgroundThread != null) {
                closeCamera();
                closeBackgroundThread();
            }
        } else {
            settedsurface = false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        prefs.edit().putInt("currentPhotoTab", 0).apply();

        if (rootView != null && psuedoCollections != null && psuedoCollections.getStatus() != AsyncTask.Status.FINISHED) {
            psuedoCollections.cancel(true);
            firstTimeLoad = true;
        }

        if (mBackgroundThread != null) {
            closeCamera();
            closeBackgroundThread();
        }
    }

    private static int toggleCamera = 0;
    private static final int AUTO_FLASH = 0;
    private static final int FLASH_OFF = 1;
    private static final int FLASH_ON = 2;
    private static int toggleFlash = AUTO_FLASH;
    private static final int BEAMING_ON = 0;
    private static final int BEAMING_OFF = 1;
    private static int toggleBeaming = BEAMING_ON;

    public void swapcamera(View view) {

        if (toggleCamera == 0) {
            mCameraType = FRONTFACINGCAMERA;

            if (mBackgroundThread != null) {
                closeCamera();
                closeBackgroundThread();

                openBackgroundThread();
                if (mTextureView.isAvailable()) {
                    setupCamera(mTextureView.getWidth(), mTextureView.getHeight());
                    openCamera();
                } else {
                    mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
                    settedsurface = true;
                }
            }

            view.setBackground(getResources().getDrawable(R.drawable.ic_camera_rear_white_24dp));
            toggleCamera = 1;
        } else if (toggleCamera == 1) {
            mCameraType = REARFACINGCAMERA;

            if (mBackgroundThread != null) {
                closeCamera();
                closeBackgroundThread();

                openBackgroundThread();
                if (mTextureView.isAvailable()) {
                    setupCamera(mTextureView.getWidth(), mTextureView.getHeight());
                    openCamera();
                } else {
                    mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
                    settedsurface = true;
                }
            }

            view.setBackground(getResources().getDrawable(R.drawable.ic_camera_front_white_24dp));

            toggleCamera = 0;
        }
    }

    public void beaming(View view) {
        if (toggleBeaming == BEAMING_OFF) {
            view.setBackground(getResources().getDrawable(R.drawable.ic_wifi_tethering_white_24dp));
            toggleBeaming = BEAMING_ON;
        } else if (toggleBeaming == BEAMING_ON) {
            view.setBackground(getResources().getDrawable(R.drawable.ic_portable_wifi_off_white_24dp));
            toggleBeaming = BEAMING_OFF;
        }
    }

    public void flash(View view) {
        if (toggleFlash == AUTO_FLASH) {
            view.setBackground(getResources().getDrawable(R.drawable.ic_flash_on_white_24dp));
            toggleFlash = FLASH_ON;
        } else if (toggleFlash == FLASH_ON) {
            view.setBackground(getResources().getDrawable(R.drawable.ic_flash_off_white_24dp));
            toggleFlash = FLASH_OFF;
        } else if (toggleFlash == FLASH_OFF) {
            view.setBackground(getResources().getDrawable(R.drawable.ic_flash_auto_white_24dp));
            toggleFlash = AUTO_FLASH;
        }
        setAutoFlash(mPreviewCaptureRequestBuilder);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupCamera(int width, int height) {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {

            //rearfacingcamera
            if (mCameraType == REARFACINGCAMERA) {
                for (String cameraId : cameraManager.getCameraIdList()) {
                    CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
                    if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                        continue;
                    }
                    StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                    //output image
                    Size largestImageSize = Collections.max(
                            Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                            new Comparator<Size>() {
                                @Override
                                public int compare(Size lhs, Size rhs) {
                                    return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getWidth() * rhs.getHeight());
                                }
                            }
                    );
                    mImageReader = ImageReader.newInstance(largestImageSize.getWidth(), largestImageSize.getHeight(), ImageFormat.JPEG, 1);
                    mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mBackgroundHandler);

                    mPreviewSize = getPreferredPreviewSize(map.getOutputSizes(SurfaceTexture.class), width, height);
                    mCameraId = cameraId;


                    // Check if the flash is supported.
                    Boolean available = cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                    mFlashSupported = available == null ? false : available;

                    return;
                }
            }
            //frontfacingcamera
            else if (mCameraType == FRONTFACINGCAMERA) {
                for (String cameraId : cameraManager.getCameraIdList()) {
                    CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
                    if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                        continue;
                    }
                    StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                    //output image
                    Size largestImageSize = Collections.max(
                            Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                            new Comparator<Size>() {
                                @Override
                                public int compare(Size lhs, Size rhs) {
                                    return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getWidth() * rhs.getHeight());
                                }
                            }
                    );
                    mImageReader = ImageReader.newInstance(largestImageSize.getWidth(), largestImageSize.getHeight(), ImageFormat.JPEG, 1);
                    mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mBackgroundHandler);

                    mPreviewSize = getPreferredPreviewSize(map.getOutputSizes(SurfaceTexture.class), width, height);
                    mCameraId = cameraId;
                    return;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Size getPreferredPreviewSize(Size[] mapSizes, int width, int height) {
        List<Size> collectedSizes = new ArrayList<>();
        for (Size option : mapSizes) {
            if (width > height) {
                if (option.getWidth() > width && option.getHeight() > height) {
                    collectedSizes.add(option);
                } else {
                    if (option.getWidth() > height && option.getHeight() > width) {
                        collectedSizes.add(option);
                    }
                }
            }
        }

        if (collectedSizes.size() > 0) {
            return Collections.min(collectedSizes, new Comparator<Size>() {
                @Override
                public int compare(Size lhs, Size rhs) {
                    return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getWidth() * rhs.getHeight());
                }
            });
        }

        return mapSizes[0];
    }
}
