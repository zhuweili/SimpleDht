package edu.buffalo.cse.cse486586.simpledht;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Created by user on 3/19/16.
 */
public class Socket_thread implements Runnable {
    static final String TAG = SimpleDhtActivity.class.getSimpleName();
    //private BufferedReader Bu_read;
    private final Uri mUri;
    private RingList ring_list=new RingList();

    private Socket socket = null;
    public Socket_thread(Socket socket) {
        this.socket = socket;
        mUri = Uri.parse("content://edu.buffalo.cse.cse486586.simpledht.provider");


    }
    public Socket getSocket() {
        return socket;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
        try {
            this.socket.setKeepAlive(true);
            this.socket.setSoTimeout(500);
        } catch (SocketException e) {
            e.printStackTrace();
        }

    }

    private String genHash(String input) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        byte[] sha1Hash = sha1.digest(input.getBytes());
        Formatter formatter = new Formatter();
        for (byte b : sha1Hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    @Override
    public void run() {

        System.out.println("new thread!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        String Rec_msg="";

//        try {
//            Bu_read=new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "utf8"));
//            Rec_msg = Bu_read.readLine();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        while(true){
            //new RunHandlerQue().update_screen("vfb");

            try {

                InputStream ips = socket.getInputStream();
                byte[] b = new byte[128];

                if(ips.read(b)!=-1) {

                    String srt = new String(b, "utf-8");


                    Rec_msg = srt.trim();
                    System.out.println(Rec_msg);


                    // new RunHandlerQue().update_screen(Rec_msg);

                    String[] exc=Rec_msg.split("--");

                    if (exc[0].equals("join")){
                        ring_list.node_join(Integer.valueOf(exc[1]));
                        new RunHandlerQue().update_screen(Rec_msg);
                    }

                    if (exc[0].equals("update_pre")){
                        new RingPos().update_pre(Integer.valueOf(exc[1]));
                        new RunHandlerQue().update_screen(Rec_msg);


//                        String key="@";
//                        Cursor resultCursor=new Get_CS().get_cs().query(mUri, null,
//                                key, null, null);
//
//                        int keyIndex = resultCursor.getColumnIndex("key");
//                        int valueIndex = resultCursor.getColumnIndex("value");
//
//                        resultCursor.moveToFirst();

//                        int rownum=resultCursor.getCount();
//                        System.out.println(rownum);
//
//                        String hash_self=genHash(new RingPos().get_self().toString());
//                        String hash_pre=genHash(new RingPos().get_self().toString());

//                        for (Integer i=1;i<=rownum;i++) {
//                            String returnKey = resultCursor.getString(keyIndex);
//                            String returnValue = resultCursor.getString(valueIndex);
//
//                            String hash_key=genHash(returnKey);
//
//                            if (hash_key.compareTo(hash_pre)<=0 && hash_pre.compareTo(hash_self)<0){
//                                String exct="insert--"+returnKey+"--"+returnValue;
//
//                                new Thread(new Client_Thread(exct,new RingPos().get_pre())).start();
//
//                            }
//
//                            if (hash_key.compareTo(hash_self)>0 ){
//
//                                String exct="insert--"+returnKey+"--"+returnValue;
//
//                                new Thread(new Client_Thread(exct,new RingPos().get_pre())).start();
//
//                            }
//
//
//                            //publishProgress(i.toString()+"\n");
//                            resultCursor.moveToNext();
//                        }


                    }

                    if (exc[0].equals("update_suc")){
                        new RingPos().update_suc(Integer.valueOf(exc[1]));
                        new RunHandlerQue().update_screen(Rec_msg);
                    }


                    if(exc[0].equals("update_nodenum")){
                        new RingPos().update_nodenum(Integer.valueOf(exc[1]));
                        new RunHandlerQue().update_screen(Rec_msg);

                    }

                    if (exc[0].equals("insert")){
                        ContentValues cv = new ContentValues();
                        cv.put("key", exc[1]);
                        cv.put("value", exc[2]);
                        ContentResolver mContentResolver=new Get_CS().get_cs();
                        mContentResolver.insert(mUri, cv);
                        new RunHandlerQue().update_screen(Rec_msg);
                    }

                    if (exc[0].equals("query")){

                        ContentResolver mContentResolver=new Get_CS().get_cs();
                        mContentResolver.query(mUri, null, exc[1] + "--" + exc[2], null, null);
                        new RunHandlerQue().update_screen(Rec_msg);

                    }

                    if (exc[0].equals("return_query")){
                        ReturnQuery rq= new ReturnQuery();
                        rq.set_receive(exc[1]+"--"+exc[2]);
                        rq.set_return();
                        Message msg=new Message();
                        msg.obj=Rec_msg;
                        Tv_Handler th=new Tv_Handler();
                        th.get_handler().sendMessage(msg);
                    }

                    if (exc[0].equals("return*")){
                        new RunHandlerQue().update_screen(Rec_msg);
                        ReturnQuery rq= new ReturnQuery();
                        rq.set_receive_list(Rec_msg);
                        //System.out.println(Rec_msg);

                    }

                    if (exc[0].equals("done*")){

                        ReturnQuery rq= new ReturnQuery();
                        rq.add_done_num();
                        new RunHandlerQue().update_screen(Rec_msg);
                    }

                    if (exc[0].equals("delete")){

                        new RunHandlerQue().update_screen(Rec_msg);
                        ContentResolver mContentResolver=new Get_CS().get_cs();
                        mContentResolver.delete(mUri,exc[1]+"--"+exc[2],null);
                    }

                    //Thread.sleep(100);
                }


                //new RunHandlerQue().update_screen(Rec_msg);




            }catch(SocketTimeoutException e){
                //Log.e(TAG, " connect failed " + socket.getPort());
//                        Message msg=new Message();
//                        msg.obj=portname+" is dead";
//                        handler.sendMessage(msg);
//
//                        socket.close();
//                        alive=false;
                e.printStackTrace();

            }catch(SocketException e){
                Log.e(TAG, " connect failed 2222");
                //onProgressUpdate("dead");
                e.printStackTrace();
            } catch (Exception e) {
                Log.e(TAG, " connect failed  3333");
                //onProgressUpdate("dead");
                e.printStackTrace();
            }


        }
    }
}
