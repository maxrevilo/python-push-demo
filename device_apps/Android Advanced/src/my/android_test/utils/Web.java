package my.android_test.utils;

import java.util.ArrayList;
import java.io.IOException;
import java.io.ByteArrayOutputStream;

import com.google.android.gcm.GCMRegistrar;
import android.content.Context;

import org.apache.http.*;
import org.apache.http.util.*;
import org.apache.http.client.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.*;
import org.apache.http.protocol.HTTP;
import org.apache.http.message.BasicNameValuePair;

import my.android_test.Properties;
import static my.android_test.utils.AppCommon.displayMessage;


public final class Web {

    private static final HttpClient httpclient = new DefaultHttpClient();

    /**
     * Register this device on django server.
     */
    public static boolean register(final Context context, final String token) {
        String serverUrl = Properties.SERVER_URL + Properties.REGISTER_URL;
        
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
        //The server expects the device token (GCM Registration Id)
        params.add(new BasicNameValuePair("token", token));
        //And also expects the type of device GA (Google Android)
        //The "GA" value is defined in python_push.gcm.gcm_push_service on GCMPushService.type
        params.add(new BasicNameValuePair("type", "GA"));

        try {
            displayMessage(context, "Attempting to register on Django Server");

            post(context, serverUrl, params);
            String message = "From Django Server: successfully added device!";
            AppCommon.displayMessage(context, message);

            return true;
        } catch (IOException e) {
            String message = "Error during device registration:\n"+e;
            AppCommon.displayMessage(context, message);

            return false;
        }
    }

    /**
     * Sync device with data on the server.
     */
    public static String sync(final Context context, final String token) {
        String serverUrl =  Properties.SERVER_URL + Properties.SYNC_URL;
        
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("type", "GA"));

        try {
            displayMessage(context, "Attempting to sync with Django Server");

            String result = post(context, serverUrl, params);
            AppCommon.displayMessage(context, "Sync result: "+result);
            return result;

        } catch (IOException e) {
            AppCommon.displayMessage(context, "Error during device sync:\n"+e);
            return null;
        }
    }



    /*
     * Sends a Http Post request to a server.
     */
    private static String post(final Context context, String endpoint, ArrayList<NameValuePair> params) 
        throws ClientProtocolException, IOException {
        HttpPost httppost = new HttpPost(endpoint);
        try {
            httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

            int status = response.getStatusLine().getStatusCode();
            if(status != HttpStatus.SC_OK) throw new HttpResponseException(status, "Status "+status);

            ByteArrayOutputStream outstream = new ByteArrayOutputStream();
            response.getEntity().writeTo(outstream);
            return outstream.toString();

        } catch (ClientProtocolException e) {
            AppCommon.displayMessage(context, "ClientProtocolException: "+e.toString());
            throw e;
        } catch (IOException e) {
            AppCommon.displayMessage(context, "IOException: "+e.toString());
            throw e;
        }
    }

}
