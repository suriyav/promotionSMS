package com.cat.promotionsms.dao;

import com.cat.promotionsms.jdbc.DBConnect;
import com.cat.promotionsms.model.SmsLogModel;
import com.cat.promotionsms.util.Tools;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

public class SmsLogDAO implements Serializable {
    private final static Logger log = Logger.getLogger(SmsLogDAO.class);

    public static void insertRecord(TreeMap<String, SmsLogModel> tSmsLog) throws SQLException {
        String sqlQuery = " INSERT INTO SMSLog (MDN, SMS_Message, Sender, SendDateTime,`Status`)"
                + " values (?, ?, ?, ?, ?)";
        Connection conn = null;
        int COUNT_INSERT_SUCCESS = 0;
        PreparedStatement preparedStmt = null;
        int commit = 0;
        try {
            conn = DBConnect.getConnectionCATATP();
            for (Map.Entry<String, SmsLogModel> entry : tSmsLog.entrySet()) {
                SmsLogModel value = entry.getValue();
                log.info("Data insert DB => Telephone : " + Tools.replaceTelephoneNumber(String.valueOf(value.getMDN())) + ", SMS Message : " + value.getSmsMessage().substring(0, 10) + ", Sender: " + value.getSender() + ", Status: " + value.getStatus());
                preparedStmt = conn.prepareStatement(sqlQuery);
                preparedStmt.setInt(1, value.getMDN());
                preparedStmt.setString(2, value.getSmsMessage());
                preparedStmt.setString(3, value.getSender());
                preparedStmt.setTimestamp(4, value.getSendDateTime());
                preparedStmt.setString(5, value.getStatus());
                commit = preparedStmt.executeUpdate();
                COUNT_INSERT_SUCCESS += 1;
                preparedStmt.close();
            }

            if (commit > 0) {
                conn.commit();
                log.info("Data insert Sucess: " + COUNT_INSERT_SUCCESS);
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
}

