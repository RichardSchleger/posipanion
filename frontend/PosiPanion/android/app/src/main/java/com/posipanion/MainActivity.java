package com.posipanion;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.posipanion.location.LocationUpdatesService;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactRootView;

public class MainActivity extends ReactActivity {

  /**
   * Returns the name of the main component registered from JavaScript. This is used to schedule
   * rendering of the component.
   */
  @Override
  protected String getMainComponentName() {
    return "PosiPanion";
  }

  // Tracks the bound state of the service.
  private boolean mBound = false;

  // Monitors the state of the connection to the service.
  private final ServiceConnection mServiceConnection = new ServiceConnection() {
      @Override
      public void onServiceConnected(ComponentName name, IBinder service) {
          LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
          mBound = true;
      }

      @Override
      public void onServiceDisconnected(ComponentName name) {
          mBound = false;
      }
  };

  @Override
  protected void onStart() {
      super.onStart();
      bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
              Context.BIND_AUTO_CREATE);
  }

  @Override
  protected void onStop() {
      if (mBound) {
          // Unbind from the service. This signals to the service that this activity is no longer
          // in the foreground, and the service can respond by promoting itself to a foreground
          // service.
          unbindService(mServiceConnection);
          mBound = false;
      }
      super.onStop();
  }
}
