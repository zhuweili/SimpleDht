package edu.buffalo.cse.cse486586.simpledht;

import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by user on 3/20/16.
 */
public class Socket_Map {
    private static Map<Integer, Socket> socket_map = new LinkedHashMap<Integer, Socket>();

    public void insert(Integer port_num, Socket socket){
        socket_map.put(port_num,socket);
    }

    public boolean find(Integer port_num){
        return socket_map.containsKey(port_num);
    }

    public Socket get(Integer port_num) {
        return socket_map.get(port_num);
    }
}
