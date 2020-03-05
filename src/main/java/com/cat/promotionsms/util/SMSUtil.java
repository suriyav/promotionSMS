package com.cat.promotionsms.util;

import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SMSUtil {
    public SMSUtil() {
    }

    private final static Logger log = Logger.getLogger(SMSUtil.class);

    public boolean sendSms(String from, String toTelephoneNo, String userid, String password, String message) throws Exception {
        boolean success = false;
        HttpURLConnection con = null;
        String encMessage = URLEncoder.encode(message, "UTF-8");
        String destUrl = "http://www.thsms.com/api/rest?method=send&username=" + userid + "&password=" + password + "&from=" + from + "&to=" + toTelephoneNo + "&message=" + encMessage;
        System.out.println("Promotion = " + destUrl);

        try {
            URL updateServer = new URL(destUrl);
            con = (HttpURLConnection) updateServer.openConnection();
            con.setConnectTimeout(4000);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("GET");
            OutputStream output = null;

            try {
                output = con.getOutputStream();
                output.write("".getBytes());
                output.flush();
            } catch (Exception var31) {
                throw var31;
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException var30) {
                    }
                }

            }

            InputStream in = con.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            boolean var15 = false;

            int len;
            while ((len = in.read(buffer)) > 0) {
                baos.write(buffer, 0, len);
            }

            in.close();
            String response = new String(baos.toByteArray());
            System.out.println("response : " + response);
//            log.info(response);
            if (response != null && response.contains(">OK<")) {
                success = true;
            }
        } catch (IOException var33) {
            var33.printStackTrace();
                throw var33;
        } finally {
            if (con != null) {
                con.disconnect();
            }

        }

        return success;
    }

//    public static void main(String[] s) throws Exception {
//        SMSUtil smsutil = new SMSUtil();
//        boolean success = false;
//        try {
//            success = smsutil.sendSms("TOPUP", "0917701676", "dbditsms", "ABCTech@1", "ทดสอบ SMS");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(success);
//    }
}
