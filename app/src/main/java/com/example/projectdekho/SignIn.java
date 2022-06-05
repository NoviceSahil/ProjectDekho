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
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        EditText uNameEdt = findViewById(R.id.etusername);
        EditText pwdEdt = findViewById(R.id.etPassword);
        Button loginBtn = findViewById(R.id.btnLogin);
        TextView newUserTV = findViewById(R.id.SignUp2);
        mAuth = FirebaseAuth.getInstance();
        loadingPB = findViewById(R.id.progressBar);

        newUserTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SignIn.this, SignUp.class);
                startActivity(i);
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //  loadingPB.setVisibility(View.GONE);

                String email = uNameEdt.getText().toString();
                String password = pwdEdt.getText().toString();

                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                    Toast.makeText(SignIn.this, "Please enter your credentials..", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                              // loadingPB.setVisibility(View.GONE);
                                Toast.makeText(SignIn.this, "Login Successful", Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(SignIn.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                            // loadingPB.setVisibility(View.GONE);
                                Toast.makeText(SignIn.this, "Please enter valid user credentials..", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

            Intent i = new Intent(SignIn.this, MainActivity.class);
            startActivity(i);
            this.finish();
        }
    }
}
