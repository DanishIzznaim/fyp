package com.heroku.java.util;

import java.time.*;

public class TimezoneUtils {

    public static LocalTime convertToTimezone(LocalTime localTime, ZoneId fromZone, ZoneId toZone) {
        ZonedDateTime zonedDateTime = localTime.atDate(LocalDate.now()).atZone(fromZone);
        ZonedDateTime convertedTime = zonedDateTime.withZoneSameInstant(toZone);
        return convertedTime.toLocalTime();
    }
}
