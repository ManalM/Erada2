package com.example.android.erada2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

public class FormActivity extends AppCompatActivity {


    private ProgressBar progressBar;
    private ImageView userImage;

    private EditText name_input;
    private EditText address_input;
    private EditText descrp_input;
    private EditText phone_input;
    private EditText email_input;
    private EditText lat_input;
    private EditText lag_input;
    private EditText website_input;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    private String userId;
    private User user;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private Uri download_uri;
    private StorageReference storageReference;
    private static final int PERMISSIONS_REQUEST = 100;
    private FusedLocationProviderClient client;
    private Button register_btn;
    private Button logout;
    private Button saveLocation;

    UploadTask image_path;

    private Uri mainImageURI = null;

    private Boolean isChanged = false;

    private Bitmap compressedImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        mAuth = FirebaseAuth.getInstance();

        user = new User();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("User");
        storageReference = FirebaseStorage.getInstance().getReference();

        final FirebaseUser userKey = mAuth.getCurrentUser();
        userId = userKey.getUid();


        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        name_input = (EditText) findViewById(R.id.name);
        address_input = (EditText) findViewById(R.id.address);
        descrp_input = (EditText) findViewById(R.id.descrition);
        lag_input = (EditText) findViewById(R.id.lag);
        lat_input = (EditText) findViewById(R.id.lat);

        website_input = (EditText) findViewById(R.id.website);
        phone_input = (EditText) findViewById(R.id.number);
        email_input = (EditText) findViewById(R.id.email);


        progressBar = (ProgressBar) findViewById(R.id.form_progressBar);
        userImage = (ImageView) findViewById(R.id.userImage);

        register_btn = (Button) findViewById(R.id.register_btn);
        logout = (Button) findViewById(R.id.logout);
        saveLocation = (Button) findViewById(R.id.saveLocation);

        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);



        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();

                Intent intent = new Intent(FormActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        saveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permission = ContextCompat.checkSelfPermission(FormActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);


                if (permission == PackageManager.PERMISSION_GRANTED) {

                    requestLocationUpdates();
                } else {


                    ActivityCompat.requestPermissions(FormActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_REQUEST);
                }
            }
        });
          /*
   ask permission and upload image
 */
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(FormActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(FormActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(FormActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {
                        BringImagePicker();
                    }

                } else {
                    BringImagePicker();
                }
            }
        });

        /*
         retrieve data from database
*/

        databaseReference.child("clinic").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    radioGroup.check(R.id.radioClinic);

                    if (data.exists()) {
                        user = dataSnapshot.getValue(User.class);
                        retrieveValue();
                    } else {
                        Toast.makeText(FormActivity.this, "لاتوجد بيانات للمستخدم", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("center").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    radioGroup.check(R.id.radioCenter);

                    if (data.exists()) {

                        user = dataSnapshot.getValue(User.class);
                        retrieveValue();

                    } else {
                        Toast.makeText(FormActivity.this, "لاتوجد بيانات للمستخدم", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("school").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    radioGroup.check(R.id.radioSchool);

                    if (data.exists()) {

                        user = dataSnapshot.getValue(User.class);
                        retrieveValue();

                    } else {
                        Toast.makeText(FormActivity.this, "لاتوجد بيانات للمستخدم", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mainImageURI != null) {

                    if (isChanged) {

                        File newImageFile = new File(mainImageURI.getPath());
                        try {

                            compressedImageFile = new Compressor(FormActivity.this).compressToBitmap(newImageFile);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] thumbData = baos.toByteArray();
                        storageReference.child("profile_images").child(userId + ".jpg")
                                .putBytes(thumbData)
                                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                        if (task.isSuccessful()) {
                                            task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {
                                                    if (task.isSuccessful()) {
                                                        download_uri = task.getResult();
                                                        storeData();
                                                    }
                                                }
                                            });
                                            progressBar.setVisibility(View.VISIBLE);
                                        } else {

                                            progressBar.setVisibility(View.INVISIBLE);

                                            String error = task.getException().getMessage();
                                            Toast.makeText(FormActivity.this, error, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                    } else {
                        storeData();
                        progressBar.setVisibility(View.VISIBLE);
                        Toast.makeText(FormActivity.this, "تم تعديل المعلومات   ", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }




    /*

    METHODS
     */
    private void requestLocationUpdates() {
        client = LocationServices.getFusedLocationProviderClient(this);

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {

            client.getLastLocation().addOnSuccessListener(FormActivity.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        String latit = Double.toString(location.getLatitude());
                        String longi = Double.toString(location.getLongitude());

                        lat_input.setText(latit);
                        lag_input.setText(longi);
                    }
                }
            });
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {

        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


            requestLocationUpdates();
        } else {

            Toast.makeText(this, "Please enable location services to allow GPS tracking", Toast.LENGTH_SHORT).show();
        }
    }

    private void storeData() {

        if(!isChanged){
            download_uri = mainImageURI;
        }
        user.setName(name_input.getText().toString());
        user.setAddress(address_input.getText().toString());
        user.setDescription(descrp_input.getText().toString());
        Double lattitu = Double.parseDouble(lat_input.getText().toString());
        user.setLat(lattitu);
        Double longitu = Double.parseDouble(lag_input.getText().toString());
        user.setLag(longitu);
        user.setEmail(email_input.getText().toString());
        user.setPhone(phone_input.getText().toString());
        user.setWebsite(website_input.getText().toString());
        user.setmImageResourceId(download_uri.toString());

        int id = radioGroup.getCheckedRadioButtonId();
        String type;
        switch (id) {
            case R.id.radioClinic:
                type = "clinic";
                break;
            case R.id.radioCenter:
                type = "center";
                break;
            default:
                type = "school";
                break;
        }

        databaseReference.child(type).child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(FormActivity.this, "تم حفظ المعلومات ", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(FormActivity.this, "فشل في حفظ المعلومات ", Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    private void retrieveValue() {
        try {
            name_input.setText(user.getName());
            address_input.setText(user.getAddress());
            descrp_input.setText(user.getDescription());
            String latit = Double.toString(user.getLat());
            String longi = Double.toString(user.getLag());
            //   location_input.setText(user.getLocation());
            lat_input.setText(latit);
            lag_input.setText(longi);
            email_input.setText(user.getEmail());
            phone_input.setText(user.getPhone());
            website_input.setText(user.getWebsite());
            mainImageURI = Uri.parse(user.getmImageResourceId());
            //Picasso.get().load(mainImageURI).placeholder(R.drawable.defaul).into(userImage);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.defaul);
            Glide.with(FormActivity.this).setDefaultRequestOptions(requestOptions).load(mainImageURI).into(userImage);
        } catch (Exception e) {
            Toast.makeText(FormActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();
                userImage.setImageURI(mainImageURI);

                isChanged = true;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

    }


    private void BringImagePicker() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(FormActivity.this);
    }


}
