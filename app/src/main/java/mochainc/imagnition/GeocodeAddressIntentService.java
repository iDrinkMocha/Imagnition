package mochainc.imagnition;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class GeocodeAddressIntentService extends IntentService {

    private SharedPreferences settings;
    private String errorMessage = "";
    private static final String TAG = "FetchAddyIntentService";

    public GeocodeAddressIntentService() {
        super("GeocodeAddressIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        System.out.println("OnHandleGeoCodeAddressIntent");

        Map<String, Location> stringLocationMap = (Map<String, Location>) intent.getSerializableExtra("LocationsArrays");
        ArrayList<String> filepathsArray = new ArrayList<>();
        filepathsArray.addAll(stringLocationMap.keySet());

        String modifiedOutput = "";
        Map<String, ArrayList<String>> addressToPath = new TreeMap<>();
        AddressToPath adPath = new AddressToPath();


        for (int g = 0; g < filepathsArray.size(); g++) {
            String filePath = filepathsArray.get(g);
            Location location = stringLocationMap.get(filePath);

            try {
                addresses = geocoder.getFromLocation(
                        location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException ioException) {
                errorMessage = "Service Not Available";
                Log.e(TAG, errorMessage, ioException);
                addresses = null;
            } catch (IllegalArgumentException illegalArgumentException) {
                errorMessage = "Invalid Latitude or Longitude Used";
                Log.e(TAG, errorMessage + ". " +
                        "Latitude = " + location.getLatitude() + ", Longitude = " +
                        location.getLongitude(), illegalArgumentException);
                addresses = null;
            }


            if (addresses == null || addresses.size() == 0) {
                if (errorMessage.isEmpty()) {
                    errorMessage = "Not Found";
                }
                System.out.println(errorMessage);
            } else {
                for (Address address : addresses) {
                    String outputAddress = "";
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        outputAddress += " --- " + address.getAddressLine(i);
                    }
                }

                Address address = addresses.get(0);
                String outputAddress = "";
                String ouputAddressGeneral = "";

                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    outputAddress += address.getAddressLine(i);
                }

                if (outputAddress.contains(" ")) {
                    String[] outputAddressWords = outputAddress.split(" ");
                    ouputAddressGeneral = "";
                    for (String s : outputAddressWords) {
                        String temp = s.replaceAll("\\d+.*", "");
                        if (s.equals("E")) {
                            temp = "East";
                        }
                        if (s.equals("N")) {
                            temp = "North";
                        }
                        if (s.equals("W")) {
                            temp = "West";
                        }
                        if (s.equals("S")) {
                            temp = "South";
                        }
                        if (s.equals("Way") || s.equals("Ave") || s.equals("Rd") || s.equals("Heights") || s.equals("St") || s.equals("Street") || s.equals("Dr") || s.equals("Boulevard") || s.equals("Blvd") || s.equals("Link") || s.equals("Farmway") || s.equals("Central")) {
                            temp = "";
                        }
                        ouputAddressGeneral += temp + " ";
                    }
                } else {
                    ouputAddressGeneral = outputAddress.replaceAll("\\d+", "");
                }

                ouputAddressGeneral = ouputAddressGeneral.trim();
                String modifiedTemp = modifiedOutput + filePath + "?" + outputAddress + "?" + ouputAddressGeneral + ";";
                modifiedOutput = modifiedTemp;
                System.out.println(ouputAddressGeneral);


                ArrayList<String> tas = new ArrayList<>();
                if (addressToPath.get(ouputAddressGeneral) != null) { //checks if got anything already
                    ArrayList<String> tas1 = new ArrayList<>();
                    tas1 = addressToPath.get(ouputAddressGeneral);
                    tas.addAll(tas1);
                    tas.add(filePath);
                    addressToPath.put(ouputAddressGeneral, tas);
                } else {
                    tas.add(filePath);
                    addressToPath.put(ouputAddressGeneral, tas);
                }

            }
        }


        adPath.setAddressToPath(addressToPath);

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        settings.edit().putString("LocationAddress", modifiedOutput).apply();
        System.out.println(modifiedOutput);
    }
}
