# CMP SDK example
## implementation example
### latest version 1.0.26


setting.gradle or build.gradle(project level) file
```sh
    repositories {
        google()
        mavenCentral()
        jcenter() // Warning: this repository is going to shut down soon
        maven {
            url "https://quadrantsdk2.jfrog.io/artifactory/quadrant-sdk/"
        }
    }
```


build.gradle(app level)
```sh
defaultConfig {
        ....
        multiDexEnabled true

        ....
    }
dependencies {
    ....
    implementation 'io.quadrant.sdk.compliance:compliancesdk:1.0.26'
    implementation "androidx.multidex:multidex:2.0.1"
}
```


### to implement
#### java

implement instance
```sh
Compliance compliance = Compliance.getInstance("YOUR KEY");
```

implement open consent form if needed.
notes: this function will check for user country location to display needed consent(GDPR/CCPA/No consent needed).
```sh
            compliance.openConsentFormIfNeeded(this, getSupportFragmentManager(), new Compliance.ConsentCallback() {
            @Override
            public void onSuccess(ConsentResult consentResult) {
                switch (consentResult){
                    case ccpaAccept:
                        //CCPA accept button clicked
                        break;
                    case ccpaDecline:
                        //CCPA decline button clicked
                        break;
                    case gdprAccept:
                        //GDPR accept button clicked
                        break;
                    case gdprDecline:
                        //GDPR decline button clicked
                        break;
                    case notConsent:
                        //user location no need for consent
                        break;
                }
            }

            @Override
            public void onError(String result) {
                Log.d("ConsentError",result);
            }
        });
```

implement request compliance
```sh
            // constant for request_integer
            // int REQUEST_OPTOUT = 0;
            // int REQUEST_DO_NOT_SELL = 1;
            // int REQUEST_DELETE_DATA = 2;
            // int REQUEST_ACCESS_DATA = 3;
            compliance.requestCompliance(MainActivity.this, MainActivity.this.getSupportFragmentManager(), request_integer, new Compliance.ResultCallback(){
                @Override
                public void onSuccess(String result) {
                    Log.d("MainActivity",result);
                }

                @Override
                public void onError(String result) {
                    Log.d("MainActivity",result);
                }
            });
```

implement this if you don't want to use our checking user location and want directly display GDPR/CCPA form
```sh
            // Open GDPR form
            compliance.openConsentForm(getSupportFragmentManager(),Compliance.GDPR, new Compliance.ConsentCallback(){
            @Override
            public void onSuccess(ConsentResult consentResult) {
                Log.d("GDPR",
                        "consent.onSuccess: " + consentResult +
                                ", tcfString: " + compliance.getTCFString(MainActivity.this) +
                                ", consentValue: " + compliance.getConsentValue(MainActivity.this) +
                                ", nonIab: " + compliance.getNonIabPartners(MainActivity.this));
            }

            @Override
            public void onError(String result) {
                Log.d("GDPRerror", result);
            }
            
            // Open CCPA form
            compliance.openConsentForm(getSupportFragmentManager(), Compliance.CCPA, new Compliance.ConsentCallback() {
            @Override
            public void onSuccess(ConsentResult consentResult) {
                Log.d("CCPA",
                        "consent.onSuccess: " + consentResult +
                                ", tcfString: " + compliance.getUSPrivacyString(MainActivity.this) +
                                ", consentValue: " + compliance.getConsentValue(MainActivity.this));
            }

            @Override
            public void onError(String result) {
                Log.d("CCPAerror", result);
            }
```

to get the necessary String resource on GDPR
```sh
// TCF
String tcf = compliance.getTCFString(MainActivity.this);
// Consent Value
String consentValue = compliance.getConsentValue(MainActivity.this);
// Non IAB partners selected by user
String nonIabPartners = compliance.getNonIabPartners(MainActivity.this));
```

to get the necessary String resource on CCPA
```sh
// TCF/US privacy
String tcf = compliance.getUSPrivacyString(MainActivity.this);
// Consent Value
String consentValue = compliance.getConsentValue(MainActivity.this);
```

#### kotlin
implement instance
```sh
Compliance compliance = Compliance.getInstance("YOUR KEY");
```

implement open consent form if needed.
notes: this function will check for user country location to display needed consent(GDPR/CCPA/No consent needed).
```sh
            compliance.openConsentFormIfNeeded(this, supportFragmentManager, object : ConsentCallback {
            override fun onSuccess(consentResult: ConsentResult) {
                when (consentResult) {
                    ConsentResult.ccpaAccept -> {
                        //CCPA accept button clicked
                    }
                    ConsentResult.ccpaDecline -> {
                        //CCPA decline button clicked
                    }
                    ConsentResult.gdprAccept -> {
                        //GDPR accept button clicked
                    }
                    ConsentResult.gdprDecline -> {
                        //GDPR decline button clicked
                    }
                    ConsentResult.notConsent -> {
                        //user location no need for consent
                    }
                }
            }

            override fun onError(result: String) {
                Log.d("ConsentError", result)
            }
        })
```

