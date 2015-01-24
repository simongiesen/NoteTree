package com.ian.notetree.entities;

import android.database.sqlite.SQLiteException;
import com.ian.notetree.database.DatabaseHandler;
import com.ian.notetree.database.DatabaseTable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is an N-ary tree structure, where the default category is the root.
 * Created by Ian on 1/6/2015.
 */
public final class Category implements Comparable<Category>
{
    /**
     *
     */
    private String cName;

    /**
     *
     */
    private Category parentCategory;

    /**
     * A <em>Singleton</em> instance.
     */
    private static final Category DEFAULT_CATEGORY = new Category();

    /**
     *
     */
    private final HashMap<String, Category> subCategories;

    /**
     * This is to be initialized from the database, and not in the constructor.
     */
    private ArrayList<Note> notes;

    /**
     *
     */
    private final DatabaseHandler db;

    /**
     *
     * @param cName
     * @param parentCategory
     */
    public Category(String cName, Category parentCategory) throws SQLiteException {
        this.cName = cName;
        this.parentCategory = parentCategory;
        this.subCategories = new HashMap<>();

        this.db = DatabaseHandler.getInstanceOf();
        this.db.insertCategory(this);
    }

    /**
     * Used to instantiate the <em>Default</em> <code>Category</code>, returned from the <code>getDefaultCategory</code> method.
     */
    private Category() {
        this.cName = DatabaseTable.Category.CName.Default();
        this.subCategories = new HashMap<>();
        
        this.db = DatabaseHandler.getInstanceOf();
    }

    /**
     *
     * @return
     */
    public String getCName() {
        return this.cName;
    }

    /**
     *
     * @param cName
     * @return
     * @throws Exception
     */
    public void changeCName(String cName) throws Exception {
        if (cName.equals(DatabaseTable.Category.CName.Default())) {
            throw new IllegalArgumentException(String.format(
                "Error -- the category name cannot be changed to %s!",
                DatabaseTable.Category.CName.Default()
            ));
        }

        this.db.changeCName(this, cName);
        this.cName = cName;
        for (Note note : this.notes) {
            note.changeCName(cName);
        }
    }

    /**
     *
     * @return
     */
    public Category getParentCategory() {
        return this.parentCategory;
    }

    /**
     *
     * @return a <em>Singleton</em> instance of this class, representing the <em>Default</em> <code>Category</code>.
     * @throws Exception
     */
    public static final Category getDefaultCategory() throws Exception {
        return Category.DEFAULT_CATEGORY;
    }

    /**
     *
     * @param cat
     * @throws Exception
     */
    public void changeParentCategory(Category cat) throws Exception {
        if (this.cName.equals(DatabaseTable.Category.CName.Default())) {
            throw new IllegalArgumentException(String.format(
                "Error -- the %s category cannot have a parent category!",
                DatabaseTable.Category.CName.Default()
            ));
        }

        this.db.changeParentCategory(this, cat);
        this.parentCategory = cat;
    }

    /**
     *
     * @param cat
     * @throws Exception
     */
    public void addSubCategory(Category cat) throws Exception {
        if (cat.getCName().equals(DatabaseTable.Category.CName.Default())) {
            throw new IllegalArgumentException(String.format(
                "Error -- the %s category cannot be a subcategory!",
                DatabaseTable.Category.CName.Default()
            ));
        } else if (!this.equals(cat.getParentCategory())) {
            throw new IllegalArgumentException(String.format(
                "Error -- this category %s is not the parent category of %s! %s is the parent category of %s.",
                this.getCName(),
                cat.getCName(),
                cat.getParentCategory(),
                cat.getCName()
            ));
        }

        this.db.insertCategory(cat);
        this.subCategories.put(cat.getCName(), cat);
    }

    /**
     *
     * @param cat
     * @throws SQLiteException
     */
    public void deleteSubCategory(Category cat) throws SQLiteException {
        this.db.deleteCategory(cat);
        this.subCategories.remove(cat.getCName());
    }

    /**
     *
     * @return
     */
    public ArrayList<Category> getSubCategories() {
        return (ArrayList<Category>)this.subCategories.values();
    }

    /**
     *
     * @param c
     * @return
     */
    public boolean isParentOf(Category c) {
        return this.equals(c.getParentCategory());
    }

    /**
     *
     * @param c
     * @return
     */
    public boolean isChildOf(Category c) {
        return this.getParentCategory().equals(c);
    }

    /**
     *
     * @param n
     */
    public void addNote(Note n) { this.notes.add(n); }

    /**
     * Initializes the <code>ArrayList&lt;Note&gt;</code> instance, to be called from <code>this.db./code>.
     * @param notes
     */
    public void addNotes(ArrayList<Note> notes) {
        this.notes = notes;
    }

    /**
     *
     * @return
     */
    public ArrayList<Note> getNotes() {
        return (ArrayList<Note>)this.notes;
    }



    /**
     *
     * @param n
     * @throws SQLiteException
     */
    public void deleteNote(Note n) throws SQLiteException {
        this.db.deleteNote(n);
        this.notes.remove(n);
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Category) {
            Category c = (Category)obj;

            return this.cName.equals(c.cName)
                && this.getParentCategory().equals(c.getParentCategory());
        }

        return false;
    }

    /**
     * This sorts by name on the same level only, not by hierarchy.
     * @param c
     * @return
     */
    @Override
    public int compareTo(Category c) {
        if (this.equals(c)) {
            return 0;

        } else if (c.cName.equals(DatabaseTable.Category.CName.Default())) {
            return -1;

        } else {
            return this.cName.compareTo(c.cName);
        }
    }
}
