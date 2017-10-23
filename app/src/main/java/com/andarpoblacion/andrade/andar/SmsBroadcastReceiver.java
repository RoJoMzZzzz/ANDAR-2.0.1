package com.andarpoblacion.andrade.andar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by Bok on 10/4/17.
 */

public class SmsBroadcastReceiver extends BroadcastReceiver {

    public static final String SMS_BUNDLE = "pdus";

    @Override
    public void onReceive(Context context, Intent intent) {

        Database db = new Database(context);
        String userType="";

        Cursor res12 = db.getUserType();
        while(res12.moveToNext()) {
            userType = res12.getString(0);
        }

        // Sets an ID for the notification
        int mNotificationId = 001;

        Bundle intentExtras = intent.getExtras();

        if (userType.equalsIgnoreCase("ADMIN")) {

            if (intentExtras != null) {
                Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
                String smsMessageStr = "";
                for (int i = 0; i < sms.length; ++i) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                    String smsBody = smsMessage.getMessageBody().toString();
                    String address = smsMessage.getOriginatingAddress();

                    smsMessageStr += "SMS From: " + address + "\n";
                    smsMessageStr += smsBody;

                }

                if(smsMessageStr.contains("ANDAR ALERT MESSAGE")) {

                    Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle("ANDAR ALERT")
                                    .setContentText("Emergency!")
                                    .setDefaults(Notification.DEFAULT_ALL)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText(smsMessageStr))
                            /*.setStyle(new NotificationCompat.InboxStyle()
                                .addLine(smsMessageStr))*/
                                    .setAutoCancel(true);

                    Intent resultIntent = new Intent(context, AlertActivity.class);

                    // Because clicking the notification opens a new ("special") activity, there's
                    // no need to create an artificial back stack.

                    PendingIntent resultPendingIntent =
                            PendingIntent.getActivity(
                                    context,
                                    0,
                                    resultIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );

                    mBuilder.setContentIntent(resultPendingIntent);

                    // Gets an instance of the NotificationManager service
                    NotificationManager mNotifyMgr =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                    // Builds the notification and issues it.
                    mNotifyMgr.notify(mNotificationId, mBuilder.build());


                    //this will update the UI with message
                    AlertActivity inst = AlertActivity.instance();
                    inst.updateList(smsMessageStr);

                }
            }

        } else if(userType.equalsIgnoreCase("RESCUER")){
            if (intentExtras != null) {
                Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
                String smsMessageStr = "";
                for (int i = 0; i < sms.length; ++i) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                    String smsBody = smsMessage.getMessageBody().toString();
                    String address = smsMessage.getOriginatingAddress();

                    smsMessageStr += "SMS From: " + address + "\n";
                    smsMessageStr += smsBody;

                }

                if(smsMessageStr.contains("ANDAR ALERT MESSAGE")) {

                    Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle("ANDAR ALERT")
                                    .setContentText("Emergency!")
                                    .setDefaults(Notification.DEFAULT_ALL)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText(smsMessageStr))
                            /*.setStyle(new NotificationCompat.InboxStyle()
                                .addLine(smsMessageStr))*/
                                    .setAutoCancel(true);

                    Intent resultIntent = new Intent(context, AlertRescueActivity.class);

                    // Because clicking the notification opens a new ("special") activity, there's
                    // no need to create an artificial back stack.

                    PendingIntent resultPendingIntent =
                            PendingIntent.getActivity(
                                    context,
                                    0,
                                    resultIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );

                    mBuilder.setContentIntent(resultPendingIntent);

                    // Gets an instance of the NotificationManager service
                    NotificationManager mNotifyMgr =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                    // Builds the notification and issues it.
                    mNotifyMgr.notify(mNotificationId, mBuilder.build());


                    //this will update the UI with message
                    AlertActivity inst = AlertActivity.instance();
                    inst.updateList(smsMessageStr);

                }
            }
        }


    }

}
