package edu.buffalo.cse.cse486586.simpledht;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by user on 3/20/16.
 */
public class Client_Thread implements Runnable {
    String pass_to_suc;
    //Integer portnum;
    static final String TAG = SimpleDhtActivity.class.getSimpleName();
    @Override
    public void run() {
        //new RunHandlerQue().update_screen(pass_to_suc);
        //new ClientTask(new RingPos().get_suc()*2).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, pass_to_suc, "11112");
        System.out.println(pass_to_suc);
        RingPos rp=new RingPos();



        Socket_Map socket_map=new Socket_Map();
        if (!socket_map.find(rp.get_suc())) {
            try {


                Socket socket = new Socket("10.0.2.2", rp.get_suc()*2);
                socket.setKeepAlive(true);

                String msgToSend = pass_to_suc;
//                msgToSend+="--";
//                msgToSend+=n.toString();
//                n++;

                int len=msgToSend.length();

                for (int j=1;j<=128-len;j++) {
                    msgToSend+=" ";
                }

                OutputStream Send_out = socket.getOutputStream();
                Send_out.write(msgToSend.getBytes("utf-8"));
                //Send_out.close();

                //socket.close();
                socket_map.insert(rp.get_suc(), socket);
                System.out.println("message send");

            } catch (UnknownHostException e) {
                Log.e(TAG, "ClientTask UnknownHostException");
            } catch (IOException e) {
                Log.e(TAG, "ClientTask socket IOException");
            }
        } else{

            try {




                String msgToSend = pass_to_suc;
                int len=msgToSend.length();

//                msgToSend+="--";
//                msgToSend+=n.toString();
//                n++;

                for (int j=1;j<=128-len;j++) {
                    msgToSend += " ";
                }

                OutputStream Send_out = socket_map.get(rp.get_suc()).getOutputStream();
                Send_out.write(msgToSend.getBytes("utf-8"));
                //Send_out.close();

                //socket.close();

                System.out.println("message send");

            } catch (UnknownHostException e) {
                Log.e(TAG, "ClientTask UnknownHostException");
            } catch (IOException e) {
                Log.e(TAG, "ClientTask socket IOException");
            }

        }



    }

    public Client_Thread(String s){
        pass_to_suc=s;

    }
}
