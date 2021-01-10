package mochainc.imagnition;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private SharedPreferences prefs;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if(prefs.getBoolean("isAppRunningInForeground", false)) {
            System.out.println("SERVICE IS RUNNING WITH APP IN FOREGROUND");
            //final Thread t = new Thread(new ClientComs());
            int status = NetworkUtil.getConnectivityStatusString(context);

            if (!"android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
                if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                    //t.interrupt();
                    System.out.println("Network Disconnected");
                } else if (status == NetworkUtil.NETWORK_STAUS_WIFI) {
                    //t.start();
                    System.out.println("Network Reconnected to wifi");
                } else if (status == NetworkUtil.NETWORK_STATUS_MOBILE) {
                    //t.interrupt();
                    System.out.println("Network Reconnected to mobile data");
                }

            }
        }
        else {
            System.out.println("SERVICE IS RUNNING WITH APP CLOSED");
        }


    }
}