package org.dpppt.android.app.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;

import org.dpppt.android.app.R;

public class NotificationUtil {

	public static final String NOTIFICATION_CHANNEL_ID = "contact-channel";
	public static final int NOTIFICATION_ID_CONTACT = 42;
	public static final int NOTIFICATION_ID_UPDATE = 43;

	@RequiresApi(api = Build.VERSION_CODES.O)
	public static void createNotificationChannel(Context context) {
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		String channelName = context.getString(R.string.app_name);
		NotificationChannel channel =
				new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
		channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
		notificationManager.createNotificationChannel(channel);
	}

}
