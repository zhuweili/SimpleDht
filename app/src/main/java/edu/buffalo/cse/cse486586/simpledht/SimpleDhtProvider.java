package edu.buffalo.cse.cse486586.simpledht;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

public class SimpleDhtProvider extends ContentProvider {

    static final String TAG = SimpleDhtActivity.class.getSimpleName();
    public static final String PREFS_NAME = "PA3";
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    static boolean first_time=true;
    static Map<String, Integer> Received_map = new LinkedHashMap<String, Integer>();
    static Map<String, Integer> query_map = new LinkedHashMap<String, Integer>();

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        String selection_1=selection;
        String[] temp={};
        String exc="";
        if (selection_1.contains("--")){
            temp=selection.split("--");
            selection=temp[0];
            exc="delete--"+selection+"--"+temp[1];
        }
        else{
            exc="delete--"+selection+"--"+new RingPos().get_self().toString();
        }

        if (selection.equals("@")){
            dump_old();
            return 0;
        }

        if(selection.equals("*")){

            dump_old();



            //return 0;

        }// end of "*"

        sp=this.getContext().getSharedPreferences(PREFS_NAME, 0);
        edit=sp.edit();
        edit.remove(selection);
        edit.commit();

        if(selection_1.contains("--")) {
            String suc = new RingPos().get_suc().toString();
            if (!suc.equals(temp[1])){

                Thread t= new Thread(new Client_Thread(exc));
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            Thread t= new Thread(new Client_Thread(exc));
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //for any key and "*" always send mes to suc


        return 0;
    }






    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO Auto-generated method stub





        sp=this.getContext().getSharedPreferences(PREFS_NAME, 0);
        edit=sp.edit();

        String key=(String)values.get("key");
        String value=(String)values.get("value");
        String into_avd=value;

        System.out.println(key);
        System.out.println(value);

        String hash_key="";
        String hash_self="";
        String hash_pre="";
        RingPos rs=new RingPos();

        try {


            hash_key=genHash(key);
            hash_self=rs.get_self().toString();
            System.out.println(hash_self);
            hash_self=genHash(hash_self);


            hash_pre=genHash(rs.get_pre().toString());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        if ( ( hash_pre.compareTo(hash_key)<0 || hash_pre.compareTo(hash_self)>=0 )  && hash_self.compareTo(hash_key)>=0 ) {
            edit.putString(key, into_avd);
            edit.commit();
            return uri;

        }


        if ( hash_pre.compareTo(hash_self)>=0 && hash_pre.compareTo(hash_key)<0 ) {

            edit.putString(key, into_avd);
            edit.commit();
            Received_map=new LinkedHashMap<String, Integer>();
            return uri;

        }

        String pass_to_suc="insert--"+key+"--"+into_avd;

        //new Thread(new Client_Thread(pass_to_suc)).start();

        Thread t= new Thread(new Client_Thread(pass_to_suc));
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //new ClientTask(rs.get_suc()*2).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, pass_to_suc, "11112");


        Log.v("insert", values.toString());
        return uri;

    }

    @Override
    public boolean onCreate() {
        // TODO Auto-generated method stub
        try {

            ServerSocket serverSocket = new ServerSocket(10000);
            new ReceiveMessage().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSocket);

        } catch (IOException e) {

            Log.e(TAG, "Can't create a ServerSocket");
            return false;
        }

        dump_old();
        new RingJoin().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "join", "11112");





