package com.ian.notetree.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.ian.notetree.R;
import com.ian.notetree.entities.Note;
import java.util.ArrayList;

/**
 * Created by Ian on 1/13/2015.
 */
public final class NoteAdapter extends ArrayAdapter<Note>
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
    private ArrayList<Note> data = null;

    /**
     *
     * @param context
     * @param layoutResourceId
     * @param data
     */
    public NoteAdapter(Context context, int layoutResourceId, ArrayList<Note> data) {
        super(context, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
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
        View row = convertView;

        if(row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            Note note = data.get(position);
            NoteHolder noteHolder = new NoteHolder(row, note);
            row.setTag(noteHolder);
        }

        return row;
    }

    /**
     *
     */
    private final class NoteHolder
    {
        /**
         *
         */
        private View row;

        /**
         *
         */
        private Note n;

        /**
         *
         */
        private TextView nTextView;

        /**
         *
         */
        private TextView dateTimeTextView;

        /**
         *
         * @param row
         * @param n
         */
        NoteHolder(View row, Note n) {
            this.row = row;
            this.n = n;

            this.nTextView = (TextView)row.findViewById(R.id.note_ntext_textview);
            this.nTextView.setText(this.n.getNText());

            this.dateTimeTextView = (TextView)row.findViewById(R.id.note_datetime_textview);
            this.dateTimeTextView.setText(this.n.getDateTime());
        }

        /**
         *
         * @return
         */
        TextView getNTextView() {
            return this.nTextView;
        }

        /**
         *
         * @return
         */
        TextView getDateTimeTextView() {
            return this.dateTimeTextView;
        }

        /**
         *
         * @return
         */
        Note getNote() {
            return this.n;
        }

        /**
         *
         * @param n
         */
        void changeNote(Note n) {
            this.n = n;
            this.nTextView.setText(this.n.getNText());
            this.dateTimeTextView.setText(this.n.getDateTime());
        }
    }
}