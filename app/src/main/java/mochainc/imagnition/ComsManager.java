package mochainc.imagnition;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Chun Wei on 6/6/2016.
 */
public class ComsManager {
    private Context mcontext;
    private ClientComs clientComs;

    public ComsManager() {
    }

    public void startComsManager(Context context, final ArrayList<String> aspath){
        //GalleryImageData gim = new GalleryImageData();
        System.loadLibrary("opencv_java3");
        mcontext = context;
        clientComs = new ClientComs(context);

        //IDConversion idc = new IDConversion(context);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int state = prefs.getInt("firstruncoms", 1);
        if (state == 1) {
            //TODO Initial run
            //prefs.edit().putInt("firstruncoms", 0).apply(); // declare app is already run for the first time
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
            if (isConnected && isWiFi) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                    new FacialReco(context, aspath);
                    clientComs.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, aspath);
                } else {
//                    new FacialReco(context, aspath);
                    clientComs.execute(aspath);
                }
            }


            /*TODO To avoid null, make sure this is called after imagePathArray is set in gallery
            initial is apath = Gallery.imagePathArray, simply removed unused variable
             */
            /*ClientComs cc = new ClientComs(context);
            cc.doInBackground(Gallery.imagePathArray);*/
        } else {
            //TODO Subsequent runs
            prefs.edit().putInt("firstruncoms", 0).apply(); // Just in case
            /*TODO Create class, should also create a failed: preferences
             to retag those which failed in subsequent runs
             or even retag automatically after initial first run
            */
        }
    }

    public void RestartClientComms(ArrayList<String> newImagePathArray){
        if(clientComs!=null){
            if(clientComs.getStatus() == AsyncTask.Status.FINISHED){
                clientComs = new ClientComs(mcontext);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    clientComs.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, newImagePathArray);
                }
                else{
                    clientComs.execute(newImagePathArray);
                }
            }
            else{
                clientComs.cancel(true);
                clientComs = new ClientComs(mcontext);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    clientComs.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, newImagePathArray);
                }
                else{
                    clientComs.execute(newImagePathArray);
                }
            }
        }
    }
}