        return false;
    }

    public void dump_old(){
        sp=this.getContext().getSharedPreferences(PREFS_NAME, 0);
        edit=sp.edit();
        edit.clear();
        edit.commit();
    }





    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // TODO Auto-generated method stub

        sp=this.getContext().getSharedPreferences(PREFS_NAME, 0);

        String[] temp={};
        String selection_1=selection;
        if (selection_1.contains("--")){
            temp=selection.split("--");
            selection=temp[0];
        }


        if ( selection.equals("@")  ) {
            Map<String,?> all_out = sp.getAll();

            String[] row_name={"key", "value"};
            MatrixCursor mc=new MatrixCursor(row_name);


            for(Map.Entry<String,?> entry : all_out.entrySet()){

                String key=entry.getKey().toString();
                String value=entry.getValue().toString();

                MatrixCursor.RowBuilder add1=mc.newRow();
                add1.add(key);
                add1.add(value);

            }

            return mc;

        }

        // return for "@"

        if (selection.equals("*")) {

            String exc ="";
            String next_port="";
            ReturnQuery rt = new ReturnQuery();
            rt.ini();

            if (selection_1.contains("--")) {

                 exc = "query--*"+"--"+temp[1];
                 next_port=temp[1];
            }
            else{
                 exc ="query--*"+"--"+new RingPos().get_self().toString();
                 next_port=new RingPos().get_self().toString();
            }

            String succ=new RingPos().get_suc().toString();

            if(!succ.equals(next_port)) {

                Thread t = new Thread(new Client_Thread(exc));
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //new RunHandlerQue().update_screen(succ+"  -----  "+next_port);
            }

            if(!selection_1.contains("--")) { //host node

//                ReturnQuery rt = new ReturnQuery();
//                rt.ini();

                RingPos rp = new RingPos();
                Integer node_num = rp.get_nodenum();


                while (rp.get_nodenum() > 1 && rt.get_done_num() < node_num) {

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //rt=new ReturnQuery();
                    System.out.println( rt.get_done_num());

                }

                //new RunHandlerQue().update_screen("get message num--"+rt.get_str_list().size() );
                List<String> rs_list=new ReturnQuery().get_str_list();
                Map<String,?> all_out = sp.getAll();
                String[] row_name={"key", "value"};
                MatrixCursor mc=new MatrixCursor(row_name);

                for(Map.Entry<String,?> entry : all_out.entrySet()){

                    String key=entry.getKey().toString();
                    String value=entry.getValue().toString();

                    MatrixCursor.RowBuilder add1=mc.newRow();
                    add1.add(key);
                    add1.add(value);

                }//load local

                for (int k=1;k<rs_list.size();k++) {
                    String[] temp_pair=rs_list.get(k).split("--");
                    MatrixCursor.RowBuilder add1 = mc.newRow();
                    //String[] temp_str=rs_list.get(0).split("--");
                    add1.add(temp_pair[1]);
                    add1.add(temp_pair[2]);
                }// load network return
                rt.set_finish();
                //System.out.println(temp_str[2]+"---"+temp_str[3]);



                return mc;
            }


            if (selection_1.contains("--")){// not host node
                rt.set_finish();
                String exc_1="return*";

                Map<String,?> all_out = sp.getAll();

                String[] row_name={"key", "value"};
                MatrixCursor mc=new MatrixCursor(row_name);


                for(Map.Entry<String,?> entry : all_out.entrySet()){

                    String key=entry.getKey().toString();
                    String value=entry.getValue().toString();

                    exc_1=exc_1+"--"+key;
                    exc_1=exc_1+"--"+value;
                    new ClientTask(Integer.valueOf(temp[1])*2).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, exc_1, "11112");
                    exc_1="return*";
                }

                new ClientTask(Integer.valueOf(temp[1])*2).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "done*--"+(new RingPos().get_self()), "11112");

                System.out.println(exc_1);
                return null;

            }



            return null;


        }

        //return for "*"


        //start for any key
        String hash_key="";
        String hash_self="";
        String hash_pre="";
        RingPos rs=new RingPos();

        try {


            hash_key=genHash(selection);
            hash_self=genHash(rs.get_self().toString());

            hash_pre=genHash(rs.get_pre().toString());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


        if ( ( hash_pre.compareTo(hash_key)<0 || hash_pre.compareTo(hash_self)>=0 ) && hash_self.compareTo(hash_key)>=0 ) {


            String msg ="----- "+selection+" is in my local";
            new RunHandlerQue().update_screen(msg);

            String out_avd=sp.getString(selection, null);
            String key=selection;
            String value=out_avd;

            if(selection_1.contains("--")) {
                String exc = "return_query--" + key + "--" + value;
                new ClientTask(Integer.valueOf(temp[1])*2).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, exc, "11112");
                return null;

            }
            else {


                String[] row_name = {"key", "value"};
                MatrixCursor mc = new MatrixCursor(row_name);
                MatrixCursor.RowBuilder add1 = mc.newRow();
                add1.add(key);
                add1.add(value);

                return mc;
            }

        }

        if (hash_pre.compareTo(hash_self)>=0 && hash_pre.compareTo(hash_key)<0 ) {


            String s_msg="------ "+selection+" is in my local";

            Message msg=new Message();
            msg.obj=s_msg;
            Tv_Handler th=new Tv_Handler();
            th.get_handler().sendMessage(msg);

            String out_avd=sp.getString(selection, null);
            String key=selection;
            String value=out_avd;

            if(selection_1.contains("--")) {
                String exc = "return_query--" + key + "--" + value;
                new ClientTask(Integer.valueOf(temp[1])*2).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, exc, "11112");
                return null;

            }
            else {


                String[] row_name = {"key", "value"};
                MatrixCursor mc = new MatrixCursor(row_name);
                MatrixCursor.RowBuilder add1 = mc.newRow();
                add1.add(key);
                add1.add(value);

                return mc;
            }

        }


        // start to wait for Return
        String pass_to_suc;
        ReturnQuery st=new ReturnQuery();
        if (selection_1.contains("--")) {

            pass_to_suc = "query--" + selection+"--"+temp[1];
            System.out.println(pass_to_suc);
            st.set_finish();

        }
        else{
            pass_to_suc = "query--" + selection+"--"+rs.get_self().toString();
            System.out.println(pass_to_suc);
            st.ini();


        }

        System.out.println("hbjvjdfkv bdfkv dfnkv ndfgl");



        Thread t= new Thread(new Client_Thread(pass_to_suc));
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //System.out.println("hbjvjdfkv bdfkv dfnkv ndfgl");

//        try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
        System.out.println(st.is_Receive_return());


        if (st.is_Wait_for_return()) {

            //new RunHandlerQue().update_screen("wait query !!!!!!!!!!!!");

            while (!st.is_Receive_return()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (st.is_Receive_return()) {
                //new RunHandlerQue().update_screen("received query !!!!!!!!!!!!");

                String[] row_name = {"key", "value"};
                MatrixCursor mc = new MatrixCursor(row_name);
                MatrixCursor.RowBuilder add1 = mc.newRow();
                //new RunHandlerQue().update_screen(st.get_mes());
                String[] res=st.get_mes().split("--");
                add1.add(res[0]);
                add1.add(res[1]);
                st.set_finish();
                return mc;
            }
        }








        return null;

    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub




        return 0;
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
}
