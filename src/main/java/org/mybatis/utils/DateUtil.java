package org.mybatis.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

public interface DateUtil {
    static Date now() {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return Date.from(instant);
    }

    static String getFormatDate(Date date){
        Objects.requireNonNull(date);
        return getFormatDate(date,"yyyy-MM-dd HH:mm:ss");
    }

    static String getFormatDate(Date date,String formatString){
        Objects.requireNonNull(date);
        Objects.requireNonNull(formatString);
        SimpleDateFormat formatter = new SimpleDateFormat(formatString);
        return formatter.format(date);
    }

    static String getSimpleDate(Date date){
        Objects.requireNonNull(date);
        return getFormatDate(date,"yyyy-MM-dd");
    }

    static String getSimpleDate(){
        return getSimpleDate("yyyy/MM/dd");
    }

    static String getSimpleDate(String dateTimeFormatter){
        Objects.requireNonNull(dateTimeFormatter);
        LocalDate localDate = LocalDate.now();
        return localDate.format(DateTimeFormatter.ofPattern(dateTimeFormatter));
    }
}
