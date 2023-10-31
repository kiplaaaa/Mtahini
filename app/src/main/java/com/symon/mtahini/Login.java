package com.symon.mtahini;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Login extends AppCompatActivity {
    ImageButton imageButton;
    ProgressBar progressBar;
    Button loginButton;
    TextView regText;
    EditText emailInput;
    EditText passwordInput;
    String email, password;

    private FirebaseAuth mAuth;
    Navigation appNavigation;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            appNavigation.moveToHomeActivity();
        }
    }

    private void startSignUp() {
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);
    }

    private void signUpComplete() {
        progressBar.setVisibility(View.GONE);
        loginButton.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        appNavigation = new Navigation(this, Login.this);

        imageButton = findViewById(R.id.arrowButton);
        regText = findViewById(R.id.register_student_text);
        emailInput = findViewById(R.id.email_edit_text);
        passwordInput = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);
        progressBar = findViewById(R.id.progressBarLogin);


        loginButton.setOnClickListener(
                v -> {
                    startSignUp();
                    email = emailInput.getText().toString().trim();
                    password = passwordInput.getText().toString().trim();

                    if (String.valueOf(email).isEmpty()) {
                        emailInput.setError("Email address cannot be empty!");
                        emailInput.requestFocus();
                        emailInput.setBackgroundResource(R.drawable.alert_bg);
                        return;
                    }

                    if (String.valueOf(password).isEmpty()) {
                        passwordInput.setError("Password field cannot be empty!");
                        passwordInput.requestFocus();
                        passwordInput.setBackgroundResource(R.drawable.alert_bg);
                        return;
                    }

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this, task -> {
                                if (task.isSuccessful()) {
                                    appNavigation.moveToHomeActivity();
                                } else {
                                    signUpComplete();
                                    Toast.makeText(Login.this, "Login unsuccessful", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(
                                    e -> Log.d("FB_AUTH", Objects.requireNonNull(e.getLocalizedMessage()))
                            );
                }
        );

        imageButton.setOnClickListener(
                v -> {
                    Intent previousPage = new Intent(this, MainActivity.class);
                    startActivity(previousPage);
                }
        );
        regText.setOnClickListener(
                v -> {
                    Intent regActivity = new Intent(this, StudentRegistration.class);
                    startActivity(regActivity);
                }
        );
    }
}