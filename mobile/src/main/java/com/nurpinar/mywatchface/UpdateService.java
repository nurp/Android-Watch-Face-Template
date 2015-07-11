package com.nurpinar.mywatchface;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by nurpinar on 27.4.2015.
 */
public class UpdateService extends WearableListenerService {

    private static final String TAG = "UpdateService";
    public static final String KEY_BTC_BUYPRICE = "BtcBuy";
    public static final String PATH_BTC_PRICE = "/BtcWatchFace/BtcInfo";
    public static final String PATH_BTC_REQUIRE = "/BtcService/Require";

    private String mPeerId;

    private GoogleApiClient mGoogleApiClient;

    private static double mBtcValue = Double.MAX_VALUE;
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        mPeerId = messageEvent.getSourceNodeId();
        Log.d(TAG, "MessageReceived: " + messageEvent.getPath());
        if (messageEvent.getPath().equals(PATH_BTC_REQUIRE)) {
            startTask();
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)/*
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d(TAG, "onConnected: " + connectionHint);
                        // Now you can use the Data Layer API
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                        Log.d(TAG, "onConnectionSuspended: " + cause);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d(TAG, "onConnectionFailed: " + result);
                    }
                })*/
                        // Request access only to the Wearable API
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();


    }
    private void startTask() {






            //https://www.bitstamp.net/api/ticker
            /*URL url = new URL("http://ip.jsontest.com/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            BufferedReader json  = new BufferedReader(new InputStreamReader(connection.getInputStream()));*/
           // Log.d(TAG, "BitTicker json: " + json);


        Log.d(TAG, "Start Btc AsyncTask");

        Task task = new Task();
        task.execute();
    }

    private class Task extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            try {
                Log.d(TAG, "Task Running");

                if (mGoogleApiClient.isConnected()) {
                    String path  ="https://www.bitstamp.net/api/ticker/";

                    try {
                        URL queryUrl = new URL(path);
                        HttpsURLConnection connection = (HttpsURLConnection) queryUrl.openConnection();
                        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; Java Test client)");
                        connection.setRequestMethod("GET");

                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                        StringBuilder builder = new StringBuilder();

                        builder.append(reader.readLine());

                        try {
                            JSONObject json = new JSONObject(builder.toString());
                            Log.d(TAG, json.getString("last"));
                            mBtcValue = json.getDouble("last");
                        }catch(JSONException e) {
                            Log.d(TAG, "Task Fail JSONException: " + e);

                        }

                    } catch (IOException e) {
                        Log.d(TAG, "Task Fail IOException: " + e);
                    }

                    PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(PATH_BTC_PRICE);

                    putDataMapRequest.getDataMap().putDouble(KEY_BTC_BUYPRICE, mBtcValue);
                    //putDataMapRequest.getDataMap().putLong("time", new Date().getTime());

                    Wearable.DataApi.putDataItem(mGoogleApiClient, putDataMapRequest.asPutDataRequest());


                    /*DataMap config = new DataMap();
                    config.putInt(KEY_BTC_BUYPRICE, mBtcValue);
                    mBtcValue += 5;

                    Wearable.MessageApi.sendMessage(mGoogleApiClient, mPeerId, PATH_BTC_PRICE, config.toByteArray()).setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
                        @Override
                        public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                            Log.d(TAG, "SendUpdateMessage: " + sendMessageResult.getStatus());
                        }
                    });*/
                }
            } catch (Exception e) {
                Log.d(TAG, "Task Fail: " + e);
            }
            return null;
        }
    }
}
