package com.airlocksoftware.encryptifyandroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PerformEncryptionActivity extends Activity {

	// Interaction with the DevicePolicyManager
	DevicePolicyManager mDPM;
	ComponentName mDeviceAdminSample;

	/**
	 * First displays "enable admin" dialog (if needed), then starts the
	 * encryption activity.
	 **/
	private View.OnClickListener mActivate = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			EncryptifyAndroid.enableEncryption(PerformEncryptionActivity.this);
		}
	};

	private DialogInterface.OnClickListener mFinish = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_perform_encryption);

		// Prepare to work with the DPM
		mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		mDeviceAdminSample = new ComponentName(this,
				EADeviceAdminReceiver.class);

		// Check if we're not already encrypted -- should not have been sent
		// here.
		if (EncryptifyAndroid.isDeviceEncrypted(this)) {
			finish();
		}

		// Check to see if encryption is even supported on this device (it's
		// optional). If not, show a dialog then finish the Activity.
		if (mDPM.getStorageEncryptionStatus() == DevicePolicyManager.ENCRYPTION_STATUS_UNSUPPORTED) {
			new AlertDialog.Builder(this)
					.setMessage(R.string.encryption_not_supported)
					.setCancelable(false)
					.setNeutralButton(R.string.encryption_not_supported_ok,
							mFinish).show();
		}

		// setup the activate button that starts encryption
		Button btnActivate = (Button) findViewById(R.id.btn_activate);
		btnActivate.setText(getString(R.string.encrypt_device));
		btnActivate.setOnClickListener(mActivate);
		
		// You can add additional UI controls here (for example, UI related to requiring a stronger password)
	}

	/**
	 * Helper to determine if we are an active admin
	 */
	private boolean isActiveAdmin() {
		return mDPM.isAdminActive(mDeviceAdminSample);
	}

	public static class EADeviceAdminReceiver extends DeviceAdminReceiver {

		public CharSequence onDisableRequested(Context context, Intent intent) {
			// OPTIONAL -- display warning message when user tries to disable
			// admin rights
			// return "Warning -- Disabling Admin status for
			// EncryptionRequiredApplication
			// will wipe all data from the application";
			return null;
		}

		public void onDisabled(Context context, Intent intent) {
			// OPTIONAL
			// Toast.makeText(context, "All of your data has been wiped.",
			// Toast.LENGTH_LONG).show();
			// wipe the device, or just delete all databases and files
		}

		public void onEnabled(Context context, Intent intent) {
			// setup password requirements, etc.
		}

	}

}
