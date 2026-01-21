package com.example.utaste.util;

import android.app.Activity;
import android.content.Intent;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

public class QRCodeScanner {


    public interface ScanCallback {
        void onResult(String barcode);
        void onCancelled();
    }

    // Lance la caméra de scan (QR + codes-barres)
    public static void scan(Activity activity) {
        IntentIntegrator integrator = new IntentIntegrator(activity);


        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);

        integrator.setPrompt("Scanne un code-barres ou un QR code");
        integrator.setCameraId(0); // caméra arrière
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(false);
        integrator.setOrientationLocked(false);


        integrator.setCaptureActivity(CaptureActivity.class);

        integrator.initiateScan();
    }


    public static String parseResult(int requestCode, int resultCode, Intent data, ScanCallback callback) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() != null) {
                String barcode = result.getContents();
                if (callback != null) callback.onResult(barcode);
                return barcode;
            } else {
                if (callback != null) callback.onCancelled();
            }
        }
        return null;
    }
}
