package com.chaoalex.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

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

    setupSubmitButton();
  }


void setupSubmitButton() {
  //    Cognito Verification Logic
    Amplify.Auth.confirmSignUp(emailEditText.getText().toString(),
            verificationCodeEditText.getText().toString(),
            success -> {
              Log.i(TAG, "Verification succeeded: " + success.toString());
            },
            failure -> {
              Log.i(TAG, "Verification failed: " + failure.toString());
            }
    );
    //TODO ON SUCCESS, MOVE TO LOGIN PAGE
  }
}