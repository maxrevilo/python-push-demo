package my.android_test.utils;

import android.content.Context;
import android.content.Intent;
import com.google.android.gcm.GCMRegistrar;

import android.os.AsyncTask;

public final class AppCommon {
    public static final String DISPLAY_MESSAGE_ACTION = "my.android_test.DISPLAY_MESSAGE";


    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra("M", message);
        context.sendBroadcast(intent);
    }
}
