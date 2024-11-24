package moye.wearqq.forward;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class GotoActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 这个活动是为了打开WearQQ的

        Intent intent = ((Intent) getIntent().getParcelableExtra("intent")).getParcelableExtra("intent"); // 获取一下包含消息详情的Intent
        if (intent != null) {
            // 通过发送广播来打开WearQQ进行回复
            Intent intent1 = new Intent("com.tencent.qq.action.conversation.reply");
            intent1.putExtra("intent", intent);
            sendBroadcast(intent1);
        }

        finish(); // 这个活动只为了打开WearQQ，所以可以直接销毁
    }
}
