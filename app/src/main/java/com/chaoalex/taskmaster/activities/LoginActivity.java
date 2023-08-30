package com.chaoalex.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.amplifyframework.core.Amplify;
import com.chaoalex.taskmaster.R;

public class LoginActivity extends AppCompatActivity {
  private static final String TAG = "LoginActivity";

  EditText emailEditText;
  EditText passwordEditText;
  Button submitButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    emailEditText = findViewById(R.id.LoginActivityEmailInputEditText);
    passwordEditText = findViewById(R.id.LoginActivityPasswordEditText);
    submitButton = findViewById(R.id.LoginActivityLoginButton);


    //    Cognito Login Logic
    Amplify.Auth.signIn(emailEditText.getText().toString(),
            passwordEditText.getText().toString(),
            success -> Log.i(TAG, "Login succeeded: " + success.toString()),
            failure -> Log.i(TAG, "Login failed: " + failure.toString())
    );
  }
}