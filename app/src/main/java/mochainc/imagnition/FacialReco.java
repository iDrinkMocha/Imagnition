package mochainc.imagnition;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;

import com.kairos.Kairos;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/**
 * Detection algorithm and segmentation
 * Created by Chun Wei 10/7/2016
 */

public class FacialReco {
    public FacialReco(Context context, ArrayList<String> ifpa) {
//        Kairos kr = new Kairos();
        DetectFace df = new DetectFace(context);
//        final String APP_ID = "11cb3e69";
//        final String API_KEY = "047c4d497a6744f26f47daea53630f26";
//        kr.setAuthentication(context, APP_ID, API_KEY);
        System.out.println("FACE:Hello, OpenCV");

        // Load the native library.
//        System.loadLibrary("opencv_java3");
        //TODO parse required fields
        for(int i = 0; i < 1; ++i){
            String path = ifpa.get(i);
            df.startDetection(path);
        }
    }
}


