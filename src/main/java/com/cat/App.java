package com.cat;

import com.cat.promotionsms.dao.PackageOverDAO;
import com.cat.promotionsms.dao.PackageRecDTO;
import com.cat.promotionsms.model.PromotionOverModel;
import com.cat.promotionsms.model.SmsLogModel;
import com.cat.promotionsms.properties.PropertiesClass;
import com.cat.promotionsms.service.Kieserver;
import com.cat.promotionsms.util.PromotionUtil;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

/**
 * Run Batch File
 */
public class App {
    private final static Logger log = Logger.getLogger(App.class);

    public static void main(String[] args) throws IOException {
        TreeSet<String> tFileData = new TreeSet<>();
//        TreeMap<String, PackageRecDTO> hPackageRec = new TreeMap<String, PackageRecDTO>();
        TreeMap<String, PromotionOverModel> hPackageOver = new TreeMap<String, PromotionOverModel>();
        ArrayList<String> LogPackage = new ArrayList<>();
        TreeMap<String, SmsLogModel> tSmsLog = new TreeMap<String, SmsLogModel>();
        PromotionOverModel promo = null;
        PropertiesClass prop = new PropertiesClass();
        int FILE_DATA_LENGTH = 4;
        int MAX_SIZE_ARRAY_OF_ROWS = 29;
        String fileHeader = prop.getProperties().getProperty("FILE_HEADER");
        int packageMin = parseInt(prop.getProperties().getProperty("PACKAGE_MIN"));
        int packageMax = parseInt(prop.getProperties().getProperty("PACKAGE_MAX"));
        String filePath = prop.getProperties().getProperty("PATH_FILE_RAW");
        String destinationPath = prop.getProperties().getProperty("DIRECTORY_ARCHIVE");
        String logPath = prop.getProperties().getProperty("LOG_PATH");
        String workMode = prop.getProperties().getProperty("WORK_MODE");
        int dummyTelephone = parseInt(prop.getProperties().getProperty("SMS_DUMMY_TELEPHONE"));
        float dummyIdd = parseFloat(prop.getProperties().getProperty("DUMMY_VALUE_IDD"));
        String senderName = prop.getProperties().getProperty("SENDER");
        String smsUserName = prop.getProperties().getProperty("SMS_USERNAME");
        String smsPassword = prop.getProperties().getProperty("SMS_PASSWORD");
        Double ZERO_VALUE = 0.00;
        int INDEX_MONTH = 0;
        int INDEX_TELEPHONE = 1;
        int INDEX_PACKAGE_ID = 2;
        int INDEX_ONNET = 5;
        int INDEX_OFFNET = 9;
        int INDEX_DATA = 21;
        int INDEX_IDD = 28;
        try {
            // Read text file > return T-Set
            tFileData = PromotionUtil.ReadFile(filePath);
            boolean existsFileInput = tFileData != null;
            if (existsFileInput) {
                //add File Header for Write log
                LogPackage.add(0, fileHeader);
                int countPackages = 0;
                int COUNT_DATA_INPUT = 1;
                for (String rowData : tFileData) {
                    String[] splited = rowData.split("\\|");
                    if (splited.length > FILE_DATA_LENGTH) {
                        boolean isPackageNotEmpty = splited[INDEX_PACKAGE_ID].length() > 0;
                        if (isPackageNotEmpty) {
                            boolean isCheckPackageRule = parseInt(splited[INDEX_PACKAGE_ID]) >= packageMin && parseInt(splited[INDEX_PACKAGE_ID]) < packageMax;
                            if (isCheckPackageRule) {
                                boolean isDataEmpty = parseFloat(splited[INDEX_ONNET]) > ZERO_VALUE && parseFloat(splited[INDEX_OFFNET]) > ZERO_VALUE && parseFloat(splited[INDEX_DATA]) > ZERO_VALUE;
                                if (isDataEmpty) {
                                    promo = new PromotionOverModel();
                                    promo.setData_month(splited[INDEX_MONTH]);
                                    promo.setTelephoneNumber(parseInt(splited[INDEX_TELEPHONE]));
                                    promo.setPackageID(parseInt(splited[INDEX_PACKAGE_ID]));
                                    promo.setVoi_onnet(parseFloat(splited[INDEX_ONNET]));
                                    promo.setVoi_offnet(parseFloat(splited[INDEX_OFFNET]));
                                    promo.setData(parseFloat(splited[INDEX_DATA]));
                                    boolean isIddCheckLength = splited.length == MAX_SIZE_ARRAY_OF_ROWS;
                                    if (isIddCheckLength) {
                                        promo.setIdd(parseFloat(splited[INDEX_IDD]));
                                    } else {
                                        promo.setIdd(dummyIdd);
                                        log.info("dummyIdd " + dummyIdd);
                                    }
                                    String PackageRec = null;
                                    try {

                                        log.info("Rows : " + COUNT_DATA_INPUT + ", Data Call Kie API  => " + rowData);
                                        //Call Kie Server API
                                        PackageRec = Kieserver.CallKieAPI(promo);
                                        PackageRecDTO packDTO = new PackageRecDTO();

                                        if (PackageRec != null) {
                                            packDTO.setTelephoneRec(splited[INDEX_TELEPHONE]);
                                            packDTO.setPackageRecID(PackageRec);
                                            promo.setPackageRecommend(parseInt(PackageRec));
                                            //add PackageOver to object
                                            hPackageOver.put(splited[INDEX_TELEPHONE] + "|" + splited[INDEX_PACKAGE_ID], promo);
//                                            hPackageRec.put(splited[INDEX_TELEPHONE] + "|" + splited[INDEX_PACKAGE_ID], packDTO);
                                            log.info("Kie API Success  => " + rowData);
                                            countPackages += 1;
                                        }
                                        log.info("Package Recommend : " + PackageRec);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    LogPackage.add(rowData);
                                    log.info("rows: " + COUNT_DATA_INPUT + " ,Data not Process :  => " + rowData);
                                }
                            } else {
                                LogPackage.add(rowData);
                                log.info("rows: " + COUNT_DATA_INPUT + " ,Data not Process :  => " + rowData);
                            }
                        } else {
                            LogPackage.add(rowData);
                            log.info("rows: " + COUNT_DATA_INPUT + " ,Data not Process :  => " + rowData);
                        }
                    } else {
                        LogPackage.add(rowData);
                        log.info("rows: " + COUNT_DATA_INPUT + " ,Data not Process :  => " + rowData);
                    }
                    COUNT_DATA_INPUT += 1;
                }
                log.info("Count data before Call Kie API : " + countPackages + "rows.");
                log.info("Data not process =>  : " + (LogPackage.size() - 1) + " rows.");
                try {
                    PromotionUtil.WriteFile(logPath, LogPackage);
                    PackageOverDAO.insertPackageOver(hPackageOver);
                    PromotionUtil.MoveFile(filePath, destinationPath);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("Error : " + e.getMessage());
                }
//                SMSUtil smsutil = new SMSUtil();
//                SmsServices smsService = new SmsServices();
//                boolean success = false;
//                boolean isWorkMode = workMode.equals("PROD");
//                int COUNT_SMS_SUCCESS = 0;
//                int COUNT_SMS_UNSUCCESS = 0;
//                //Check Package information
//                for (Map.Entry<String, PackageInfoModel> entry : PackageInfoDAO.GetPromotionInfo(hPackageRec).entrySet()) {
//                    String key = entry.getKey();
//                    PackageInfoModel value = entry.getValue();
//                    SmsLogModel smslog = new SmsLogModel();
//                    if (isWorkMode) {
//                        smslog.setMDN(parseInt(value.getTelephoneNumber()));
//                    } else {
//                        smslog.setMDN(dummyTelephone);
//                    }
//                    smslog.setSender(senderName);
//                    smslog.setSendDateTime(Tools.getSqlDateTime());
//                    smslog.setSmsMessage(value.getSmsMessage());
//                    try {
//                        if (isWorkMode) {
//                            //Send SMS
//                            success = smsutil.sendSms(smslog.getSender(), "0" + String.valueOf(smslog.getMDN()), smsUserName, smsPassword, smslog.getSmsMessage());
//                        } else {
//                            success = smsService.CallSmsService();
//                        }
//                        if (success == true) {
//                            smslog.setStatus("Success");
//                            COUNT_SMS_SUCCESS += 1;
//                        } else {
//                            smslog.setStatus("Unsuccess");
//                            COUNT_SMS_UNSUCCESS += 1;
//                        }
//                        //Result send SMS
//                        tSmsLog.put(key, smslog);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                log.info("SMS Send Success : " + COUNT_SMS_SUCCESS);
//                log.info("SMS Send Unsuccess : " + COUNT_SMS_UNSUCCESS);
//                try {
//                    //Store SMS Log to DB
//                    SmsLogDAO.insertRecord(tSmsLog);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            } else {
                log.error(" Input file not found ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



