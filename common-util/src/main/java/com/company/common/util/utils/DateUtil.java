package com.company.common.util.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

public class DateUtil {

    private static final Logger log = LoggerFactory.getLogger(DateUtil.class);

    public static Date yesterday() {
        return offsiteDate(new Date(), Calendar.DAY_OF_YEAR, -1);
    }

    public static Date lastWeek() {
        return offsiteDate(new Date(), Calendar.WEEK_OF_YEAR, -1);
    }

    public static Date lastMonth() {
        return offsiteDate(new Date(), Calendar.MONTH, -1);
    }

    public static Date offsiteDate(Date date, int calendarField, int offsite) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(calendarField, offsite);
        return cal.getTime();
    }

    public static Date atEndOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return localDateTimeToDate(endOfDay);
    }

    public static Date atStartOfDay(Date date) {
        LocalDateTime localDateTime = dateToLocalDateTime(date);
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return localDateTimeToDate(startOfDay);
    }

    public static LocalDate lastOfMonth(Integer year, Month month) {
        return LocalDate.of(year, month, 1).with(lastDayOfMonth());
    }

    public static LocalDate lastOfYear(Integer year) {
        return LocalDate.of(year, Month.DECEMBER, 31);
    }

    public static LocalDate firstOfMonth(Integer year, Month month) {
        return LocalDate.of(year, month, 1);
    }

    public static LocalDate firstOfYear(Integer year) {
        return LocalDate.of(year, Month.JANUARY, 1);
    }

    private static LocalDateTime dateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    private static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String formatDateLog(Date date) {
        String dateStr = formatDateWithPattern(date, "yyyy-MM-dd - HH:mm:ss Z");
        return dateStr.replace("0700", "07:00");
    }

    public static String formatDateWithPattern(Date date, String patternFormatDate) {
        SimpleDateFormat format = new SimpleDateFormat(patternFormatDate);
        return format.format(date);
    }

    public static Date formatDate(final String date, final String pattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.parse(date);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    private DateUtil() {
        throw new IllegalStateException("Utility class");
    }
}
