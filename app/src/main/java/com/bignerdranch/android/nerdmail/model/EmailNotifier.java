package com.bignerdranch.android.nerdmail.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.bignerdranch.android.nerdmail.R;
import com.bignerdranch.android.nerdmail.controller.DrawerActivity;
import com.bignerdranch.android.nerdmailservice.Email;

import java.util.List;

/**
 * Created by SamMyxer on 4/7/16.
 */
public class EmailNotifier {
    private static final int EMAIL_NOTIFICATION_ID = 0;

    private static EmailNotifier sEmailNotifier;

    private Context context;

    private EmailNotifier(Context context) {
        this.context = context.getApplicationContext();
    }

    public static EmailNotifier get(Context context) {
        if(sEmailNotifier == null) {
            sEmailNotifier = new EmailNotifier(context);
        }
        return sEmailNotifier;
    }

    public void notifyOfEmails(List<Email> emails) {
        if(emails.size() == 0) {
            return;
        }
        Notification notification;
        if(emails.size() == 1) {
            notification = createSingleEmailNotification(emails.get(0));
        } else {
            notification = createMultipleEmailNotification(emails);
        }
        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(EMAIL_NOTIFICATION_ID, notification);
    }

    private Notification createSingleEmailNotification(Email email) {
        String sender = email.getSenderAddress();
        String subject = email.getSubject();
        String body = email.getBody();

        Intent intent = new Intent(context, DrawerActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);

         return new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                 .setContentTitle(sender)
                 .setContentText(subject)
                 .setSubText(body)
                 .setContentIntent(pi)
                 .setAutoCancel(true)
                 .setColor(context.getResources().getColor(R.color.colorPrimary))
                 .build();
    }

    private Notification createMultipleEmailNotification(List<Email> emails) {
        NotificationCompat.InboxStyle style = createInboxStyle(emails);

        Intent intent = new Intent(context, DrawerActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);

        String contentTitle =
                context.getString(R.string.multiple_emails_title, emails.size());

        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(contentTitle)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setStyle(style)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .build();
    }

    private NotificationCompat.InboxStyle createInboxStyle(List<Email> emails) {
        NotificationCompat.InboxStyle inboxStyle
                = new NotificationCompat.InboxStyle();
        String bigTitle
                = context.getString(R.string.multiple_emails_title, emails.size());
        inboxStyle.setBigContentTitle(bigTitle);

        int notificationCount = emails.size() > 5 ? 5 : emails.size();
        for (int i = 0; i < notificationCount; i ++) {
            Email email = emails.get(i);
            String text = email.getSenderAddress() + " " + email.getSubject();
            inboxStyle.addLine(text);
        }

        return inboxStyle;
    }

    public void clearNotifications() {
        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(EMAIL_NOTIFICATION_ID);
    }


}
