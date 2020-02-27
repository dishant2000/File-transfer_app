package com.example.javatest2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.io.IOException;
import java.net.Socket;

public class Mainiptaker extends AppCompatActivity {
    static Socket s1;
    static  String dstAddress;
    EditText Iptaker ;
    SwitchCompat switchapna;
    static final int Porthai = 6125;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mainiptaker);
        Iptaker = (EditText)findViewById(R.id.ipDaal);
        switchapna = (SwitchCompat)findViewById(R.id.switchHai);
        switchapna.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {

                if (isChecked) {
                    ApnaThread apnaThread =
                            new ApnaThread(
                                    Iptaker.getText().toString(),
                                    Porthai);
                    apnaThread.start();
                }

            }
        });
    }
    private class ApnaThread extends Thread{

        int dstPort;
        public ApnaThread(String str, int port){
            dstAddress = str;
            dstPort = port;

        }
        @Override
        public void run(){
            try {
                s1 = new Socket(dstAddress,dstPort);
                Intent intent = new Intent(Mainiptaker.this,takeIP.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
