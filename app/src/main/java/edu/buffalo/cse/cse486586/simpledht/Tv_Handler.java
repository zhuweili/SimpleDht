package edu.buffalo.cse.cse486586.simpledht;

import android.os.Handler;

/**
 * Created by user on 3/17/16.
 */
public class Tv_Handler {
    private static Handler handler;

    public void set_handler(Handler handler){
        this.handler=handler;
    }

    public Handler get_handler(){
        return handler;
    }
}
