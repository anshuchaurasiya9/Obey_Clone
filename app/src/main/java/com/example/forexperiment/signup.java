package com.example.forexperiment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class signup extends AppCompatActivity {

    TextInputLayout t1, t2;
        ProgressBar bar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        t1=(TextInputLayout)findViewById(R.id.userEmail);
        t2=(TextInputLayout)findViewById(R.id.password);
        bar=(ProgressBar)findViewById(R.id.progressBar);

    }

    public void GoToLogin(View view) {
        startActivity(new Intent(signup.this, login.class));
    }
    
    
    public void forloggedIn(View view) {
        {
            bar.setVisibility(View.VISIBLE);
            String email;
            email = Objects.requireNonNull(t1.getEditText()).getText().toString();
            String password = Objects.requireNonNull(t2.getEditText()).getText().toString();

            mAuth = FirebaseAuth.getInstance();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                bar.setVisibility(View.INVISIBLE);
                                t1.getEditText().setText("");
                                t2.getEditText().setText("");
                                Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_LONG).show();
                            } else {
                                bar.setVisibility(View.INVISIBLE);
                                t1.getEditText().setText("");
                                t2.getEditText().setText("");
                                Toast.makeText(getApplicationContext(), "Process Error", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }
    }


}