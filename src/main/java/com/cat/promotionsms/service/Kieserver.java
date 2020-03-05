package com.cat.promotionsms.service;

import com.cat.promotionsms.model.KieJsonResponseModel;
import com.cat.promotionsms.model.PromotionModel;
import com.cat.promotionsms.model.PromotionOverModel;
import com.cat.promotionsms.properties.PropertiesClass;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Kieserver {

    private final static Logger log = Logger.getLogger(Kieserver.class);

    public static String CallKieAPI(PromotionOverModel Promo) {
        HttpURLConnection urlConn;
        String result = null;
        String packageRec = null;
        PropertiesClass prop = new PropertiesClass();
        String version = null;
        String fullKieUrl = null;
        try {
            version = getKieContainers();
            fullKieUrl = prop.getProperties().getProperty("KIE_URL_API") + "/instances/" + version;

            //Connect
            urlConn = (HttpURLConnection) ((new URL(fullKieUrl).openConnection()));
            urlConn.setDoOutput(true);
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.setRequestProperty("Authorization", "Basic " + prop.getProperties().getProperty("AUTHENTICATION"));
            urlConn.setRequestProperty("Accept", "application/json");
            urlConn.setRequestMethod("POST");
            urlConn.setConnectTimeout(10000);
            urlConn.connect();

            //Object to  Call KieServer
            String input = "{ \"lookup\": \"" + prop.getProperties().getProperty("KIE_SESSION")
                    + "\", \"commands\": [ { \"insert\": { \"object\": { \"" + prop.getProperties().getProperty("KIE_DATA_OBJECT")
                    + "\": { \"package_ID\": " + Promo.getPackageID()
                    + ", \"voi_ONNET_OVER\": " + Promo.getVoi_onnet()
                    + ", \"voi_OFFNET_OVER\": " + Promo.getVoi_offnet()
                    + ", \"data_OVER\": " + Promo.getData()
                    + ", \"idd_OVER\": " + Promo.getIdd()
                    + " } }, \"return-object\": true, \"out-identifier\": \"PromotionRequest\" } } ]}";

            //Write
            OutputStream outputStream = urlConn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(input);
            writer.close();
            outputStream.close();
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
                Gson gson = new Gson();
                PromotionModel model = gson.fromJson(result, PromotionModel.class);

                //Check Promotion package not match
                if (model.getResult().getExecutionResults().getResults().get(0).getValue().getPromotionOverModel().getPackageRecommend() != 0) {
                    //Set result : Package Recommended
                    packageRec = String.valueOf(model
                            .getResult()
                            .getExecutionResults()
                            .getResults()
                            .get(0)
                            .getValue()
                            .getPromotionOverModel()
                            .getPackageRecommend()
                    );
                }
            } else {
                new String("false : " + responseCode);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return packageRec;
    }

    public static String getKieContainers() {
        HttpURLConnection urlConn;
        String result = null;
        PropertiesClass prop = new PropertiesClass();
        String host = prop.getProperties().getProperty("KIE_URL_API");
        String groupId = prop.getProperties().getProperty("GROUP_ID");
        String artifactId = prop.getProperties().getProperty("ARTIFACT_ID");
        String containerId = null;
        try {
            //Connect
            urlConn = (HttpURLConnection) ((new URL(host + "?groupId=" + groupId + "&artifactId=" + artifactId).openConnection()));
            urlConn.setDoOutput(true);
            urlConn.setRequestProperty("Content-Type", "application/json");
            urlConn.setRequestProperty("Authorization", "Basic " + prop.getProperties().getProperty("AUTHENTICATION"));
            urlConn.setRequestMethod("GET");
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
                Gson gson = new Gson();
                KieJsonResponseModel model = gson.fromJson(result, KieJsonResponseModel.class);
                // Check Version
                if (model.getResult().getKieContainers().getKieContainer().get(0).getContainerId() != null) {
                    //Set containerId = Version
                    containerId = String.valueOf(model.getResult().getKieContainers().getKieContainer().get(0).getContainerId());
                    log.info("containerId: " + containerId);
                }
            } else {
                new String("false : " + responseCode);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("error: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("error: " + e.getMessage());
        }
        return containerId;
    }
}