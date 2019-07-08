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

public class clinicFragment extends Fragment {


    private User user;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private ArrayList<User> arrayList;
    private UserAdapter adapter;
    private ListView list;

    private int item = 0;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.list, container, false);

        user = new User();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");

        list = (ListView) rootView.findViewById(R.id.list);

        arrayList = new ArrayList();
        adapter = new UserAdapter(getActivity(),R.layout.list_item,arrayList);
        databaseReference.child("clinic").addValueEventListener(new ValueEventListener() {
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
                    Intent intent = new Intent(getActivity() , InfoActivity.class);
                    intent.putExtra("name",user.getName());
                    intent.putExtra("address", user.getAddress());
                    intent.putExtra("description",user.getDescription());
                    intent.putExtra("phone",user.getPhone());
                    intent.putExtra("website",user.getWebsite());
                    intent.putExtra("location" , user.getLocation());
                    intent.putExtra("email",user.getEmail());
                    intent.putExtra("photo",user.getmImageResourceId());
                    intent.putExtra("lag",user.getLag());
                    intent.putExtra("lat",user.getLat());
                    startActivity(intent);

                }

            }
        });

        return rootView;
    }

}
