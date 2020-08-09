package net.simplifiedlearning.firebaseauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChildLoginActivity extends AppCompatActivity implements View.OnClickListener {


    EditText editTextChildLUsername, editTextChildLPassword;
    ProgressBar progressBar;



    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_login);

        editTextChildLUsername = (EditText) findViewById(R.id.editTextChildLUsername);
        editTextChildLPassword = (EditText) findViewById(R.id.editTextChildLPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);


        mAuth = FirebaseAuth.getInstance();



        findViewById(R.id.ChildbuttonLogin).setOnClickListener(this);
        findViewById(R.id.textViewChildSignup).setOnClickListener(this);
        findViewById(R.id.textViewParent).setOnClickListener(this);
    }
    private void userLogin() {
        String username = "";
        String password = "";

        username = editTextChildLUsername.getText().toString().trim();
        password = editTextChildLPassword.getText().toString().trim();


        if(TextUtils.isEmpty(username)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password", Toast.LENGTH_LONG).show();
            return;
        }

        DocumentReference docRef = db.collection("profiles").document(username);
        final String finalPassword = password;

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) // if username is present
                    {
                        String email= document.getData().get("email").toString(); //get email from document
                        login(email, finalPassword);
                    } else {

                        //add code (user not found/registered)
                    }
                } else {
                }
            }
        });

    }
    public void login(String email,String finalPassword)
    {
        mAuth.signInWithEmailAndPassword(email, finalPassword) //signin with email
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) // if password and email id matches
                        {
                            Intent intent = new Intent(ChildLoginActivity.this, ProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ChildLoginActivity.this, "login failed", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.textViewChildSignup:

                startActivity(new Intent(ChildLoginActivity.this, ChildSignUpActivity.class));
                break;

            case R.id.ChildbuttonLogin:

                userLogin();

                break;

            case R.id.textViewParent:
                startActivity(new Intent(ChildLoginActivity.this, MainActivity.class));


        }
    }
}


