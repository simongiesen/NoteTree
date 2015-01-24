package com.ian.notetree.errors;

import android.util.Log;
import com.ian.notetree.database.DatabaseHandler;

/**
 * Created by Ian on 1/5/2015.
 */
public final class ErrorHandler
{
    /**
     * To be used with <code>Log.wtf</code>.
     */
    private static final String ERROR_TAG = "a handled error";

    /**
     * empty implementation, <code>private</code> to prevent outside instantiation.
     */
    private ErrorHandler() {
    }

    /**
     *
     * @param errorMessage
     */
    public static final void handle(String errorMessage) {
        System.err.println(errorMessage);
        Log.wtf(ErrorHandler.ERROR_TAG, errorMessage);
    }

    /**
     *
     * @param e
     */
    public static final void handle(Throwable e) {
        e.printStackTrace();
        Log.wtf(ErrorHandler.ERROR_TAG, e);
    }

    /**
     *
     * @param errorMessage
     */
    public static final void handleAndExit(String errorMessage) {
        ErrorHandler.handle(errorMessage);
        try {
            DatabaseHandler.getInstanceOf().closeConnection();

        } catch (Throwable databaseError) {
            ErrorHandler.handle(databaseError);
        }
        System.exit(0);
    }

    /**
     *
     * @param e
     */
    public static final void handleAndExit(Throwable e) {
        ErrorHandler.handle(e);
        try {
            DatabaseHandler.getInstanceOf().closeConnection();

        } catch (Throwable databaseError) {
            ErrorHandler.handle(databaseError);
        }
        System.exit(0);
    }
}
