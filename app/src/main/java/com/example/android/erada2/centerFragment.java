package com.example.android.erada2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class centerFragment extends Fragment {

    private User user;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private List<User> arrayList;
    private UserAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list,container,false);
        user = new User();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");



        final ListView list = (ListView) rootView.findViewById(R.id.list);

        arrayList = new ArrayList();
        adapter = new UserAdapter(getActivity(),R.layout.list_item,arrayList);
        databaseReference.child("center").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){

                    user = data.getValue(User.class);
                    adapter.addElement(user);


                }
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = adapter.getItem(i);
                if(user != null){
                    Intent intent = new Intent(getActivity(), CenInfoActivity.class);
                    intent.putExtra("center_name" , user.getName());
                    intent.putExtra("center_address", user.getAddress());
                    intent.putExtra("center_description",user.getDescription());
                    intent.putExtra("center_phone",user.getPhone());
                    intent.putExtra("center_website",user.getWebsite());
                    intent.putExtra("center_location",user.getLocation());
                    intent.putExtra("center_email",user.getEmail());
                    intent.putExtra("center_photo",user.getmImageResourceId());
                    intent.putExtra("center_lag",user.getLag());
                    intent.putExtra("center_lat",user.getLat());
                    startActivity(intent);
                }
            }
        });
        return rootView;
    }

}
