package com.chaoalex.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.core.Amplify;
import com.chaoalex.taskmaster.MainActivity;
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

    String email = getIntent().getStringExtra("email");
    if (email != null && !email.isEmpty()) {
      emailEditText.setText(email);
    } else {
      Log.i(TAG, "Email was not passed or is empty");
    }


    setupSubmitButton();

  }


  void setupSubmitButton() {
    submitButton.setOnClickListener(v -> {
      //    Cognito Login Logic
      Amplify.Auth.signIn(
              emailEditText.getText().toString(),
              passwordEditText.getText().toString(),
              success -> {
                Log.i(TAG, "Login succeeded: " + success.toString());
//                Toast.makeText(LoginActivity.this, "Login succeeded", Toast.LENGTH_SHORT).show();

                Intent goToMainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(goToMainActivityIntent);
              },
              failure -> {
                Log.i(TAG, "Login failed: " + failure.toString());
//                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
              }
      );
    });
  }

}
