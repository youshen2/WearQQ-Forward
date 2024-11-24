package moye.wearqq.forward.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.util.Log;

import java.io.File;

import moye.wearqq.forward.GotoActivity;
import moye.wearqq.forward.R;
import moye.wearqq.forward.utils.ToolUtils;

public class QQMsgReceiver extends BroadcastReceiver {

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if ("com.tencent.qqlite.watch.conversation".equalsIgnoreCase(intent.getAction())) {
            Log.e("WearQQ", "接收到消息");

            String conversationType = intent.getExtras().getString("conversationType");
            String contactName = intent.getExtras().getString("contactName");
            String buddyDetailInfo = intent.getExtras().getString("conversationContent");
            String faceUrl = intent.getExtras().getString("contactAvatarUri");

            Bitmap bm = ToolUtils.DrawableToBitmap(context.getResources().getDrawable(R.drawable.icon));
            if (!faceUrl.equals("")) {
                File file = new File(faceUrl);
                if (file.exists()) {
                    bm = BitmapFactory.decodeFile(faceUrl);
                }
            }

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            ToolUtils.requestNotification(context);

            Intent intent1 = new Intent(context, GotoActivity.class);
            intent1.putExtra("intent", intent);
            intent1.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            Notification.Builder notificationBuilder = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                switch (conversationType) {
                    case "1":
                        //好友申请
                        notificationBuilder = (new Notification.Builder(context, "friend_add"));
                        break;
                    case "2":
                        //群聊及好友
                        notificationBuilder = (new Notification.Builder(context, "qq_msg"))
                                .setContentIntent(pendingIntent);
                        ;
                        break;
                }
                if (notificationBuilder != null) {
                    notificationBuilder.setSmallIcon(Icon.createWithBitmap(bm))
                            .setLargeIcon(bm)
                            .setContentTitle(contactName)
                            .setContentText(buddyDetailInfo);
                }
            } else {
                notificationBuilder = new Notification.Builder(context)
                        .setSmallIcon(R.drawable.icon)
                        .setLargeIcon(bm)
                        .setContentTitle(contactName)
                        .setContentText(buddyDetailInfo)
                        .setPriority(Notification.PRIORITY_HIGH);
                if (conversationType.equals("2"))
                    notificationBuilder.setContentIntent(pendingIntent);
            }

            if (notificationBuilder != null) {
                Notification notification = notificationBuilder.build();
                notificationManager.notify(140, notification);
            }
        }
    }
}
