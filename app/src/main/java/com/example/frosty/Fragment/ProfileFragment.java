package com.example.frosty.Fragment;


import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.frosty.R;
import com.example.frosty.Registration.UserNameActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private CircleImageView profile_pic;
    private TextView profile_name;              //TextView , not String
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;



    private String currentuser;



    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profile_name=view.findViewById(R.id.profile_name);
        profile_pic=view.findViewById(R.id.profile_pic);
        firebaseAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();

        currentuser=firebaseAuth.getCurrentUser().getUid();

        firestore.collection("users").document(currentuser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        String url= (String) task.getResult().get("profile_url");
                        String name= (String) task.getResult().get("username");

                        profile_name.setText(name);
                        Glide.with(getActivity()).load(url).into(profile_pic);
                    }
            }
        });


    }
}
