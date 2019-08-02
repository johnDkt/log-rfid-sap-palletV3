package com.decathlon.log.rfid.pallet.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.*;

public class DaysUtilsTest {

    @Test
    public void isNotSunday() {
        assertFalse(DaysUtils.isSunday(DateTime.now().withDayOfWeek(DateTimeConstants.MONDAY)));
    }

    @Test
    public void isSunday() {
        assertTrue(DaysUtils.isSunday(DateTime.now().withDayOfWeek(DateTimeConstants.MONDAY).minusDays(1)));
    }

    @Test
    public void get4DaysKeepSundays() {
        DateTime base = DateTime.now().withDayOfWeek(DateTimeConstants.MONDAY);
        List<DateTime> days = DaysUtils.getDayAndDaysBefore(base, 4, true);

        assertEquals(4, days.size());
        assertEquals(days.get(0), base);
        assertEquals(days.get(1), base.minusDays(1));
        assertEquals(days.get(2), base.minusDays(2));
        assertEquals(days.get(3), base.minusDays(3));

        assertEquals(DateTimeConstants.MONDAY, days.get(0).getDayOfWeek());
        assertEquals(DateTimeConstants.SUNDAY, days.get(1).getDayOfWeek());
        assertEquals(DateTimeConstants.SATURDAY, days.get(2).getDayOfWeek());
        assertEquals(DateTimeConstants.FRIDAY, days.get(3).getDayOfWeek());

    }

    @Test
    public void get4DaysDotKeepSundays() {
        DateTime base = DateTime.now().withDayOfWeek(DateTimeConstants.MONDAY);

        List<DateTime> days = DaysUtils.getDayAndDaysBefore(base, 4, false);

        assertEquals(4, days.size());
        assertEquals(days.get(0), base);
        assertEquals(days.get(1), base.minusDays(2));
        assertEquals(days.get(2), base.minusDays(3));
        assertEquals(days.get(3), base.minusDays(4));

        assertEquals(DateTimeConstants.MONDAY, days.get(0).getDayOfWeek());
        assertEquals(DateTimeConstants.SATURDAY, days.get(1).getDayOfWeek());
        assertEquals(DateTimeConstants.FRIDAY, days.get(2).getDayOfWeek());
        assertEquals(DateTimeConstants.THURSDAY, days.get(3).getDayOfWeek());

    }

    public void dayIsInArray() {
        DateTimeFormatter fmt = ISODateTimeFormat.date();
        DateTime toCheck = fmt.parseDateTime("2015-02-18");

        List<DateTime> listDays = newArrayList(fmt.parseDateTime("2015-02-17"), fmt.parseDateTime("2015-02-18"), fmt.parseDateTime("2015-02-19"), fmt.parseDateTime("2015-02-20"));

        assertTrue(DaysUtils.dateOnlyIsInArray(listDays, toCheck));

    }

    public void dayIsNotInArray() {
        DateTimeFormatter fmt = ISODateTimeFormat.date();
        DateTime toCheck = fmt.parseDateTime("2015-02-02");

        List<DateTime> listDays = newArrayList(fmt.parseDateTime("2015-02-17"), fmt.parseDateTime("2015-02-18"), fmt.parseDateTime("2015-02-19"), fmt.parseDateTime("2015-02-20"));
        assertFalse(DaysUtils.dateOnlyIsInArray(listDays, toCheck));

    }
}
