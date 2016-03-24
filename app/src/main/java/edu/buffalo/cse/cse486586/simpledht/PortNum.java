package edu.buffalo.cse.cse486586.simpledht;

/**
 * Created by user on 3/18/16.
 */
public class PortNum {
    private  static Integer port_num=-1;

    public void set_port_num(Integer port_num){
        this.port_num=port_num;

    }

    public Integer get_port_num(){
        return port_num;
    }
}
