package com.example.javatest2;

import android.content.Context;
import android.view.CollapsibleActionView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class adapt extends BaseAdapter {
    Context context;
    String name[];
    String date[];
    LayoutInflater li;

    adapt(Context a, String name[],String  date[]){
        this.name=name;
        this.date=date;
        this.context=a;
        li = (LayoutInflater.from(a));




    }
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=li.inflate(R.layout.onerow,null);
        TextView t1 = convertView.findViewById(R.id.t1);
        TextView t2 = convertView.findViewById(R.id.t2);
        t1.setText(name[position]);
        t2.setText(date[position]);
        return convertView;

    }
}
