package com.chaoalex.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.chaoalex.taskmaster.R;

public class VerifyActivity extends AppCompatActivity {
  private static final String TAG = "VerifyActivity";

  EditText emailEditText;
  EditText verificationCodeEditText;
  Button submitButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_verify);

    emailEditText = findViewById(R.id.VerifyActivityEmailInputEditTextTextEmailAddress);
    verificationCodeEditText = findViewById(R.id.VerifyActivityVerificationCodeEditTextNumber);
    submitButton = findViewById(R.id.VerifyActivityVerifyButton);

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
      Amplify.Auth.confirmSignUp(
              emailEditText.getText().toString(),
              verificationCodeEditText.getText().toString(),
              success -> {
                Log.i(TAG, "Verification succeeded: " + success.toString());
//                Toast.makeText(VerifyActivity.this, "Verification succeeded", Toast.LENGTH_SHORT).show();

                Intent goToLoginIntent = new Intent(VerifyActivity.this, LoginActivity.class);
                goToLoginIntent.putExtra("email", emailEditText.getText().toString());

                startActivity(goToLoginIntent);
              },
              failure -> {
                Log.i(TAG, "Verification failed: " + failure.toString());
//                Toast.makeText(VerifyActivity.this, "Verification failed", Toast.LENGTH_SHORT).show();
              }
      );
    });
  }

}