package com.example.forexperiment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class login extends AppCompatActivity {
    TextInputLayout t1, t2;
    SignInButton btn;
    ProgressBar bar;
    GoogleSignInClient mGoogleSignInClient;
    TextView ForgotTextLink;
    FirebaseAuth mAuth;




    //TO REMAIN LOGGED IN,  IF APPLICATION IS CLOSED BY USER AND AGAIN STARTED//

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null)
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        processrequest();
        btn= findViewById(R.id.googlebtn);
        t1= findViewById(R.id.LoginUserEmail);
        t2= findViewById(R.id.LoginPassword);
        bar= findViewById(R.id.progressBar2);
        ForgotTextLink= findViewById(R.id.passwordReset);
        mAuth = FirebaseAuth.getInstance();





        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processlogin();
            }
        });
    }

// TO CREATE REQUEST FOR PASS GOOGLE SIGNIN COPY AND PASTED FROM GOOGLE AUTH//

    private void processrequest()
    {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    //TO OPEN THE GOOGLE DIALOG BOX FOR AUTH//

    private void processlogin() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 101);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(Objects.requireNonNull(account).getIdToken());
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(),"Error in getting Google's Information", Toast.LENGTH_LONG).show();

            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(), Dashboard.class));
                            finish();

                        } else {

                            Toast.makeText(getApplicationContext(),"problem found in firebase login", Toast.LENGTH_LONG).show();
                        }
                    }
                });


        // to forgot the paassword
        //creating alert dialog box

        ForgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password ?");
                passwordResetDialog.setMessage("Enter Your Email To Received Reset Link.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(login.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(login.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();

            }
        });


    }


    public void SignInHere(View view) {
        bar.setVisibility(View.VISIBLE);
        String email= requireNonNull(t1.getEditText()).getText().toString();
        String password= requireNonNull(t2.getEditText()).getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<
                            AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            bar.setVisibility(View.INVISIBLE);
                            Intent intent=new Intent(login.this,Dashboard.class);
                            intent.putExtra("email", requireNonNull(mAuth.getCurrentUser()).getEmail());
                            intent.putExtra("uid",mAuth.getCurrentUser().getUid());
                            startActivity(intent);
                        } else
                        {
                            bar.setVisibility(View.INVISIBLE);
                            t1.getEditText().setText("");
                            t2.getEditText().setText("");
                            Toast.makeText(getApplicationContext(),"Invalid email/password", Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });



    }

}

