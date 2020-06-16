package com.example.imagefs;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class PanCard extends AppCompatActivity {

    private static final String TAG ="TAG" ;
    private ImageView userImage;
    private EditText userImageName;
    Button submit;
    private ProgressDialog progressDialog;
    //Chose Image
    private Bitmap compressed;
    private Uri imageUri;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;
    private String message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pan_card);

        progressDialog = new ProgressDialog(this);

        userImage = findViewById(R.id.imageView);
        userImageName = findViewById(R.id.editText4);
        submit=findViewById(R.id.uploadImage);
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (ContextCompat.checkSelfPermission(PanCard.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(PanCard.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(PanCard.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    }else{
                        ChoseImage();

                    }

                }else {

                    ChoseImage();
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    progressDialog.setMessage("Storing Data...");
                    progressDialog.show();

                    final String username = userImageName.getText().toString();
                    if(!TextUtils.isEmpty(username)&&imageUri!=null){

                        File newFile = new File(imageUri.getPath());
                        try {

                            compressed = new Compressor(PanCard.this)
                                    .setMaxHeight(125)
                                    .setMaxWidth(125)
                                    .setQuality(50)
                                    .compressToBitmap(newFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        compressed.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                        byte[] thumbData = byteArrayOutputStream.toByteArray();

                        UploadTask image_path = storageReference.child("User").child(user_id + ".jpg").putBytes(thumbData);
                        image_path.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                if (task.isSuccessful()){
                                    storeData(task,username);
                                }else{
                                    String error = task.getException().getMessage();
                                    Toast.makeText(PanCard.this, "(IMAGE Error) : " + error, Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(PanCard.this, "Fill all the Fields", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

    }

    private void storeData(Task<UploadTask.TaskSnapshot> task, String username) {
        final Uri download_uri;

        if (task!=null){
            //download_uri=task.getResult().getDownloadUrl();
            download_uri=task.getResult().getUploadSessionUri();
        }else{

            download_uri=imageUri;
        }

        final Map<String, String> userData = new HashMap<>();
        userData.put("userName",username);
        userData.put("ImageVerification","0");
        userData.put("userImage",download_uri.toString());


        firebaseFirestore.collection("Users")
                .document(user_id)
                .collection("roomA")
                .document(user_id)
                .set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(PanCard.this, "user data successfully Added", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PanCard.this,Image.class));
                    Intent mainIntent = new Intent(PanCard.this, Image.class);
                    startActivity(mainIntent);
                    finish();
                }else{
                    String error = task.getException().getMessage();
                    Toast.makeText(PanCard.this, "Firestore error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });


    }

    private void ChoseImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(PanCard.this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                userImage.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

    }
}
