package edu.buffalo.cse.cse486586.simpledht;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;


/**
 * Created by user on 2/12/16.
 */
public class ReceiveMessage extends AsyncTask<ServerSocket, String, Void> {
    static final String TAG = SimpleDhtActivity.class.getSimpleName();

    private final Uri mUri;
    private int num = 0;




    public ReceiveMessage() {


        mUri = Uri.parse("content://edu.buffalo.cse.cse486586.SimleDht.provider");


//        mContentResolver = _cr;
//        mUri = buildUri("content", "edu.buffalo.cse.cse486586.groupmessenger1.provider");
//        mContentValues = initTestValues();
    }

    @Override
    protected Void doInBackground(ServerSocket... sockets) {
        ServerSocket serverSocket = sockets[0];


        String Rec_msg = "";
        Socket Rec_in = null;

//        try {
//            Rec_in.setSoTimeout(10000);
//        } catch (SocketException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
//        }
        BufferedReader Bu_read = null;
        boolean isListening = true;
        boolean atlist = false;

        while (isListening ) {

            try {

                        /*
                        http://developer.android.com/reference/java/net/ServerSocket.html
//                         */
                Rec_in = serverSocket.accept();
//
//
//                Bu_read=new BufferedReader(new InputStreamReader(Rec_in.getInputStream(),"utf8"));
//
//                Rec_msg = Bu_read.readLine();
//                Bu_read.close();
//                Rec_in.close();
//                new RunHandlerQue().update_screen(Rec_msg);







                new Thread(new Socket_thread(Rec_in)).start();




            } catch (IOException e) {

                Log.e(TAG, " connect failed");
            }

        }


        return null;

    }


    protected void onProgressUpdate(String... strings) {
            /*
             * The following code displays what is received in doInBackground().
             */
        String strReceived = strings[0].trim();


        return;
    }






}

