package mochainc.imagnition;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        Boolean network = isNetworkAvailable();

        ImageView launchlogo = (ImageView) findViewById(R.id.launchlogo);
        Glide.with(this).load(R.drawable.applogo).into(launchlogo);

        /*if (!network) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Network Connection is disabled");
            dialog.setMessage("Imagnition requires a network connection to function.");
            dialog.setPositiveButton("Open settings", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    startActivity(myIntent);
                }
            });
            dialog.setNegativeButton("Close app", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    finish();
                }
            });
            AlertDialog alert = dialog.create();
            alert.show();
            alert.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.rgb(1, 87, 155));
            alert.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.rgb(1, 87, 155));

        }*/
        //else {
            /*if (prefs.getBoolean("FirstTime", true) == true) {
                prefs.edit().putString("stepLength", "0.70104").commit();
                Intent i = new Intent(FirstTimeRunChecker.this, SetupScreen.class);
                this.startActivity(i);
            } else {*/
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent mainIntent = new Intent(SplashScreen.this, Gallery.class);
                        SplashScreen.this.startActivity(mainIntent);
                        SplashScreen.this.finish();
                    }
                }, 0);
            //}
        //}

    }



    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
