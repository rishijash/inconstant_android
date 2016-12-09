# inconstant_android

Build awesome things without worrying about Content! inConstant takes the pain out of changing code and putting update of your app so you can focus on what makes your product great not the content.

inConstant-Android is an easy to use Android library for constant management via cloud.

### Getting Started

* Sign up for Developer Account on [inConstant Admin Platform](https://www.hoodoomail.com/inconstant/)
* Create Application on Admin Platform
* Download [inConstant-Android](https://github.com/rishijash/inconstant_android) and check out the included example app.

### How to Setup

* Download [inConstant-Android](https://github.com/rishijash/inconstant_android) from Github
* Add AES Dependency in your App Gridle:
```
        compile 'com.scottyab:aescrypt:0.0.1'
```
* Add following in your project Manifest:
```
         <uses-permission android:name="android.permission.INTERNET" />
         <uses-permission android:name="android.permission.WAKE_LOCK" />
         <uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />
         <permission android:name="com.example.gcm.permission.C2D_MESSAGE"
        android:protectionLevel="signature" />
        <uses-permission android:name="com.example.gcm.permission.C2D_MESSAGE" />
        
        <service
            android:name=".PushNotificationService"
            android:exported="false">
            <intent-filter>
                <action android:name="com.google.android.c2dm.intent.RECEIVE" />
            </intent-filter>
        </service>

        <receiver
            android:name="com.google.android.gms.gcm.GcmReceiver"
            android:exported="true"
            android:permission="com.google.android.c2dm.permission.SEND">
            <intent-filter>
                <action android:name="com.google.android.c2dm.intent.RECEIVE" />
                <category android:name="com.rishi.shareit" />
            </intent-filter>
        </receiver>
```
* Import inConstant.java, inConstant_GCMClientManager.java, inConstant_DB.java, PUSHDATA.java, PushNotificationService.java to your project.

### Example Usage

```
//Initialize
  inConstant_DB idb = new inConstant_DB(getApplicationContext());
  
//Set Credentials
  idb.setCredential("APP_ID","APP_SECRET");

//Fetch Data
  String myconstant = idb.getConstant("constantname");
```

### Acknowledgements

Thanks to [AESCrypt](https://github.com/scottyab/AESCrypt-Android) which is used for Encryption.

### Lisence

inConstant is licensed under the MIT LICENSE. See the LICENSE file for details.
