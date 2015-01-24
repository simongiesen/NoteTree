package com.ian.notetree.entities;

import android.database.sqlite.SQLiteException;
import com.ian.notetree.database.DatabaseHandler;
import com.ian.notetree.util.DateTimeHandler;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Ian on 1/6/2015.
 */
public final class Note implements Comparable<Note>
{
    /**
     *
     */
    private String cName;

    /**
     *
     */
    private final String nText;

    /**
     *
     */
    private final Calendar dateTime;

    /**
     * Used to create new notes, to be stored in the database.
     * @param cName
     * @param nText
     * @throws SQLiteException
     */
    public Note(String cName, String nText) throws SQLiteException {
        this.cName = cName;
        this.nText = nText;

        // note that this calendar instance is used at the application-level only:
        this.dateTime = GregorianCalendar.getInstance();

        // the actual date/time is created in the database upon insert:
        DatabaseHandler.getInstanceOf().insertNote(this);
    }

    /**
     * Used to create pre-existing notes retrieved from the database.
     * @param cName
     * @param nText
     * @param dateTime
     */
    public Note(String cName, String nText, String dateTime) {
        this.cName = cName;
        this.nText = nText;
        this.dateTime = DateTimeHandler.getCalendar(dateTime);
    }

    /**
     *
     * @return
     */
    public String getCName() { return this.cName; }

    /**
     * This method is to be called after performing the same action on a <code>Category</code> object.
     * Note that database changes are not required here, as they are to be performed from the <code>Category</code> object.
     * @param cName
     * @return
     */
    public void changeCName(String cName) {
        this.cName = cName;
    }

    /**
     *
     * @return
     */
    public String getNText() { return this.nText; }

    /**
     *
     * @return
     */
    public String getDateTime() { return DateTimeHandler.getString(this.dateTime); }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Note) {
            Note n = (Note)obj;

            return this.cName.equals(n.cName)
                && this.nText.equals(n.nText)
                && this.dateTime.equals(n.dateTime);
        }

        return false;
    }

    /**
     * Used to sort messages by creation date.
     * @param n
     * @return
     */
    @Override
    public int compareTo(Note n) { return this.dateTime.compareTo(n.dateTime); }
}
