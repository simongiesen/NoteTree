package com.ian.notetree.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.ian.notetree.R;

/**
 * Created by Ian on 1/16/2015.
 */
public final class OptionsAdapter extends ArrayAdapter<Object>
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
    private Object[] data = null;

    /**
     *
     * @param context
     * @param layoutResourceId
     * @param data
     */
    public OptionsAdapter(Context context, int layoutResourceId, String[] data) {
        super(context, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.context = context;

        this.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // add hint as last item:
        Object[] newData = new Object[data.length + 1];
        int i = 0;
        for (int len = data.length; i < len; i++) {
            newData[i] = data[i];
        }
        newData[i] = context.getResources().getDrawable(R.drawable.gear);
        this.data = newData;
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

            OptionHolder optionHolder = new OptionHolder(row, this.data[position]);
            row.setTag(optionHolder);
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
    private final class OptionHolder
    {
        /**
         *
         */
        private View row;

        /**
         *
         */
        private Object option;

        /**
         *
         */
        private TextView optionTextView;

        /**
         *
         */
        private ImageView optionsImage;

        /**
         *
         * @param row
         * @param option
         */
        OptionHolder(View row, Object option) {
            this.row = row;
            this.option = option;

            if (option instanceof String) {
                this.optionTextView = (TextView)row.findViewById(R.id.option_textview);
                this.optionTextView.setText(option.toString());

            } else {
                this.optionsImage = (ImageView)row.findViewById(R.id.option_imageview);
                this.optionsImage.setImageResource(R.drawable.gear);
            }
        }
    }
}