package com.example.frosty.Registration;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.frosty.MainActivity;
import com.example.frosty.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;



import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class OTPFragment extends Fragment {


    public OTPFragment(String email, String phone, String password) {
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    private EditText otp;
    private ProgressBar progressBar;
    private Button verifyBtn;
    private String email,password,phone;

    private TextView tvphone;
    private Button resendbutton;

    private Timer timer;
    private Integer count=30;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallback;

    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ot, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        firebaseAuth= FirebaseAuth.getInstance();
        tvphone.setText("Verification code has been sent to +91"+ phone );

        sendOTP();

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp.getText()==null || otp.getText().toString().isEmpty())
                {
                    return;
                }
                otp.setError(null);

                String code= otp.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                signInWithPhoneAuthCredential(credential);
                //  progressBar.setVisibility(View.VISIBLE);
                //((RegisterActivity)getActivity()).setFragment(new );
            }
        });

        timer= new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        if(count==0)
                        {
                            resendbutton.setText("Resend");
                            resendbutton.setEnabled(true);
                            resendbutton.setAlpha(1f);
                        }
                        else
                        {
                            resendbutton.setText("Resend in"+ count);
                            count--;
                        }
                    }
                });


            }
        }, 0,1000);


            resendbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resendOTP();
                    resendbutton.setText("");
                    resendbutton.setEnabled(false);
                    resendbutton.setAlpha(0.5f);
                    count=30;
                }
            });




    }

    private void init(View view){
        otp = view.findViewById(R.id.otp);
        progressBar = view.findViewById(R.id.progressBar);
        verifyBtn = view.findViewById(R.id.verify_btn);
        tvphone=view.findViewById(R.id.tv_phone);
        resendbutton=view.findViewById(R.id.resend_button);

    }
    private void sendOTP()
    {       mcallback= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            //      Log.d(TAG, "onVerificationCompleted:" + credential);

            //       signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            //         Log.w(TAG, "onVerificationFailed", e);

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                otp.setError(e.getMessage());
            } else if (e instanceof FirebaseTooManyRequestsException) {
                otp.setError(e.getMessage());
            }
            progressBar.setVisibility(View.INVISIBLE);

            // Show a message and update the UI
            // ...
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            //         Log.d(TAG, "onCodeSent:" + verificationId);

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;

            // ...
        }
    };
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91 "+phone,        // Phone number to verify                              //+91 must be included for getting message
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                 mcallback);        // OnVerificationStateChangedCallbacks
    }


    private void resendOTP()
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91 "+phone,        // Phone number to verify                              //+91 must be included for getting message
                30,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mcallback,mResendToken);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
      //                      Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            AuthCredential credential= EmailAuthProvider.getCredential(email,password);
                            user.linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task)
                                {
                                    if(task.isSuccessful())
                                    {

                                        Map<String,Object> map = new HashMap<>();
                                        map.put("email",email);
                                        map.put("phone",phone);

                                        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
                                        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).set(map)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                if(task.isSuccessful())
                                                {
                                                    Intent usernameintent= new Intent(getContext(), UserNameActivity.class);
                                                    startActivity(usernameintent);
                                                    getActivity().finish();

                                                }
                                                else
                                                {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                                   // progressBar.setVisibility(View.INVISIBLE);

                                                }
                                            }
                                        });



                                    }
                                    else
                                        {
                                            String error =task.getException().getMessage();
                                            Toast.makeText(getContext(),error,Toast.LENGTH_SHORT).show();
                                     //       progressBar.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
       //                     Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                otp.setError("Invalid OTP");// The verification code entered was invalid
                            }
                        //    progressBar.setVisibility(View.INVISIBLE);

                        }
                    }
                });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
