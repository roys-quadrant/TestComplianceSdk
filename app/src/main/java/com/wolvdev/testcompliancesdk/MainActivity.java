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

        compliance = Compliance.getInstance("1bfc5d9df8830245c482660849809272");
        if(PermissionUtil.accessLocation(this)){
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
                    Toast.makeText(MainActivity.this, consentResult.toString(), Toast.LENGTH_LONG).show();
                    Log.i("MainActivity",consentResult.toString());
                }

                @Override
                public void onError(String result) {
                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                    Log.i("MainActivity",result);
                }
            });
        }

        Button buttonGDPR = findViewById(R.id.buttonShowConsentForm);
        buttonGDPR.setOnClickListener(view -> compliance.openConsentForm(getSupportFragmentManager(),Compliance.GDPR, new Compliance.ConsentCallback(){
            @Override
            public void onSuccess(ConsentResult consentResult) {

            }

            @Override
            public void onError(String result) {

            }
        }));

        Button buttonCCPA = findViewById(R.id.buttonCCPA);
        buttonCCPA.setOnClickListener(view -> compliance.openConsentForm(getSupportFragmentManager(), Compliance.CCPA, new Compliance.ConsentCallback() {
            @Override
            public void onSuccess(ConsentResult consentResult) {

            }

            @Override
            public void onError(String result) {

            }
        }));

        ListView listview = findViewById(R.id.listViewMain);
        listview.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            compliance.requestCompliance(MainActivity.this, MainActivity.this.getSupportFragmentManager(), position, new Compliance.ResultCallback(){
                @Override
                public void onSuccess(String result) {
                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                    Log.i("MainActivity",result);
                }

                @Override
                public void onError(String result) {
                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                    Log.e("MainActivity",result);
                }
            });
            //}
        });
        if(compliance.getNonIabPartners(this)!=null){
            Log.d("nonIab", compliance.getNonIabPartners(this).toString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtil.PERMISSION_ACCESS_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                compliance.openConsentFormIfNeeded(this, getSupportFragmentManager(), new Compliance.ConsentCallback() {
                    @Override
                    public void onSuccess(ConsentResult consentResult) {
                        Toast.makeText(MainActivity.this, consentResult.toString(), Toast.LENGTH_LONG).show();
                        Log.i("MainActivity", consentResult.toString());
                    }

                    @Override
                    public void onError(String result) {
                        Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                        Log.i("MainActivity",result);
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
                compliance.openConsentFormIfNeeded(this, getSupportFragmentManager(), new Compliance.ConsentCallback() {
                    @Override
                    public void onSuccess(ConsentResult consentResult) {
                        Toast.makeText(MainActivity.this, consentResult.toString(), Toast.LENGTH_LONG).show();
                        Log.i("MainActivity", consentResult.toString());
                    }

                    @Override
                    public void onError(String result) {
                        Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
                        Log.i("MainActivity",result);
                    }
                });
            }
        }
    }
}