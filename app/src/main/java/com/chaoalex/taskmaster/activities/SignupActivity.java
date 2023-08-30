package com.chaoalex.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.chaoalex.taskmaster.R;

public class SignupActivity extends AppCompatActivity {
  public static final String TAG = "SignupActivity";

  Button submitButton;
  EditText emailEditText;
  EditText passwordEditText;

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
      //    Cognito Signup Logic
      Amplify.Auth.signUp(emailEditText.getText().toString(),
              passwordEditText.getText().toString(),
              AuthSignUpOptions.builder()
                      .userAttribute(AuthUserAttributeKey.email(), emailEditText.getText().toString())
//                      .userAttribute(AuthUserAttributeKey.nickname(), "Chao")
                      .build(),
              successResponse -> Log.i(TAG, "Signup succeeded: " + successResponse.toString()),
              failureResponse -> Log.i(TAG, "Signup failed with username: " + "chaoalex93@gmail.com" + " with this message: " + failureResponse.toString())
      );
    });
  }
}