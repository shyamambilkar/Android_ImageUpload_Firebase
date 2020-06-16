package com.example.imagefs;

import androidx.annotation.NonNull;
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

public class SignupActivity extends AppCompatActivity {

    EditText emailFiled,passwordFiled,checkPasswordFiled;
    private Button register,signin;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailFiled=findViewById(R.id.editText);
        passwordFiled=findViewById(R.id.editText2);
        checkPasswordFiled=findViewById(R.id.editText3);

        register=findViewById(R.id.button2);
        signin=findViewById(R.id.button);

        mAuth=FirebaseAuth.getInstance();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this,Login.class));
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email =emailFiled.getText().toString();
                String password=passwordFiled.getText().toString();
                String repassword=checkPasswordFiled.getText().toString();

                if(!TextUtils.isEmpty(email)&!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(repassword)){

                    if (password.equals(repassword)){

                        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()){
                                    startActivity(new Intent(SignupActivity.this,MainActivity.class));
                                    finish();
                                }else{

                                    Toast.makeText(SignupActivity.this, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        
                    }else {

                        Toast.makeText(SignupActivity.this, "Password Does not match", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(SignupActivity.this, "All fields are Required", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
}
