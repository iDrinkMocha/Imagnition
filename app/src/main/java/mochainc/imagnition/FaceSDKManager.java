package mochainc.imagnition;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.kairos.*;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Chun Wei on 17/8/2016.
 */
public class FaceSDKManager {
    Kairos ikr;
    Context ctxt;
    boolean success;
    int count = 0;

    public FaceSDKManager(Context context, Kairos kr){
        ikr = kr;
        ctxt = context;
    }
    //TODO Change return type
    public void enroll(String ipath){
        //Can ignore this listener is not impt
        KairosListener dtctlist = new KairosListener() {
            @Override
            public void onSuccess(String response) {

                //part 5 should be Face ID
                String[] part = response.split(Pattern.quote(":"));
                String[] id = part[5].split(Pattern.quote(","));
                System.out.println("KFACE: " + response);
                System.out.println("KFACE: " + id[0]);
                //TODO map key(ID) to data (path)
            }

            @Override
            public void onFail(String response) {
                System.out.println("KFACE: " + response);
            }
        };

        Bitmap bmp = BitmapFactory.decodeFile(ipath);
        try{

                /*
                TODO request - recognise
                if fail - enroll
                if ok - group photos -> file path and face
                reverse map - face ID to file paths
                Key to this is that if face is recognized, the face will not be enrolled

                 */
            String[] parts = ipath.split(Pattern.quote("/"));
            //bmp, personid, galleryid, selector, multiface, minheadscale, listener
            ikr.enroll(bmp, "ppl" + count , "grpunknown", null, null , null, dtctlist);
            count++;
            //fetch thumbnail using OpenCV method
            //remove grayscale for thumbnail

        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public void recognise(String ipath){
        //Used for recog
        KairosListener recoglist = new KairosListener() {
            @Override
            public void onSuccess(String s) {
                System.out.println("KFACE: " + s);
                success = true;
            }

            @Override
            public void onFail(String s) {
                System.out.println("KFACE: " + s);
                success = false;
            }
        };

        Bitmap bmp = BitmapFactory.decodeFile(ipath);
        try{
//        bmp, galleryid, selector, threshold, minheadscale, maxnumresults, listener
            ikr.recognize(bmp, "grpunknown", null, null , null, "1", recoglist);
        } catch(Exception e){
            e.printStackTrace();
        }

    }
    public void listGalleries(){
        //Can ignore this listener is not impt
        KairosListener dtctlist = new KairosListener() {
            @Override
            public void onSuccess(String response) {
                System.out.println("KFACE: " + response);

            }

            @Override
            public void onFail(String response) {
                System.out.println("KFACE: " + response);
            }
        };
        try {
            ikr.listGalleries(dtctlist);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
