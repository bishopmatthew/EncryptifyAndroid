# Protect sensitive data using EncryptifyAndroid

Applications that deal with sensitive data (e.g. data covered by HIPAA, corporate secrets, etc) can increase the level of protection they provide by using the capabilities of the Android  Device Administration API. Once the user has given your application Admin status, you have significant control over the devices various security features, including:
- Length and composition of the user's device password 
- How frequently the password needs to be changed
- When the device locks
- How many times a password can be entered incorrectly
- Performing remote wipes of all data from a lost or stolen device
- Require encryption of the storage area to view or handle your sensitive data.

# What we cover

This repository contains a library that simplifies the process making your app require storage encryption. 

# Cloning the library project
`git clone https://github.com/bishopmatthew/EncryptifyAndroid.git` the repository to the directory you're working from. In Eclipse, Import -> Android -> Existing Android Code Into Workspace. Check the box next to "DeviceEncryptify" and click finish.

For whichever application you want to use the library with, right click on the application name in Package Explore and select Properties -> Android -> (under "Library") Add -> and select "DeviceEncryptify".

# Adding DeviceEncryptifyActivity to manifest
You'll need to add this activity and the DeviceAdminReceiver to your manifest. In the "Application" tag, add

```
<activity android:name="com.airlocksoftware.encryptifyandroid.PerformEncryptionActivity"
            android:theme="@style/EncryptifyAndroidTheme" 
            android:label="@string/app_name" >
        </activity>
```

# Running the encryption check
Simply call `DeviceEncryptify.ensureDeviceIsEncrypted(context);` in the onCreate() method of any activities with intent filters (including your main activity). If we detect that the device is not encrypted, another activity will start that walks the user through the process of encrypting the device. After the device has been encrypted, the user will be able to use your app normally. But now your data is encrypted!

# Extending PerformEncryptionActivity and EADeviceAdminReceiver

If you require further control over your user's security settings, you can add EADeviceAdminReceiver to your activity manifest:
```
        <receiver
            android:name="com.airlocksoftware.encryptifyandroid.PerformEncryptionActivity$EADeviceAdminReceiver"
            android:description="@string/device_encryptify_admin_description"
            android:label="@string/app_name"
            android:permission="android.permission.BIND_DEVICE_ADMIN" >
            <meta-data
                android:name="android.app.device_admin"
                android:resource="@xml/device_admin_sample" />

            <intent-filter>
                <action android:name="android.app.action.DEVICE_ADMIN_ENABLED" />
                <action android:name="android.app.action.ACTION_DEVICE_ADMIN_DISABLE_REQUESTED" />
                <action android:name="android.app.action.ACTION_DEVICE_ADMIN_DISABLED" />
            </intent-filter>
        </receiver>
```

You can control which policies affect your app by adding / removing them from /xml/device_admin_sample. You can then use the method EncryptifyAndroid.enableAdmin(context) to get the user to grant admin status to your app. Then you'll extend the methods in EADeviceAdminReceiver to do the necessary checks. For example, if you want to wipe all data from your app if the user disables your applications admin status (and thus its ability to ensure that sensitive data is protected), override `onDisableRequested(Context context, Intent intent)` to tell them that you're going to wipe the data, and in `onDisabled(Context context, Intent intent)` delete any files stored in your applications /data directory.

More information can be found in the Android developer docs:
http://developer.android.com/guide/topics/admin/device-admin.html

# License
Copyright (c) 2013 Matthew Bishop

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.