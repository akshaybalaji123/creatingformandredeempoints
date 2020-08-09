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

import java.util.AbstractCollection;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextSUEmail, editTextSUPassword;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextSUEmail = (EditText) findViewById(R.id.editTextSUEmail);
        editTextSUPassword = (EditText) findViewById(R.id.editTextSUPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.buttonSignUp).setOnClickListener(this);
        findViewById(R.id.textViewLogin).setOnClickListener(this);

    }

    private void registerUser (){
            String email = editTextSUEmail.getText().toString().trim();
            String password = editTextSUPassword.getText().toString().trim();

            if (email.isEmpty()) {
                editTextSUEmail.setError("Email is required");
                editTextSUEmail.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextSUEmail.setError(("Please enter a valid email"));
                editTextSUEmail.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                editTextSUPassword.setError("Password is required");
                editTextSUPassword.requestFocus();
                return;
            }

            if (password.length() < 6) {
                editTextSUPassword.setError("Your password must be at least 6 characters");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Check1", Toast.LENGTH_SHORT).show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull final Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        // send user verification link using firebase

                        FirebaseUser user = mAuth.getCurrentUser();
                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(SignUpActivity.this, "Verification Email Has Been Sent", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT);
                        }

                    }
                }
            });

        }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonSignUp:
                registerUser();
                break;

            case R.id.textViewLogin:
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                break;

        }
    }
}
