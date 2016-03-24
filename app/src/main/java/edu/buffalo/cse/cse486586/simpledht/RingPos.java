package edu.buffalo.cse.cse486586.simpledht;

import android.os.AsyncTask;
import android.os.Message;

import java.util.logging.Handler;

/**
 * Created by user on 3/18/16.
 */
public class RingPos {
    private static Integer pre;
    private static Integer self;
    private static Integer suc;
    private static Integer node_num;


    public void set_RingPos(){

        PortNum port_num=new PortNum();

        self=port_num.get_port_num();
        suc=self;
        pre=self;
        node_num=1;


        String msg=self.toString()+" is online";
        new RunHandlerQue().update_screen(msg);

        //System.out.println("RingPos is ok !!!!!");
    }

    public void update_nodenum(Integer num){
        node_num=num;
        String msg="now net node num is "+node_num.toString();
        new RunHandlerQue().update_screen(msg);
    }

    public void update_pre(Integer pre){
        this.pre=pre;

        String msg="now my pre is "+this.pre.toString();
        new RunHandlerQue().update_screen(msg);
        new ClientTask(this.pre*2).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "Hello", "11112");
    }

    public void update_suc(Integer suc){
        this.suc=suc;

        String msg="now my suc is "+this.suc.toString();
        new RunHandlerQue().update_screen(msg);
        new ClientTask(this.suc*2).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "Hello", "11112");
    }

    public  Integer get_pre() {
        return pre;
    }

    public  Integer get_self() {
        return self;
    }

    public  Integer get_suc() {
        return suc;
    }

    public Integer get_nodenum(){
        return node_num;
    }
}
