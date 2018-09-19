package com.ncookhom.Contact;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ncookhom.R;

import java.util.ArrayList;

/**
 * Created by Ma7MouD on 4/29/2018.
 */

public class ContactAdapter  extends BaseAdapter{

    Context context ;
    ArrayList<ContactModel> list ;

    public ContactAdapter(Context context, ArrayList<ContactModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = LayoutInflater.from(context).inflate(R.layout.contact_item, viewGroup, false);

        TextView name = (TextView) view.findViewById(R.id.contact_name);
        TextView description = (TextView) view.findViewById(R.id.contact_desc);

        name.setText(list.get(i).getName()+":");
        name.setTextColor(Color.parseColor("#FFCB01"));
        description.setText(list.get(i).getDescription());
        return view;
    }
}
