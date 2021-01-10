package mochainc.imagnition;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.ExifInterface;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Chun Wei on 31/5/2016.
 */
public class ClientComs extends AsyncTask<ArrayList<String>, Void, ArrayList<String>> {

    private static byte size;
    private static String[] instorage;

    private String simplifiedTags;
    private static String serverName = null;
    private static int port;
    private static String dpath;
    private Context mContext;
    private static ArrayList<String> phototags = new ArrayList<String>();
    private SharedPreferences prefs;
    private ExifInterface exif;
    private DetectFace df;
    private String finalised = "";

    public ClientComs(Context context) {
        mContext = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        df = new DetectFace(context);
    }


    @Override
    protected ArrayList<String> doInBackground(final ArrayList<String>... spath) {
        final ArrayList<String> photopaths = spath[0];
        serverName = "52.76.60.116"; //127.0.0.1 , 52.76.60.116
        port = 12345;

        //TODO change to iterator
        for (int i = 0; i < photopaths.size(); i++) {
            if (this.isCancelled()) {
                System.out.println("cancelled");
                break;
            }

            dpath = photopaths.get(i);
            try {
                exif = new ExifInterface(dpath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (exif != null) {
                if (exif.getAttribute("UserComment") == null || !exif.getAttribute("UserComment").contains("IMAGNITIONS")) {
                    String tagged = postImage();
                    phototags.add(tagged);
                    boolean facedtcted =  df.startDetection(dpath);
                    if(facedtcted){
                        finalised = "|IMGFACE|1861:people" + tagged;
                    }else{
                        finalised = "|IMGFACE|" + tagged;
                    }
                    try {
                        exif.setAttribute("UserComment", finalised);
                        exif.saveAttributes();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //HERE
                    System.out.println("OK" + finalised);

                    //TODO SEE THE DIFFERENCE DO YOU SEE IT JORELLE SEE
                    //String original = prefs.getString(dpath + ":tags", "");
                    //prefs.edit().putString(dpath + ":tags", tagged).apply();

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    boolean facedtcted =  df.startDetection(dpath);
                    if(facedtcted){
                        finalised = "|IMGFACE|1861:people" + exif.getAttribute("UserComment");
                    }else{
                        finalised = "|IMGFACE|" + exif.getAttribute("UserComment");
                    }
                    try {
                        exif.setAttribute("UserComment", finalised);
                        exif.saveAttributes();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        return phototags;
    }

    protected void onPostExecute(ArrayList<String> phototags) {
        //prefs.edit().putInt("firstruncoms", 0).apply();
    }

    private String postImage() {
        File file = new File(dpath);
        byte[] bytes = new byte[(int) file.length()];
        BufferedInputStream bis;
        String temp = "";
        DataInputStream dIn;
        String input = "";

        try {
            InetAddress ip = InetAddress.getLocalHost();
            String ipv4 = ip.getHostAddress();
            System.out.println(ipv4);
            Socket client = new Socket(serverName, port);
            DataOutputStream output = new DataOutputStream(client.getOutputStream());


            //Declare ID 1 is IP address
            output.writeByte(1);
            output.flush();
            //Send IP over
            output.writeUTF(ipv4);
            output.flush();
            //Declare IP sent
            output.writeByte(-1);
            output.flush();


            try {
                bis = new BufferedInputStream(new FileInputStream(file));
                bis.read(bytes, 0, bytes.length);

                ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
                oos.writeObject(bytes);
                oos.flush();

                final String sentMsg = "File sent to: " + client.getInetAddress();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

//                BufferedImage bimg = ImageIO.read(new File("C:\\Users\\Chun Wei\\Desktop\\Events\\Photos\\27april.jpg"));
//                ImageIO.write(bimg,"JPG",client.getOutputStream());
            System.out.println("Sent");
            //client.shutdownOutput();

            dIn = new DataInputStream(client.getInputStream());
            int check = dIn.readByte();
            if (check == 3) {
                size = dIn.readByte();
                System.out.println(size);
                input = dIn.readUTF();
                System.out.println("ALL" + input);
                client.close();
            } else {
                client.close();
                System.out.println("Failed");
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("no data");
        }


        //TODO decide if you want the semicolon or not
        temp = input;

        return temp;
    }
}