package com.tribhaskar.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Utility {
    public static String convertUnixTimeToDate(Long unixTime) {
        final DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        final String formattedDtm = Instant.ofEpochSecond(unixTime)
                .atZone(ZoneId.of("GMT"))
                .format(formatter);
        return formattedDtm;
    }

    public static double round(double value, int places) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    //write a utility method which will make email like eg. rohitabc123@gmail.com to roh*****23@gmail.com

    public static String maskEmail(String email) {
        String[] emailParts = email.split("@");
        String maskedEmail = emailParts[0].substring(0, 3) + "*****" + emailParts[0].substring(emailParts[0].length() - 2) + "@" + emailParts[1];
        return maskedEmail;
    }
}
