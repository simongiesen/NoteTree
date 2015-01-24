package com.ian.notetree;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ian.notetree.database.DatabaseHandler;
import com.ian.notetree.entities.Category;
import com.ian.notetree.errors.ErrorHandler;
import com.ian.notetree.images.ImageHandler;
import com.ian.notetree.ui.CategoryAdapter;
import com.ian.notetree.ui.NoteAdapter;
import com.ian.notetree.ui.OptionsAdapter;

import java.util.ArrayList;

/**
 *
 */
public final class MainActivity extends ActionBarActivity
{
    /**
     *
     */
    private Category tree;

    /**
     *
     */
    private CategoryAdapter categoryArrayAdapter;

    /**
     *
     */
    private Spinner categorySpinner;

    /**
     *
     */
     private ImageButton parentCategoryButton;

    /**
     *
     */
     private String[] options;

    /**
     *
     */
     private OptionsAdapter optionsAdapter;

    /**
     *
     */
     private Spinner optionsSpinner;

    /**
     *
     */
     private ListView notesListView;

    /**
     *
     */
     private NoteAdapter notesArrayAdapter;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // first instantiating the database Singleton here:
        DatabaseHandler db = DatabaseHandler.getInstanceOf(this.getApplicationContext());

        try {
            // getting the category tree here:
            this.tree = db.getCategoryTree();

            // populating the category dropdown spinner here:
            this.categorySpinner = new Spinner(this);
            this.categoryArrayAdapter = new CategoryAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                this.tree.getSubCategories()
            );
            this.categorySpinner.setAdapter(this.categoryArrayAdapter);

            // show category hint:
            this.categorySpinner.setSelection(this.categoryArrayAdapter.getCount());

            // setting the "up" parent category button here:
            this.parentCategoryButton = new ImageButton(this);

            // populating the options dropdown spinner here:
            this.optionsSpinner = new Spinner(this);
            this.options = new String[] {
                "",
                "",
                "",
                ""
            };
            this.optionsAdapter = new OptionsAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                this.options
            );

            // show options hint:
            this.optionsSpinner.setSelection(this.optionsAdapter.getCount());

            // populating the notes list here:
            this.notesListView = (ListView)findViewById(R.id.notes_listview);
            View header = getLayoutInflater().inflate(R.layout.notes_listview_header_row, null);
            this.notesListView.addHeaderView(header);
            this.notesArrayAdapter = new NoteAdapter(this, R.layout.notes_listview_item_row, tree.getNotes());
            this.notesListView.setAdapter(this.notesArrayAdapter);

            // setting the category spinner item-click listener here:
            this.categorySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String itemValue = ((TextView)view).getText().toString();
                }
            });

        } catch (Exception e) {
            //ErrorHandler.handleAndExit(e);
            ErrorHandler.handle(e);
        }
    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
