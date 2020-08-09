package net.simplifiedlearning.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.AbstractCollection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChildSignUpActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextChildSUEmail, editTextChildSUPassword, editTextChildSUUsername;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_sign_up);

        editTextChildSUEmail = (EditText) findViewById(R.id.editTextChildSUEmail);
        editTextChildSUPassword = (EditText) findViewById(R.id.editTextChildSUPassword);
        editTextChildSUUsername = (EditText) findViewById(R.id.editTextChildSUUsername);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.childbuttonsignup).setOnClickListener(this);
        findViewById(R.id.textViewChildLogin).setOnClickListener(this);

    }
    private void registerUser (){
        final String email = editTextChildSUEmail.getText().toString().trim();
        String username = editTextChildSUUsername.getText().toString().trim();
        final String password = editTextChildSUPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextChildSUEmail.setError("Email is required");
            editTextChildSUEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextChildSUEmail.setError(("Please enter a valid email"));
            editTextChildSUEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextChildSUPassword.setError("Password is required");
            editTextChildSUPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextChildSUPassword.setError("Your password must be at least 6 characters");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        String arr[]=email.split("@");
        Random rand = new Random();
        int temp=rand.nextInt(100000000);
        final String newEmail=arr[0]+"+"+temp+"@"+arr[1];
        Map<String, Object> user = new HashMap<>();
        user.put("email", newEmail);

        db.collection("profiles").document(username) //username is set as firestore document name, profiles is the collection name
                .set(user)  //Store the email in above document.
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) // If data is added successfully
                    {
                        mAuth.createUserWithEmailAndPassword(newEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull final Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // send user verification link using firebase
                                    progressBar.setVisibility(View.GONE);
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(ChildSignUpActivity.this, "Verification Email Has Been Sent", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();;
                                        }
                                    });



                                    Toast.makeText(getApplicationContext(), "User Register Succesfull", Toast.LENGTH_SHORT).show();
                                } else {

                                    if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        Toast.makeText(getApplicationContext(), "Parent already exists", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT);
                                    }

                                }
                            }
                        });

                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.childbuttonsignup:
                registerUser();
                break;

            case R.id.textViewChildLogin:
                startActivity(new Intent(ChildSignUpActivity.this, MainActivity.class));
                break;



        }
    }
}