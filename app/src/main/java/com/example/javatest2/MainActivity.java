package com.example.javatest2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import android.content.Intent;
import android.os.StatFs;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static java.lang.String.valueOf;


public class MainActivity extends AppCompatActivity {

    EditText editTextAddress;
    Button buttonConnect;
    TextView textPort,totalMemo;
    String path;

    static final int SocketServerPORT = 6125;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalMemo = (TextView)findViewById(R.id.storage);
        editTextAddress = (EditText) findViewById(R.id.address);
        textPort = (TextView) findViewById(R.id.port);
        textPort.setText("port: " + SocketServerPORT);
        buttonConnect = (Button) findViewById(R.id.connect);

        buttonConnect.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                ClientRxThread clientRxThread =
                        new ClientRxThread(
                                editTextAddress.getText().toString(),
                                SocketServerPORT);
                      clientRxThread.Asign(path);
                clientRxThread.start();
            }});
        long totalInternalValue = getTotalInternalMemorySize();

        String Intval = formatSize(totalInternalValue);
        totalMemo.setText(Intval);
    }
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return totalBlocks * blockSize;
    }
    public static String formatSize(long size) {
        String suffixSize = null;

        if (size >= 1024) {
            suffixSize = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffixSize = "MB";
                size /= 1024;
            }
        }

        StringBuilder BufferSize = new StringBuilder(
                Long.toString(size));

        int commaOffset = BufferSize.length() - 3;
        while (commaOffset > 0) {
            BufferSize.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffixSize != null) BufferSize.append(suffixSize);
        return BufferSize.toString();
    }

    public void filechoose(View v){
        Intent t = new Intent(Intent.ACTION_GET_CONTENT);
        t.setType("*/*");
        startActivityForResult(t,10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 10: if(resultCode==RESULT_OK){
                String pat;
                pat= data.getData().getPath();
                char a[] = pat.toCharArray();
                int c = a.length-15;
                char pt[]=new char[c] ;

                for(int i= 15;i< a.length;i++){

                    pt[i-15]=a[i];
                    System.out.println(pt[i-15]);

                }
                path= new String(pt);
                System.out.println(path);
                Toast.makeText(this,path,Toast.LENGTH_LONG).show();
            }
        }
    }
    private class ClientRxThread extends Thread {
        String dstAddress;
        int dstPort;
        String path;
        public void Asign(String str){
            path = str;
        }
        ClientRxThread(String address, int port) {
            dstAddress = address;
            dstPort = port;
        }

        @Override
        public void run() {
            Socket socket = null;

            try {
                socket = new Socket(dstAddress, dstPort);

                File file = new File(
                        Environment.getExternalStorageDirectory() + path);
                String fname =  file.getName();
                PrintWriter p1 = new PrintWriter(socket.getOutputStream());
                p1.write(fname+"\n");
                p1.flush();
                System.out.println(fname);
                FileInputStream fin = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fin);
                byte[] myArray = new byte[(int) file.length()];
                bis.read(myArray,0,myArray.length);
                OutputStream os = socket.getOutputStream();
                os.write(myArray,0,myArray.length);
                System.out.println("file sent");
                os.flush();


                socket.close();

                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,
                                "Finished",
                                Toast.LENGTH_LONG).show();
                    }});

            } catch (IOException e) {

                e.printStackTrace();

                final String eMsg = "Something wrong: " + e.getMessage();
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,
                                eMsg,
                                Toast.LENGTH_LONG).show();
                    }});

            } finally {
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}