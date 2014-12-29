package channelsoft.com.my3;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by yuanshun on 2014/12/26.
 */
public class LocationActivity extends Activity {

    private static final String ACTIVITY_TAG = "LocationActivity";
    private static final int SHOW_LOACTION = 11;

    private LocationManager locationManger;
    private String provider;
    private TextView positionTextView;
    private String key = "8d911f906e72627228030a1e316c6a37";//百度开发key

    //处理对象，用于控制UI的变化
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_LOACTION:
                    String currentPosition = (String) msg.obj;
                    positionTextView.setText(currentPosition);
                    break;
                default:
                    break;
            }

        }
    };

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
        //尝试重新获取定位信息
        while (location == null) {

            location = locationManger.getLastKnownLocation(provider);
            Log.d(ACTIVITY_TAG, "重新获取location信息：" + location);
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

    //显示位置信息
    private void showLocation(Location location) {
        Log.d(ACTIVITY_TAG, "更新位置信息...");
        String currentPosition = "latitude is " + location.getLatitude() + "\n" + " longitude is " + location.getLongitude();
        Log.d(ACTIVITY_TAG, "地址信息：" + currentPosition);
        positionTextView.setText(currentPosition);
        updateLocation(location);
    }

    /**
     * 更新位置信息
     *
     * @param location
     */
    private void updateLocation(final Location location) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        StringBuilder url = new StringBuilder();
        url.append("http://api.map.baidu.com/geocoder?location=");
        url.append(location.getLatitude()).append(",").append(location.getLongitude());
        url.append("&output=json");
        url.append("&key=").append(key);//百度开发key

        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url.toString());
        //在请求消息头中制定语言，保证服务器会返回中文数据
        httpGet.addHeader("Accept-Language", "zh-CN");
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = httpResponse.getEntity();
            String response = null;
            try {
                response = EntityUtils.toString(entity, "utf-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //获取results节点下的位置信息
            JSONObject jsonObject1 = null;
            try {
                Log.d(ACTIVITY_TAG, "jsonObject:" + jsonObject);
                Log.d(ACTIVITY_TAG, "jsonObject:" + jsonObject.getString("status"));
                Log.d(ACTIVITY_TAG, "jsonObject:" + jsonObject.getJSONObject("result"));
                jsonObject1 = jsonObject.getJSONObject("result");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //取出格式化后的位置信息
            String address = null;
            try {
                address = jsonObject1.getString("formatted_address");
                Log.d(ACTIVITY_TAG, "获取地址信息：" + address);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Message message = new Message();
            message.what = SHOW_LOACTION;
            message.obj = address;
            handler.sendMessage(message);
        }
//            }
//        }).start();
    }


}
