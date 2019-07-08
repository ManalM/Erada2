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

public class schoolFragment extends Fragment {


    private User user;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private List<User> arrayList;
    private UserAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView= inflater.inflate(R.layout.list,container,false);
        user = new User();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");

        final ListView list = (ListView)rootView.findViewById(R.id.list);

        arrayList = new ArrayList();
        adapter = new UserAdapter(getActivity(),R.layout.list_item,arrayList);
        databaseReference.child("school").addValueEventListener(new ValueEventListener() {
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
                if (user != null){
                    Intent intent = new Intent(getActivity() , SchInfoActivity.class);
                    intent.putExtra("school_name",user.getName());
                    intent.putExtra("school_address", user.getAddress());
                    intent.putExtra("school_description",user.getDescription());
                    intent.putExtra("school_phone",user.getPhone());
                    intent.putExtra("school_website",user.getWebsite());
                    intent.putExtra("school_location", user.getLocation());
                    intent.putExtra("school_email", user.getEmail());
                    intent.putExtra("school_photo",user.getmImageResourceId());
                    intent.putExtra("school_lag",user.getLag());
                    intent.putExtra("school_lat",user.getLat());
                    startActivity(intent);

                }

            }
        });
        return rootView;
    }


}
