package com.decathlon.log.rfid.pallet.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.List;

public class DaysUtils {


    public static List<DateTime> getDayAndDaysBefore(DateTime date, Integer daysCount, Boolean keepSundays) {
        List<DateTime> daysTocheck = new ArrayList<DateTime>();
        Integer count = 0;
        while (daysTocheck.size() < daysCount) {
            if (keepSundays || !isSunday(date.minusDays(count))) {
                daysTocheck.add(date.minusDays(count));
            }
            count++;
        }

        return daysTocheck;
    }

    public static Boolean isSunday(DateTime date) {
        return date.getDayOfWeek() == DateTimeConstants.SUNDAY;
    }

    public static boolean dateOnlyIsInArray(List<DateTime> haystack, DateTime needle) {
        DateTimeComparator comparator = DateTimeComparator.getDateOnlyInstance();
        for (DateTime date : haystack) {
            if (comparator.compare(date, needle) == 0) {
                return true;
            }
        }
        return false;
    }
}
