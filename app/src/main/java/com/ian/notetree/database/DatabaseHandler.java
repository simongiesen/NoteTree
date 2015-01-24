package com.ian.notetree.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import com.ian.notetree.entities.*;
import com.ian.notetree.errors.ErrorHandler;

import java.util.ArrayList;

/**
 * Created by Ian on 1/5/2015.
 */
public final class DatabaseHandler extends SQLiteOpenHelper
{
    /**
     *
     */
    private static final String DATABASE_NAME = "FiveDice";

    /**
     *
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * The <em>Singleton</em> instance.
     */
    private static DatabaseHandler INSTANCE;

    /**
     * <code>private</code>, to prevent outside instantiation.
     */
    private DatabaseHandler(Context context) {
        super(context, DatabaseHandler.DATABASE_NAME, null, DatabaseHandler.DATABASE_VERSION);
    }

    /**
     * This method is to be called from an <code>Activity</code>, where the application <code>Context</code> is to be passed here.
     * @param context
     * @return
     */
    public static final DatabaseHandler getInstanceOf(Context context) {
        if (DatabaseHandler.INSTANCE == null) {
            DatabaseHandler.INSTANCE = new DatabaseHandler(context);
        }

        return DatabaseHandler.INSTANCE;
    }

    /**
     * This method is to be called after the <code>getInstanceOf(Context)</code> method has been first called, where it is expected that the <em>Singleton</em> has already been instantiated.
     * @return
     */
    public static final DatabaseHandler getInstanceOf() {
        if (DatabaseHandler.INSTANCE == null) {
            ErrorHandler.handleAndExit(new IllegalArgumentException("DatabaseHandler.getInstanceOf() cannot be called before DatabaseHandler.getInstanceOf(Context)!"));
        }

        return DatabaseHandler.INSTANCE;
    }

    /**
     * Warning: This method should only be called when the program is terminated.
     * @throws Throwable
     */
    public void closeConnection() throws Throwable {
        this.close();
        this.finalize();
    }

    /**
     * Used to execute sql with no return result on a writable database, within a transaction.
     * @param sql
     * @throws SQLiteException
     */
    private void affectDatabase(String sql) throws SQLiteException {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try {
            db.execSQL(sql);
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
        }
    }

