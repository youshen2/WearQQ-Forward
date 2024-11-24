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

import java.io.File;

import moye.wearqq.forward.GotoActivity;
import moye.wearqq.forward.R;
import moye.wearqq.forward.utils.ToolUtils;

public class QQMsgReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.tencent.qqlite.watch.conversation".equalsIgnoreCase(intent.getAction())) { // 这是手表QQ在接收到消息时会发送的广播
            String conversationType = intent.getExtras().getString("conversationType"); // 获取消息类型
            String contactName = intent.getExtras().getString("contactName"); // 获取消息标题
            String buddyDetailInfo = intent.getExtras().getString("conversationContent"); // 获取消息内容
            String faceUrl = intent.getExtras().getString("contactAvatarUri"); // 获取头像

            Bitmap bm = ToolUtils.DrawableToBitmap(context.getResources().getDrawable(R.drawable.icon)); // 默认使用QQ的图标
            if (!faceUrl.equals("")) { // 如果给的头像不是空的话
                File file = new File(faceUrl);
                if (file.exists()) { // 尝试判断头像是否合法（能成功加载）
                    bm = BitmapFactory.decodeFile(faceUrl); // 将头像作为通知图标
                }
            }

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE); // 获取通知服务

            ToolUtils.requestNotification(context); // 创建通知频道

            // 这里是为了点击通知后能打开QQ
            Intent intent1 = new Intent(context, GotoActivity.class);
            intent1.putExtra("intent", intent);
            intent1.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            // 开始创建通知
            Notification.Builder notificationBuilder = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // 判断一下安卓版本，高版本和低版本需要不同的逻辑
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
                if (notificationBuilder != null) { // （其实好像不用加这个判断
                    notificationBuilder.setSmallIcon(Icon.createWithBitmap(bm))
                            .setLargeIcon(bm)
                            .setContentTitle(contactName)
                            .setContentText(buddyDetailInfo);
                }
            } else {
                // 低版本安卓没有通知渠道，所以直接创建
                notificationBuilder = new Notification.Builder(context)
                        .setSmallIcon(R.drawable.icon)
                        .setLargeIcon(bm)
                        .setContentTitle(contactName)
                        .setContentText(buddyDetailInfo)
                        .setPriority(Notification.PRIORITY_HIGH);
                if (conversationType.equals("2"))
                    notificationBuilder.setContentIntent(pendingIntent); // 懒得给好友申请写同意和拒绝的逻辑

                //同意就发送com.tencent.qq.action.addFriend.accept，然后把带消息的intent传过去
                //拒绝就发送com.tencent.qq.action.addFriend.reject，然后把带消息的intent传过去
            }

            if (notificationBuilder != null) {
                Notification notification = notificationBuilder.build();
                notificationManager.notify(140, notification); // 显示通知
            }
        }
    }
}
