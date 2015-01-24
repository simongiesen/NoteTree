package com.ian.notetree.util;

import com.ian.notetree.errors.ErrorHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Ian on 1/6/2015.
 */
public final class DateTimeHandler
{
    /**
     *
     */
    private final SimpleDateFormat dateFormat;

    /**
     *
     */
    private final Calendar calendar;

    /**
     * The <em>Singleton</em> instance.
     */
    private static final DateTimeHandler INSTANCE = new DateTimeHandler();

    /**
     * <code>private</code>, so as to prevent outside instantiation.
     */
    private DateTimeHandler() {
        this.dateFormat = new SimpleDateFormat("EEE M/d/yy h:mm a");
        this.calendar = GregorianCalendar.getInstance();
    }

    /**
     *
     * @param dateTime
     * @return
     */
    public static final String getString(Calendar dateTime) {
        return DateTimeHandler.INSTANCE.dateFormat.format(dateTime);
    }

    /**
     *
     * @param dateTime
     * @return
     */
    public static final Calendar getCalendar(String dateTime) {
        try {
            DateTimeHandler.INSTANCE.calendar.setTime(
                DateTimeHandler.INSTANCE.dateFormat.parse(dateTime)
            );
        } catch(Exception e) {
            ErrorHandler.handleAndExit(e);
        }

        return DateTimeHandler.INSTANCE.calendar;
    }
}
