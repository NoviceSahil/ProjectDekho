package com.example.projectdekho;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    private EditText confirmPwdEdt;
    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText uNameEdt = findViewById(R.id.username);
        EditText pwdEdt = findViewById(R.id.etPassword);
        loadingPB = findViewById(R.id.progressBar);
        confirmPwdEdt = findViewById(R.id.etCnfPassword);
        TextView loginPg = findViewById(R.id.Login2);
        Button signUpBtn = findViewById(R.id.btnSignUp);
        mAuth = FirebaseAuth.getInstance();

        loginPg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUp.this, SignIn.class);
                startActivity(i);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingPB.setVisibility(View.VISIBLE);


                String userName = uNameEdt.getText().toString();
                String pwd = pwdEdt.getText().toString();
                String cnfPwd = confirmPwdEdt.getText().toString();


                if (!pwd.equals(cnfPwd)) {
                    Toast.makeText(SignUp.this, "Password does not match", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(pwd) && TextUtils.isEmpty(cnfPwd)) {


                    Toast.makeText(SignUp.this, "Please enter the Credentials.", Toast.LENGTH_SHORT).show();
                } else {

                    mAuth.createUserWithEmailAndPassword(userName, pwd)
                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        loadingPB.setVisibility(View.GONE);
                                        Toast.makeText(SignUp.this, "User registered",
                                                Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(SignUp.this, SignIn.class);
                                        finish();
                                    } else {
                                        loadingPB.setVisibility(View.GONE);
                                Toast.makeText(SignUp.this, "User Already Registered", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}