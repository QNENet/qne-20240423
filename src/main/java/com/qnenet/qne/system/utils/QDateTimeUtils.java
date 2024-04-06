/*
 * Copyright (C) 2022 Paul F Fraser <paulf@qnenet.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qnenet.qne.system.utils;


import com.qnenet.qne.objects.classes.QDateTimeInfo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class QDateTimeUtils {

    public static final long SECONDS_PER_DAY = 60 * 60 * 24;

    public static Locale locale;
    public static TimeZone timeZone;

    public static boolean isLeapYear(int year) {
        boolean isLeapYear;
        // divisible by 4
        isLeapYear = (year % 4 == 0);
        // divisible by 4, not by 100, or divisible by 400
        return isLeapYear && (year % 100 != 0 || year % 400 == 0);
    }

    public static QDateTimeInfo getLocalDateTimeInfoNow() {
        return getLocalDateTimeInfo(LocalDateTime.now());
    }

    public static QDateTimeInfo getLocalDateTimeInfo(LocalDateTime localDateTime) {
        QDateTimeInfo info = new QDateTimeInfo();
        info.year = localDateTime.getYear();
        info.month = localDateTime.getMonthValue();
        info.day = localDateTime.getDayOfMonth();
        info.hour = localDateTime.getHour();
        info.minute = localDateTime.getMinute();
        info.isleapYear = isLeapYear(info.year);
        info.yearDay = localDateTime.getDayOfYear();
        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
        info.weekDayNumber = dayOfWeek.getValue();
        info.weekDayName = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault());
        return info;
    }

    public static Date nowUTCPlusYears(int years) {
        LocalDate localNow = LocalDate.now();
        LocalDate localNowPlus = localNow.plus(years, ChronoUnit.YEARS);
        return Date.from(localNowPlus.atStartOfDay().toInstant(ZoneOffset.UTC));
    }

    public static String yyyymmddPlusTimestampNow() {
        return yyyymmddNow() + "-" + Long.toString(System.currentTimeMillis());
    }

    private static String yyyymmddNow() {
        return LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
    }


//    private static class QDateTimeInfo {
//        public int year;
//        public int month;
//        public int day;
//        public int hour;
//        public int minute;
//        public boolean isleapYear;
//        public int yearDay;
//        public int weekDayNumber;
//        public String weekDayName;
//    }


//    public static void setLocale(Locale currentLocale) {
//        locale = currentLocale;
//    }
//
//    public static void setTimeZone(TimeZone currentTimeZone) {
//        timeZone = currentTimeZone;
//    }
//
//    public static LocalDate epoch = LocalDate.ofEpochDay(0);
//    // static LocalDateTime epochSeconds =
//    // LocalDateTime.ofEpochSecond(epochSecond, nanoOfSecond, offset)
//
//
/////////  Now  //////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public static long timeStampNow() {
//        return LocalDateTime.now().atZone(ZoneId.of(TimeZone.getDefault().getID())).toEpochSecond();
//    }
//
//    public static long timeStampUTCNow() {
//        Instant instant = Instant.now();
//        return instant.toEpochMilli();
//    }
//
//
/////////  Timestamp Instant  ////////////////////////////////////////////////////////////////////////////////////////////
//
//    public static Instant timestampToInstant(long epochSeconds) {
//        return Instant.ofEpochSecond(epochSeconds);
//    }
//
//    public static long instantToTimestamp(Instant instant) {
//        return instant.getEpochSecond();
//    }
//
/////////  Timestamp LocalDateTime  //////////////////////////////////////////////////////////////////////////////////////
//
//    public static long localDateTimeToTimestamp(LocalDateTime localDateTime) {
//        return localDateTime.atZone(ZoneId.of(TimeZone.getDefault().getID())).toEpochSecond();
//    }
//
//    public static LocalDateTime timestampToLocalDateTime(long seconds) {
//        return LocalDateTime.ofInstant(timestampToInstant(seconds), ZoneId.systemDefault());
//    }
//
//
//    public static int timestampToQNEDay(long seconds) {
//        return (int) timestampToLocalDateTime(seconds).toLocalDate().toEpochDay();
//    }
//
////    public static long qneSecondNumber(LocalDateTime localDateTime) {
////        return localDateTime.atZone(ZoneId.of(TimeZone.getDefault().getID())).toEpochSecond();
////    }
////
////    public static LocalDateTime secondsToLocalDateTime(long seconds) {
////        Instant instant = Instant.ofEpochSecond(seconds);
////
////        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
////    }
//
//
/////////  Day Number  ///////////////////////////////////////////////////////////////////////////////////////////////////
//
//    public static int qneDayNumber(LocalDate localDate) {
//        return (int) ChronoUnit.DAYS.between(epoch, localDate);
//    }
//
//    public static int qneDayNumberLocalNow() {
//        return (int) ChronoUnit.DAYS.between(epoch, LocalDate.now());
//    }
//
//    public static Integer qneDayNumberUTCNow() {
//        return (int) (Instant.now().getEpochSecond() / SECONDS_PER_DAY);
//    }
//
//    public static long timestampUTCNow() {
//        return Instant.now().getEpochSecond();
//    }
//
//    public static LocalDate toLocalDate(int qneDateTime) {
//        return epoch.plusDays(qneDateTime);
//    }
//
//    public static int qneWeekNumber(LocalDate localDate) {
//        return (int) ChronoUnit.WEEKS.between(epoch, localDate);
//    }
//
//    public static int qneMonthNumber(LocalDate localDate) {
//        return (int) ChronoUnit.MONTHS.between(epoch, localDate);
//    }
//
//    public static int qneYearNumber(LocalDate localDate) {
//        return (int) ChronoUnit.YEARS.between(epoch, localDate);
//    }
//
//    public static Integer qneDayNumberFromYYYYMMDD(String yyyyMMdd) {
//        if (yyyyMMdd == null || yyyyMMdd.isEmpty()) {
//            return null;
//        }
//        return qneDayNumber(convertYYYYMMDD(yyyyMMdd));
//    }
//
//    public static String toYYYYMMDD(Integer qneDay) {
//        if (qneDay == null) {
//            return "";
//        }
//        LocalDate localDate = toLocalDate(qneDay);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//        return localDate.format(formatter);
//    }
//
//    public static String toDDSlashMMSlashYYYY(int qneDay) {
//        LocalDate localDate = toLocalDate(qneDay);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        return localDate.format(formatter);
//    }
//
//    public static String toYYYYsMMsDD(int qneDay) {
//        LocalDate localDate = toLocalDate(qneDay);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        return localDate.format(formatter);
//    }
//
//    public static Integer qneDayNumberFromDDSlashMMSlashYYYY(String string) {
//        return qneDayNumber(convertDDSlashMMSlashYYYY(string));
//    }
//
//    public static Integer qneDayNumber(DateTimeFormatter dateFormatter, String dateStr) {
//        try {
//            LocalDate ldate = LocalDate.parse(dateStr, dateFormatter);
//            return qneDayNumber(ldate);
//        } catch (DateTimeParseException e) {
//            return null;
//        }
//
//    }
//
//    public static DateTimeFormatter formatterDDSlashMMSlashYYYY = DateTimeFormatter.ofPattern("d/M/yyyy");
//
//    public static LocalDate convertDDSlashMMSlashYYYY(String dateStr) {
//        LocalDate result;
//        try {
//            result = LocalDate.parse(dateStr, formatterDDSlashMMSlashYYYY);
//        } catch (Exception e) {
//            return null;
//        }
//        return result;
//    }
//
//    public static DateTimeFormatter formatterYYYYMMDD = DateTimeFormatter.ofPattern("yyyyMMdd");
//
//    public static LocalDate convertYYYYMMDD(String dateStr) {
//        return LocalDate.parse(dateStr, formatterYYYYMMDD);
//    }
//
//    public static DateTimeFormatter formatterMMSlashDDSlashYYYY = DateTimeFormatter.ofPattern("MM/dd/yyyy");
//
//    public static LocalDate convertMMSlashDDSlashYYYY(String dateStr) {
//        LocalDate result;
//        try {
//            result = LocalDate.parse(dateStr, formatterMMSlashDDSlashYYYY);
//        } catch (Exception e) {
//            return null;
//        }
//        return result;
//    }
//
//    public static Integer qneDayNumberFromMMSlashDDSlashYYYY(String dateStr) {
//        // System.out.println(result);
//        return qneDayNumber(convertMMSlashDDSlashYYYY(dateStr));
//    }
//
//    public static Integer fromYearMonthDay(int year, int month, int day) {
//        LocalDate localDate = LocalDate.of(year, month, day);
//        return qneDayNumber(localDate);
//    }
//
//
//    public static long timestampUTC() {
//        Instant instant = Instant.now();
//        return instant.getEpochSecond();
//    }
//
////    public static String isoDateFormatted(Date date) {
////        TimeZone tz = TimeZone.getTimeZone("UTC");
////        DateFormat df = new SimpleDateFormat("YYYY-MM-DD'T'hh:mm:ss");
////        df.setTimeZone(tz);
////        return df.format(date);
////    }
//
//    public static Date nowUTC() {
//        LocalDate localNow = LocalDate.now();
//        return Date.from(localNow.atStartOfDay().toInstant(ZoneOffset.UTC));
//    }
//
//    public static Date nowUTCPlusDays(int days) {
//        LocalDate localNow = LocalDate.now();
//        LocalDate localNowPlus = localNow.plus(days, ChronoUnit.DAYS);
//        return Date.from(localNowPlus.atStartOfDay().toInstant(ZoneOffset.UTC));
//    }
//
//    public static Date nowUTCPlusMonths(int months) {
//        LocalDate localNow = LocalDate.now();
//        LocalDate localNowPlus = localNow.plus(months, ChronoUnit.MONTHS);
//        return Date.from(localNowPlus.atStartOfDay().toInstant(ZoneOffset.UTC));
//    }
//
//
//    public static LocalDateTime dateTimeNow() {
//        return LocalDateTime.now();
//    }
//
//    public static LocalDate dateNow() {
//        return LocalDate.now();
//    }
//
//    public static OffsetDateTime dateTimeNowUTC() {
//        return OffsetDateTime.now(ZoneOffset.UTC);
//    }
//
//
//    public static String isoDateTimeSecondsNowUTC() {
//        return DateTimeFormatter.ISO_INSTANT.format(Instant.now().truncatedTo(ChronoUnit.SECONDS));
//    }
//
//    public static String isoDateTimeSecondsUTC(Instant instant) {
//        return DateTimeFormatter.ISO_INSTANT.format(instant.truncatedTo(ChronoUnit.SECONDS));
//    }
//
//    public static String isoDateTimeSecondsUTCPlusMonths(Instant instant, int months) {
//        Instant newInstant = instant.plus(Period.ofMonths(months));
//        return DateTimeFormatter.ISO_INSTANT.format(newInstant.truncatedTo(ChronoUnit.SECONDS));
//    }
//
//    public static ZonedDateTime utcNow() {
////        ZonedDateTime.now(ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT )
//        return ZonedDateTime.now(ZoneOffset.UTC );
//    }
//
//
//    public static String zonedDateTimeISO(ZonedDateTime dateTime) {
//        ZonedDateTime r = dateTime.truncatedTo(ChronoUnit.SECONDS);
//        return r.format(DateTimeFormatter.ISO_DATE_TIME);
//    }
//
//    public static String zonedDateTimeISOPlusMonths(ZonedDateTime dateTime, int months) {
//        ZonedDateTime r = dateTime.plusMonths(months).truncatedTo(ChronoUnit.SECONDS);
//        return r.format(DateTimeFormatter.ISO_DATE_TIME);
//    }
//
//    public static String SMonths(Instant instant, int months) {
//
//        // Hard code a date time
//        LocalDateTime dateTime = LocalDateTime.of(2016, Month.AUGUST, 18, 6, 17, 10);
//
//        LocalDateTime nowUTC = LocalDateTime.now(ZoneOffset.UTC);
//
//        LocalDateTime nowUTCPlus = nowUTC.plusMonths(12);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//        DateTimeFormatter formatter1 = ISO_INSTANT;
//
//        String formatDateTime = nowUTCPlus.format(ISO_INSTANT);
//
//
//        System.out.println("LocalDateTime : " + dateTime);
//
//        // Convert LocalDateTime to Instant, UTC+0
//        Instant instant1 = dateTime.toInstant(ZoneOffset.UTC);
//
//        System.out.println("Instant : " + instant);
//        return null;
//    }
//
//    public static String YYYYMMDDnow() {
//        int localNow = qneDayNumberLocalNow();
//        return toYYYYMMDD(localNow);
//    }
//
//
//    //   https://stackoverflow.com/questions/3914404/how-to-get-current-moment-in-iso-8601-format-with-date-hour-and-minute
//
//
////    public static String isoDateTimeStringUTC(Instant instant) {
//////        ZonedDateTime.now(ZoneOffset.UTC).format( DateTimeFormatter.ISO_INSTANT)
////
////        long r = ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond();
////
////        ZonedDateTime.now(ZoneOffset.UTC).truncatedTo(ChronoUnit.MINUTES).format(DateTimeFormatter.ISO_DATE_TIME);
////
////
////        ZonedDateTime.now(ZoneId.of("Europe/Paris"))
////                .truncatedTo(ChronoUnit.MINUTES)
////                .format(DateTimeFormatter.ISO_DATE_TIME);
////
////        instant.toEpochMilli();
////
////        Instant.ofEpochSecond();
////
////        ZonedDateTime r = ZonedDateTime.now(ZoneOffset.UTC);
////        r.toEpochSecond();
////
//////        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
////        return formatter.format(instant);
////    }
//
//
}
