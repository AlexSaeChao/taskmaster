package com.chaoalex.taskmaster;

import android.app.Application;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.core.Amplify;

public class TaskMasterAmplifyApplication extends Application {
  public static final String TAG = "taskmasterapplication";

  @Override
  public void onCreate() {
    super.onCreate();

//    try {
//      Amplify.addPlugin(new AWSApiPlugin());
//      Amplify.configure(getApplicationContext());
//    } catch (AmplifyException ae) {
//      Log.e(TAG, "Error Initializing Amplify: " + ae.getMessage(), ae);
//    }
    try {
      Amplify.addPlugin(new AWSApiPlugin()); // If you're using GraphQL.
      Amplify.configure(getApplicationContext());
      Log.i("AmplifySetup", "Amplify was configured.");
    } catch (AmplifyException e) {
      Log.e("AmplifySetup", "Could not configure Amplify.", e);
    }
  }
}