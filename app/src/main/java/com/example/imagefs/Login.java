package com.example.imagefs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.annotations.NonNull;

public class Login extends AppCompatActivity {

    private EditText emailAdress,password;
    private Button signin,signup;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailAdress = findViewById(R.id.emailFiled);
        password = findViewById(R.id.passwordFiled);
        signin = findViewById(R.id.signinButton);
        signup = findViewById(R.id.signupButton);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,SignupActivity.class));
            }
        });

        mAuth = FirebaseAuth.getInstance();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailAdress.getText().toString();
                String Pass = password.getText().toString();

                if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(Pass)){
                    mAuth.signInWithEmailAndPassword(email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(Login.this,List.class));
                                finish();
                            }else {
                                Toast.makeText(Login.this,"Error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(Login.this,"You forget to put some data",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
