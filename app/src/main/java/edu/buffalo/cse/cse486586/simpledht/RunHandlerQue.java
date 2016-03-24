package edu.buffalo.cse.cse486586.simpledht;

import android.os.AsyncTask;

/**
 * Created by user on 3/19/16.
 */
public class RunHandlerQue {

    public void update_screen(String mes){
        new HandlerQue().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, mes, "11112");
    }
}
