package edu.buffalo.cse.cse486586.simpledht;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by user on 3/17/16.
 */
public class ClientTask  extends AsyncTask<String, Void, Void> {
    static final String TAG = SimpleDhtActivity.class.getSimpleName();
    private Integer port_num;
    //static Map<Integer, Socket> socket_map = new LinkedHashMap<Integer, Socket>();
    static Integer n=0;
    @Override
    protected Void doInBackground(String... msgs) {
        Integer[] PORT={11108,11112,11116,11120,11124};
        Socket_Map socket_map=new Socket_Map();
        if (!socket_map.find(port_num)) {
            try {


                Socket socket = new Socket("10.0.2.2", port_num);
                socket.setKeepAlive(true);

                String msgToSend = msgs[0];
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
                socket_map.insert(port_num, socket);
                System.out.println("message send");

            } catch (UnknownHostException e) {
                Log.e(TAG, "ClientTask UnknownHostException");
            } catch (IOException e) {
                Log.e(TAG, "ClientTask socket IOException");
            }
        } else{

            try {




                String msgToSend = msgs[0];
                int len=msgToSend.length();

//                msgToSend+="--";
//                msgToSend+=n.toString();
//                n++;

                for (int j=1;j<=128-len;j++) {
                    msgToSend += " ";
                }

                OutputStream Send_out = socket_map.get(port_num).getOutputStream();
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

        return null;
    }

    public ClientTask(Integer port_num) {
        this.port_num=port_num;
    }
}