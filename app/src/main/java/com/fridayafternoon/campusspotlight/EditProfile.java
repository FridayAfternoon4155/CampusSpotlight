package com.fridayafternoon.campusspotlight;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfile extends AppCompatActivity {
    private static final String TAG = "demo";
    ImageView image;
    EditText etFirstName;
    EditText etLastName;
    EditText etEmail;
    EditText etPassword;
    EditText etPasswordRepeat;
    Button buttonSignUp;
    Button buttonCancel;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        image = findViewById(R.id.camera);
        etFirstName = findViewById(R.id.FName);
        etLastName = findViewById(R.id.LName);
        etEmail = findViewById(R.id.Email);
        etPassword = findViewById(R.id.Password);
        etPasswordRepeat = findViewById(R.id.passwordSignUpRepeat);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonCancel = findViewById(R.id.buttonCancel);


        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String repeat = etPasswordRepeat.getText().toString().trim();
                final String firstName = etFirstName.getText().toString().trim();
                final String lastName = etLastName.getText().toString().trim();
                Toast.makeText(EditProfile.this, "Sign Up Button.", Toast.LENGTH_SHORT).show();
                if(firstName.equals("")){
                    Toast.makeText(EditProfile.this, "Enter First Name", Toast.LENGTH_SHORT).show();
                } else if (lastName.equals("")){
                    Toast.makeText(EditProfile.this, "Enter Last Name", Toast.LENGTH_SHORT).show();
                } else if (email.equals("")){
                    Toast.makeText(EditProfile.this, "Enter Email", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")){
                    Toast.makeText(EditProfile.this, "Enter Password", Toast.LENGTH_SHORT).show();
                } else if (repeat.equals("")){
                    Toast.makeText(EditProfile.this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                } else if (!repeat.equals(password)){
                    Toast.makeText(EditProfile.this, "Enter Passwords Don't Match", Toast.LENGTH_SHORT).show();
                } else{
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(EditProfile.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("demo", "signUpWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(firstName +" "+ lastName)
                                                .build();

                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d("demo", "User profile updated");
                                                            Intent intent = new Intent(EditProfile.this, MainActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }
                                                });

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("demo", "signUpWithEmail:failure", task.getException());
                                        Toast.makeText(EditProfile.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }

            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditProfile.this, "Cancel Button.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditProfile.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


}