implement request compliance
```sh
            // constant for request_integer
            // int REQUEST_OPTOUT = 0;
            // int REQUEST_DO_NOT_SELL = 1;
            // int REQUEST_DELETE_DATA = 2;
            // int REQUEST_ACCESS_DATA = 3;
            compliance.requestCompliance(
                    this@MainActivityKotlin,
                    this@MainActivityKotlin.supportFragmentManager,
                    request_integer,
                    object : Compliance.ResultCallback {
                        override fun onSuccess(result: String) {
                            Log.d("MainActivity", result)
                        }

                        override fun onError(result: String) {
                            Log.d("MainActivity", result)
                        }
                    })
```

implement this if you don't want to use our checking user location and want directly display GDPR/CCPA form
```sh
            // Open GDPR form
            compliance.openConsentForm(
                supportFragmentManager,
                Compliance.GDPR,
                object : ConsentCallback {
                    override fun onSuccess(consentResult: ConsentResult) {
                        Log.d(
                            "GDPR",
                            "consent.onSuccess: " + consentResult +
                                    ", tcfString: " + compliance.getTCFString(this@MainActivityKotlin) +
                                    ", consentValue: " + compliance.getConsentValue(this@MainActivityKotlin) +
                                    ", nonIab: " + compliance.getNonIabPartners(this@MainActivityKotlin)
                        )
                    }

                    override fun onError(result: String) {
                        Log.d("GDPRerror", result)
                    }
                })
            
            // Open CCPA form
            compliance.openConsentForm(
                supportFragmentManager,
                Compliance.CCPA,
                object : ConsentCallback {
                    override fun onSuccess(consentResult: ConsentResult) {
                        Log.d(
                            "CCPA",
                            "consent.onSuccess: " + consentResult +
                                    ", tcfString: " + compliance.getUSPrivacyString(this@MainActivityKotlin) +
                                    ", consentValue: " + compliance.getConsentValue(this@MainActivityKotlin)
                        )
                    }

                    override fun onError(result: String) {
                        Log.d("CCPAerror", result)
                    }
                })
```

to get the necessary String resource on GDPR
```sh
// TCF
val tcf = compliance.getTCFString(this);
// Consent Value
val consentValue = compliance.getConsentValue(this);
// Non IAB partners selected by user
val nonIabPartners = compliance.getNonIabPartners(this));
```

to get the necessary String resource on CCPA
```sh
// TCF/US privacy
val tcf = compliance.getUSPrivacyString(this);
// Consent Value
val consentValue = compliance.getConsentValue(this);
```

#### Proguard
if you want enable proguard in your release app, add this to your proguard rules file
```sh
-keep class com.quadrant.sdk.compliance.** {*;}
```

-----------------------------------------------------------------------

## Non-Transitive
### latest version 1.0.4
On some special case, when your gradle library clash with ours regarding version issues, please use our non-transitive SDK.
The different is on on build.gradle on app level. The rest is similar with the above.

build.gradle(app level)
```sh
defaultConfig {
        ....
        multiDexEnabled true

        ....
    }
dependencies {
    ....
    //THIS OUR SDK LIBRARY
    implementation 'io.quadrant.sdk.compliance:compliancesdk-non-transitive:1.0.4'
    
    //THIS LIBRARY NEEDED BY OUR SDK.
    //Please replace library version number(x.x.x) with your need
    implementation "androidx.multidex:multidex:x.x.x"
    implementation 'com.facebook.shimmer:shimmer:x.x.x'
    implementation 'com.google.android.gms:play-services-ads-identifier:x.x.x'
    implementation 'com.iabtcf:iabtcf-encoder:x.x.x'
    implementation 'com.iabtcf:iabtcf-decoder:x.x.x'
    implementation 'io.jsonwebtoken:jjwt-api:x.x.x'
    implementation 'io.jsonwebtoken:jjwt-impl:x.x.x'
    implementation 'io.jsonwebtoken:jjwt-jackson:x.x.x'
    implementation 'com.squareup.retrofit2:retrofit:x.x.x'
    implementation 'com.squareup.retrofit2:converter-gson:x.x.x'
    implementation 'com.google.android.gms:play-services-safetynet:x.x.x'
    implementation 'androidx.lifecycle:lifecycle-extensions:x.x.x'
    implementation 'com.google.android.gms:play-services-location:x.x.x'
    implementation 'com.google.code.gson:gson:x.x.x'
    implementation 'com.squareup.okhttp3:okhttp:x.x.x'
    implementation 'com.squareup.okhttp3:logging-interceptor:x.x.x'
    
}
```
