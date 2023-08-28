package com.chaoalex.taskmaster.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;
import com.chaoalex.taskmaster.R;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
  public static final String USER_NICKNAME_TAG = "userNickName";
  SharedPreferences preferences;
  SharedPreferences.Editor preferencesEditor;
  Spinner teamSpinner;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    preferences = PreferenceManager.getDefaultSharedPreferences(this);

    teamSpinner = findViewById(R.id.SettingsActivityTeamSpinner);
    preferencesEditor = preferences.edit();

    setupSettingsActivityUsernameTextView();
    setupTeamSpinner();
    setupSaveButton();
  }

  void setupSettingsActivityUsernameTextView() {
    String userNickname = preferences.getString(USER_NICKNAME_TAG, null);
    ((EditText)findViewById(R.id.SettingsActivityUsernameTextView)).setText(userNickname);
  }

  void setupTeamSpinner() {
    List<String> teamNames = new ArrayList<>();

    Amplify.API.query(
            ModelQuery.list(Team.class),
            success -> {
              for (Team team : success.getData()) {
                teamNames.add(team.getName());
              }
              ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, teamNames);
              adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

              runOnUiThread(() -> {
                teamSpinner.setAdapter(adapter);

                String selectedTeam = preferences.getString("selected_team", "Default Team");
                if (teamNames.contains(selectedTeam)) {
                  teamSpinner.setSelection(adapter.getPosition(selectedTeam));
                }
              });
            },
            failure -> Log.e(TAG, "Could not fetch team names")
    );

    teamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedTeam = (String) parent.getItemAtPosition(position);
        preferencesEditor.putString("selected_team", selectedTeam);
        preferencesEditor.apply();
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
  }

  void setupSaveButton() {
    ((Button)findViewById(R.id.SettingsActivitySaveButton)).setOnClickListener(view -> {
      SharedPreferences.Editor preferencesEditor = preferences.edit();

      EditText userNicknameEditText = (EditText) findViewById(R.id.SettingsActivityUsernameTextView);
      String userNicknameString = userNicknameEditText.getText().toString();
      preferencesEditor.putString(USER_NICKNAME_TAG, userNicknameString);

      String selectedTeam = teamSpinner.getSelectedItem().toString();
      preferencesEditor.putString("selected_team", selectedTeam);

      preferencesEditor.apply();

      Toast.makeText(SettingsActivity.this, "Settings saved!!", Toast.LENGTH_SHORT).show();
    });
  }
}