package my.android_test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.google.android.gcm.GCMRegistrar;

import static my.android_test.utils.AppCommon.DISPLAY_MESSAGE_ACTION;
import static my.android_test.utils.Web.register;

public class MainActivity extends Activity
{
    /** Fields */
    TextView displayLog;

    private final BroadcastReceiver pushReceiver =
            new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String newMessage = intent.getExtras().getString("M");
                    displayLog.append(newMessage + "\n");
                }
            };



    
    /** Overriden: */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        //Logic:
        super.onCreate(savedInstanceState);

        //Display:
        setContentView(R.layout.main);
        displayLog = (TextView) findViewById(R.id.display);

        Context context = getApplicationContext();

        //GCM:
        //Sanity Checks:
        GCMRegistrar.checkDevice(context);
        GCMRegistrar.checkManifest(context);

        displayLog.append("\n\nSanity checks done.\n");

        //Dinamically register pushReciver:
        registerReceiver(pushReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));
        displayLog.append("PushReceiver registered.\n");

        final String token = GCMRegistrar.getRegistrationId(context);

        if (token.equals("")) {
            displayLog.append("Sending ID ("+Properties.SENDER_ID+") to GCM.\n\n");
            GCMRegistrar.register(context, Properties.SENDER_ID);
            displayLog.append("Got Device Token\n");

        } else {
            displayLog.append("Got Device Token\n");
            register(context, token); //This will freeze the GUI
        }
        
    }


    @Override
    protected void onDestroy() {
        Context context = getApplicationContext();
        
        unregisterReceiver(pushReceiver);
        GCMRegistrar.onDestroy(context);
        
        super.onDestroy();
    }
}
