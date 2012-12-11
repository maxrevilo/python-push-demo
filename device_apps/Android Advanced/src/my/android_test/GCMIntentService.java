package my.android_test;

import my.android_test.utils.*;

import static my.android_test.utils.AppCommon.displayMessage;
import static my.android_test.utils.Web.register;
import static my.android_test.utils.Web.sync;

import android.content.Context;
import android.content.Intent;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;




public class GCMIntentService extends GCMBaseIntentService {
    
    public GCMIntentService() {
        super(Properties.SENDER_ID);
    }
    
    @Override
    protected void onRegistered(Context context, String token) {
        displayMessage(context, "Device registered: Token = "+ token);
        register(context, token); //This will freeze the GUI
        sync(this, token); //This will freeze the GUI
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        displayMessage(context, "Got Message");
        final String token = GCMRegistrar.getRegistrationId(context);
        String message = sync(this, token); //This will freeze the GUI
        generateNotification(context, "New message!: "+message);
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

    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        String title = context.getString(R.string.app_name);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
    }

}
