package com.cat.promotionsms.service;

import com.cat.promotionsms.properties.PropertiesClass;
import org.apache.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class SmsServices {
    private final static Logger log = Logger.getLogger(SmsServices.class);

    public static boolean CallSmsService() {
        HttpURLConnection urlConn;
        String result = null;
        PropertiesClass prop = new PropertiesClass();
        try {
            //Connect
            urlConn = (HttpURLConnection) ((new URL("http://0.0.0.0:8080/promotionWebService/api/v1/sendsms").openConnection()));
            urlConn.setDoOutput(true);
            urlConn.setRequestMethod("POST");
            urlConn.setConnectTimeout(10000);
            urlConn.connect();

            int responseCode = urlConn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                //Read
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));
                String line = null;
                StringBuilder sb = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                result = sb.toString();

                System.out.println("result : " + result);
                log.info(result);
            } else {
                new String("false : " + responseCode);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
