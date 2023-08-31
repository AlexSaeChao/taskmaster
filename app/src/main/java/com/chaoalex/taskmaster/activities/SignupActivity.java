package com.chaoalex.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.chaoalex.taskmaster.R;

public class SignupActivity extends AppCompatActivity {
  public static final String TAG = "SignupActivity";

  EditText emailEditText;
  EditText passwordEditText;
  Button submitButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);


    emailEditText = findViewById(R.id.SignupActivityEditTextTextEmailAddress);
    passwordEditText = findViewById(R.id.SignupAcitivityEditTextTextPassword);
    submitButton = findViewById(R.id.SignupActivitySignupButton);


    setupSubmitButton();

  }

  void setupSubmitButton() {
    submitButton.setOnClickListener(v -> {
      Amplify.Auth.signUp(
              emailEditText.getText().toString(),
              passwordEditText.getText().toString(),
              AuthSignUpOptions.builder()
                      .userAttribute(AuthUserAttributeKey.email(), emailEditText.getText().toString())
                      .build(),
              successResponse -> {
                Log.i(TAG, "Signup succeeded: " + successResponse.toString());
//                Toast.makeText(SignupActivity.this, "Signup succeeded", Toast.LENGTH_SHORT).show();

                Intent goToVerifyActivity = new Intent(SignupActivity.this, VerifyActivity.class);
                goToVerifyActivity.putExtra("email", emailEditText.getText().toString());

                startActivity(goToVerifyActivity);
              },
              failureResponse -> {
                Log.i(TAG, "Signup failed: " + failureResponse.toString());
//                Toast.makeText(SignupActivity.this, "Signup failed", Toast.LENGTH_SHORT).show();
              }
      );
    });
  }
}