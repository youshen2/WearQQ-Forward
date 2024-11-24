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

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
        ToolUtils.requestNotification(this);

        receiver = new QQMsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.tencent.qqlite.watch.conversation");
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        if (receiver != null) unregisterReceiver(receiver);
        super.onDestroy();
    }
}