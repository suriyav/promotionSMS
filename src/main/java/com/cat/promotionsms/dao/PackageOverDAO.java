package com.cat.promotionsms.dao;

import com.cat.promotionsms.jdbc.DBConnect;
import com.cat.promotionsms.model.PackageOverResultModel;
import com.cat.promotionsms.model.PromotionOverModel;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class PackageOverDAO implements Serializable {
    private final static Logger log = Logger.getLogger(PackageOverDAO.class);

    public static void insertPackageOver(TreeMap<String, PromotionOverModel> tSmsLog) throws SQLException {
        String sqlQuery = " INSERT INTO PackageOver (MONTHS, MDN, PACKAGE_ID, VOI_ONNET_PRO,VOI_OFFNET_PRO,IDD_PRO,DATA_PRO,PACKAGE_RECOMMEND,FLG_Send_SMS)"
                + " values (?, ?, ?, ?, ?, ?, ?, ?,?)";
        Connection conn = null;
        int COUNT_INSERT_SUCCESS = 0;
        PreparedStatement preparedStmt = null;
        int commit = 0;
        try {
            conn = DBConnect.getConnectionCATATP();
            for (Map.Entry<String, PromotionOverModel> entry : tSmsLog.entrySet()) {
                PromotionOverModel value = entry.getValue();

//                log.info("Data insert DB => Telephone : " + Tools.replaceTelephoneNumber(String.valueOf(value.getMDN())) + ", SMS Message : " + value.getSmsMessage().substring(0, 10)substring(0, 10) + ", Sender: " + value.getSender() + ", Status: " + value.getStatus());
                preparedStmt = conn.prepareStatement(sqlQuery);
                preparedStmt.setString(1, value.getData_month());
                preparedStmt.setInt(2, value.getTelephoneNumber());
                preparedStmt.setInt(3, value.getPackageID());
                preparedStmt.setFloat(4, value.getVoi_onnet());
                preparedStmt.setFloat(5, value.getVoi_offnet());
                preparedStmt.setFloat(6, value.getData());
                preparedStmt.setFloat(7, value.getIdd());
                preparedStmt.setInt(8, value.getPackageRecommend());
                preparedStmt.setBoolean(9, value.isFlg_send_sms());
                commit = preparedStmt.executeUpdate();
                COUNT_INSERT_SUCCESS += 1;
                preparedStmt.close();
            }

            if (commit > 0) {
                conn.commit();
                log.info("Data insert success: " + COUNT_INSERT_SUCCESS);
            } else {
                conn.rollback();
                log.error("Data insert mistake ");
            }
        } catch (SQLException sql) {
            if (conn != null) {
                conn.rollback();
            }
            sql.printStackTrace();
        } catch (Exception e) {
            log.error("Error in DAO Insert " + e.toString());
            e.printStackTrace();
        } finally {
            if (preparedStmt != null) {
                preparedStmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    public static ArrayList<PackageOverResultModel> GetPromotionOver() throws SQLException {
        Connection conn = null;
        PreparedStatement query = null;
        ResultSet rs = null;
        PackageOverResultModel resultPromoOver = null;
        ArrayList<PackageOverResultModel> list = new ArrayList<PackageOverResultModel>();
        try {
            conn = DBConnect.getConnectionCATATP();
//            String sqlQuery = "SELECT MONTHS, MDN, PACKAGE_ID, VOI_ONNET_PRO, VOI_OFFNET_PRO, IDD_PRO, DATA_PRO, FLG_Send_SMS FROM PackageOver";
            String sqlQuery = "SELECT po.MDN               as telephone_number,\n" +
                    "       po.PACKAGE_ID        as Old_Packages,\n" +
                    "       po.PACKAGE_RECOMMEND as New_Packages,\n" +
                    "       pi.SMS_Message       as SMS_Message,\n" +
                    "       po.FLG_Send_SMS      as FLG_Send_SMS,\n" +
                    "       CONCAT(\"Onnet: \",po.VOI_ONNET_PRO,\" ,Offnet: \",po.VOI_OFFNET_PRO,\" ,Data: \",po.DATA_PRO,\" ,IDD: \",po.IDD_PRO) as Remarts\n" +
                    "FROM PackageOver po\n" +
                    "         inner join Packages pi\n" +
                    "                    ON po.PACKAGE_RECOMMEND = pi.PACKAGE_ID;";
            query = conn.prepareStatement(sqlQuery);
            rs = query.executeQuery();
            while (rs.next()) {
                resultPromoOver = new PackageOverResultModel();
                if (rs.getBoolean("FLG_Send_SMS") == false) {
                    resultPromoOver.setTelephoneNumber(rs.getInt("telephone_number"));
                    resultPromoOver.setPackageID(rs.getInt("Old_Packages"));
                    resultPromoOver.setNewPackage(rs.getInt("New_Packages"));
                    resultPromoOver.setSmsMessage(rs.getString("SMS_Message"));
                    resultPromoOver.setFlg_send_sms(rs.getBoolean("FLG_Send_SMS"));
                    resultPromoOver.setRemarts(rs.getString("Remarts"));
                    list.add(resultPromoOver);
                }
//                log.info("resultPromoOver : " + resultPromoOver);
            }
            log.info("GetPromotionOver => Success!");
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (query != null) {
                query.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        log.info("list : " + list);
        return list;
    }
}
