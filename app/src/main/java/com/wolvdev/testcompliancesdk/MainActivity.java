package com.wolvdev.testcompliancesdk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.quadrant.sdk.compliance.core.Compliance;
import com.quadrant.sdk.compliance.util.ConsentResult;
import com.quadrant.sdk.compliance.util.GetCountry;
import com.quadrant.sdk.compliance.util.PermissionUtil;

public class MainActivity extends AppCompatActivity {

    private Compliance compliance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        compliance = Compliance.getInstance("YOUR KEY");
        if(PermissionUtil.accessLocation(this)){
            openConsentForm();
        }

        Button buttonGDPR = findViewById(R.id.buttonShowConsentForm);
        buttonGDPR.setOnClickListener(view -> compliance.openConsentForm(getSupportFragmentManager(),Compliance.GDPR, new Compliance.ConsentCallback(){
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
        }));

        Button buttonCCPA = findViewById(R.id.buttonCCPA);
        buttonCCPA.setOnClickListener(view -> compliance.openConsentForm(getSupportFragmentManager(), Compliance.CCPA, new Compliance.ConsentCallback() {
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
        }));

        ListView listview = findViewById(R.id.listViewMain);
        listview.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            compliance.requestCompliance(MainActivity.this, MainActivity.this.getSupportFragmentManager(), position, new Compliance.ResultCallback(){
                @Override
                public void onSuccess(String result) {
                    Log.d("MainActivity",result);
                }

                @Override
                public void onError(String result) {
                    Log.d("MainActivity",result);
                }
            });
        });
    }

    private void openConsentForm(){
        compliance.openConsentFormIfNeeded(this, getSupportFragmentManager(), new Compliance.ConsentCallback() {
            @Override
            public void onSuccess(ConsentResult consentResult) {
                switch (consentResult){
                    case ccpaAccept:
                        //function for CCPA accept button clicked
                        break;
                    case ccpaDecline:
                        //function for CCPA decline button clicked
                        break;
                    case gdprAccept:
                        //function for GDPR accept button clicked
                        break;
                    case gdprDecline:
                        //function for GDPR decline button clicked
                        break;
                    case notConsent:
                        //function for Not consent button clicked
                        break;
                }
            }

            @Override
            public void onError(String result) {
                Log.d("ConsentError",result);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtil.PERMISSION_ACCESS_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                compliance.openConsentFormIfNeeded(this, getSupportFragmentManager(), new Compliance.ConsentCallback() {
                    @Override
                    public void onSuccess(ConsentResult consentResult) {
                        Log.d("MainActivity", consentResult.toString());
                    }

                    @Override
                    public void onError(String result) {
                        Log.d("MainActivity",result);
                    }
                });
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GetCountry.GPS_REQUEST) {
                openConsentForm();
            }
        }
    }
}