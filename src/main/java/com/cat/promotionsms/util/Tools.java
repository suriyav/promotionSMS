package com.cat.promotionsms.util;

import java.sql.Timestamp;
import java.util.Date;

public class Tools {
    public static Timestamp getSqlDateTime() {
        Date javaDate = new Date();
        long javaTime = javaDate.getTime();
        Timestamp sqlTimestamp = new Timestamp(javaTime);
        return sqlTimestamp;
    }

    public static String replaceTelephoneNumber(String telephoneNumber) {
        String result = null;
        result = "(" + telephoneNumber.substring(0, 3) + ")-" + telephoneNumber.substring(3, 6) + "-" + telephoneNumber.substring(6, telephoneNumber.length()).replaceAll("[^\\d+]|(?!^)\\+", "x");;
        return result;
    }
}