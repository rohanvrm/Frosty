package com.example.frosty.Registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.frosty.R;

public class RegisterActivity extends AppCompatActivity {


    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        frameLayout= findViewById(R.id.frame_layout);
        setFragment(new CreateAccountFragment());
    }

    public void setFragment(Fragment fragment)          //This method takes us from one activity to another
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        if (fragment instanceof ForgetPasswordFragment || fragment instanceof OTPFragment){
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}
