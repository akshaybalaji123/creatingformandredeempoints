package net.simplifiedlearning.firebaseauth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    Button AddChoreButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        AddChoreButton = (Button) findViewById(R.id.AddChoreButton);


        findViewById(R.id.AddChoreButton).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.AddChoreButton:
                startActivity(new Intent(ProfileActivity.this, AddChoreActivity.class));
        }

    }
}
