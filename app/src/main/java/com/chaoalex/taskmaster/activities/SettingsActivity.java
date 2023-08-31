package com.chaoalex.taskmaster.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult;
import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;
import com.chaoalex.taskmaster.R;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
  public static final String TAG = "SettingsActivity";
  SharedPreferences preferences;
  SharedPreferences.Editor preferencesEditor;
  Spinner teamSpinner;
  Button signupButton;
  Button loginButton;
  Button signoutButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    preferences = PreferenceManager.getDefaultSharedPreferences(this);
    preferencesEditor = preferences.edit();

    teamSpinner = findViewById(R.id.SettingsActivityTeamSpinner);
    signupButton = findViewById(R.id.SettingsActivitySignupButton);
    loginButton = findViewById(R.id.SettingsActivityLoginButton);

    updateAuthUI();
    setupTeamSpinner();
    setupSaveButton();
    setupSignupButton();
    setupLoginButton();
    setupSignOutButton();
  }

  @Override
  protected void onResume() {
    super.onResume();

    updateAuthUI();
  }

  private void updateAuthUI() {
    Amplify.Auth.getCurrentUser(
            authUser -> {
              runOnUiThread(() -> {
                signupButton.setVisibility(View.GONE);
                loginButton.setVisibility(View.GONE);
                signoutButton.setVisibility(View.VISIBLE);
              });
            },
            authException -> {
              runOnUiThread(() -> {
                signupButton.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.VISIBLE);
                signoutButton.setVisibility(View.GONE);
              });
            }
    );
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
    ((Button) findViewById(R.id.SettingsActivitySaveButton)).setOnClickListener(view -> {
      SharedPreferences.Editor preferencesEditor = preferences.edit();

      String selectedTeam = teamSpinner.getSelectedItem().toString();
      preferencesEditor.putString("selected_team", selectedTeam);

      preferencesEditor.apply();

      Toast.makeText(SettingsActivity.this, "Settings saved!!", Toast.LENGTH_SHORT).show();
    });
  }

  void setupSignupButton() {
    signupButton.setOnClickListener(v -> {
      Intent goToSignupActivityIntent = new Intent(SettingsActivity.this, SignupActivity.class);
      startActivity(goToSignupActivityIntent);
    });
  }

  void setupLoginButton() {
    loginButton.setOnClickListener(v -> {
      Intent goToLoginActivityIntent = new Intent(SettingsActivity.this, LoginActivity.class);
      startActivity(goToLoginActivityIntent);
    });
  }

  void setupSignOutButton() {
    signoutButton = findViewById(R.id.SettingsActivityLogoutButton);
    signoutButton.setOnClickListener(v -> {

      AuthSignOutOptions signOutOptions = AuthSignOutOptions.builder()
              .globalSignOut(true)
              .build();

      Amplify.Auth.signOut(signOutOptions, signOutResult -> {
        if (signOutResult instanceof AWSCognitoAuthSignOutResult.CompleteSignOut) {
          Log.i(TAG, "Global sign out Successful!");
        } else if (signOutResult instanceof AWSCognitoAuthSignOutResult.PartialSignOut) {
          Log.i(TAG, "Partial sign out Successful!");
        } else if (signOutResult instanceof AWSCognitoAuthSignOutResult.FailedSignOut) {
          Log.i(TAG, "Sign out FAILED!");
        }
      });
    });
  }
}
