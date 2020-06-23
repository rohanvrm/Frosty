package com.example.frosty.Registration;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.frosty.MainActivity;
import com.example.frosty.R;
import com.example.frosty.SplashActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;


public class UserNameActivity extends AppCompatActivity {

    //private CircleImageView profileImageView;
    private Button removeBtn, createAccountBtn;
    private ProgressBar progressBar;
    private EditText username;
    private Uri photoUri;
    //private StorageReference storage;
    private FirebaseAuth firebaseAuth;

    private CircleImageView profileImageView;

    private FirebaseFirestore firestore;

    private StorageReference  storage;
    private String url="";

    public static final String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);

        storage=FirebaseStorage.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        init();


        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dexter.withActivity(UserNameActivity.this)
                        .withPermissions(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report)
                    {
                        if (report.areAllPermissionsGranted())
                        {
                            selectImage();
                        }
                        else
                            {
                                Toast.makeText(UserNameActivity.this, "Please allow permissions", Toast.LENGTH_SHORT).show();
                            }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> list, PermissionToken permissionToken) {

                    }

                }).check();
            }
        });

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoUri = null;
                profileImageView.setImageResource(R.drawable.profileplaceholder);
            }
        });

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username.setError(null);
                if (username.getText().toString().isEmpty() || username.getText().toString().length()<3){
                    username.setError("At least 3 characters required");
                    return;
                }
                if (!username.getText().toString().matches(USERNAME_PATTERN)){
                    username.setError("Only \"a to z, 0 to 9, _ and -\" these characters allowed!");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                firestore.collection("users").whereEqualTo("username",username.getText().toString())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            List<DocumentSnapshot> document = task.getResult().getDocuments();
                            if (document.isEmpty()){
                                uploadData();
                            }else {
                                progressBar.setVisibility(View.INVISIBLE);
                                username.setError("Already taken");
                                return;
                            }
                        }else {
                            progressBar.setVisibility(View.INVISIBLE);
                            String error = task.getException().getMessage();
                            Toast.makeText(UserNameActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private void init() {
        profileImageView=findViewById(R.id.profile_image);
        removeBtn=findViewById(R.id.remove_btn);
        progressBar=findViewById(R.id.progressbar);
        username=findViewById(R.id.username);
        createAccountBtn=findViewById(R.id.create_account_btn);
    }

    private void uploadData()
    {
        if (photoUri != null){
            final StorageReference ref = storage.child("profile"+firebaseAuth.getCurrentUser().getUid());
            final UploadTask uploadTask = ref.putFile(photoUri);

               uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        String error = task.getException().getMessage();
                        Toast.makeText(UserNameActivity.this, error, Toast.LENGTH_SHORT).show();
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            url=uri.toString();
                        }
                    });
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        //Uri downloadUri = task.getResult();
                        //url= ref.getDownloadUrl().toString();
                        uploadusername();
                    } else {
                        // Handle failures
                        // ...
                        progressBar.setVisibility(View.INVISIBLE);
                        String error = task.getException().getMessage();
                        Toast.makeText(UserNameActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
            else
            {
                uploadusername();
            }
    }

    private void selectImage() {
        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityMenuIconColor(getResources().getColor(R.color.colorAccent))
                .setActivityTitle("Profile Photo")
                .setFixAspectRatio(true)
                .setAspectRatio(1, 1)
                .start(this);
    }

    private void uploadusername()
    {   Map<String,Object> map=new HashMap<>();
            map.put("username",username.getText().toString());
            map.put("profile_url",url);
        firestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    Intent mainintent = new Intent(UserNameActivity.this, MainActivity.class);
                    startActivity(mainintent);
                    finish();
                    return;
                }
                else
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    String error = task.getException().getMessage();
                    Toast.makeText(UserNameActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                 photoUri = result.getUri();

                Glide
                        .with(this)
                        .load(photoUri)
                        .centerCrop()
                        .placeholder(R.drawable.profileplaceholder)
                        .into(profileImageView);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

