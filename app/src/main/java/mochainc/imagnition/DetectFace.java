package mochainc.imagnition;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;

import com.kairos.Kairos;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Chun Wei on 10/7/2016.
 */
public class DetectFace {
    private int counter = 0;
    private Bitmap bmp;
    private InputStream inputStream;
    private File f;
    private CascadeClassifier faceDetector;
    Context context1;
    Kairos kr1;
    FaceSDKManager fsm;
    boolean facedetected = false;

    public DetectFace(Context context){
         context1 = context;
    }

    public boolean startDetection(String path) {
        System.out.println("\nFACE:Running DetectFaceDemo");
        //TODO Change file paths, change to link to the xml file
        //xml file is located under the "res" folder
        try{
            // load cascade file from application resources
            InputStream is = context1.getResources().openRawResource(R.raw.haarcascade_frontalface_alt);
            File cascadeDir = context1.getDir("cascade", Context.MODE_PRIVATE);
            //File mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt.xml");
            //File mCascadeFile = new File(cascadeDir, "haarcascade_mcs_nose.xml");
            //File mCascadeFile = new File(cascadeDir, "haarcascade_eye.xml");
            //File mCascadeFile = new File(cascadeDir, "haarcascade_mcs_mouth.xml");
            File mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();

            faceDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
            if (faceDetector.empty()) {
                System.out.println("FACE:Failed to load xml");
                faceDetector = null;
            } else
                System.out.println("FACE:loadedxml");

            cascadeDir.delete();
        }catch(IOException e){
            e.printStackTrace();
        }



        Mat image = Imgcodecs.imread(path);
        bmp = BitmapFactory.decodeFile(path);

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        System.out.println(String.format("FACE:Detected %s faces",
                faceDetections.toArray().length));

        if(faceDetections.toArray().length != 0){ facedetected = true;}
        else {facedetected = false;}
        return facedetected;
    }

    public void createThumbnail(MatOfRect fd, Mat img, String path){
        // Crop
        for (Rect rect : fd.toArray()) {
            Imgproc.rectangle(img, new Point(rect.x, rect.y), new Point(rect.x
                    + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));

            Bitmap croppedbmp = Bitmap.createBitmap(bmp, rect.x, rect.y, rect.width, rect.height);

            //Resize
            Bitmap resizedbmp = Bitmap.createScaledBitmap(croppedbmp, 300, 300, true);
            //TODO get application context, all training set will be written into app folder
            ContextWrapper cw = new ContextWrapper(context1);
            File directory = cw.getDir("train", Context.MODE_PRIVATE);
            if (!directory.exists()) {
                directory.mkdir();
            }
            File mypath = new File(directory, path + counter + ".jpg");
            System.out.println("FACE:Wrote file - " + path  + counter + ".jpg");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);
                resizedbmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.close();
            } catch (Exception e) {
                Log.e("SAVE_IMAGE", e.getMessage(), e);
            }
            counter++;
        }
    }

}