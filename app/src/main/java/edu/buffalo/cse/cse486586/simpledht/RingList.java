package edu.buffalo.cse.cse486586.simpledht;

import android.os.AsyncTask;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by user on 3/18/16.
 */
public class RingList {
    private  static List<ring_node> ring_list= new ArrayList<ring_node>();


    public void node_join(Integer port_num){

        String temp=port_num.toString();

        try {
            temp=genHash(temp);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        ring_node node= new ring_node(port_num,temp);
        ring_list.add(node);

        Collections.sort(ring_list, new Comparator<ring_node>()//sort for topk
                {
                    public int compare(ring_node s1, ring_node s2) {

                        String l1 = s1.get_hash_val();
                        String l2 = s2.get_hash_val();

                        return l1.compareTo(l2);
                    }
                }
        );

        int i=0;

        for(i=0;i<ring_list.size();i++){
            if(ring_list.get(i).get_port_num()==port_num)
                break;
        }

        System.out.println(" Ringlist is ok");

        if (i==0){
            if (i+1<ring_list.size()){
                Integer next_node=ring_list.get(i+1).get_port_num();
                String exc="update_pre--"+port_num.toString();
                new ClientTask(next_node*2).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, exc, "11112");

                exc="update_suc--"+next_node.toString();
                new ClientTask(port_num*2).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, exc, "11112");

                Integer tail=ring_list.get(ring_list.size()-1).get_port_num();
                exc="update_suc--"+port_num.toString();
                new ClientTask(tail*2).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, exc, "11112");

                exc="update_pre--"+tail.toString();
                new ClientTask(port_num*2).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, exc, "11112");

                Integer n_num=ring_list.size();

                exc="update_nodenum--"+n_num.toString();

                for (int j=0;j<ring_list.size();j++){

                    new ClientTask(ring_list.get(j).get_port_num()*2).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, exc, "11112");

                }

            }



        }

        if (i==ring_list.size()-1 && ring_list.size()>1) {
            Integer pre_node=ring_list.get(i-1).get_port_num();
            String exc="update_suc--"+port_num.toString();
            new ClientTask(pre_node*2).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, exc, "11112");

            exc="update_pre--"+pre_node.toString();
            new ClientTask(port_num*2).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, exc, "11112");

            Integer head=ring_list.get(0).get_port_num();
            exc="update_suc--"+head.toString();
            new ClientTask(port_num*2).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, exc, "11112");

            exc="update_pre--"+port_num.toString();
            new ClientTask(head*2).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, exc, "11112");


            Integer n_num=ring_list.size();

            exc="update_nodenum--"+n_num.toString();

            for (int j=0;j<ring_list.size();j++){

                new ClientTask(ring_list.get(j).get_port_num()*2).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, exc, "11112");

            }


        }

        if (0< i && i<ring_list.size()-1) {
            Integer pre_node=ring_list.get(i-1).get_port_num();
            String exc="update_suc--"+port_num.toString();
            new ClientTask(pre_node*2).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, exc, "11112");
            exc="update_pre--"+pre_node.toString();
            new ClientTask(port_num*2).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, exc, "11112");

            Integer next_node=ring_list.get(i+1).get_port_num();
            exc="update_pre--"+port_num.toString();
            new ClientTask(next_node*2).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, exc, "11112");
            exc="update_suc--"+next_node.toString();
            new ClientTask(port_num*2).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, exc, "11112");


            Integer n_num=ring_list.size();

            exc="update_nodenum--"+n_num.toString();

            for (int j=0;j<ring_list.size();j++){

                new ClientTask(ring_list.get(j).get_port_num()*2).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, exc, "11112");

            }


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







    class ring_node{
        private Integer port_num;
        private String hash_val;

        public ring_node(Integer PortNum, String Hash_val)  {
            port_num=PortNum;

            hash_val=Hash_val;
        }



        public Integer get_port_num(){
            return port_num;
        }

        public String get_hash_val() {
            return hash_val;
        }
    }


}
