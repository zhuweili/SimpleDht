package edu.buffalo.cse.cse486586.simpledht;

import android.os.AsyncTask;
import android.os.Message;

/**
 * Created by user on 3/19/16.
 */
public class HandlerQue extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... params) {
        Message msg=new Message();
        msg.obj=params[0];
        Tv_Handler th=new Tv_Handler();
        th.get_handler().sendMessage(msg);

        return null;
    }
}
