package moye.wearqq.forward;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import moye.wearqq.forward.receiver.QQMsgReceiver;
import moye.wearqq.forward.utils.ToolUtils;

public class MainActivity extends Activity {

    private QQMsgReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1); // 申请一下高版本安卓需要的通知权限
        ToolUtils.requestNotification(this); // 注册一下通知渠道（为高版本安卓）

        // 注册Receiver，用于接受消息广播
        receiver = new QQMsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.tencent.qqlite.watch.conversation");
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        // 销毁Activity时把Receiver也一起注销掉
        if (receiver != null) unregisterReceiver(receiver);
        super.onDestroy();
    }
}