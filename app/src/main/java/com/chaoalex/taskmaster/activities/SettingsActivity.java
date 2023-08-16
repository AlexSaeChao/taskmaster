package com.chaoalex.taskmaster.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chaoalex.taskmaster.R;
import com.google.android.material.snackbar.Snackbar;

public class SettingsActivity extends AppCompatActivity {
  public static final String USER_NICKNAME_TAG = "userNickName";
  SharedPreferences preferences;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    preferences = PreferenceManager.getDefaultSharedPreferences(this);

    setupSettingsActivityUsernameTextView();
    setupSaveButton();
  }

  void setupSettingsActivityUsernameTextView() {
    String userNickname = preferences.getString(USER_NICKNAME_TAG, null);
    ((EditText)findViewById(R.id.SettingsActivityUsernameTextView)).setText(userNickname);
  }
  void setupSaveButton() {
    ((Button)findViewById(R.id.SettingsActivitySaveButton)).setOnClickListener(view -> {
      SharedPreferences.Editor preferencesEditor = preferences.edit();
      EditText userNicknameEditText = (EditText) findViewById(R.id.SettingsActivityUsernameTextView);

      String userNicknameString = userNicknameEditText.getText().toString();

      preferencesEditor.putString(USER_NICKNAME_TAG, userNicknameString);
      preferencesEditor.apply();

//      Snackbar.make(findViewById(R.id.settingProfileActivityView), "Settings Saved!", Snackbar.LENGTH_SHORT).show();
      Toast.makeText(SettingsActivity.this, "Settings saved!!", Toast.LENGTH_SHORT).show();
    });
  }
}