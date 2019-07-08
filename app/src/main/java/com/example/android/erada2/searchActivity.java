package com.example.android.erada2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter_LifecycleAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class searchActivity extends AppCompatActivity {


    private EditText searchField;
    private Button searchBtn;
    private RecyclerView recyclerView, recyclerView1, recyclerView2;
    private DatabaseReference databaseReference, databaseReferenceS, databaseReferencec;
    private FirebaseRecyclerAdapter<User, UserViewHolder> firebaseRecyclerAdapter;
    private FirebaseRecyclerAdapter<User, UserViewHolder1> firebaseRecyclerAdapter1;
    private FirebaseRecyclerAdapter<User, UserViewHolder2> firebaseRecyclerAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        databaseReference = FirebaseDatabase.getInstance().getReference("User").child("center");
        databaseReferenceS = FirebaseDatabase.getInstance().getReference("User").child("school");
        databaseReferencec = FirebaseDatabase.getInstance().getReference("User").child("clinic");

        searchField = (EditText) findViewById(R.id.search_field);
        searchBtn = (Button) findViewById(R.id.search_btn);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        recyclerView1 = (RecyclerView) findViewById(R.id.recycle_view1);
        recyclerView2 = (RecyclerView) findViewById(R.id.recycle_view2);

        //  recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textSearch = searchField.getText().toString();
                Query firebaseSearch = databaseReference.orderByChild("name").startAt(textSearch).endAt(textSearch + "\uf8ff");
                Query firebaseSearchS = databaseReferenceS.orderByChild("name").startAt(textSearch).endAt(textSearch + "\uf8ff");
                Query firebaseSearchc = databaseReferencec.orderByChild("name").startAt(textSearch).endAt(textSearch + "\uf8ff");

                FirebaseRecyclerOptions queryOptions = new FirebaseRecyclerOptions.Builder<User>().setQuery(firebaseSearch, User.class).build();
                FirebaseRecyclerOptions queryOptions1 = new FirebaseRecyclerOptions.Builder<User>().setQuery(firebaseSearchS, User.class).build();
                FirebaseRecyclerOptions queryOptions2 = new FirebaseRecyclerOptions.Builder<User>().setQuery(firebaseSearchc, User.class).build();

                // search code for centers
                firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(queryOptions) {

                    @Override
                    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {
                        holder.setDetails(getApplicationContext(), model);

                    }

                    @NonNull
                    @Override
                    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.list_item, parent, false);

                        return new UserViewHolder(view);
                    }
                };

                // search code for schools
                firebaseRecyclerAdapter2 = new FirebaseRecyclerAdapter<User, UserViewHolder2>(queryOptions2) {

                    @Override
                    protected void onBindViewHolder(@NonNull UserViewHolder2 holder, int position, @NonNull User model) {
                        holder.setDetails2(getApplicationContext(), model);

                    }

                    @NonNull
                    @Override
                    public UserViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.list_item, parent, false);

                        return new UserViewHolder2(view);
                    }
                };

                // search code for clinic
                firebaseRecyclerAdapter1 = new FirebaseRecyclerAdapter<User, UserViewHolder1>(queryOptions1) {

                    @Override
                    protected void onBindViewHolder(@NonNull UserViewHolder1 holder, int position, @NonNull User model) {
                        holder.setDetails1(getApplicationContext(), model);

                    }

                    @NonNull
                    @Override
                    public UserViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.list_item, parent, false);

                        return new UserViewHolder1(view);
                    }
                };

                recyclerView.setAdapter(firebaseRecyclerAdapter);
                recyclerView1.setAdapter(firebaseRecyclerAdapter1);
                recyclerView2.setAdapter(firebaseRecyclerAdapter2);
                firebaseRecyclerAdapter.startListening();
                firebaseRecyclerAdapter1.startListening();
                firebaseRecyclerAdapter2.startListening();
                Toast.makeText(searchActivity.this, "start searching", Toast.LENGTH_LONG).show();
            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        View v;

        public UserViewHolder(View itemView) {
            super(itemView);
            v = itemView;


        }

        public void setDetails(Context ctx, final User model) {

            TextView user_name = (TextView) v.findViewById(R.id.name_text_view);
            TextView user_address = (TextView) v.findViewById(R.id.address_text_view);
            ImageView user_image = (ImageView) v.findViewById(R.id.image);

            user_name.setText(model.getName());
            user_address.setText(model.getAddress());

            Glide.with(ctx).load(model.getmImageResourceId()).into(user_image);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(searchActivity.this, CenInfoActivity.class);
                    intent.putExtra("center_name", model.getName());
                    intent.putExtra("center_address", model.getAddress());
                    intent.putExtra("center_description", model.getDescription());
                    intent.putExtra("center_phone", model.getPhone());
                    intent.putExtra("center_website", model.getWebsite());
                    intent.putExtra("center_location", model.getLocation());
                    intent.putExtra("center_email", model.getEmail());
                    intent.putExtra("center_photo", model.getmImageResourceId());
                    intent.putExtra("center_lag", model.getLag());
                    intent.putExtra("center_lat", model.getLat());
                    startActivity(intent);

                }
            });
        }
    }

    public class UserViewHolder1 extends RecyclerView.ViewHolder {
        View v;

        public UserViewHolder1(View itemView) {
            super(itemView);
            v = itemView;


        }

        public void setDetails1(Context ctx, final User model) {

            TextView user_name = (TextView) v.findViewById(R.id.name_text_view);
            TextView user_address = (TextView) v.findViewById(R.id.address_text_view);
            ImageView user_image = (ImageView) v.findViewById(R.id.image);

            user_name.setText(model.getName());
            user_address.setText(model.getAddress());

            Glide.with(ctx).load(model.getmImageResourceId()).into(user_image);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(searchActivity.this, SchInfoActivity.class);
                    intent.putExtra("school_name", model.getName());
                    intent.putExtra("school_address", model.getAddress());
                    intent.putExtra("school_description", model.getDescription());
                    intent.putExtra("school_phone", model.getPhone());
                    intent.putExtra("school_website", model.getWebsite());
                    intent.putExtra("school_location", model.getLocation());
                    intent.putExtra("school_email", model.getEmail());
                    intent.putExtra("school_photo", model.getmImageResourceId());
                    intent.putExtra("school_lag", model.getLag());
                    intent.putExtra("school_lat", model.getLat());
                    startActivity(intent);

                }
            });
        }
    }

    public class UserViewHolder2 extends RecyclerView.ViewHolder {
        View v;

        public UserViewHolder2(View itemView) {
            super(itemView);
            v = itemView;


        }

        public void setDetails2(Context ctx, final User model) {

            TextView user_name = (TextView) v.findViewById(R.id.name_text_view);
            TextView user_address = (TextView) v.findViewById(R.id.address_text_view);
            ImageView user_image = (ImageView) v.findViewById(R.id.image);

            user_name.setText(model.getName());
            user_address.setText(model.getAddress());

            Glide.with(ctx).load(model.getmImageResourceId()).into(user_image);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(searchActivity.this, InfoActivity.class);
                    intent.putExtra("name", model.getName());
                    intent.putExtra("address", model.getAddress());
                    intent.putExtra("description", model.getDescription());
                    intent.putExtra("phone", model.getPhone());
                    intent.putExtra("website", model.getWebsite());
                    intent.putExtra("location", model.getLocation());
                    intent.putExtra("email", model.getEmail());
                    intent.putExtra("photo", model.getmImageResourceId());
                    intent.putExtra("lag", model.getLag());
                    intent.putExtra("lat", model.getLat());
                    startActivity(intent);

                }
            });
        }
    }

}
