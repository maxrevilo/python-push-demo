package my.android_test;

import my.android_test.utils.*;

import static my.android_test.utils.AppCommon.displayMessage;
import static my.android_test.utils.Web.register;

import android.content.Context;
import android.content.Intent;

import com.google.android.gcm.GCMBaseIntentService;


public class GCMIntentService extends GCMBaseIntentService {
    
    public GCMIntentService() {
        super(Properties.SENDER_ID);
    }
    
    @Override
    protected void onRegistered(Context context, String token) {
        displayMessage(context, "Device registered: Token = "+ token);
        register(context, token); //This will freeze the GUI
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        displayMessage(context, "Got Message");
    }



    @Override
    protected void onUnregistered(Context context, String token) {
        displayMessage(context, "Device unregistered");
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        displayMessage(context, "Messages Deleted");
    }

    @Override
    public void onError(Context context, String errorId) {
        displayMessage(context, "Received error: " + errorId);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        displayMessage(context, "Received recoverable error: " + errorId);
        return super.onRecoverableError(context, errorId);
    }

}
