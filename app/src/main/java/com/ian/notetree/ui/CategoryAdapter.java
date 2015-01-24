package com.ian.notetree.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.ian.notetree.R;
import com.ian.notetree.entities.Category;
import java.util.ArrayList;

/**
 * Created by Ian on 1/13/2015.
 */
public final class CategoryAdapter extends ArrayAdapter<Category>
{
    /**
     *
     */
    private Context context;

    /**
     *
     */
    private int layoutResourceId;

    /**
     *
     */
    private ArrayList<Category> data = null;

    /**
     *
     * @param context
     * @param layoutResourceId
     * @param data
     */
    public CategoryAdapter(Context context, int layoutResourceId, ArrayList<Category> data) {
        super(context, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;

        this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // add hint as last item:
        data.add(this.data.get(0).getParentCategory());
    }

    /**
     * See <a href="https://yakivmospan.wordpress.com/2014/03/31/spinner-hint/">https://yakivmospan.wordpress.com/2014/03/31/spinner-hint/</a> for reference.
     * @return
     */
    @Override
    public int getCount() {
        // don't display last item. It is used as hint.
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    private View getCustomView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if(row == null) {
            LayoutInflater inflater = ((Activity)this.context).getLayoutInflater();
            row = inflater.inflate(this.layoutResourceId, parent, false);

            Category cat = this.data.get(position);
            CategoryHolder categoryHolder = new CategoryHolder(row, cat);
            row.setTag(categoryHolder);
        }

        return row;
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getDropDownView(int position, View convertView,  ViewGroup parent) {
        return this.getCustomView(position, convertView, parent);
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return this.getCustomView(position, convertView, parent);
    }

    /**
     *
     */
    private final class CategoryHolder
    {
        /**
         *
         */
        private View row;

        /**
         *
         */
        private Category cat;

        /**
         *
         */
        private TextView cNameTextView;

        /**
         *
         * @param row
         * @param cat
         */
        CategoryHolder(View row, Category cat) {
            this.row = row;
            this.cat = cat;

            this.cNameTextView = (TextView)row.findViewById(R.id.category_textview);
            this.cNameTextView.setText(this.cat.getCName());
        }

        /**
         *
         * @return
         */
        TextView getCNameTextView() {
            return this.cNameTextView;
        }

        /**
         *
         * @return
         */
        Category getCategory() {
            return this.cat;
        }

        /**
         *
         * @param cat
         */
        void changeCategory(Category cat) {
            this.cat = cat;
            this.cNameTextView.setText(this.cat.getCName());
        }
    }
}