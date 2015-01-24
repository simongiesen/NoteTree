package com.ian.notetree.images;

import android.content.Context;

/**
 * Created by Ian on 1/5/2015.
 */
public final class ImageHandler
{
    /**
     * <code>private</code>, so as to prevent outside instantiation.
     */
    private ImageHandler() {
    }

    /**
     * 
     * @param context
     * @param name
     * @return
     */
    public static final int getDrawable(Context context, String name) {
        return context.getResources().getIdentifier(name,
                "drawable", context.getPackageName());
    }
}
