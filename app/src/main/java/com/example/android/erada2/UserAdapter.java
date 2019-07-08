package com.example.android.erada2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Placeholder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;


public class UserAdapter extends ArrayAdapter<User> {

    private Activity context;
    private int resource;
    private List<User> list;

    private  View v;
    public UserAdapter(@NonNull Context context, int resource,  @NonNull List<User> objects) {
        super(context, resource, objects);
      list = objects;
    }

    @NonNull

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        v = convertView;
        if (v== null) {
            v = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        TextView name = (TextView) v.findViewById(R.id.name_text_view);
        TextView address = (TextView)  v.findViewById(R.id.address_text_view);
        ImageView image = (ImageView) v.findViewById(R.id.image);


        name.setText(list.get(position).getName());
        address.setText(list.get(position).getAddress());

        Picasso.get().load(list.get(position).getmImageResourceId()).placeholder(R.drawable.defaul).into(image);


    return v;
    }







    public void addElement(User element) {

        list.add(element);
    }
}