    /**
     * Called from this class' <code>getCategoryTree</code> method.
     * @param cat
     * @return
     * @throws SQLiteException
     */
    private ArrayList<Note> getNotes(Category cat) throws SQLiteException {
        if (cat == null) {
            throw new SQLiteException("The CategoryEntity parameter must not be null!");
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(
            "SELECT C." + DatabaseTable.Category.CName() + ", N." + DatabaseTable.Note.NText() + ", N." + DatabaseTable.Note.DateTime() + " " +
            "FROM " + DatabaseTable.Category() + " C, " + DatabaseTable.Note() + " M " +
            "WHERE N." + DatabaseTable.Note.CID() + " = C." + DatabaseTable.Category.CID() + " " +
            "AND C." + DatabaseTable.Category.CName() + " = '" + cat.getCName() + "';",
        null);

        ArrayList<Note> notes = new ArrayList<>();
        while (c.moveToNext()) {
            notes.add(new Note(
                c.getString(0),
                c.getString(1),
                c.getString(2)
            ));
        }
        c.close();
        return notes;
    }

    /**
     *
     * @return
     * @throws Exception
     */
    public Category getCategoryTree() throws Exception {
        Category cat = Category.getDefaultCategory();
        this.buildCategoryTree(cat);

        // navigating back to the root here:
        while (cat.getParentCategory() != null) {
            cat = cat.getParentCategory();
        }

        return cat;
    }

    /**
     * Building the tree with top-down recursion, called from <code>getCategoryTree</code>.
     * @param cat
     * @throws Exception
     * @return
     */
    private void buildCategoryTree(Category cat) throws Exception {
        cat.addNotes(this.getNotes(cat));

        // getting the current category ID, from the current category name, here:
        SQLiteDatabase db = DatabaseHandler.INSTANCE.getReadableDatabase();
        Cursor c = db.rawQuery(
            "SELECT " + DatabaseTable.Category.CID() + " FROM " + DatabaseTable.Category() + " " +
            "WHERE " + DatabaseTable.Category.CName() + " = " + cat.getCName() + ";",
        null);
        c.moveToFirst();
        int currentCID = c.getInt(0);
        c.close();

        // getting all subcategory names, where the parent category's ID equals the current category ID:
        c = db.rawQuery(
            "SELECT " + DatabaseTable.Category.CName() + " FROM " +DatabaseTable.Category() + " " +
            "WHERE " + DatabaseTable.Category.ParentCID() + " = " + currentCID + ";",
        null);

        // iterating through each subcategory name:
        while (c.moveToNext()) {

            // creating and adding each subcategory object to the tree here:
            Category subCat = new Category(c.getString(0), cat);
            cat.addSubCategory(subCat);

            // recursively processing each subcategory here:
            this.buildCategoryTree(subCat);
        }
        c.close();
    }

    /**
     *
     * @param cat
     * @throws SQLiteException
     */
    public void insertCategory(Category cat) throws SQLiteException {
        this.affectDatabase(
                "INSERT INTO " + DatabaseTable.Category() + "(" + DatabaseTable.Category.CName() + ", " + DatabaseTable.Category.ParentCID() + ") " +
                        "VALUES(" + cat.getCName() + ", " +
                        "(SELECT C." + DatabaseTable.Category.CID() + " FROM " + DatabaseTable.Category() + " C " +
                        "WHERE C." + DatabaseTable.Category.CName() + " = '" + cat.getParentCategory().getCName() + "'));"
        );
    }

    /**
     *
     * @param cat
     * @throws SQLiteException
     */
    public void deleteCategory(Category cat) throws SQLiteException {
        this.affectDatabase(
                "DELETE FROM " + DatabaseTable.Category() + " C " +
                        "WHERE C." + DatabaseTable.Category.CName() + " = '" + cat.getCName() + " " +
                        "AND C." + DatabaseTable.Category.CName() + " <> '" + DatabaseTable.Category.CName.Default() + "';"
        );
    }

    /**
     *
     * @param cat
     * @param newCName
     */
    public void changeCName(Category cat, String newCName) {
        this.affectDatabase(
                "UPDATE " + DatabaseTable.Category() + " " +
                        "SET " + DatabaseTable.Category.CName() + " = '" + newCName + "' " +
                        "WHERE " + DatabaseTable.Category.CName() + " = '" + cat.getCName() + "';"
        );
    }

    /**
     * 
     * @param cat
     * @param newParentCat
     */
    public void changeParentCategory(Category cat, Category newParentCat) {
        this.affectDatabase(
                "UPDATE " + DatabaseTable.Category() + " " +
                        "SET " + DatabaseTable.Category.ParentCID() + " = " +
                        "(SELECT C." + DatabaseTable.Category.CID() + " C " +
                        "WHERE C." + DatabaseTable.Category.CName() + " = '" + newParentCat.getCName() + "') " +
                        "WHERE " + DatabaseTable.Category.CName() + " = '" + cat.getCName() + "';"
        );
    }

    /**
     *
     * @param n
     * @throws SQLiteException
     */
    public void insertNote(Note n) throws SQLiteException {
        this.affectDatabase(
                "INSERT INTO " + DatabaseTable.Note() + "(" + DatabaseTable.Note.NText() + ", " + DatabaseTable.Note.CID() + ") " +
                        "VALUES(" + n.getNText() + ", " +
                        "(SELECT C." + DatabaseTable.Category.CID() + " FROM " + DatabaseTable.Category() + " C " +
                        "WHERE C." + DatabaseTable.Category.CName() + " = '" + n.getCName() + "'));"
        );
    }

    /**
     *
     * @param n
     * @throws SQLiteException
     */
    public void deleteNote(Note n) throws SQLiteException {
        this.affectDatabase(
                "DELETE FROM " + DatabaseTable.Note() + " M " +
                        "WHERE N." + DatabaseTable.Note.NText() + " = '" + n.getNText() + "';"
        );
    }

    /**
     *
     * @param db The database.
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        // Enabling foreign-key constraints here:
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // for testing purposes only:
        db.execSQL("DROP DATABASE IF EXISTS '" + DatabaseHandler.DATABASE_NAME + "'");

        // creating table here:
        db.execSQL(
            "CREATE TABLE " + DatabaseTable.Category() + " ( " +
            DatabaseTable.Category.CID() + " INTEGER AUTOINCREMENT NOT NULL UNIQUE, " +
            DatabaseTable.Category.CName() + " TEXT NOT NULL UNIQUE, " +
            DatabaseTable.Category.ParentCID() + " INTEGER, " +
            "PRIMARY KEY(" + DatabaseTable.Category.CID() + "), " +
            "FOREIGN KEY(" + DatabaseTable.Category.ParentCID() + ") " +
                "REFERENCES " + DatabaseTable.Category() + "(" + DatabaseTable.Category.CID() + ") " +
                "ON DELETE CASCADE, " +
            "CHECK (" + DatabaseTable.Category.ParentCID() + " <> " + DatabaseTable.Category.CID() + "));"
        );

        // creating table here:
        db.execSQL(
            "CREATE TABLE " + DatabaseTable.Note() + " ( " +
            DatabaseTable.Note.NID() + " INTEGER AUTOINCREMENT NOT NULL UNIQUE, " +
            DatabaseTable.Note.CID() + " INTEGER NOT NULL, " +
            DatabaseTable.Note.NText() + " TEXT NOT NULL, " +
            DatabaseTable.Note.DateTime() + " TEXT DEFAULT (datetime('now','localtime'))," +
            "PRIMARY KEY(" + DatabaseTable.Note.NID() + "), " +
            "FOREIGN KEY(" + DatabaseTable.Note.CID() + ") " +
                "REFERENCES " + DatabaseTable.Category() + "(" + DatabaseTable.Note.CID() + ") " +
                "ON DELETE CASCADE );"
        );

        // inserting the default category here:
        this.affectDatabase(
                "INSERT INTO " + DatabaseTable.Category() + "(" + DatabaseTable.Category.CName() + ") " +
                        "VALUES(" + DatabaseTable.Category.CName.Default() + ");"
        );
    }

    /**
     * This method is currently not implemented.
     * <p/>
     * <p>
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
