package com.nurpinar.mywatchface;

/**
 * Created by nurpinar on 1.5.2015.
 */
public class BtcReader {
    private double last;
    private double high;
    private double low;
    private double bid;
    private double ask;

   /* public void update(){
        JSONObject jobj = new JSONObject();

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        URL url = new URL("https://www.bitstamp.net/api/ticker/");
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setSSLSocketFactory(context.getSocketFactory());
        InputStream in = urlConnection.getInputStream();
    }*/
}
