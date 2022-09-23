package com.wolvdev.testcompliancesdk

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.quadrant.sdk.compliance.core.Compliance
import com.quadrant.sdk.compliance.core.Compliance.ConsentCallback
import com.quadrant.sdk.compliance.util.ConsentResult
import com.quadrant.sdk.compliance.util.GetCountry
import com.quadrant.sdk.compliance.util.PermissionUtil

class MainActivityKotlin: AppCompatActivity() {

    private lateinit var compliance: Compliance;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        compliance = Compliance.getInstance("YOUR KEY", this)

        if (PermissionUtil.accessLocation(this)) {
            openConsentForm()
        }

        val buttonGDPR = findViewById<Button>(R.id.buttonShowConsentForm)
        buttonGDPR.setOnClickListener { view: View? ->
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
        }

        val buttonCCPA = findViewById<Button>(R.id.buttonCCPA)
        buttonCCPA.setOnClickListener { view: View? ->
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
        }

        val listview = findViewById<ListView>(R.id.listViewMain)
        listview.onItemClickListener =
            OnItemClickListener { arg0: AdapterView<*>?, arg1: View?, position: Int, arg3: Long ->
                compliance.requestCompliance(
                    this@MainActivityKotlin,
                    this@MainActivityKotlin.supportFragmentManager,
                    position,
                    object : Compliance.ResultCallback {
                        override fun onSuccess(result: String) {
                            Log.d("MainActivity", result)
                        }

                        override fun onError(result: String) {
                            Log.d("MainActivity", result)
                        }
                    })
            }
    }

    private fun openConsentForm() {
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
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionUtil.PERMISSION_ACCESS_LOCATION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openConsentForm()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == GetCountry.GPS_REQUEST) {
                openConsentForm()
            }
        }
    }
}