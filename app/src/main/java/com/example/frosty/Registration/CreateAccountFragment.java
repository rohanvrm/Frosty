package com.example.frosty.Registration;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frosty.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.io.FileReader;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateAccountFragment extends Fragment {


    public CreateAccountFragment() {
        // Required empty public constructor
    }

     private EditText email,phone,password,confirmpassword;
    private Button createaccountbtn;
    private TextView loginTV;

    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);     //called method made for view

        firebaseAuth= FirebaseAuth.getInstance();
        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ((RegisterActivity)getActivity()).setFragment(new LoginFragment());     //Activity setFragment is called for transfer to other activity
            }
        });

        createaccountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email.setError(null);
                phone.setError(null);
                password.setError(null);
                confirmpassword.setError(null);
                createaccountbtn.setError(null);
                if (email.getText().toString().isEmpty()) {
                    email.setError("Required");
                    return;
                }
                if (phone.getText().toString().isEmpty()) {
                    phone.setError("Required");
                    return;
                }
                if (password.getText().toString().isEmpty()) {
                    password.setError("Required");
                    return;
                }
                if (confirmpassword.getText().toString().isEmpty()) {
                    password.setError("Required");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    email.setError("Please enter a valid Email");
                    return;
                }
                if (phone.getText().toString().length() != 10) {
                    phone.setError("Please enter a valid Phone");
                    return;
                }
                if (!password.getText().toString().equals(confirmpassword.getText().toString())) {
                    password.setError("Password mismatch");
                }
                    createAccount();
            }
            });
    }

    private void createAccount(){
       // progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.fetchSignInMethodsForEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.isSuccessful()){
                    if (task.getResult().getSignInMethods().isEmpty()){
                        ((RegisterActivity)getActivity()).setFragment(new OTPFragment(email.getText().toString(),phone.getText().toString(),password.getText().toString()));

                    }
                    else {
                        email.setError("Email already taken");
                    //    progressBar.setVisibility(View.INVISIBLE);
                    }
                }
                else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                //    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


        private void init(View view)
        {
            email=view.findViewById(R.id.email);
            phone=view.findViewById(R.id.phone);
            password=view.findViewById(R.id.password);
            confirmpassword=view.findViewById(R.id.confirmpassword);
            createaccountbtn=view.findViewById(R.id.createaccountbtn);
            loginTV=view.findViewById(R.id.loginTV);
        }
}
