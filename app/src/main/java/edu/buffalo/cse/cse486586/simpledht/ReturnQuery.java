package edu.buffalo.cse.cse486586.simpledht;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 3/19/16.
 */
public class ReturnQuery {
    private static boolean wait_for_return;
    private static boolean receive_return;
    private static String str;
    private static List<String> str_list;
    private static Integer num_done=1;


    public void ini(){
        wait_for_return=true;
        receive_return=false;
        str="";
        str_list=new ArrayList<String>();
        str_list.add("ini_head");
        num_done=1;
    }

    public void set_receive(String s){
        str=s;
    }

    public void set_receive_list(String s) {
        str_list.add(s);
        System.out.println(str_list.size());
        System.out.println("------");
    }

    public void set_return(){
        receive_return=true;
    }
    public String get_mes(){
        return str;
    }


    public  List<String> get_str_list() {
        return str_list;
    }

    public Integer get_done_num(){
        return num_done;
    }
    public void add_done_num(){
        ++num_done;
    }

    public void set_finish(){
        wait_for_return=false;
        receive_return=false;
        str="";
        str_list=new ArrayList<String>();
        str_list.add("ini_head");
        num_done=1;
    }

    public boolean is_Wait_for_return(){
        return wait_for_return;
    }

    public boolean is_Receive_return(){
        return receive_return;
    }
}
