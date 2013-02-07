package com.airlocksoftware.encryptifyandroid;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.airlocksoftware.encryptifyandroid.PerformEncryptionActivity.EADeviceAdminReceiver;

public class EncryptifyAndroid {

	/**
	 * Verifies whether the device is encrypted or not. If it is not, finishes
	 * the current activity and launches PerformEncryptionActivity
	 **/
	public static void verifyEncryption(Activity activity) {
		if (!isDeviceEncrypted(activity)) {
			Intent intent = new Intent(activity,
					PerformEncryptionActivity.class);
			activity.startActivity(intent);
			activity.finish();
		}
	}

	/** Launch the activity to have the user enable encryption. **/
	public static void enableEncryption(Context context) {
		// Launch the activity to activate encryption. May or may not
		// return!
		Intent intent = new Intent(DevicePolicyManager.ACTION_START_ENCRYPTION);
		context.startActivity(intent);
	}

	public static boolean isDeviceEncrypted(Context context) {
		DevicePolicyManager DPM = (DevicePolicyManager) context
				.getSystemService(Context.DEVICE_POLICY_SERVICE);
		return DPM.getStorageEncryptionStatus() == DevicePolicyManager.ENCRYPTION_STATUS_ACTIVE;
	}

	// /** Launch the activity to have the user enable our admin. **/
	public static void enableAdmin(Context context) {
		ComponentName componentName = new ComponentName(context,
				EADeviceAdminReceiver.class);
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
				context.getString(R.string.enable_admin_message));
		context.startActivity(intent);

	}

}
