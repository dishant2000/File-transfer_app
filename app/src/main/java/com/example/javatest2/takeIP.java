package com.example.javatest2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.javatest2.Mainiptaker.Porthai;
import static com.example.javatest2.Mainiptaker.dstAddress;
import static com.example.javatest2.Mainiptaker.s1;

public class takeIP extends AppCompatActivity {
  Button Selectf;
  Button Sendf;
    String fname;
    String path;
  TextView totalMemo;
  TextView avamemo;
  TextView Showper;
  ProgressBar p1;
  Handler handler = new Handler();
  ListView v1 ;
  String name[]=new String[50];
  String date[]=new String[50];
  int a=0;
    adapt a1;
    int pStatus = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_take_ip);
        Selectf = (Button)findViewById(R.id.selectFile);
        totalMemo = (TextView)findViewById(R.id.dekh);
        avamemo = (TextView)findViewById(R.id.dikha);
        Showper = findViewById(R.id.progressShow);
        Sendf = (Button)findViewById(R.id.sendFile);
        v1= findViewById(R.id.lv);
        a1= new adapt(this,name,date);
        v1.setAdapter(a1);

        Sendf.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
             try {
                 Sendkaro sending = new Sendkaro();
                 DateFormat DF= new SimpleDateFormat("hh:mm:ss");
                 Date db = new Date();
                 date[a]= DF.format(db);
                 sending.start();
                 sending.join();



                 name[a] = fname;
                 a1.notifyDataSetChanged();
                 System.out.println(name[a]);
                 ++a;
                 System.out.println(a);
             }
             catch (Exception e){
                 e.printStackTrace();
             }

            }});
        long totalInternalValue = getTotalInternalMemorySize();
        long Availablemem = getAvailableInternalMemorySize();
        double percentage = ((double)Availablemem/(double)totalInternalValue)*100;
        System.out.println(percentage);
        final int realper = (int)percentage;
        String percen1 = String.valueOf(100-realper);
        String percen2 = percen1 + "%";
        String Intval = formatSize(totalInternalValue);
        String Avaval = formatSize(Availablemem);
        totalMemo.setText(Intval);
        avamemo.setText(Avaval);
        Showper.setText(percen2);
        p1= findViewById(R.id.progressBar);
        p1.setMax(0);
        p1.setMax(100);
        p1.setSecondaryProgress(100);

        new Thread(new Runnable() {

            @Override
            public void run() {

                while (pStatus < (100-realper)) {
                    pStatus += 1;

                    handler.post(new Runnable() {

                        @Override
                        public void run() {

                            p1.setProgress(pStatus);


                        }
                    });
                    try {

                        Thread.sleep(16);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    public static long getTotalInternalMemorySize() {
        File path1 = Environment.getDataDirectory();
        StatFs stat = new StatFs(path1.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return totalBlocks * blockSize;
    }
    public static long getAvailableInternalMemorySize() {
        File path1 = Environment.getDataDirectory();
        StatFs stat = new StatFs(path1.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return availableBlocks * blockSize;
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
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {
                    String pat;
                    pat = data.getData().getPath();
                    char a[] = pat.toCharArray();
                    int c = a.length - 15;
                    char pt[] = new char[c];

                    for (int i = 15; i < a.length; i++) {

                        pt[i - 15] = a[i];
                        System.out.println(pt[i - 15]);

                    }
                    path = new String(pt);
                    System.out.println(path);

                }
        }
    }
    protected  class Sendkaro extends Thread{

        @Override
        public void run(){
            try{
                Socket soc= s1;
                File file = new File(
                        Environment.getExternalStorageDirectory() + path);
                fname =  file.getName();
                PrintWriter p1 = new PrintWriter(soc.getOutputStream());
                p1.write(fname+"\n");
                p1.flush();
                System.out.println(fname);
                FileInputStream fin = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fin);
                byte[] myArray = new byte[(int) file.length()];
                bis.read(myArray,0,myArray.length);
                OutputStream os = soc.getOutputStream();
                os.write(myArray,0,myArray.length);
                System.out.println("file sent");

                os.flush();
                soc.close();

                Thread.sleep(1000);
                s1 = new Socket(dstAddress,Porthai);





                takeIP.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(takeIP.this,
                                "Finished",
                                Toast.LENGTH_LONG).show();
                    }});

            }
            catch (Exception e){
                e.printStackTrace();
            }
                    }

    }


}
