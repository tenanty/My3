package channelsoft.com.my3;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by yuanshun on 2014/12/26.
 */
public class LocationActivity extends Activity {

    private static final String ACTIVITY_TAG = "LocationActivity";

    private LocationManager locationManger;
    private String provider;
    private TextView positionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        positionTextView = (TextView) findViewById(R.id.position_text_view);
        getLocation();


    }

    public void getLocation() {
        Log.d(ACTIVITY_TAG, "进入 getLocation()");
        locationManger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providerList = locationManger.getProviders(true);



        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(this, "No Location provider to use", Toast.LENGTH_SHORT);
            return;
        }
        Location location = locationManger.getLastKnownLocation(provider);

        while(location == null){

            location = locationManger.getLastKnownLocation(provider);
            Log.d(ACTIVITY_TAG,"重新获取location信息："+location);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (location != null) {
            showLocation(location);
        }
        locationManger.requestLocationUpdates(provider, 5000, 1, locationListener);
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    private void showLocation(Location location) {
        Log.d(ACTIVITY_TAG, "更新位置信息...");
        String currentPosition = "latitude is " + location.getLatitude() + "\n" + " longitude is " + location.getLongitude();
        Log.d(ACTIVITY_TAG,"地址信息："+currentPosition);
        positionTextView.setText(currentPosition);
    }
}
