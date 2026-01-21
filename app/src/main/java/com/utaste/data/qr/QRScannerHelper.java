package com.utaste.data.qr;

import android.app.Activity;
import android.content.Intent;

public final class QRScannerHelper {
    public static final int REQUEST_SCAN = 6021;
    private QRScannerHelper() {}

    public static void startScan(Activity activity) {
        Intent i = new Intent("com.google.zxing.client.android.SCAN");
        i.putExtra("SCAN_MODE", "QR_CODE_MODE");
        activity.startActivityForResult(i, REQUEST_SCAN);
    }

    public static String extractBarcode(int requestCode, int resultCode, Intent data) {
        if (requestCode==REQUEST_SCAN && resultCode==Activity.RESULT_OK && data!=null) {
            return data.getStringExtra("SCAN_RESULT");
        }
        return null;
    }
}
