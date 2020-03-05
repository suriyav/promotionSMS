package com.cat.promotionsms.dao;

import com.cat.promotionsms.jdbc.DBConnect;
import com.cat.promotionsms.model.PackageInfoModel;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

public class PackageInfoDAO implements Serializable {
    private final static Logger log = Logger.getLogger(PackageInfoDAO.class);

    public static TreeMap<String, PackageInfoModel> GetPromotionInfo(TreeMap<String, PackageRecDTO> hPackageRec) throws SQLException {
        Connection conn = null;
        PreparedStatement query = null;
        ResultSet rs = null;
        PackageInfoModel resultPromotionInfo = new PackageInfoModel();
        TreeMap<String, PackageInfoModel> result = new TreeMap<String, PackageInfoModel>();
        try {
            conn = DBConnect.getConnectionCATATP();
            for (Map.Entry<String, PackageRecDTO> entry : hPackageRec.entrySet()) {
                String key = entry.getKey();
                PackageRecDTO value = entry.getValue();
                String sqlQuery = "SELECT * FROM Packages where PACKAGE_ID = ?";
                query = conn.prepareStatement(sqlQuery);
                query.setString(1, value.getPackageRecID());
                rs = query.executeQuery();
                while (rs.next()) {
                    resultPromotionInfo.setTelephoneNumber(value.getTelephoneRec());
                    resultPromotionInfo.setPackageId(rs.getString("PACKAGE_ID"));
                    resultPromotionInfo.setDescription(rs.getString("Description"));
                    resultPromotionInfo.setDataPro(rs.getFloat("DATA_PRO"));
                    resultPromotionInfo.setVoiOnnetPro(rs.getFloat("VOI_ONNET_PRO"));
                    resultPromotionInfo.setVoiOffnetPro(rs.getFloat("VOI_OFFNET_PRO"));
                    resultPromotionInfo.setIddPro(rs.getFloat("IDD_PRO"));
                    resultPromotionInfo.setSmsMessage(rs.getString("SMS_Message"));
                }
                log.info("GetPromotionInfo => PackageID: " + resultPromotionInfo.getPackageId());
                result.put(key, resultPromotionInfo);
                rs.close();
                query.close();
            }
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
        return result;
    }
}
