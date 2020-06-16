package com.example.imagefs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String curent_user_id;
    private Button signout,dataStore;

    protected void onStart() {
        super.onStart();

        if(curent_user_id==null){
            startActivity(new Intent(MainActivity.this,Login.class));
            finish();
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        curent_user_id = mAuth.getUid();

        signout = findViewById(R.id.button3);
        dataStore=findViewById(R.id.button4);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this,Login.class));
                finish();
            }
        });
dataStore.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(MainActivity.this,PanCard.class));
    }
});

    }
}
