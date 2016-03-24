package edu.buffalo.cse.cse486586.simpledht;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by user on 3/18/16.
 */
public class RingJoin extends AsyncTask<String, Void, Void> {
    static final String TAG = SimpleDhtActivity.class.getSimpleName();

    @Override
    protected Void doInBackground(String... params) {

        PortNum PN=new PortNum();
        while(PN.get_port_num()==-1){

        }
        Integer port_num=PN.get_port_num();

        RingPos myself=new RingPos();
        myself.set_RingPos();

        try {


            Socket socket = new Socket("10.0.2.2", 5554*2);

            String msgToSend = "join--"+port_num.toString();

            OutputStream Send_out = socket.getOutputStream();
            Send_out.write(msgToSend.getBytes("utf-8"));
            Send_out.close();

            socket.close();
            System.out.println("message send__RingJoin");

        } catch (UnknownHostException e) {
            Log.e(TAG, "ClientTask UnknownHostException__Ringjoin");
        } catch (IOException e) {
            Log.e(TAG, "ClientTask socket IOException__Ringjoin");
        }


        return null;
    }
}
