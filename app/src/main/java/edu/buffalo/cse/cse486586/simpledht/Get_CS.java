package edu.buffalo.cse.cse486586.simpledht;

import android.app.Activity;
import android.content.ContentResolver;

/**
 * Created by user on 3/18/16.
 */
public class Get_CS  {
    private static ContentResolver mContentResolver;

    public void set_cs(ContentResolver mContentResolver){
        this.mContentResolver=mContentResolver;
    }

    public ContentResolver get_cs(){
        return mContentResolver;
    }
}
