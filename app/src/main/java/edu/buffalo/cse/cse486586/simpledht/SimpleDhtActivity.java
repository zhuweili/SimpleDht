package edu.buffalo.cse.cse486586.simpledht;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.widget.TextView;

public class SimpleDhtActivity extends Activity {
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_dht_main);

        tv = (TextView) findViewById(R.id.textView1);
        tv.setMovementMethod(new ScrollingMovementMethod());

        new Get_CS().set_cs(getContentResolver());

        findViewById(R.id.button3).setOnClickListener(
                new OnTestClickListener(tv, getContentResolver()));

        findViewById(R.id.button1).setOnClickListener(
                new LDumpClickListener(tv, getContentResolver()));

        findViewById(R.id.button2).setOnClickListener(
                new GDumpClickListener(tv, getContentResolver()));

        Handler  handler=new Handler() {

            @Override
            public void handleMessage(Message msg) {
                tv.append(msg.obj.toString()+"\n");
            }

        };

        Tv_Handler th=new Tv_Handler();
        th.set_handler(handler);

        TelephonyManager tel = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
        String myPort = String.valueOf((Integer.parseInt(portStr) * 2));
        System.out.println(portStr);

        PortNum port_num=new PortNum();
        port_num.set_port_num(Integer.valueOf(portStr));
        System.out.println(port_num.get_port_num());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_simple_dht_main, menu);
        return true;
    }

}
